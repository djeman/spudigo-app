package spudigo.localization;

import java.util.ListResourceBundle;

public class LangBundle_de extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return contents;
	}

	private Object[][] contents = {
		{ "statusRowNumbers"   				, "%1$d Datensätze" },
		{ "noFileOpen"   					, "Keine Datei geöffnet" },
		{ "selectFilesToOpen"   			, "Ausgewählte Datei öffnen" },
		{ "fileNotFound"   					, "Datei nicht gefunden" },
		
		{ "exportTxt"   					, "Exportieren in 'txt'" },
		{ "exportSpud"   					, "Exportieren in 'spud'" },
		
		{ "exportNormal"   					, "Exportieren in eine Datei" },
		{ "exportNormalAll"   				, "Exportieren Ausgewählte in eine Datei" },
		{ "exportByType"   					, "Exportieren Separat nach Blitzertyp" },
		{ "exportAllInOne" 					, "Exportieren Alles in eine Datei" },
		
		{ "saveTxt"   						, "Speichern als 'txt'" },
		{ "saveSpud"   						, "Speichern als 'spud'" },
		
		{ "warning"   						, "Warnung" },
		{ "lineUnreadable"   				, "<html>[<b>%1$s</b>] %2$d unlesbarer Link and %3$d with missing element</html>" },
		{ "linesUnreadable"   				, "<html>[<b>%1$s</b>] %2$d unlesbare Links and %3$d with missing element</html>" },
		
		{ "error"   						, "Fehler" },
		{ "errorSelectFile"   				, "Problem beim Auswählen der Datei" },
		{ "errorSelectFolder"   			, "Problem beim Auswählen des Ordners" },
		{ "errorModelIn"   					, "Bitte Eingabeformat wählen" },
		{ "errorModelOut"   				, "Bitte Ausgabeformat wählen" },
		
		{ "none"	   						, "Keines" },
		{ "cancel"   						, "Abbrechen" },
		{ "yes"   							, "Ja" },
		{ "yesToAll"   						, "Alles Übernehmen" },
		{ "no"   							, "Nein" },
		{ "ignore"   						, "Verwerfen" },
		{ "go"   							, "Gehe zu" },
		{ "apply"   						, "Übernehmen" },
		
		{ "askIgoVers"						, "Für welche iGO Version soll die Datei generiert werden?" },
		{ "askIgoVersTitle"					, "iGO Version auswählen" },
		{ "askReplaceFile"					, "<html>Die Datei [<b>%1$s</b>] existiert bereits.<br><br>Datei überschreiben?</html>" },
		{ "askReplaceFileTitle"				, "Vorhandene Datei" },
		
		{ "aboutTitle"						, "Über Spudigo" },
		{ "aboutBuildBy"					, "Erstellt durch" },
		
		{ "askDupDistance"					, "<html><b>Maximale Entfernung zwischen zwei doppelten Einträgen:</b></html>" },
		{ "askDupDistanceTitle"				, "Doppelte Einträge suchen" },
		
		{ "tbNew"   						, "Neu" },
		{ "tbOpen"   						, "Öffnen..." },
		{ "tbClose"   						, "Schließen" },
		{ "tbSave"   						, "Speichern" },
		{ "tbSaveAs"   						, "Speichern als..." },
		{ "tbUndo"   						, "Rückgängig" },
		{ "tbRedo"   						, "Wiederherstellen" },
		{ "tbCopy"   						, "Kopieren" },
		{ "tbCut"   						, "Ausschneiden" },
		{ "tbPaste"   						, "Einfügen" },
		{ "tbAddLine"   					, "Neuen Eintrag hinzufügen" },
		{ "tbDelete"   						, "Löschen" },
		{ "tbMapToggle" 		  			, "Kartenanordnung ändern" },
		{ "tbViewToggle"   					, "Dateien Ansicht ändern" },
		
		{ "tbModelIn" 		  				, "Eingabeformat:" },
		{ "tbModelOut"   					, "Ausgabeformat:" },
		
		{ "sbWelcome"   					, "Willkommen" },
		{ "sbDistance"   					, "Entfernung:" },
		
		{ "mbFile"   						, "Datei" },
		{ "mbCloseAll" 						, "Alles Schließen" },
		{ "mbSaveAll"   					, "Alles Speichern" },
		{ "mbExit" 							, "Beenden" },
		{ "mbEdit" 							, "Bearbeiten" },
		{ "mbDupSearch"						, "Doppelte Einträge suchen" },
		{ "mbDupNext"						, "Nächster doppelter Eintrag" },
		{ "mbDupClear"						, "Suche löschen" },
		{ "mbExport"						, "Export" },
		{ "mbExportAllTxt"   				, "Exportieren Alles in 'txt'" },
		{ "mbExportAllSpud"   				, "Exportieren Alles in 'spud'" },
		{ "mbOptions" 						, "Optionen" }, 
		{ "mbLanguageSetting"               , "Menüsprache" },
		{ "mbMapSettings"					, "Karten Einstellungen" },
		{ "mbMapSelectCenter"				, "Gewählten Punkt in der Mitte anzeigen" },
		
		{ "langNeedRestartMessage"          , "Um die neue Sprache anzuwenden, muss Spudigo neu gestartet werden" },
		{ "langNeedRestartTitle"            , "Neustart erforderlich" },
		
		{ "french"                          , "Französisch" },
		{ "german"                          , "Deutsch" },
		{ "spanish"                         , "Spanisch" },
		{ "english"                         , "Englisch" },
		{ "hungarian"                       , "Ungarisch" },
		{ "italian"                         , "Italienisch" },
		{ "russian"                         , "Russisch" },		
		
		{ "askToSave" 						, "<html>[<b>%1$s</b>] Datei wurde geändert.<br><br>Was wollen Sie tun?</html>" },
		
		{ "result"   						, "Resultat" },
		{ "exportResult"					, "Export Resultat:" },
		
		{ "enumDirTypeAll"					, "Alle" },
		{ "enumDirTypeUni" 					, "Einzeln" },
		{ "enumDirTypeBi" 					, "Dual" },
		
		{ "enumStatusNew"					, "Neu" },
		{ "enumStatusDel" 					, "Gelöscht" },
		{ "enumStatudEdit" 					, "Bearbeitet" },
		
		{ "errorNoModels"  					, "Format kann nicht gelesen werden" },
		{ "errorReadModel"					, "[%1$s] Fehler Lesen Format" },
		{ "errorDirModel"					, "Kann nicht in den Ordner gespeichert werden,\na Datei mit gleichen Namen existiert bereits [%1$s]" },
		{ "errorDefModel"					, "Format kann nicht erzeugt werden [%1$smdl_eur++_fr.txt]" },
		
		{ "errorParseNotKnow"				, "[%1$s] unbekannte Datei" },
		{ "errorParseRead"					, "[%1$s] Lesefehler" },
		{ "errorParseNotFound"				, "[%1$s] Datei nicht gefunden" },
		{ "errorSaveFile"					, "[%1$s] Datei nicht gespeichert" },
		
		{ "errorExportFile"					, "[<b>%1$s</b>]<br><font color=\"red\">Datei Speichern Fehler</font><br>" },
		{ "errorExportUnknownType"			, "\n<u>Unbekannter Typ:</u> <font color=\"red\"><b>" },
		{ "saveSucessMes"					, "[<b>%1$s</b>]<br><font color=\"green\">Export Erfolg</font>%2$s<br>" },
		{ "saveSucess"						, "[<b>%1$s</b>]<br><font color=\"green\">Export Erfolg</font><br>" },
		
		{ "tableColumnNames"				, new String[] { "Länge (°)", "Breite (°)", "Typ", 
				"Geschwindigkeit (Km/h)", "Richtung", "Winkel (°)", "Kommentar", "Status" } },
		
		{ "mapShowCompass"					, "Kompass anzeigen" },
		{ "mapShowCompassSelection"			, "Kompass bei Auswahl anzeigen" },
		{ "mapRoad"							, "Karte" },
		{ "mapHybrid" 						, "Hybrid" },
		{ "mapSatellite" 					, "Satellit" },
		{ "mapTerrain" 						, "Gelände" },
		{ "mapChoice" 						, "Kartenanbieter auswählen" },
		{ "mapGoToAddress"					, "Gehe zur Addresse" },
		{ "mapGoToPosition" 				, "Gehe zu Koordinaten" },
		{ "mapStreetView" 					, "Google Street View" },
		{ "mapOpenStreetView" 				, "Webbrowser an dieser Position öffnen" },
		{ "mapHelp" 						, "Hilfe Karte" },
		{ "mapHelpMessage" 					, "<html>POI's können mit 3 Tastenkombinationen bearbeitet werden:"+
					"<table><tr><td>{<b>Shift+Left click</b>}</td><td>Neuen POI erstellen</td></tr>"+
					"<tr><td>{<b>Shift+Move mouse</b>}</td><td>Position vom ausgewählten POI ändern</td></tr>" +
					"<tr><td>{<b>Alt+Move mouse</b>}</td><td>Winkel vom ausgewählten POI ändern</td></tr></table></html>"},
		
		{ "diagIputTextLabel"				, "<html><b>Bitte eine Adresse eingeben:</b></html>" },
		
		{ "diagLatLngLabel"					, "<html><b>Bitte Längen und Breitengrad eingeben:</b></html>" },
		{ "diagLatLngLabelErr"				, "<html><b><font color=red>Unzulässiger Wert !!!</font></b></html>" },
		{ "diagLongitude"					, "Längengrad:" },
		{ "diagLatitude"					, "Breitengrad:" },
		{ "diagAddressErr"					, "Adresse nicht gefunden" },
		
		{ "diagOptMapConfigSetZone"			, "<html><b>Einstellungen Erkennungsbereich</b></html>" },
		{ "diagOptMapConfigRadAngle"		, "Radius Winkel:" },
		{ "diagOptMapConfigFactorX"			, "Entfernung Faktor X:" },
		{ "diagOptMapConfigFactorY"			, "Entfernung Faktor Y:" },
		{ "diagOptMapConfigColor0"			, "Innen Farbe:" },
		{ "diagOptMapConfigColor1"			, "Außen Farbe:" },
		{ "diagOptMapConfigColor2"			, "Farbe Pfeil:" },
		{ "diagOptMapConfigSetImage"		, "<html><b>Einstellungen Icon für POI</b></html>" },
		{ "diagOptMapConfigSetSpeed"		, "<html><b>Einstellungen Geschwindigkeitstext im POI</b></html>" },
		{ "diagOptMapConfigSetZonePoi"		, "<html><b>Einstellungen POI's</b></html>" },
		{ "diagOptMapConfigSpeedFont"		, "Text Schriftart:" },
		{ "diagOptMapConfigSpeedHelp"		, "<html><b>Der Ausgangspunkt vom Text sind die POI Koordinaten.</b></html>" },
		{ "diagOptMapConfigSpeedX"			, "Position X:" },
		{ "diagOptMapConfigSpeedY"			, "Position Y:" },
		
		{ "diagOptMapConfigZoneBeginPoi"	, "Maximum POI's:" },
		{ "diagOptMapConfigZoneMinSize"		, "Min Größenzone:" },
		{ "diagOptMapConfigZoneFont"		, "Text Schriftart:" },
		
		{ "diagFontChooser"					, "Wählen Sie eine Schriftart" },
		{ "diagFontChooserFont"				, "Schriftart" },
		{ "diagFontChooserFontName"			, "Schriftart" },
		{ "diagFontChooserFontSize"			, "Schriftgrad" },
		{ "diagFontChooserFontEffects"		, "Effekte" },
		{ "diagFontChooserFontColor"		, "Farbe:" },
		{ "diagFontChooserFontPreview"		, "Vorschau" },
		{ "diagFontChooserFontBold"			, "Fett" },
		{ "diagFontChooserFontItalic"		, "Kursiv" },
		{ "diagFontChooserFontUnderline"	, "Unterstrichen" },
		{ "diagFontChooserFontStrikethrough", "Durchgestrichen" },
		{ "diagFontChooserFontSubscript"	, "Tiefgestellt" },
		{ "diagFontChooserFontSuperscript"	, "Hochgestellt" },
		
		{ "diagColorChooser"				, "Wählen Sie eine Farbe" },
		
		{ "diagOptMapConfigSelected"		, "Ausgewählt:" },
		{ "diagOptMapConfigUnknown"			, "Unbekannt:" },
		{ "diagOptMapConfigType"			, "Typ" },
		{ "diagOptMapConfigImgPath"			, "Icon Pfad" },
		{ "diagOptMapConfigAskImg"			, "Neues Bild auswählen" },
		
		{ "tableTipColLon"					, "Akzeptierter Wert -180 bis 180" },
		{ "tableTipColLat" 					, "Akzeptierter Wert -90 bis 90" },
		{ "tableTipColSpeed" 				, "Akzeptierter Wert 0 bis 255" },
		{ "tableTipColAngle" 				, "Akzeptierter Wert 0 bis 359" },
		{ "tableTipColCombo" 				, "Alle Ausgewählten ansehen" },
		
		{ "centerOnMap" 					, "Mitte der Karte" },
		{ "centerZoomOnMap"					, "Mitte und Vergrößern der Karte" },
		{ "dupFindProgress" 				, "Suche läuft..." },
		{ "dupEndFile" 						, "Ende der Datei erreicht" },
		{ "dupEndFileTitle" 				, "Datensuche in Datei" },
		{ "dupSearch" 						, "Suchen" },
		{ "dupCheckIgnore" 					, "Ignorieren entgegengesetzte Winkel" },
		{ "dupCheckAngle" 					, "<html><b>Differenz zwischen entgegengesetzten Winkel:</b></html>" },
		
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
	};
}
