
package org.jxmapviewer.viewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ResponseCache;
import java.net.URI;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author joshy
 */
public class LocalResponseCache extends ResponseCache
{
	private static final Log log = LogFactory.getLog(LocalResponseCache.class);
	
	private final File cacheDir;

	private boolean checkForUpdates;

	private String baseURL;

	/**
	 * Private constructor to prevent instantiation.
	 * @param baseURL the URI that should be cached or <code>null</code> (for all URLs)
	 * @param cacheDir the cache directory
	 * @param checkForUpdates true if the URL is queried for newer versions of a file first
	 */
	private LocalResponseCache(String baseURL, File cacheDir, boolean checkForUpdates)
	{
		this.baseURL = baseURL;
		this.cacheDir = cacheDir;
		this.checkForUpdates = checkForUpdates;

		if (!cacheDir.exists())
		{
			cacheDir.mkdirs();
		}
	}

	/**
	 * Sets this cache as default response cache
	 * @param baseURL the URL, the caching should be restricted to or <code>null</code> for none
	 * @param cacheDir the cache directory
	 * @param checkForUpdates true if the URL is queried for newer versions of a file first
	 */
	public static void installResponseCache(String baseURL, File cacheDir, boolean checkForUpdates)
	{
		ResponseCache.setDefault(new LocalResponseCache(baseURL, cacheDir, checkForUpdates));
	}

	/**
	 * Returns the local File corresponding to the given remote URI.
	 * @param remoteUri the remote URI
	 * @return the corresponding local file
	 */
	public File getLocalFile(URI remoteUri)
	{
		if (baseURL != null)
		{
			String remote = remoteUri.toString();
			
			if (!remote.startsWith(baseURL))
			{
				return null;
			}
		}
		
		String name;
		MessageDigest messageDigest;
		StringBuilder sb = new StringBuilder();
		
		String host = remoteUri.getHost();
		if (host != null)
			sb.append(host);
		
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(remoteUri.toString().getBytes());
			byte[] digest = messageDigest.digest();
			sb.append(File.separator);
			sb.append(DatatypeConverter.printHexBinary(digest));
			name = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			name = null;
		}
				
		File f = new File(cacheDir, name);
		
		return f;
	}

	/**
	 * @param remoteUri the remote URI
	 * @param localFile the corresponding local file
	 * @return true if the resource at the given remote URI is newer than the resource cached locally.
	 */
	private static boolean isUpdateAvailable(URI remoteUri, File localFile)
	{
		URLConnection conn;
		try
		{
			conn = remoteUri.toURL().openConnection();
		}
		catch (MalformedURLException ex)
		{
			log.error("An exception occurred", ex);
			return false;
		}
		catch (IOException ex)
		{
			log.error("An exception occurred", ex);
			return false;
		}
		if (!(conn instanceof HttpURLConnection))
		{
			// don't bother with non-http connections
			return false;
		}

		long localLastMod = localFile.lastModified();
		long remoteLastMod = 0L;
		HttpURLConnection httpconn = (HttpURLConnection) conn;
		// disable caching so we don't get in feedback loop with ResponseCache
		httpconn.setUseCaches(false);
		try
		{
			httpconn.connect();
			remoteLastMod = httpconn.getLastModified();
		}
		catch (IOException ex)
		{
			// log.error("An exception occurred", ex);();
			return false;
		}
		finally
		{
			httpconn.disconnect();
		}

		return (remoteLastMod > localLastMod);
	}

	@Override
	public CacheResponse get(URI uri, String rqstMethod, Map<String, List<String>> rqstHeaders) throws IOException
	{
		File localFile = getLocalFile(uri);
		
		if (localFile == null)
		{
			// we don't want to cache this URL 
			return null;
		}
		
		if (!localFile.exists())
		{
			// the file isn't already in our cache, return null
			return null;
		}

		if (checkForUpdates)
		{
			if (isUpdateAvailable(uri, localFile))
			{
				// there is an update available, so don't return cached version
				return null;
			}
		}

		return new LocalCacheResponse(localFile, rqstHeaders);
	}

	@Override
	public CacheRequest put(URI uri, URLConnection conn) throws IOException
	{
		// only cache http(s) GET requests
		if (!(conn instanceof HttpURLConnection) || !(((HttpURLConnection) conn).getRequestMethod().equals("GET")))
		{
			return null;
		}

		File localFile = getLocalFile(uri);
		
		if (localFile == null)
		{
			// we don't want to cache this URL 
			return null;
		}
		
		new File(localFile.getParent()).mkdirs();
		return new LocalCacheRequest(localFile);
	}

	private class LocalCacheResponse extends CacheResponse
	{
		private FileInputStream fis;
		private final Map<String, List<String>> headers;

		private LocalCacheResponse(File localFile, Map<String, List<String>> rqstHeaders)
		{
			try
			{
				this.fis = new FileInputStream(localFile);
			}
			catch (FileNotFoundException ex)
			{
				// should not happen, since we already checked for existence
				log.error("An exception occurred", ex);
			}
			this.headers = rqstHeaders;
		}

		@Override
		public Map<String, List<String>> getHeaders() throws IOException
		{
			return headers;
		}

		@Override
		public InputStream getBody() throws IOException
		{
			return fis;
		}
	}

	private class LocalCacheRequest extends CacheRequest
	{
		private final File localFile;
		private FileOutputStream fos;

		private LocalCacheRequest(File localFile)
		{
			this.localFile = localFile;
			try
			{
				this.fos = new FileOutputStream(localFile);
			}
			catch (FileNotFoundException ex)
			{
				// should not happen if cache dir is valid
				log.error("An exception occurred", ex);
			}
		}

		@Override
		public OutputStream getBody() throws IOException
		{
			return fos;
		}

		@Override
		public void abort()
		{
			// abandon the cache attempt by closing the stream and deleting
			// the local file
			try
			{
				fos.close();
				localFile.delete();
			}
			catch (IOException e)
			{
				// ignore
			}
		}
	}
}
