package spudigo.localization;

import java.util.ListResourceBundle;

public class LangBundle_it extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return contents;
	}

	private Object[][] contents = {
		{ "statusRowNumbers"   				, "%1$d righe" },
		{ "noFileOpen"   					, "Nessun file aperto" },
		{ "selectFilesToOpen"   			, "Seleziona i file da aprire" },
		{ "fileNotFound"   					, "File non trovato" },

		{ "exportTxt"   					, "Esporta come file 'txt'" },
		{ "exportSpud"   					, "Esporta come file 'spud'" },

		{ "exportNormal"   					, "Esporta singolo file normale" },
		{ "exportNormalAll"   				, "Esporta una scheda per file" },
		{ "exportByType"   					, "Esporta in file separati per tipo" },
		{ "exportAllInOne" 					, "Esporta tutto in un unico file" },

		{ "saveTxt"   						, "Salva file 'txt'" },
		{ "saveSpud"   						, "Salva file 'spud'" },

		{ "warning"   						, "Attenzione" },
		{ "lineUnreadable"   				, "<html>[<b>%1$s</b>] %2$d linea illegibbile and %3$d with missing element</html>" },
		{ "linesUnreadable"   				, "<html>[<b>%1$s</b>] %2$d linee illegibili and %3$d with missing element</html>" },

		{ "error"   						, "Errore" },
		{ "errorSelectFile"   				, "Problema nella selezione del file" },
		{ "errorSelectFolder"   			, "Problema nella selezione di questa cartella" },
		{ "errorModelIn"   					, "Devi scegliere un modello sorgente" },
		{ "errorModelOut"   				, "Devi scegliere un modello destinazione" },

		{ "ok"	   							, "OK" },
		{ "none"	   						, "Nessuno" },
		{ "cancel"   						, "Annulla" },
		{ "yes"   							, "Sì" },
		{ "yesToAll"   						, "Sì a tutto" },
		{ "no"   							, "No" },
		{ "ignore"   						, "Non salvare" },
		{ "go"   							, "Vai a" },
		{ "apply"   						, "Applica" },

		{ "askIgoVers"						, "Per quale versione di iGO vuoi generare il file?" },
		{ "askIgoVersTitle"					, "Scelta versione iGO" },
		{ "askReplaceFile"					, "<html>Il file [<b>%1$s</b>] esiste già.<br><br>Vuoi sovrascriverlo?</html>" },
		{ "askReplaceFileTitle"				, "File esistente" },

		{ "aboutTitle"						, "Info su Spudigo" },
		{ "aboutBuildBy"					, "Traduzione di <b>bovirus</b> v. <b>16.11.2018</b><br/>Sviluppato da" },

		{ "askDupDistance"					, "<html><b>Distanza massima tra due voci sospette:</b></html>" },
		{ "askDupDistanceTitle"				, "Cerca duplicati" },

		{ "tbNew"   						, "Nuovo" },
		{ "tbOpen"   						, "Apri..." },
		{ "tbClose"   						, "Chiudi" },
		{ "tbSave"   						, "Salva" },
		{ "tbSaveAs"   						, "Salva come..." },
		{ "tbUndo"   						, "Annulla" },
		{ "tbRedo"   						, "Ripeti" },
		{ "tbCopy"   						, "Copia" },
		{ "tbCut"   						, "Taglia" },
		{ "tbPaste"   						, "Incolla" },
		{ "tbAddLine"   					, "Aggiungi nuova linea" },
		{ "tbDelete"   						, "Elimina" },
		{ "tbMapToggle" 		  			, "Visualizza vista mappa" },
		{ "tbViewToggle"   					, "Abilita/disabilita vista schede" },

		{ "tbModelIn" 		  				, "Modello sorgente:" },
		{ "tbModelOut"   					, "Modello destinazione:" },

		{ "sbWelcome"   					, "Benvenuto in Spudigo" },
		{ "sbDistance"   					, "Distanza:" },
		{ "sbOSMRoad"   					, "Strada OSM:" },

		{ "mbFile"   						, "File" },
		{ "mbCloseAll" 						, "Chiudi tutto" },
		{ "mbSaveAll"   					, "Salva tutto" },
		{ "mbExit" 							, "Esci" },
		{ "mbEdit" 							, "Modifica" },
		{ "mbDupSearch"						, "Trova duplicati" },
		{ "mbDupNext"						, "Duplicato successivo" },
		{ "mbDupClear"						, "Azzera ricerca" },
		{ "mbExport"						, "Esporta" },
		{ "mbExportAllTxt"   				, "Esporta tutto come file 'txt'" },
		{ "mbExportAllSpud"   				, "Esporta tutto come file 'spud'" },
		{ "mbOptions"						, "Opzioni" },
		{ "mbLanguageSetting"               , "Lingua interfaccia" },
		{ "mbMapSettings"					, "Impostazioni mappa" },
		{ "mbMapSelectCenter"				, "Cerca duplicati" },
		
		{ "langNeedRestartMessage"          , "Per applicare la nuova lingua, è necessario riavviare Spudigo" },
		{ "langNeedRestartTitle"            , "Riavvio richiesto" },
		
		{ "french"                          , "Francese" },
		{ "german"                          , "Tedesco" },
		{ "spanish"                         , "Spagnolo" },
		{ "english"                         , "Inglese" },
		{ "hungarian"                       , "Ungherese" },
		{ "italian"                         , "Italiano" },
		{ "russian"                         , "Russo" },

		{ "askToSave" 						, "<html>[<b>%1$s</b>] è stato modificato.<br><br>Cosa vuoi fare?</html>" },

		{ "result"   						, "Risultato" },
		{ "exportResult"					, "Esporta risultato:" },

		{ "enumDirTypeAll"					, "Tutto" },
		{ "enumDirTypeUni" 					, "Singolo" },
		{ "enumDirTypeBi" 					, "Duale" },

		{ "enumStatusNew"					, "Nuovo" },
		{ "enumStatusDel" 					, "Elimina" },
		{ "enumStatudEdit" 					, "Modifica" },

		{ "errorNoModels"  					, "Impossibile leggere i modelli" },
		{ "errorReadModel"					, "[%1$s] Errore durante la lettura del modello" },
		{ "errorDirModel"					, "Impossibile creare la cartella modelli.\nEsiste già un file con lo stesso nome [%1$s]" },
		{ "errorDefModel"					, "Impossibile creare il modello predefinito\n[%1$smdl_eur++_fr.txt]" },

		{ "errorParseNotKnow"				, "[%1$s] File non riconosciuto" },
		{ "errorParseRead"					, "[%1$s] Errore lettura" },
		{ "errorParseNotFound"				, "[%1$s] File non trovato" },
		{ "errorSaveFile"					, "[%1$s] File non salvato" },

		{ "errorExportFile"					, "[<b>%1$s</b>]<br><font color=\"red\">Errore salvataggio file</font><br>" },
		{ "errorExportUnknownType"			, "\n<u>Tipo sconsociuto:</u> <font color=\"red\"><b>" },
		{ "saveSucessMes"					, "[<b>%1$s</b>]<br><font color=\"green\">Esportazione completata</font>%2$s<br>" },
		{ "saveSucess"						, "[<b>%1$s</b>]<br><font color=\"green\">Esportazione completata</font><br>" },

		{ "tableColumnNames"				, new String[] { "Longitudine (°)", "Latitudine (°)", "Tipo", 
					"Velocità (Km/h)", "Direzione", "Angolo (°)", "Commento", "Stato" } },
	
		{ "mapShowCompass"					, "Visualizza bussola" },
		{ "mapShowCompassSelection"			, "Visualizza bussola alla selezione" },
		{ "mapRoad"							, "Stradale" },
		{ "mapHybrid" 						, "Ibrida" },
		{ "mapSatellite" 					, "Satellitare" },
		{ "mapTerrain" 						, "Terreno" },
		{ "mapChoice" 						, "Seleziona fornitore mappa" },
		{ "mapGoToAddress"					, "Vai all'indirizzo" },
		{ "mapGoToPosition" 				, "Vai alle coordinate" },
		{ "mapStreetView" 					, "Google Street Map" },
		{ "mapOpenStreetView" 				, "Apri il browser web alla posizione" },
		{ "mapHelp" 						, "Mappa aiuto" },
		{ "mapHelpMessage" 					, "<html>La mappa include tre collegamenti per interagire con i marcatori POI:"+
					"<table><tr><td>{<b>Maiuscolo+clic sinistro</b>}</td><td>Posiziona un nuovo marcatore</td></tr>"+
					"<tr><td>{<b>Maiuscolo+spostamento mouse</b>}</td><td>Modifica posizione del marcatore selezionato</td></tr>" +
					"<tr><td>{<b>Alt+spostamento mouse</b>}</td><td>Permette modifica angolo del marcatore selezionato</td></tr></table></html>"},

		{ "diagIputTextLabel"				, "<html><b>Inserisci un indirizzo:</b></html>" },

		{ "diagLatLngLabel"					, "<html><b>Inserisci latitudine e longitudine:</b></html>" },
		{ "diagLatLngLabelErr"				, "<html><b><font color=red>Valori errati!!!</font></b></html>" },
		{ "diagLongitude"					, "Longitudine:" },
		{ "diagLatitude"					, "Latitudine:" },
		{ "diagAddressErr"					, "Indirizzo non trovato" },

		{ "diagOptMapConfigSetZone"			, "<html><b>Impostazioni zona rilevamento</b></html>" },
		{ "diagOptMapConfigRadAngle"		, "Angolo arco:" },
		{ "diagOptMapConfigFactorX"			, "Fattore X:" },
		{ "diagOptMapConfigFactorY"			, "Fattore Y:" },
		{ "diagOptMapConfigColor0"			, "Colore interno:" },
		{ "diagOptMapConfigColor1"			, "Colore esterno:" },
		{ "diagOptMapConfigColor2"			, "Bordo selezionato:" },
		{ "diagOptMapConfigSetImage"		, "<html><b>Impostazione immagini marcatori</b></html>" },
		{ "diagOptMapConfigSetSpeed"		, "<html><b>Impostazione velocità testi marcatori</b></html>" },
		{ "diagOptMapConfigSetZonePoi"		, "<html><b>Impostazione zone marcatore</b></html>" },
		{ "diagOptMapConfigSpeedFont"		, "Font testo:" },
		{ "diagOptMapConfigSpeedHelp"		, "<html><b>L'origine della posizione del testo sono le coordinate del marcatore.</b></html>" }, 
		{ "diagOptMapConfigSpeedX"			, "Posizione X:" },
		{ "diagOptMapConfigSpeedY"			, "Posizione Y:" },
		{ "diagOptMapConfigCenterTextX"		, "Centra testo sull'asse X" },

		{ "diagOptMapConfigZoneBeginPoi"	, "N. max marcatori:" },
		{ "diagOptMapConfigZoneMinSize"		, "Dim. min. zone:" },
		{ "diagOptMapConfigZoneFont"		, "Font testo:" },

		{ "diagFontChooser"					, "Scegli una font" },
		{ "diagFontChooserFont"				, "Font" },
		{ "diagFontChooserFontName"			, "Nome font" },
		{ "diagFontChooserFontSize"			, "Dim. font" },
		{ "diagFontChooserFontEffects"		, "Effetti" },
		{ "diagFontChooserFontColor"		, "Colore:" },
		{ "diagFontChooserFontPreview"		, "Anteprima" },
		{ "diagFontChooserFontBold"			, "Grassetto" },
		{ "diagFontChooserFontItalic"		, "Corsivo" },
		{ "diagFontChooserFontUnderline"	, "Sottolineato" },
		{ "diagFontChooserFontStrikethrough", "Ribattuto" },
		{ "diagFontChooserFontSubscript"	, "Pedice" },
		{ "diagFontChooserFontSuperscript"	, "Apice" },

		{ "diagColorChooser"				, "Scegli un colore" },

		{ "diagOptMapConfigSelected"		, "Selezionata:" },
		{ "diagOptMapConfigUnknown"			, "Sconosciuta:" },
		{ "diagOptMapConfigType"			, "Tipo" },
		{ "diagOptMapConfigImgPath"			, "Percorso immagine" },
		{ "diagOptMapConfigAskImg"			, "Seleziona nuova immagine" },

		{ "tableTipColLon"					, "Valori accettati da -180 a 180" },
		{ "tableTipColLat" 					, "Valori accettati da -90 a 90" },
		{ "tableTipColSpeed" 				, "Valori accettati da 0 a 255" },
		{ "tableTipColAngle" 				, "Valori accettati da 0 a 359" },
		{ "tableTipColCombo" 				, "Clic per visualizzare tutte le scelte" },

		{ "centerOnMap" 					, "Centra sulla mappa" },
		{ "centerZoomOnMap"					, "Centra e zooma sulla mappa" },
		{ "dupFindProgress" 				, "Ricerca in corso..." },
		{ "dupEndFile" 						, "Raggiunta la fine del file" },
		{ "dupEndFileTitle" 				, "Fine del file" },
		{ "dupSearch" 						, "Cerca" },
		{ "dupCheckIgnore" 					, "Ignora angoli inversi" },
		{ "dupCheckAngle" 					, "<html><b>Differenze accettate per rilevare un angolo inverso:</b></html>" },

		{ "templateEurPlus" 					, ";name       Nome modello visualizzato nell'elenco scelte%1$s%1$s" +
				   "name=Europa + Francia%1$s%1$s" +
				   ";uid=txt_type:spud_type:label%1$s" +
				   ";%1$s" +
				   ";uid        L'ID è usato per la corrispondenza tra i modelli,%1$s" +
				   ";           puoi mettere qualunque cosa tu voglia un nome, numero, ...%1$s" +
				   ";txt_type   Tipo di autovelox in un file di testo%1$s" +
				   ";spud_type  Tipo di autovelox in un file spud%1$s" +
				   ";label      Nome visualizzato nell'app, è usato per la selezione%1$s%1$s" +
				   "0=1:0:Postazione autovelox fissa%1$s" +
				   "1=2:2:Postazione autovelox mobile%1$s" +
				   "2=3:4:Uscita superstrada%1$s" +
				   "3=4:3:Rilevamento velocità media%1$s" +
				   "4=5:1:Ingresso superstrada%1$s" +
				   "5=6:6:Telecamera semaforo%1$s" +
				   "6=7:7:Limite altezza%1$s" +
				   "7=8:8:Rilevatore distanza%1$s" +
				   "8=9:9:Area pericolosa%1$s" +
				   "9=10:10:Radar nel tunnel%1$s" +
				   "10=11:11:Pasaaggio a livello%1$s" +
				   "11=12:12:Zona a 30 Km/h%1$s" +
				   "12=13:13:Stop%1$s" +
				   "13=14:14:Limite larghezza%1$s" +
				   "14=15:15:Radar rilevazione peso%1$s" +
				   "15=16:16:Controllo alcolemico%1$s" +
				   "16=17:17:Pannello infromazioni%1$s" +
				   "17=18:18:Probabile radar%1$s" +
				   "18=19:19:Superamento della zona%1$s" +
				   "19=20:20:Ext1%1$s" +
				   "20=21:21:Ext2%1$s" +
				   "21=22:22:Ext3%1$s" +
				   "22=23:23:Ext4%1$s" +
				   "23=69:5:Telecamera semaforo%1$s%1$s" +
				   ";Usato per convertire più categorie in una%1$s" +
				   "32=69:5%1$s" +
				   "36=69:5%1$s%1$s" +
				   ";Se l'UID non viene trovato nel modello di destinazione, non verrà convertito%1$s" },
		{ "templateIgoStd" 				, ";name       Nome del modello visualizzato nell'elenco delle scelte%1$s%1$s" +
	 			   "name=iGO Standard%1$s%1$s" +
				   ";uid=txt_type:spud_type:label%1$s%1$s" +
				
				   ";uid        L'ID usato per la corrispondenza tra i modelli,%1$s" +
				   ";           puoi mettere qualunque cosa tu voglia un nome, numero, ...%1$s" +
				   ";txt_type   Tipo di autovelox in un file di testo%1$s" +
				   ";spud_type  Tipo di autovelox in un file spud%1$s" +
				   ";label      Nome visualizzato nell'app, usato per effettuare le scelte%1$s%1$s" +
				   "0=1:0:Postazione autovelox fissa%1$s" +
				   "1=5:1:Postazione autovelox mobile%1$s" +
				   "32=2:2:Autovelox integrato%1$s" +
				   "3=4:3:Rilevamento velocità media%1$s" +
				   "23=3:4:Telecamera semaforo%1$s" +
				   "10=6:6:Attraversamento ferrovia%1$s" +
				   "33=7:7:Telecamera corsia autobus%1$s" +
				   "8=8:8:Zona ad alto rischio%1$s" +
				   "34=9:9:Zona scuola%1$s" +
				   "35=10:10:Punto entrata in città%1$s" +
				   "36=11:11:Telecamera semaforo ed autovelox%1$s" +
				   "37=12:12:Casello%1$s" +
				   "38=13:13:Ospedale, ambulanza%1$s" +
				   "39=14:14:Vigili del fuoco%1$s" +
				   "40=15:15:Zona congestionata%1$s" +
				   "41=31:31:Area pericolosa%1$s" },

	};
}
