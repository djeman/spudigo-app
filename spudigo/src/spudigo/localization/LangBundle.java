package spudigo.localization;

import java.util.ListResourceBundle;

public class LangBundle extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return contents;
	}

	private Object[][] contents = {
		{ "statusRowNumbers"   				, "%1$d Rows" },
		{ "noFileOpen"   					, "No files opened" },
		{ "selectFilesToOpen"   			, "Select files to open" },
		{ "fileNotFound"   					, "File not found" },
		
		{ "exportTxt"   					, "Export 'txt'" },
		{ "exportSpud"   					, "Export 'spud'" },
		
		{ "exportNormal"   					, "Export normal one file" },
		{ "exportNormalAll"   				, "Export normal a tab per file" },
		{ "exportByType"   					, "Export to separate files by type" },
		{ "exportAllInOne" 					, "Export all in one file" },
		
		{ "saveTxt"   						, "Save 'txt'" },
		{ "saveSpud"   						, "Save 'spud'" },
		
		{ "warning"   						, "Warning" },
		{ "lineUnreadable"   				, "<html>[<b>%1$s</b>] %2$d unreadable line and %3$d with missing element</html>" },
		{ "linesUnreadable"   				, "<html>[<b>%1$s</b>] %2$d unreadable lines and %3$d with missing element</html>" },
		{ "badheader"   					, "The column header does not exist, please add it to make the file readable (X,Y,TYPE,SPEED,DirType,Direction,...)" },
		
		{ "error"   						, "Error" },
		{ "errorSelectFile"   				, "Problem to select this file" },
		{ "errorSelectFolder"   			, "Problem to select this folder" },
		{ "errorModelIn"   					, "You must choose an input model" },
		{ "errorModelOut"   				, "You must choose an output model" },
		
		{ "ok"	   							, "OK" },
		{ "none"	   						, "None" },
		{ "cancel"   						, "Cancel" },
		{ "yes"   							, "Yes" },
		{ "yesToAll"   						, "Yes to All" },
		{ "no"   							, "No" },
		{ "ignore"   						, "Ignore" },
		{ "go"   							, "Go To" },
		{ "apply"   						, "Apply" },
		
		{ "askIgoVers"						, "For what version of iGO you want to generate the file?" },
		{ "askIgoVersTitle"					, "iGO version choice" },
		{ "askReplaceFile"					, "<html>The file [<b>%1$s</b>] already exists.<br><br>Want you to replace it?</html>" },
		{ "askReplaceFileTitle"				, "Existing file" },
		
		{ "aboutTitle"						, "About Spudigo" },
		{ "aboutBuildBy"					, "Build by" },
		
		{ "askDupDistance"					, "<html><b>Maximum distance between two suspicious entries:</b></html>" },
		{ "askDupDistanceTitle"				, "Search duplicate" },
		
		{ "tbNew"   						, "New" },
		{ "tbOpen"   						, "Open..." },
		{ "tbClose"   						, "Close" },
		{ "tbSave"   						, "Save" },
		{ "tbSaveAs"   						, "Save As..." },
		{ "tbUndo"   						, "Undo" },
		{ "tbRedo"   						, "Redo" },
		{ "tbCopy"   						, "Copy" },
		{ "tbCut"   						, "Cut" },
		{ "tbPaste"   						, "Paste" },
		{ "tbInsert"   						, "Insert" },
		{ "tbAddLine"   					, "Add a new line" },
		{ "tbInsertLine"   					, "Insert a new line" },
		{ "tbDelete"   						, "Delete" },
		{ "tbMapToggle" 		  			, "Show the map view" },
		{ "tbViewToggle"   					, "Toggle tab view" },
		
		{ "tbModelIn" 		  				, "Input template:" },
		{ "tbModelOut"   					, "Output template:" },
		
		{ "sbWelcome"   					, "Welcome" },
		{ "sbDistance"   					, "Distance:" },
		{ "sbOSMRoad"   					, "OSM Road:" },
		
		{ "mbFile"   						, "File" },
		{ "mbCloseAll" 						, "Close All" },
		{ "mbSaveAll"   					, "Save All" },
		{ "mbExit" 							, "Exit" },
		{ "mbEdit" 							, "Edit" },
		{ "mbDupSearch"						, "Find Duplicates" },
		{ "mbDupNext"						, "Next Duplicate" },
		{ "mbDupClear"						, "Clear Search" },
		{ "mbExport"						, "Export" },
		{ "mbExportAllTxt"   				, "Export All 'txt'" },
		{ "mbExportAllSpud"   				, "Export All 'spud'" },
		{ "mbOptions"						, "Options" },
		{ "mbLanguageSetting"               , "Menus Languages" },
		{ "mbMapSettings"					, "Map Settings" },
		{ "mbMapSelectCenter"				, "Center the map on the selection" },
		
		{ "langNeedRestartMessage"          , "To apply the new language, it is necessary to restart Spudigo" },
		{ "langNeedRestartTitle"            , "Need restart" },
		
		{ "french"                          , "French" },
		{ "german"                          , "German" },
		{ "spanish"                         , "Spanish" },
		{ "english"                         , "English" },
		{ "hungarian"                       , "Hungarian" },
		{ "italian"                         , "Italian" },
		{ "russian"                         , "Russian" },
		
		{ "askToSave" 						, "<html>[<b>%1$s</b>] file has been modified.<br><br>What do you do?</html>" },
		
		{ "result"   						, "Result" },
		{ "exportResult"					, "Export result:" },
		
		{ "enumDirTypeAll"					, "All" },
		{ "enumDirTypeUni" 					, "Single" },
		{ "enumDirTypeBi" 					, "Dual" },
		
		{ "enumStatusNew"					, "New" },
		{ "enumStatusDel" 					, "Deleted" },
		{ "enumStatudEdit" 					, "Edited" },
		
		{ "errorNoModels"  					, "Can not read the templates" },
		{ "errorReadModel"					, "[%1$s] Error reading template" },
		{ "errorDirModel"					, "Unable to create the template folder,\na file of the same name already exists [%1$s]" },
		{ "errorDefModel"					, "Unable to create the default template [%1$smdl_eur++_fr.txt]" },
		
		{ "errorParseNotKnow"				, "[%1$s] Unrecognized file" },
		{ "errorParseRead"					, "[%1$s] Read error" },
		{ "errorParseNotFound"				, "[%1$s] File not found" },
		{ "errorSaveFile"					, "[%1$s] File not saved" },
		
		{ "errorExportFile"					, "[<b>%1$s</b>]<br><font color=\"red\">File saving error</font><br>" },
		{ "errorExportUnknownType"			, "\n<u>Unknown type:</u> <font color=\"red\"><b>" },
		{ "saveSucessMes"					, "[<b>%1$s</b>]<br><font color=\"green\">Export succeeded</font>%2$s<br>" },
		{ "saveSucess"						, "[<b>%1$s</b>]<br><font color=\"green\">Export succeeded</font><br>" },
		
		{ "tableColumnNames"				, new String[] { "Longitude (°)", "Latitude (°)", "Type", 
				"Speed (Km/h)", "Direction", "Angle (°)", "Comment", "Status" } },
				
		{ "mapShowCompass"					, "Show Compass" },
		{ "mapShowCompassSelection"			, "Show Compass on selection" },
		{ "mapRoad"							, "Road" },
		{ "mapHybrid" 						, "Hybrid" },
		{ "mapSatellite" 					, "Satellite" },
		{ "mapTerrain" 						, "Terrain" },
		{ "mapChoice" 						, "Select map provider" },
		{ "mapGoToAddress"					, "Go To Address" },
		{ "mapGoToPosition" 				, "Go To Coordinates" },
		{ "mapStreetView" 					, "Google Street Map" },
		{ "mapOpenStreetView" 				, "Open your web browser on the position" },
		{ "mapHelp" 						, "Help Map" },
		{ "mapHelpMessage" 					, "<html>The map includes three shortcuts to interact with POI markers:"+
				"<table><tr><td>{<b>Shift+Left click</b>}</td><td>To place a new marker</td></tr>"+
				"<tr><td>{<b>Shift+Move mouse</b>}</td><td>Changes the position of the marker selected</td></tr>" +
				"<tr><td>{<b>Alt+Move mouse</b>}</td><td>Allows you to change the angle of the selected marker</td></tr></table></html>"},
		
		{ "diagIputTextLabel"				, "<html><b>Please enter an address:</b></html>" },
		
		{ "diagLatLngLabel"					, "<html><b>Please enter the latitude and longitude:</b></html>" },
		{ "diagLatLngLabelErr"				, "<html><b><font color=red>Incorrect values !!!</font></b></html>" },
		{ "diagLongitude"					, "Longitude:" },
		{ "diagLatitude"					, "Latitude:" },
		{ "diagAddressErr"					, "Address not found" },
		
		{ "diagOptMapConfigSetZone"			, "<html><b>Settings detection zone</b></html>" },
		{ "diagOptMapConfigRadAngle"		, "Arc's angle:" },
		{ "diagOptMapConfigFactorX"			, "Factor X:" },
		{ "diagOptMapConfigFactorY"			, "Factor Y:" },
		{ "diagOptMapConfigColor0"			, "Interior color:" },
		{ "diagOptMapConfigColor1"			, "Exterior color:" },
		{ "diagOptMapConfigColor2"			, "Selected outline:" },
		{ "diagOptMapConfigSetImage"		, "<html><b>Settings images on markers</b></html>" },
		{ "diagOptMapConfigSetSpeed"		, "<html><b>Settings speed's texts on markers</b></html>" },
		{ "diagOptMapConfigSetZonePoi"		, "<html><b>Settings marker's zones</b></html>" },
		{ "diagOptMapConfigSpeedFont"		, "Text font:" },
		{ "diagOptMapConfigSpeedHelp"		, "<html><b>The origin of the position of the text are the marker's coordinates.</b></html>" },
		{ "diagOptMapConfigSpeedX"			, "Position X:" },
		{ "diagOptMapConfigSpeedY"			, "Position Y:" },
		{ "diagOptMapConfigCenterTextX"     , "Center text on X" },
		
		{ "diagOptMapConfigZoneBeginPoi"	, "Maximum Markers:" },
		{ "diagOptMapConfigZoneMinSize"		, "Min Size Zones:" },
		{ "diagOptMapConfigZoneFont"		, "Text font:" },
		
		{ "diagFontChooser"					, "Choose a font" },
		{ "diagFontChooserFont"				, "Font" },
		{ "diagFontChooserFontName"			, "Font name" },
		{ "diagFontChooserFontSize"			, "Font size" },
		{ "diagFontChooserFontEffects"		, "Effects" },
		{ "diagFontChooserFontColor"		, "Color:" },
		{ "diagFontChooserFontPreview"		, "Preview" },
		{ "diagFontChooserFontBold"			, "Bold" },
		{ "diagFontChooserFontItalic"		, "Italic" },
		{ "diagFontChooserFontUnderline"	, "Underline" },
		{ "diagFontChooserFontStrikethrough", "Strikethrough" },
		{ "diagFontChooserFontSubscript"	, "Subscript" },
		{ "diagFontChooserFontSuperscript"	, "Superscript" },
		
		{ "diagColorChooser"				, "Choose a color" },
		
		{ "diagOptMapConfigSelected"		, "Selected:" },
		{ "diagOptMapConfigUnknown"			, "Unknown:" },
		{ "diagOptMapConfigType"			, "Type" },
		{ "diagOptMapConfigImgPath"			, "Image Path" },
		{ "diagOptMapConfigAskImg"			, "Select new image" },
		
		{ "tableTipColLon"					, "Accepted value -180 to 180" },
		{ "tableTipColLat" 					, "Accepted value -90 to 90" },
		{ "tableTipColSpeed" 				, "Accepted value 0 to 255" },
		{ "tableTipColAngle" 				, "Accepted value 0 to 359" },
		{ "tableTipColCombo" 				, "Click to view all choices" },
		
		{ "centerOnMap" 					, "Center on map" },
		{ "centerZoomOnMap"					, "Center and zoom on map" },
		{ "dupFindProgress" 				, "Search in progress..." },
		{ "dupEndFile" 						, "End of file reached" },
		{ "dupEndFileTitle" 				, "End of file" },
		{ "dupSearch" 						, "Search" },
		{ "dupCheckIgnore" 					, "Ignore reversed angles" },
		{ "dupCheckAngle" 					, "<html><b>Difference accepted to detect a reverse angle:</b></html>" },
		
		{ "templateEurPlus" 					, ";name       Model name displayed in the list of choices%1$s%1$s" +
				   "name=EUR+ Fr%1$s%1$s" +
				   ";uid=txt_type:spud_type:label%1$s" +
				   ";%1$s" +
				   ";uid        The id used to make the correspondence between the models,%1$s" +
				   ";           you can put whatever you want a name, number , ...%1$s" +
				   ";txt_type   Speedcam type in text file%1$s" +
				   ";spud_type  Speedcam type in spud file%1$s" +
				   ";label      Name displayed in the app, it is used to make the choices%1$s%1$s" +
				   "0=1:0:Fixed Speed Camera%1$s" +
				   "1=2:2:Mobile Speed Camera%1$s" +
				   "2=3:4:Exit Highway%1$s" +
				   "3=4:3:Average Speed Camera%1$s" +
				   "4=5:1:Input Highway%1$s" +
				   "5=6:6:Franch. de Feux Rouge%1$s" +
				   "6=7:7:Hauteur Limite%1$s" +
				   "7=8:8:Radar de Distance%1$s" +
				   "8=9:9:Dangerous area%1$s" +
				   "9=10:10:Radar de Tunnel%1$s" +
				   "10=11:11:Passage à Niveau%1$s" +
				   "11=12:12:Zone 30%1$s" +
				   "12=13:13:Stop%1$s" +
				   "13=14:14:Largeur Limite%1$s" +
				   "14=15:15:Radar de Poids Lourd%1$s" +
				   "15=16:16:Contrôle d'Alcoolémie%1$s" +
				   "16=17:17:Panneau d'Information%1$s" +
				   "17=18:18:Radar Douteux%1$s" +
				   "18=19:19:Zone de dépassement%1$s" +
				   "19=20:20:Ext1%1$s" +
				   "20=21:21:Ext2%1$s" +
				   "21=22:22:Ext3%1$s" +
				   "22=23:23:Ext4%1$s" +
				   "23=69:5:Red Light Camera%1$s%1$s" +
				   ";Used to convert multiple categories in one%1$s" +
				   "32=69:5%1$s" +
				   "36=69:5%1$s%1$s" +
				   ";If the uid is not found in the target model it will not be converted%1$s" },
		{ "templateIgoStd" 				, ";name       Model name displayed in the list of choices%1$s%1$s" +
	 			   "name=iGO Standard%1$s%1$s" +
				   ";uid=txt_type:spud_type:label%1$s" +
				   ";%1$s" +
				   ";uid        The id used to make the correspondence between the models,%1$s" +
				   ";           you can put whatever you want a name, number , ...%1$s" +
				   ";txt_type   Speedcam type in text file%1$s" +
				   ";spud_type  Speedcam type in spud file%1$s" +
				   ";label      Name displayed in the app, it is used to make the choices%1$s%1$s" +
				   "0=1:0:Fixed Speed Camera%1$s" +
				   "1=5:1:Mobile Speed Camera%1$s" +
				   "32=2:2:Built-in Speed Camera%1$s" +
				   "3=4:3:Average Speed Camera%1$s" +
				   "23=3:4:Red Light Camera%1$s" +
				   "10=6:6:Railway crossing%1$s" +
				   "33=7:7:Bus lane Camera%1$s" +
				   "8=8:8:High accident zone%1$s" +
				   "34=9:9:School zone%1$s" +
				   "35=10:10:Town entry point%1$s" +
				   "36=11:11:Red light and speed camera%1$s" +
				   "37=12:12:Toll booth%1$s" +
				   "38=13:13:Hospital, Ambulance%1$s" +
				   "39=14:14:Fire Station%1$s" +
				   "40=15:15:Congestion Charge Zone%1$s" +
				   "41=31:31:Dangerous area%1$s" },
		
		// Not translated
		{ "igoPrimo"   						, "iGO Primo" },
		{ "igo8"   							, "iGO 8" },
    };
}
