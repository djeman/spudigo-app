package spudigo.localization;

import java.util.ListResourceBundle;

public class LangBundle_hu extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return contents;
	}

  private Object[][] contents = {
    { "statusRowNumbers", "%1$d Sor" }, 
    { "noFileOpen", "Nincs megnyitott fájl" }, 
    { "selectFilesToOpen", "Fálj kiválasztása megnyitásra" }, 
    { "fileNotFound", "Fájl nem található" }, 
    
    { "exportTxt", "Exportálás- 'txt'" }, 
    { "exportSpud", "Exportálás- 'spud'" }, 
    
    { "exportNormal", "Normál fájl exportálása" }, 
    { "exportNormalAll", "Az exportálás normál laponként történik" }, 
    { "exportByType", "Exportálás külön fájlokba típus szerint" }, 
    { "exportAllInOne", "Exportálás mind egy fájlba (AIO)" }, 
    
    { "saveTxt", "Mentés 'txt'" }, 
    { "saveSpud", "Mentés 'spud'" }, 
    
    { "warning", "Figyelmeztetés" }, 
    { "lineUnreadable", "<html>[<b>%1$s</b>] %2$d olvashatatlan sor and %3$d with missing element</html>" }, 
    { "linesUnreadable", "<html>[<b>%1$s</b>] %2$d olvashatatlan sor and %3$d with missing element</html>" }, 
    
    { "error", "Hiba" }, 
    { "errorSelectFile", "Fájl kiválasztás hiba" }, 
    { "errorSelectFolder", "Mappa kiválasztás hiba" }, 
    { "errorModelIn", "Válasszon bemenő formátumot" }, 
    { "errorModelOut", "Válasszon kimenő formátumot" }, 
    
    { "ok", "OK" }, 
    { "none", "Nincs" }, 
    { "cancel", "Mégse" }, 
    { "yes", "Igen" }, 
    { "yesToAll", "Igen,mindet" }, 
    { "no", "Nem" }, 
    { "ignore", "Kihagy" }, 
    { "go", "Ugrás" }, 
    { "apply", "Alkalmaz" }, 
    
    { "askIgoVers", "Melyik IGO verzióhoz hozza létre a fájlt?" }, 
    { "askIgoVersTitle", "iGO verzió választás" }, 
    { "askReplaceFile", "<html>A [<b>%1$s</b>] fájl már létezik.<br><br>Lecseréli?</html>" }, 
    { "askReplaceFileTitle", "Létező fájl" }, 
    
    { "aboutTitle", "Névjegy" }, 
    { "aboutBuildBy", "Magyar nyelv: Zsiga68 & LaMoLa & Cserrobi " }, 
    
    { "askDupDistance", "<html><b>Két gyanús tétel közötti maximális távolság:</b></html" }, 
    { "askDupDistanceTitle", "Ismétlődések keresése" }, 
    
    { "tbNew", "Új" }, 
    { "tbOpen", "Megnyitás..." }, 
    { "tbClose", "Bezár" }, 
    { "tbSave", "Mentés" }, 
    { "tbSaveAs", "Mentés másként.." }, 
    { "tbUndo", "Visszavonás" }, 
    { "tbRedo", "Ismétlés" }, 
    { "tbCopy", "Másolás" }, 
    { "tbCut", "Kivágás" }, 
    { "tbPaste", "Beillesztés" }, 
    { "tbAddLine", "Új sor hozzáadása" }, 
    { "tbDelete", "Törlés" }, 
    { "tbMapToggle", "Térképnézet" }, 
    { "tbViewToggle", "Lapnézet váltása" }, 
    
    { "tbModelIn", "Bemeneti formátum:" }, 
    { "tbModelOut", "Kimeneti formátum:" }, 
    
    { "sbWelcome", "Üdv." }, 
    { "sbDistance", "Távolság:" }, 
    { "sbOSMRoad", "OSM Út:" }, 
    
    { "mbFile", "Fájl" }, 
    { "mbCloseAll", "Mind bezárása" }, 
    { "mbSaveAll", "Mind mentése" }, 
    { "mbExit", "Kilépés" }, 
    { "mbEdit", "Szerkesztés" }, 
    { "mbDupSearch", "Ismétlődések keresése" }, 
    { "mbDupNext", "Következő ismétlés" }, 
    { "mbDupClear", "Keresés törlése" }, 
    { "mbExport", "Exportálás" }, 
    { "mbExportAllTxt", "Mind exportálása- 'txt'" }, 
    { "mbExportAllSpud", "Mind exportálása- 'spud'" },
    { "mbOptions", "Opciók" }, 
	{ "mbLanguageSetting", "Program nyelve" },
    { "mbMapSettings", "Térkép beállítások" }, 
    { "mbMapSelectCenter", "A jelölő a térkép közepén jelenik meg" }, 
	
	{ "langNeedRestartMessage", "Az új nyelv alkalmazásához újra kell indítani a Spudigo programot" },
	{ "langNeedRestartTitle", "Újra kell indítani" },
	
	{ "french", "Francia" },
	{ "german", "Német" },
	{ "spanish", "Spanyol" },
	{ "english", "Angol" },
	{ "hungarian", "Magyar" },
	{ "italian", "Olasz" },
	{ "russian", "Orosz" },
    
    { "askToSave", "<html>[<b>%1$s</b>] fájl módosítva lett.<br><br>Mit kíván tenni?</html>" }, 
    
    { "result", "Eredmény" }, 
    { "exportResult", "Exportálás eredmény:" }, 
    
    { "enumDirTypeAll", "Körkörös" }, 
    { "enumDirTypeUni", "Egyirányú" }, 
    { "enumDirTypeBi", "Kétirányú" }, 
    
    { "enumStatusNew", "Új" }, 
    { "enumStatusDel", "Törölt" }, 
    { "enumStatudEdit", "Szerkesztett" }, 
    
    { "errorNoModels", "A sablonokat nem lehet olvasni" }, 
    { "errorReadModel", "[%1$s] Hiba történt a sablon olvasásakor" }, 
    { "errorDirModel", "A sablon mappa létrehozása sikertelen,\\nugyanaz a fájl már létezik [%1$s]" }, 
    { "errorDefModel", "Nem sikerült létrehozni az alapértelmezett sablont [%1$smdl_eur++_fr.txt]" }, 
    
    { "errorParseNotKnow", "[%1$s] Felismerhetetlen fájl" }, 
    { "errorParseRead", "[%1$s] Olvasási hiba" }, 
    { "errorParseNotFound", "[%1$s] Fájl nem található" }, 
    { "errorSaveFile", "[%1$s] Fájl nincs elmentve" }, 
    
    { "errorExportFile", "[<b>%1$s</b>]<br><font color=\"red\">Fájl mentési hiba</font><br>" }, 
    { "errorExportUnknownType", " <u>Ismeretlen típus:</u> <font color=\"red\"><b>" }, 
    { "saveSucessMes", "[<b>%1$s</b>]<br><font color=\"green\">Sikeres Exportálás</font>%2$s<br>" }, 
    { "saveSucess", "[<b>%1$s</b>]<br><font color=\"green\">Sikeres Exportálás </font><br>" }, 
    
    { "tableColumnNames",  new String[] { "Hosszúság (°)", "Szélesség (°)", "Típus", 
    "Sebesség (Km/h)", "Irány", "Szög (°)", "Megjegyzés", "Állapot" } }, 
    
    { "mapShowCompass", "Iránytű látszik" }, 
	{ "mapShowCompassSelection"			, "Iránytű látszik a kiválasztáskor" },
    { "mapRoad", "Út" }, 
    { "mapHybrid", "Hibrid" }, 
    { "mapSatellite", "Műhold" }, 
    { "mapTerrain", "Domborzat" }, 
    { "mapChoice", "Térkép szolgáltató választás" }, 
    { "mapGoToAddress", "Ugrás a címre" }, 
    { "mapGoToPosition", "Ugrás a koordinátára" }, 
    { "mapStreetView", "Google Térkép" }, 
    { "mapOpenStreetView", "Pozíció megnyitása a böngészőben" }, 
    { "mapHelp", "Térkép súgó" }, 
    { "mapHelpMessage", "<html>A térképen három módon szerkeszthetők a jelölők:" +
			"<table><tr><td>{<b>Shift+bal egér gomb</b>}</td><td>Új jelölő helye</td></tr>" +
			"<tr><td>{<b>Shift+egér mozgatás</b>}</td><td>A kiválasztott jelölő helyének megváltoztatása</td></tr>" +
			"<tr><td>{<b>Alt+egér mozgatás</b>}</td><td>A kiválasztott jelölő szögértékének módosítása</td></tr></table></html>" }, 
    
    { "diagIputTextLabel", "<html><b>Adja meg a címet:</b></html>" }, 
    
    { "diagLatLngLabel", "<html><b>Adja meg a szélessséget és a hosszúságot:</b></html>" }, 
    { "diagLatLngLabelErr", "<html><b><font color=red>Helytelen értékek !!!</font></b></html>" }, 
    { "diagLongitude", "Hosszúság:" }, 
    { "diagLatitude", "Szélesség:" }, 
    { "diagAddressErr", "A cím nem található" }, 
    
    { "diagOptMapConfigSetZone", "<html><b>Érzékelési zóna beállítása</b></html>" }, 
    { "diagOptMapConfigRadAngle", "A körív szögértéke:" }, 
    { "diagOptMapConfigFactorX", "Faktor X:" }, 
    { "diagOptMapConfigFactorY", "Faktor Y:" }, 
    { "diagOptMapConfigColor0", "Belső szín:" }, 
    { "diagOptMapConfigColor1", "Külső szín:" }, 
    { "diagOptMapConfigColor2", "Kiválasztott körvonal:" }, 
    { "diagOptMapConfigSetImage", "<html><b>Jelölők képének beállításai</b></html>" }, 
    { "diagOptMapConfigSetSpeed", "<html><b>Jelölők sebesség szövegének beállítása</b></html>" }, 
    { "diagOptMapConfigSetZonePoi", "<html><b>Jelölők zónáinak beállítása</b></html>" }, 
    { "diagOptMapConfigSpeedFont", "Szöveg betűtípusa:" }, 
    { "diagOptMapConfigSpeedHelp", "<html><b>A jelölők feliratának eredeti pozíciója.</b></html>" }, 
    { "diagOptMapConfigSpeedX", " X Pozíció:" }, 
    { "diagOptMapConfigSpeedY", "Y Pozíció:" }, 
    
    { "diagOptMapConfigZoneBeginPoi", "Maximum jelölők:" }, 
    { "diagOptMapConfigZoneMinSize", "Zónák legkisebb mérete" }, 
    { "diagOptMapConfigZoneFont", "Betűtípus:" }, 
    
    { "diagFontChooser", "Betűkészlet kiválasztása" },
    { "diagFontChooserFont", "Betű" }, 
    { "diagFontChooserFontName", "Betűtípus" }, 
    { "diagFontChooserFontSize", "Betűméret" }, 
    { "diagFontChooserFontEffects", "Effektek" }, 
    { "diagFontChooserFontColor", "Szín:" }, 
    { "diagFontChooserFontPreview", "Előnézet" }, 
    { "diagFontChooserFontBold", "Félkövér" }, 
    { "diagFontChooserFontItalic", "Dőlt" }, 
    { "diagFontChooserFontUnderline", "Aláhúzott" }, 
    { "diagFontChooserFontStrikethrough", "Áthúzott" }, 
    { "diagFontChooserFontSubscript", "Alindex" }, 
    { "diagFontChooserFontSuperscript", "Indexszám" }, 
    
    { "diagColorChooser", "Szín kiválasztása" }, 
    
    { "diagOptMapConfigSelected", "Kiválasztott:" }, 
    { "diagOptMapConfigUnknown", "Ismeretlen:" }, 
    { "diagOptMapConfigType", "Típus" }, 
    { "diagOptMapConfigImgPath", "Kép élérési út" }, 
    { "diagOptMapConfigAskImg", "Új kép kiválasztás" }, 
    
    { "tableTipColLon", "Elfogadott érték -180-tól 180-ig" }, 
    { "tableTipColLat", "Elfogadott érték -90-tól 90-ig" }, 
    { "tableTipColSpeed", "Elfogadott érték 0-tól 255-ig" }, 
    { "tableTipColAngle", "Elfogadott érték 0-tól359-ig" }, 
    { "tableTipColCombo", "Klikkeljen a további lehetőségekhez" }, 
    
    { "centerOnMap", "Térkép közepére" }, 
    { "centerZoomOnMap", "Térkép közepére nagyítva" }, 
    { "dupFindProgress", "Keresés folyamatban..." }, 
    { "dupEndFile", "Elérte a fájl végét" }, 
    { "dupEndFileTitle", "Fájl vége" }, 
    { "dupSearch", "Keresés" }, 
    { "dupCheckIgnore", "Mellőzi a fordított szögértéket" }, 
    { "dupCheckAngle", "<html><b>A fordított szög észlelésére elfogadott különbség:</b></html>" }, 
    
    { "templateEurPlus", ";name       A modell neve megjelenik a listában%1$s%1$s" +
			"name=EUR+ Fr%1$s%1$s" +
			";uid=txt_type:spud_type:label%1$s" +
			";%1$s" +
			";uid        Az azonosító a modellek közötti megfeleltetéshez használt,%1$s" +
			";           bármit is megadhat, amit egy név, szám, ...%1$s" +
			";txt_type   Speedcam típus szöveges fájlban%1$s" +
			";spud_type  Speedcam típus spud fájlban%1$s" +
			";label      Az alkalmazásban megjelenített név, melyet választani tud%1$s%1$s" +
			"0=1:0:Fix sebességmérö kamera%1$s" +
			"1=2:2:Mobil sebességmérö kamera%1$s" +
			"2=3:4:Fizetös útszakasz vége%1$s" +
			"3=4:3:Átlagsebesség mérö kamera%1$s" +
			"4=5:1:Autópálya bejárat%1$s" +
			"5=6:6:Piros lámpa%1$s" +
			"6=7:7:Magaságkorlátozás%1$s" + 
			"7=8:8:Követési távolság mérés%1$s" +
			"8=9:9:Veszélyes útszakasz%1$s" +
			"9=10:10:Alagút%1$s" +
			"10=11:11:Vasúti átjáró%1$s" +
			"11=12:12:30-as zóna%1$s" +
			"12=13:13:STOP%1$s" +
			"13=14:14:Súlykorlátozás%1$s" +
			"14=15:15:Nehézgépkocsi radar%1$s" +
			"15=16:16:Alkoholszint ellenörzés%1$s" +
			"16=17:17:Útbaigazító tábla%1$s" +
			"17=18:18:Lehetséges sebességmérés%1$s" +
			"18=19:19:Elözni tilos%1$s" +
			"19=20:20:Ext1%1$s" +
			"20=21:21:Ext2%1$s" +
			"21=22:22:Ext3%1$s" +
			"22=23:23:Ext4%1$s" +
			"23=69:5:Piroson áthajtást rögzítö kamera%1$s%1$s" +
			";Több kategória egybe konvertálásához%1$s" +
			"32=69:5%1$s" +
			"36=69:5%1$s%1$s" +
			";Ha az uid nem található a célmodellben, akkor nem konvertálható%1$s" }, 
	{ "templateIgoStd", ";name       A modell neve megjelenik a listában%1$s%1$s" +
			"name=iGO Standard%1$s%1$s" +
			";uid=txt_type:spud_type:label%1$s" +
			";%1$s" +
			";uid        Az azonosító a modellek közötti megfeleltetéshez használt,%1$s" +
			";           bármit megadhat, mint Pl. nén, szám stb...%1$s" +
			";txt_type   Speedcam típus szöveges fájlban%1$s" +
			";spud_type  Speedcam típus spud fájlban%1$s" +
			";label      Az alkalmazásban megjelenített név, melyet választani tud%1$s%1$s" +
			"0=1:0:Fix sebességmérö kamera%1$s" +
			"1=5:1:Mobil sebességmérö kamera%1$s" +
			"32=2:2:Jelzölámpába épített sebességmérö kamera%1$s" +
			"3=4:3:Átlagsebesség mérö kamera%1$s" +
			"23=3:4:Piroson áthajtást rögzítö kamera%1$s" +
			"10=6:6:Vasúti átjáró%1$s" +
			"33=7:7:Buszsávot figyelö kamera%1$s" +
			"8=8:8:Baleseti gócpont%1$s" +
			"34=9:9:Iskola%1$s" +
			"35=10:10:Lakott terület kezdete%1$s" +
			"36=11:11:Piroson áthajtást rögzítö és sebességmérö kamera%1$s" +
			"37=12:12:Fizetökapu%1$s" +
			"38=13:13:Kórház, Mentöállomás%1$s" +
			"39=14:14:Tüzoltóság%1$s" +
			"40=15:15:Díjfizetö zóna%1$s" +
			"41=31:31:Veszélyes útszakasz%1$s" }, 
    
    };
}
