package spudigo.localization;

import java.util.ListResourceBundle;

public class LangBundle_ru extends ListResourceBundle {

	@Override
	protected Object[][] getContents() {
		return contents;
	}

	private Object[][] contents = {
		{ "statusRowNumbers"   				, "%1$d Запись" },
		{ "noFileOpen"   					, "Файл не открыт" },
		{ "selectFilesToOpen"   			, "Выберите файлы для открытия" },
		{ "fileNotFound"   					, "Файл не найден" },
		
		{ "exportTxt"   					, "Экспорт в 'txt'" },
		{ "exportSpud"   					, "Экспорт в 'spud'" },
		
		{ "exportNormal"   					, "Обычный экспорт одного файла" },
		{ "exportNormalAll"   				, "Обычный экспорт по одному файлу на вкладку" },
		{ "exportByType"   					, "Экспорт в файлы, разделенные по типу" },
		{ "exportAllInOne" 					, "Экспортировать все в один файл" },
		
		{ "saveTxt"   						, "Сохранить в 'txt'" },
		{ "saveSpud"   						, "Сохранить в 'spud'" },
		
		{ "warning"   						, "Внимание" },
		{ "lineUnreadable"   				, "<html>[<b>%1$s</b>] %2$d нечитаемая строка и %3$d с отсутствующими элементами</html>" },
		{ "linesUnreadable"   				, "<html>[<b>%1$s</b>] %2$d неразборчивые строки и %3$d с отсутствующими элементами</html>" },
		{ "badheader"   					, "Заголовок столбца не существует, пожалуйста, добавьте его снова, чтобы сделать возможным чтение файла (X,Y,TYPE,SPEED,DirType,Direction,...)" },
		
		{ "error"   						, "Ошибка" },
		{ "errorSelectFile"   				, "Проблема с выбором файла" },
		{ "errorSelectFolder"   			, "Проблема с выбором папки" },
		{ "errorModelIn"   					, "Вам необходимо выбрать шаблон ввода" },
		{ "errorModelOut"   				, "Вам необходимо выбрать шаблон вывода" },
		
		{ "ok"	   							, "OK" },
		{ "none"	   						, "Ни один" },
		{ "cancel"   						, "Отменить" },
		{ "yes"   							, "Да" },
		{ "yesToAll"   						, "Да для всех" },
		{ "no"   							, "Нет" },
		{ "ignore"   						, "Игнорировать" },
		{ "go"   							, "Поехали" },
		{ "apply"   						, "Применить" },
		
		{ "askIgoVers"						, "Для какой версии iGO вы хотите сгенерировать файл?" },
		{ "askIgoVersTitle"					, "Выбор формата" },
		{ "askReplaceFile"					, "<html>The file [<b>%1$s</b>] уже существует.<br><br>Хотите его заменить?</html>" },
		{ "askReplaceFileTitle"				, "Существующий файл" },
		
		{ "aboutTitle"						, "О Spudigo" },
		{ "aboutBuildBy"					, "Создано" },
		
		{ "askDupDistance"					, "<html><b>Максимальное расстояние между 2 подозрительными записями:</b></html>" },
		{ "askDupDistanceTitle"				, "Поиск дубликатов" },
		
		{ "tbNew"   						, "Новый" },
		{ "tbOpen"   						, "Открыть..." },
		{ "tbClose"   						, "Закрыть" },
		{ "tbSave"   						, "Сохранить" },
		{ "tbSaveAs"   						, "Сохранить как..." },
		{ "tbUndo"   						, "Отменить" },
		{ "tbRedo"   						, "Восстановить" },
		{ "tbCopy"   						, "Скопировать" },
		{ "tbCut"   						, "Вырезать" },
		{ "tbPaste"   						, "Вставить" },
		{ "tbAddLine"   					, "Добавить строку" },
		{ "tbDelete"   						, "Удалить" },
		{ "tbMapToggle" 		  			, "Показать карту" },
		{ "tbViewToggle"   					, "Изменить вид вкладок" },
		
		{ "tbModelIn" 		  				, "Шаблон ввода:" },
		{ "tbModelOut"   					, "Шаблон вывода:" },
		
		{ "sbWelcome"   					, "Добро пожаловать" },
		{ "sbDistance"   					, "Расстояние:" },
		{ "sbOSMRoad"   					, "Дорога в OSM:" },
		
		{ "mbFile"   						, "Файл" },
		{ "mbCloseAll" 						, "Закрыть Всё" },
		{ "mbSaveAll"   					, "Сохранить Всё" },
		{ "mbExit" 							, "Выход" },
		{ "mbEdit" 							, "Редактировать" },
		{ "mbDupSearch"						, "Поиск дубликатов" },
		{ "mbDupNext"						, "Следующий дубликат" },
		{ "mbDupClear"						, "Очистить поиск" },
		{ "mbExport"						, "Экспортировать" },
		{ "mbExportAllTxt"   				, "Экспортировать всё в 'txt'" },
		{ "mbExportAllSpud"   				, "Экспортировать всё в 'spud'" },
		{ "mbOptions"						, "Опции" },
		{ "mbLanguageSetting"               , "Язык интерфейса" },
		{ "mbMapSettings"					, "Настройки карты" },
		{ "mbMapSelectCenter"				, "Центрировать карту по выделенному" },
		
		{ "langNeedRestartMessage"          , "Чтобы применить новый язык, необходимо перезапустить Spudigo" },
		{ "langNeedRestartTitle"            , "Требуется перезагрузка" },
		
		{ "french"                          , "Французский" },
		{ "german"                          , "Немецкий" },
		{ "spanish"                         , "Испанский" },
		{ "english"                         , "Английский" },
		{ "hungarian"                       , "Венгерский" },
		{ "italian"                         , "Итальянский" },
		{ "russian"                         , "Русский" },
		
		{ "askToSave" 						, "<html>[<b>%1$s</b>] файл был изменен.<br><br>Что вы хотите сделать?</html>" },
		
		{ "result"   						, "Результат" },
		{ "exportResult"					, "Результат экспорта:" },
		
		{ "enumDirTypeAll"					, "Все" },
		{ "enumDirTypeUni" 					, "Отдельный" },
		{ "enumDirTypeBi" 					, "Двойной" },
		
		{ "enumStatusNew"					, "Новый" },
		{ "enumStatusDel" 					, "Удалённый" },
		{ "enumStatudEdit" 					, "Редактированный" },
		
		{ "errorNoModels"  					, "Не удается прочитать шаблоны" },
		{ "errorReadModel"					, "[%1$s] Ошибка при чтении шаблона" },
		{ "errorDirModel"					, "Невозможно создать папку шаблонов, файл с таким именем уже существует [%1$s]" },
		{ "errorDefModel"					, "Не удается создать шаблон по умолчанию [%1$smdl_eur++_fr.txt]" },
		
		{ "errorParseNotKnow"				, "[%1$s] Нераспознанный файл" },
		{ "errorParseRead"					, "[%1$s] Ошибка чтения" },
		{ "errorParseNotFound"				, "[%1$s] Файл не найден" },
		{ "errorSaveFile"					, "[%1$s] Файл не сохранён" },
		
		{ "errorExportFile"					, "[<b>%1$s</b>]<br><font color=\"red\">Ошибка сохранения файла</font><br>" },
		{ "errorExportUnknownType"			, "\n<u>Unknown type:</u> <font color=\"red\"><b>" },
		{ "saveSucessMes"					, "[<b>%1$s</b>]<br><font color=\"green\">Успешный экспорт</font>%2$s<br>" },
		{ "saveSucess"						, "[<b>%1$s</b>]<br><font color=\"green\">Успешный экспорт</font><br>" },
		
		{ "tableColumnNames"				, new String[] { "Долгота (°)", "Широта (°)", "Тип", 
				"Скорость (Км/ч)", "Направление", "Угол (°)", "Комментарий", "Состояние" } },
				
		{ "mapShowCompass"					, "Показать Компас" },
		{ "mapShowCompassSelection"			, "Показывать компас на выделенном участке" },
		{ "mapRoad"							, "Карта дорог" },
		{ "mapHybrid" 						, "Гибридная" },
		{ "mapSatellite" 					, "Спутниковая" },
		{ "mapTerrain" 						, "Ланшафтная" },
		{ "mapChoice" 						, "Выбрать поставщика карт" },
		{ "mapGoToAddress"					, "Перейти по адресу" },
		{ "mapGoToPosition" 				, "Перейти к координатам" },
		{ "mapStreetView" 					, "Карты Google" },
		{ "mapOpenStreetView" 				, "Откройте свой интернет-браузер в нужном месте" },
		{ "mapHelp" 						, "Помошь по карте" },
		{ "mapHelpMessage" 					, "<html>Карта включает в себя три ярлыка для взаимодействия с маркерами POI:"+
				"<table><tr><td>{<b>Shift+Левая кнопка мыши</b>}</td><td>Позволяет разместить новый маркер</td></tr>"+
				"<tr><td>{<b>Shift+Движение мыши</b>}</td><td>Позволяет изменить положение выбранного маркера</td></tr>" +
				"<tr><td>{<b>Alt+Движение мыши</b>}</td><td>Позволяет изменить угол наклона выбранного маркера</td></tr></table></html>"},
		
		{ "diagIputTextLabel"				, "<html><b>Пожалуйста, введите адрес:</b></html>" },
		
		{ "diagLatLngLabel"					, "<html><b>Пожалуйста, введите широту и долготу:</b></html>" },
		{ "diagLatLngLabelErr"				, "<html><b><font color=red>Неправильные значения !!!</font></b></html>" },
		{ "diagLongitude"					, "Долгота:" },
		{ "diagLatitude"					, "Широта:" },
		{ "diagAddressErr"					, "Адрес не найден" },
		
		{ "diagOptMapConfigSetZone"			, "<html><b>Настройки зоны обнаружения</b></html>" },
		{ "diagOptMapConfigRadAngle"		, "Угол сектора" },
		{ "diagOptMapConfigFactorX"			, "По X:" },
		{ "diagOptMapConfigFactorY"			, "По Y:" },
		{ "diagOptMapConfigColor0"			, "Цвет внутри:" },
		{ "diagOptMapConfigColor1"			, "Цвет снаружи:" },
		{ "diagOptMapConfigColor2"			, "Выделение контура:" },
		{ "diagOptMapConfigSetImage"		, "<html><b>Настройки изображений маркеров</b></html>" },
		{ "diagOptMapConfigSetSpeed"		, "<html><b>Настройки скорости текста маркеров</b></html>" },
		{ "diagOptMapConfigSetZonePoi"		, "<html><b>Настройки областей маркеров</b></html>" },
		{ "diagOptMapConfigSpeedFont"		, "Шрифт текста:" },
		{ "diagOptMapConfigSpeedHelp"		, "<html><b>Исходной позицией текста являются координаты маркера.</b></html>" },
		{ "diagOptMapConfigSpeedX"			, "Позиция по X:" },
		{ "diagOptMapConfigSpeedY"			, "Позиция по Y:" },
		{ "diagOptMapConfigCenterTextX"     , "Центрировать текст по X" },
		
		{ "diagOptMapConfigZoneBeginPoi"	, "Максимум маркеров:" },
		{ "diagOptMapConfigZoneMinSize"		, "Минимальный размер Зон:" },
		{ "diagOptMapConfigZoneFont"		, "Шрифт текста:" },
		
		{ "diagFontChooser"					, "Выбор шрифта" },
		{ "diagFontChooserFont"				, "Шрифт" },
		{ "diagFontChooserFontName"			, "Имя шрифта" },
		{ "diagFontChooserFontSize"			, "Размер шрифта" },
		{ "diagFontChooserFontEffects"		, "Эффекты" },
		{ "diagFontChooserFontColor"		, "Цвет:" },
		{ "diagFontChooserFontPreview"		, "Предварительный просмотр" },
		{ "diagFontChooserFontBold"			, "Жирный" },
		{ "diagFontChooserFontItalic"		, "Italic" },
		{ "diagFontChooserFontUnderline"	, "Подчёркивание" },
		{ "diagFontChooserFontStrikethrough", "Зачеркнутый" },
		{ "diagFontChooserFontSubscript"	, "Подстрочный индекс" },
		{ "diagFontChooserFontSuperscript"	, "Надстрочный индекс" },
		
		{ "diagColorChooser"				, "Выбор цвета" },
		
		{ "diagOptMapConfigSelected"		, "Выбор:" },
		{ "diagOptMapConfigUnknown"			, "Неизвестный:" },
		{ "diagOptMapConfigType"			, "Тип" },
		{ "diagOptMapConfigImgPath"			, "Путь к изображению" },
		{ "diagOptMapConfigAskImg"			, "Выбрать новое изображение" },
		
		{ "tableTipColLon"					, "Допустимое значение от -180 до 180" },
		{ "tableTipColLat" 					, "Допустимое значение от -90 до 90" },
		{ "tableTipColSpeed" 				, "Допустимое значение от 0 до 255" },
		{ "tableTipColAngle" 				, "Допустимое значение от 0 до 359" },
		{ "tableTipColCombo" 				, "Нажмите, чтобы просмотреть все варианты" },
		
		{ "centerOnMap" 					, "Центрировать на карте" },
		{ "centerZoomOnMap"					, "Центрирование и масштабирование карты" },
		{ "dupFindProgress" 				, "Поиск продолжается..." },
		{ "dupEndFile" 						, "Достигнут конец файла" },
		{ "dupEndFileTitle" 				, "Конец файла" },
		{ "dupSearch" 						, "Поиск" },
		{ "dupCheckIgnore" 					, "Игнорировать обратные углы" },
		{ "dupCheckAngle" 					, "<html><b>Разница принята для определения обратного угла:</b></html>" },
		
		{ "templateEurPlus" 					, ";name       Название модели, отображаемое в списке вариантов%1$s%1$s" +
				   "name=EUR+ Fr%1$s%1$s" +
				   ";uid=txt_type:spud_type:label%1$s" +
				   ";%1$s" +
				   ";uid        Идентификатор, используемый для сопоставления между шаблонами,%1$s" +
				   ";           вы можете поместить туда все, что захотите, имя, номер,...%1$s" +
				   ";txt_type   Тип радара в txt файле%1$s" +
				   ";spud_type  Тип радара в spud файле%1$s" +
				   ";label      Имя, отображаемое в приложении, используется для выбора%1$s%1$s" +
				   "0=1:0:Стационарная камера%1$s" +
				   "1=2:2:Мобильная камера%1$s" +
				   "2=3:4:Съезд с автомагистрали%1$s" +
				   "3=4:3:Камера средней скорости%1$s" +
				   "4=5:1:Въезд на автомагистраль%1$s" +
				   "5=6:6:Камера проезда на красный свет%1$s" +
				   "6=7:7:Ограничение по высоте%1$s" +
				   "7=8:8:Радар дальнего действия%1$s" +
				   "8=9:9:Опасная зона%1$s" +
				   "9=10:10:Туннельный радар%1$s" +
				   "10=11:11:Железнодорожный переезд%1$s" +
				   "11=12:12:Зона 30%1$s" +
				   "12=13:13:Stop%1$s" +
				   "13=14:14:Ограничение по Ширине%1$s" +
				   "14=15:15:Радар для тяжелых условий эксплуатации%1$s" +
				   "15=16:16:Проверка содержания алкоголя в крови%1$s" +
				   "16=17:17:Информационное табло%1$s" +
				   "17=18:18:Сомнительный Радар%1$s" +
				   "18=19:19:Зона обгона%1$s" +
				   "19=20:20:Ext1%1$s" +
				   "20=21:21:Ext2%1$s" +
				   "21=22:22:Ext3%1$s" +
				   "22=23:23:Ext4%1$s" +
				   "23=69:5:Камера контроля скорости и проезда на красный цвет%1$s%1$s" +
				   ";Используется для преобразования нескольких категорий в одну%1$s" +
				   "32=69:5%1$s" +
				   "36=69:5%1$s%1$s" +
				   ";Если идентификатор модели не найден, он не будет преобразован%1$s" },
		{ "templateIgoStd" 				, ";name       Название модели, отображаемое в списке выбора%1$s%1$s" +
	 			   "name=iGO Standard%1$s%1$s" +
				   ";uid=txt_type:spud_type:label%1$s" +
				   ";%1$s" +
				   ";uid        Идентификатор, используемый для установления соответствия между моделями,%1$s" +
				   ";           вы можете поместить туда все, что захотите, имя, номер,...%1$s" +
				   ";txt_type   Тип радара в txt файле%1$s" +
				   ";spud_type  Тип радара в spud файле%1$s" +
				   ";label      Имя, отображаемое в приложении, используется для выбора%1$s%1$s" +
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
