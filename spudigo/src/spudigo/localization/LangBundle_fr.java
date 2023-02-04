package spudigo.localization;

import java.util.ListResourceBundle;

public class LangBundle_fr extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return contents;
	}

	private Object[][] contents = {
		{ "statusRowNumbers"   				, "%1$d Enregistrements" },
		{ "noFileOpen"   					, "Aucun fichier ouvert" },
		{ "selectFilesToOpen"   			, "Sélectionnez les fichiers à ouvrir" },
		{ "fileNotFound"   					, "Fichier non trouvé" },
		
		{ "exportTxt"   					, "Exporter 'txt'" },
		{ "exportSpud"   					, "Exporter 'spud'" },
		
		{ "exportNormal"   					, "Exporter normalement un seul fichier" },
		{ "exportNormalAll"   				, "Exporter normalement un fichier par onglet" },
		{ "exportByType"   					, "Exporter dans des fichiers séparés par type" },
		{ "exportAllInOne" 					, "Exporter tout en un fichier" },
		
		{ "saveTxt"   						, "Enregistrer 'txt'" },
		{ "saveSpud"   						, "Enregistrer 'spud'" },
		
		{ "warning"   						, "Attention" },
		{ "lineUnreadable"   				, "<html>[<b>%1$s</b>] %2$d ligne non lisible et %3$d avec des éléments manquants</html>" },
		{ "linesUnreadable"   				, "<html>[<b>%1$s</b>] %2$d lignes non lisibles et %3$d avec des éléments manquants</html>" },
		{ "badheader"   					, "L'entête des colonnes n'existe pas, veuillez le rajouter pour rendre la lecture du fichier possible (X,Y,TYPE,SPEED,DirType,Direction,...)" },
		
		{ "error"   						, "Erreur" },
		{ "errorSelectFile"   				, "Problème de sélection du fichier" },
		{ "errorSelectFolder"   			, "Problème pour sélectionner le répertoire" },
		{ "errorModelIn"   					, "Vous devez choisir un modèle d'entrée" },
		{ "errorModelOut"   				, "Vous devez choisir un modèle de sortie" },
		
		{ "none"	   						, "Aucun" },
		{ "cancel"   						, "Annuler" },
		{ "yes"   							, "Oui" },
		{ "yesToAll"   						, "Oui pour tous" },
		{ "no"   							, "Non" },
		{ "ignore"   						, "Ignorer" },
		{ "go"   							, "Aller à" },
		{ "apply"   						, "Appliquer" },
		
		{ "askIgoVers"						, "Pour quelle version de iGO voulez vous générer le fichier?" },
		{ "askIgoVersTitle"					, "Choix du format" },
		{ "askReplaceFile"					, "<html>Le fichier [<b>%1$s</b>] existe déjà.<br><br>Souhaitez vous le remplacer?</html>" },
		{ "askReplaceFileTitle"				, "Fichier existant" },
		
		{ "aboutTitle"						, "A propos de Spudigo" },
		{ "aboutBuildBy"					, "Fait par" },
		
		{ "askDupDistance"					, "<html><b>Distance maximum entre 2 entrées suspectes:</b></html>" },
		{ "askDupDistanceTitle"				, "Recherche de doublon" },
		
		{ "tbNew"   						, "Nouveau" },
		{ "tbOpen"   						, "Ouvrir..." },
		{ "tbClose"   						, "Fermer" },
		{ "tbSave"   						, "Enregistrer" },
		{ "tbSaveAs"   						, "Enregistrer sous..." },
		{ "tbUndo"   						, "Annuler" },
		{ "tbRedo"   						, "Rétablir" },
		{ "tbCopy"   						, "Copier" },
		{ "tbCut"   						, "Couper" },
		{ "tbPaste"   						, "Coller" },
		{ "tbAddLine"   					, "Ajouter une ligne" },
		{ "tbInsertLine"   					, "Insérer une ligne" },
		{ "tbDelete"   						, "Supprimer" },
		{ "tbMapToggle" 		  			, "Afficher la carte" },
		{ "tbViewToggle"   					, "Changer la vue des onglets" },
		
		{ "tbModelIn" 		  				, "Modèle d'entrée:" },
		{ "tbModelOut"   					, "Modèle de sortie:" },
		
		{ "sbWelcome"   					, "Bienvenue" },
		{ "sbDistance"   					, "Distance:" },
		
		{ "mbFile"   						, "Fichier" },
		{ "mbCloseAll" 						, "Fermer tout" },
		{ "mbSaveAll"   					, "Enregistrer tout" },
		{ "mbExit" 							, "Quitter" },
		{ "mbEdit" 							, "Edition" },
		{ "mbDupSearch"						, "Rechercher des doublons" },
		{ "mbDupNext"						, "Doublon Suivant" },
		{ "mbDupClear"						, "Effacer la recherche" },
		{ "mbExport"						, "Exporter" },
		{ "mbExportAllTxt"   				, "Exporter tout 'txt'" },
		{ "mbExportAllSpud"   				, "Exporter tout 'spud'" },
		{ "mbLanguageSetting"               , "Langue de l'interface" },
		{ "mbMapSettings"					, "Paramètres de la carte" },
		{ "mbMapSelectCenter"				, "Centrer la carte sur la sélection" },
		
		{ "langNeedRestartMessage"          , "Pour appliquer la nouvelle langue, il est nécessaire de redémarrer Spudigo" },
		{ "langNeedRestartTitle"            , "Redémarrage nécessaire" },
		
		{ "french"                          , "Français" },
		{ "german"                          , "Allemand" },
		{ "spanish"                         , "Espagnol" },
		{ "english"                         , "Anglais" },
		{ "hungarian"                       , "Hongrois" },
		{ "italian"                         , "Italien" },
		{ "russian"                         , "Russe" },
		
		{ "askToSave" 						, "<html>Le fichier [<b>%1$s</b>] a été modifié.<br><br>Que voulez vous faire?</html>" },
		
		{ "result"   						, "Résultat" },
		{ "exportResult"					, "Résultat de l'export:" },
		
		{ "enumDirTypeAll"					, "Toutes" },
		{ "enumDirTypeUni" 					, "Unique" },
		{ "enumDirTypeBi" 					, "Double" },
		
		{ "enumStatusNew"					, "Nouveau" },
		{ "enumStatusDel" 					, "Effacé" },
		{ "enumStatudEdit" 					, "Edité" },
		
		{ "errorNoModels"  					, "Impossible de lire les modèles" },
		{ "errorReadModel"					, "[%1$s] Erreur de lecture du modèle" },
		{ "errorDirModel"					, "Impossible de créer le dossier des modèles,\nun fichier du même nom existe déjà [%1$s]" },
		{ "errorDefModel"					, "Impossible de créer le modèle par défaut [%1$smdl_eur++_fr.txt]" },
		
		{ "errorParseNotKnow"				, "[%1$s] Fichier non reconnu" },
		{ "errorParseRead"					, "[%1$s] Erreur de lecture" },
		{ "errorParseNotFound"				, "[%1$s] Fichier non trouvé" },
		{ "errorSaveFile"					, "[%1$s] Fichier non enregistré" },
		
		{ "errorExportFile"					, "[<b>%1$s</b>]<br><font color=\"red\">Erreur d'enregistrement</font><br>" },
		{ "errorExportUnknownType"			, "\n<u>Type inconnu:</u> <font color=\"red\"><b>" },
		{ "saveSucessMes"					, "[<b>%1$s</b>]<br><font color=\"green\">Export réussi</font>%2$s<br>" },
		{ "saveSucess"						, "[<b>%1$s</b>]<br><font color=\"green\">Export réussi</font><br>" },
		
		{ "tableColumnNames"				, new String[] { "Longitude (°)", "Latitude (°)", "Type", 
				"Vitesse (Km/h)", "Direction", "Angle (°)", "Commentaire", "Status" } },
		
		{ "mapShowCompass"					, "Afficher la boussole" },
		{ "mapShowCompassSelection"			, "Afficher la boussole sur la sélection" },
		{ "mapRoad"							, "Route" },
		{ "mapHybrid" 						, "Hybride" },
		{ "mapSatellite" 					, "Satellite" },
		{ "mapTerrain" 						, "Terrain" },
		{ "mapChoice" 						, "Choix fournisseur de carte" },
		{ "mapGoToAddress"					, "Aller à l'adresse" },
		{ "mapGoToPosition" 				, "Aller aux coordonnées" },
		{ "mapStreetView" 					, "Google Street Map" },
		{ "mapOpenStreetView" 				, "Ouvrir votre navigateur internet sur la position" },
		{ "mapHelp" 						, "Aide Carte" },
		{ "mapHelpMessage" 					, "<html>La carte comprend 3 raccourcis pour interagir avec les marqueurs de POI:"+
					"<table><tr><td>{<b>Maj+Clic gauche</b>}</td><td>Permet de placer un nouveau marqueur</td></tr>"+
					"<tr><td>{<b>Maj+Mouvement souris</b>}</td><td>Permet de modifier la position du marqueur sélectionné</td></tr>" +
					"<tr><td>{<b>Alt+Mouvement souris</b>}</td><td>Permet de modifier l'angle du marqueur sélectionné</td></tr></table></html>"},
		
		{ "diagIputTextLabel"				, "<html><b>Veuillez entrer une adresse:</b></html>" },
		
		{ "diagLatLngLabel"					, "<html><b>Veuillez entrer la latitude et la longitude:</b></html>" },
		{ "diagLatLngLabelErr"				, "<html><b><font color=red>Valeurs incorrectes !!!</font></b></html>" },
		{ "diagLongitude"					, "Longitude:" },
		{ "diagLatitude"					, "Latitude:" },
		{ "diagAddressErr"					, "Adresse non trouvée" },
		
		{ "diagOptMapConfigSetZone"			, "<html><b>Réglages de la zone de détection</b></html>" },
		{ "diagOptMapConfigRadAngle"		, "Angle de l'arc:" },
		{ "diagOptMapConfigFactorX"			, "Facteur X:" },
		{ "diagOptMapConfigFactorY"			, "Facteur Y:" },
		{ "diagOptMapConfigColor0"			, "Couleur interne:" },
		{ "diagOptMapConfigColor1"			, "Couleur externe:" },
		{ "diagOptMapConfigColor2"			, "Contour sélection:" },
		{ "diagOptMapConfigSetImage"		, "<html><b>Réglages des images des marqueurs</b></html>" },
		{ "diagOptMapConfigSetSpeed"		, "<html><b>Réglages des textes de vitesse des marqueurs</b></html>" },
		{ "diagOptMapConfigSetZonePoi"		, "<html><b>Réglages des zones de marqueurs</b></html>" },
		{ "diagOptMapConfigSpeedFont"		, "Police du texte:" },
		{ "diagOptMapConfigSpeedHelp"		, "<html><b>L'origine de la position du texte sont les coordonées du marqueur.</b></html>" },
		{ "diagOptMapConfigSpeedX"			, "Position X:" },
		{ "diagOptMapConfigSpeedY"			, "Position Y:" },
		{ "diagOptMapConfigCenterTextX"     , "Centrer le texte sur X" },
		
		{ "diagOptMapConfigZoneBeginPoi"	, "Maximum Marqueurs:" },
		{ "diagOptMapConfigZoneMinSize"		, "Min Taille Zones:" },
		{ "diagOptMapConfigZoneFont"		, "Police du texte:" },
		
		{ "diagFontChooser"					, "Choix de la police" },
		{ "diagFontChooserFont"				, "Police" },
		{ "diagFontChooserFontName"			, "Nom de la police" },
		{ "diagFontChooserFontSize"			, "Taille de la police" },
		{ "diagFontChooserFontEffects"		, "Effets" },
		{ "diagFontChooserFontColor"		, "Couleur:" },
		{ "diagFontChooserFontPreview"		, "Prévisualisation" },
		{ "diagFontChooserFontBold"			, "Gras" },
		{ "diagFontChooserFontItalic"		, "Italique" },
		{ "diagFontChooserFontUnderline"	, "Souligné" },
		{ "diagFontChooserFontStrikethrough", "Barré" },
		{ "diagFontChooserFontSubscript"	, "Indice" },
		{ "diagFontChooserFontSuperscript"	, "Exposant" },
		
		{ "diagColorChooser"				, "Choix de la couleur" },
		
		{ "diagOptMapConfigSelected"		, "Sélection:" },
		{ "diagOptMapConfigUnknown"			, "Inconnu:" },
		{ "diagOptMapConfigType"			, "Type" },
		{ "diagOptMapConfigImgPath"			, "Chemin de l'image" },
		{ "diagOptMapConfigAskImg"			, "Sélectionner une nouvelle image" },
		
		{ "tableTipColLon"					, "Valeur admise -180 à 180" },
		{ "tableTipColLat" 					, "Valeur admise -90 à 90" },
		{ "tableTipColSpeed" 				, "Valeur admise 0 à 255" },
		{ "tableTipColAngle" 				, "Valeur admise 0 à 359" },
		{ "tableTipColCombo" 				, "Cliquer pour voir tous les choix" },
		
		{ "centerOnMap" 					, "Centrer sur la carte" },
		{ "centerZoomOnMap"					, "Centrer et zoomer sur la carte" },
		{ "dupFindProgress" 				, "En cours de recherche..." },
		{ "dupEndFile" 						, "Fin du fichier atteinte" },
		{ "dupEndFileTitle" 				, "Fin de fichier" },
		{ "dupSearch" 						, "Rechercher" },
		{ "dupCheckIgnore" 					, "Ignorer les angles inversés" },
		{ "dupCheckAngle" 					, "<html><b>Difference accepté pour détecter un angle inversé:</b></html>" },
		
		{ "templateEurPlus" 					, ";name       Nom du modèle affiché dans la liste de choix%1$s%1$s" +
				   "name=EUR+ Fr%1$s%1$s" +
				   ";uid=txt_type:spud_type:label%1$s" +
				   ";%1$s" +
				   ";uid        L'id utilisé pour faire la correspondance entre les modèles,%1$s" +
				   ";           vous pouvez y mettre ce que vous voulez un nom, un numéro, ...%1$s" +
				   ";txt_type   Type du radar dans le fichier texte%1$s" +
				   ";spud_type  Type du radar dans le fichier spud%1$s" +
				   ";label      Nom affiché dans le soft, sert à faire les choix%1$s%1$s" +
				   "0=1:0:Radar Fixe%1$s" +
				   "1=2:2:Radar Mobile%1$s" +
				   "2=3:4:Sortie d'Autoroute%1$s" +
				   "3=4:3:Radar de Section%1$s" +
				   "4=5:1:Entrée d'Autoroute%1$s" +
				   "5=6:6:Franch. de Feux Rouge%1$s" +
				   "6=7:7:Hauteur Limite%1$s" +
				   "7=8:8:Radar de Distance%1$s" +
				   "8=9:9:Zone dangereuse%1$s" +
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
				   "23=69:5:Radar de Feux Rouge%1$s%1$s" +
				   ";Utilisé pour convertir plusieurs catégorie en une%1$s" +
				   "32=69:5%1$s" +
				   "36=69:5%1$s%1$s" +
				   ";Si l'uid n'est pas trouvé dans le modèle cible elle ne sera pas convertie%1$s" },
		{ "templateIgoStd" 				, ";name       Nom du modèle affiché dans la liste de choix%1$s%1$s" +
	 			   "name=iGO Standard%1$s%1$s" +
				   ";uid=txt_type:spud_type:label%1$s" +
				   ";%1$s" +
				   ";uid        L'id utilisé pour faire la correspondance entre les modèles,%1$s" +
				   ";           vous pouvez y mettre ce que vous voulez un nom, un numéro, ...%1$s" +
				   ";txt_type   Type du radar dans le fichier texte%1$s" +
				   ";spud_type  Type du radar dans le fichier spud%1$s" +
				   ";label      Nom affiché dans le soft, sert à faire les choix%1$s%1$s" +
				   "0=1:0:Radar Fixe%1$s" +
				   "1=5:1:Radar Mobile%1$s" +
				   "32=2:2:Radar integré%1$s" +
				   "3=4:3:Radar de Section%1$s" +
				   "23=3:4:Radar de Feu rouge%1$s" +
				   "10=6:6:Passage à Niveau%1$s" +
				   "33=7:7:Radar couloir de bus%1$s" +
				   "8=8:8:Zone à risque élevé d'accident%1$s" +
				   "34=9:9:Zone scolaire%1$s" +
				   "35=10:10:Point d'entrée en ville%1$s" +
				   "36=11:11:Radar feu tricolore et radar vitesse%1$s" +
				   "37=12:12:Poste de péage%1$s" +
				   "38=13:13:Hôpitaux ambulances%1$s" +
				   "39=14:14:Caserne de pompiers%1$s" +
				   "40=15:15:Zone de péage urbain%1$s" +
				   "41=31:31:Zone Dangereuse%1$s" },
    };
}
