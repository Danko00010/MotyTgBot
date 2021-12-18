package ru.danko.moty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class Config {
    
	private static File file = new File("config.yml");
	private static Map<String, Object> map;
	public static void saveConfig(Map<String, Object> map) {
		 PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} 
		 DumperOptions options = new DumperOptions(); 
		 options.setIndent(2); 
		 options.setAllowUnicode(false);
		 options.setPrettyFlow(true); 
		 options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); 
		 Yaml yaml = new Yaml(options); 
		 yaml.dump(map, writer); 
	}
	private static Map<String, Object> getDefaltConfig(){
		 Map<String, Object> map = new HashMap<>(); 
		 
		 
		 
		map.put("Token", "YOUR_TELEGRAM_TOKEN");
		map.put("GroupId", "YOUR_GROUP_ID");
		map.put("Message.welcome", "Привет! Я Мотя,🦒 бот для эмпатии. \r\n"
				+ "\r\n"
				+ "Через меня можно отправить запрос на эмпатию или опубликовать предложение эмпатии. Или можно просто найти собеседника для легкого общения.\r\n"
				+ "\n"
				+ "Твое сообщение от имени бота появится в чате ☕️ Кафе Эмпатии (https://t.me/empathycafe)\n"
				+ "\n"
				+ "После этого здесь будут появляться отклики, и ты сможешь выбрать того, кто ближе твоему сердцу в данный момент 🧡, никого при этом не обидев.");
		
		map.put("Message.add_point_out", "Две апельсинки в твой мешочек💰\r\n Теперь у тебя 🍊%summ% апельсинок!");
		map.put("Message.add_point_otk_accept", "Твой отклик был принят! Одна апельсинка в твой мешочек💰 \nТеперь у тебя 🍊%summ% апельсинок!");
		map.put("Message.add_point_otk_put", "Твой отлик был отправлен автору запроса! Одна апельсинка в твой мешочек💰 \nТеперь у тебя 🍊%summ% апельсинок!");
		map.put("Message.Statistic", "Твои апельсинки (В скобках указаны апельсинки за текущий месяц):\r\n"
		 		+ "За 🦒 предл. эмпатии: %sendEmp% 🍊\r\n"
		 		+ "За 🌿 запр. эмпатии: %inviteEmp% 🍊\r\n"
		 		+ "За 💬 собеседников: %companion% 🍊\r\n"
		 		+ "За 🤝 принятые отклики: %accept%🍊\r\n"
		 		+ "\r\n"
		 		+ "Всего апельсинок: %summ%🍊");
		map.put("Message.NotMemberGroup", "Вы не подписанны на наш чат! @danko00010");
		map.put("Message.Profit", "Спасибо! Твой запрос отправлен в чат @danko00010. Ожидай, пока кто-нибудь на него откликнется! \n\nТы можешь в любой момент написать «Закрыть запрос», если он больше не актуален..");
		map.put("Message.textField_SendEmp", "Введи текст твоего предложения.\n\nПримеры:\n\nПривет! Есть желание кому-нибудь поэмпатировать.\n\nВсем привет! Есть время на эмпатию. Свободен следующие два часа. Особенно интересна тема личных отношений.");
		map.put("Message.textField_InviteEmp", "Напиши текст запроса. Расскажи о том что тебе нужна эмпатия, обозначь тему на которую хотелось бы поговорить и интенсивность переживаемых эмоций по шкале от 1 до 10.\n\nПримеры: \n\nПривет! Хотелось бы эмпатии. Тема отношения с мужем. На 8 баллов.\n\nВсе привет! У кого-нибудь есть время на эмпатию? Переживаю насчет сдачи вступительных экзаменов. Примерно на 6 баллов.");
		map.put("Message.textField_companion", "Напиши текст твоего сообщения.\n\nПример: «Хочу поболтать на свободную тему. Или, может быть, кому-то интересна тема трансгуманизма.»");
		
		map.put("ChatSend_SendEmp", "🦒 Предложение эмпатии.\n\n%message%\n\n%gender% %name% %username%");
		map.put("ChatSend_InviteEmp", "🌿 Запрос на эмпатию.\n\n%message%\n\n%gender% %name% %username%");
		map.put("ChatSend_Companion", "💬 Ищу собеседника.\n\n%message%\n\n%gender% %name% %username%");
		
		map.put("Message.otk_accept", "Ты принял отклик от %name% %username%.\n📨Напиши ему личное сообщение.");
		return map;
	}
	public static void set(String key, Object obj) {
		if(map.containsKey(key)) {
			map.remove(key);
		}
		map.put(key, obj);
		saveConfig(map);
	}
	public static Boolean getBoolean(String key) {
		boolean b = (boolean) map.get(key);
		/*if(s.equalsIgnoreCase("true")) {
			b  = true;
		} 
		if(s.equalsIgnoreCase("false")) {
			b = false;
		}*/
		return b;
	}
	public static String getString(String key) {
		return (String) map.get(key);
	}
	@SuppressWarnings("removal")
	public static Double getDouble(String key) {
		Double d = 0.0;
		try {
		d = new Double(getString(key));
		}catch(NumberFormatException ex) {
			System.out.println("Ошибка конфигураций: ошибка формата числа (double): "+key);
			ex.printStackTrace();
		}
		return d;
	}
	@SuppressWarnings("removal")
	public static Long getLong(String key) {
		long lo = 0;
		try {
		lo = new Long(getString(key));
		}catch(NumberFormatException ex) {
			System.out.println("Ошибка конфигураций: ошибка формата числа (long): "+key);
			ex.printStackTrace();
		}
		return lo;
	}
	@SuppressWarnings("removal")
	public static int getInt(String key) {
		int i = 0;
		try {
		i = new Integer(getString(key));
		}catch(NumberFormatException ex) {
			System.out.println("Ошибка конфигураций: ошибка формата числа (int): "+key);
			ex.printStackTrace();
		}
		return i;
	}
	public static List<String> getStringList(String key) {
		List<String> list = new ArrayList<String>();
		for(String s:getString(key).split(",")) {
			list.add(s);
		}
		return list;
	}
	public static void loadConfig(){
		DumperOptions options = new DumperOptions(); 
		 options.setIndent(2); 
		 options.setAllowUnicode(false);
		 options.setPrettyFlow(true); 
		 options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); 
		 Yaml yaml = new Yaml(options); 
		  InputStream inputStream = null;
		 
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			try {
				file.createNewFile();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			saveConfig(getDefaltConfig());
			try {
				inputStream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} 
		 map = yaml.load(inputStream);
		
	}
	public static  Map<String, Object>  getConfig() {
	return map;
	}
	@SuppressWarnings("unchecked")
	public static List<String> getStringListOld(String string) {
		return (List<String>) map.get(string);
	}
}
