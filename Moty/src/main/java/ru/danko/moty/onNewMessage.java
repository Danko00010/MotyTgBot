package ru.danko.moty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.response.GetChatMemberResponse;

import ru.danko.moty.objects.Respon;
import ru.danko.moty.objects.User;
import ru.danko.moty.objects.Users;
import ru.danko.moty.tgbot.Bot;
import ru.danko.moty.tgbot.events.MessageEvnet.onNewChatMessageListener;

public class onNewMessage implements onNewChatMessageListener {
private static HashMap<Long, Respon> respons = new HashMap<Long, Respon>();
	public static HashMap<Long, Respon> getRespons() {
		return respons;
	}
	@SuppressWarnings("removal")
	@Override
	public void onCommandEvent(Update up, String msg) {
		
		if(up.message() == null) return;
		if(up.message().from() == null) return;
		if(msg == null) return;
		if(Config.getString("GroupId").equals("YOUR_GROUP_ID")) {
			if(up.message().chat().id()<0 && msg.equals("/select")) {
				Config.set("GroupId", ""+up.message().chat().id());
				return;
			}
			System.out.println("Ошибка, бот не добавлен в группу!");
			return;
		}
		Long userId = up.message().from().id();
		if(up.message().chat().id()<0) return;
		if(msg.equals("null")) return;
		String cmd = msg.split(" ")[0];

    	
    	String MainChatId = Config.getString("GroupId");
		String[] args = msg.replace(cmd+"", "").split(" ");
GetChatMemberResponse chatMain = Bot.getChatMembers(MainChatId, userId);
String status = chatMain.chatMember().status().name();
if(!(status.equals("member")|| status.equals("creator") || status.equals("administrator"))) {//Message.NotMemberGroup
	String cnfMsg = Config.getString("Message.NotMemberGroup");
	Bot.sendMessage(up, cnfMsg);
	return;
}

	if(msg.equals("👨 Мужчина")) {
		if(!Users.check(userId)){
			return;
		}
		User u = Users.conUserName(userId);
		if(u.getStatus()==-1){
			statusM1(up, u, true);
		}
		return;
	}
if(msg.equals("👩 Женщина")) {
	if(!Users.check(userId)){
		return;
	}
	User u = Users.conUserName(userId);
	if(u.getStatus()==-1){
		statusM1(up, u, false);
	}
	return;
	}
	switch (cmd.toLowerCase()) {
	case "/change_gender":{
		if(!Users.check(userId)){
			return;
		}
		Bot.sendMessage(up, "Укажи свой пол.", utils.getKeyBoard("👨 Мужчина","👩 Женщина"));
		User u = Users.conUserName(userId);
		u.setStatus(-1);
		Users.save(u);
		break;
	}
	case "/feedback":{
		if(args.length == 1) {
			Bot.sendMessage(up, "Помести твоё обращение после команды\r\n"
					+ "\r\n"
					+ "Примеры:\r\n"
					+ "/feedback Хотелось бы больше гибкости при запросах, не хватает возможности изменить тип запроса после его отправки\r\n"
					+ "\r\n"
					+ "/feedback Бот сломался, я не могу отправлять сообщения(");
			return;
		}
		String messeg = msg.replace(cmd, "");
		feedBack(messeg, Users.conUserName(up.message().from().id()).getUserName());
		break;
	}
	case "/stats":{
		if(!Users.check(userId)){
			return;
		}
		User us = Users.conUserName(userId);
		String cnfMsg = Config.getString("Message.Statistic");
		cnfMsg = cnfMsg.replace("%sendEmp%", us.getSt(1)+"").replace("inviteEmp", us.getSt(1)+"").replace("%companion%", us.getSt(3)+"").replace("%accept%", us.getSt(0)+"").replace("%summ%", us.summStatic()+"");
	 Bot.sendMessage(userId, cnfMsg);
	 	break;
	}
	case "/start":{
		start(up);
	 	break;
	}
	case "начать":{
		start(up);
		break;
	}
	case "/options":{
		long chatid = new Long(Config.getString("GroupId"));
		List<ChatMember> res =	Bot.getAdministrators(chatid).administrators();
		for(ChatMember mem:res) {
			System.out.println(mem.user().id() +" : "+up.message().from().id());
			if((mem.user().id()+"").equals(up.message().from().id()+"")) {
				
				Map<String, Object> map = Config.getConfig();
				for(String s: map.keySet()) {
					if(s.equals("GroupId") || s.equals("Users") || s.equals("Token")){
						continue;
					}
					String msgC = Config.getString(s);
					s = s.replace(".", ":");
					String[] buts = ("Редактировать;"+s).split("@");
					Bot.sendMessage(up.message().from().id(), "Сообщение:\n \n"+msgC, utils.getKeyboardMarkup(buts));
				}
			}
		}
		return;
	}
	case "просто":{
		
		if(!Users.check(userId)){
			return;
		}
	
		User us = Users.conUserName(userId);
		if(us.getStatus()==1) {
			String cnfM = Config.getString("Message.textField_companion");
			Bot.sendMessage(up, cnfM,utils.getKeyBoard("Отменить", null));
			us.setStatus(300);
			Users.save(us);
		}
		break;
	}
	case "запросить":{
		if(!Users.check(userId)){
			return;
		}
	
		User us = Users.conUserName(userId);
		if(us.getStatus()==1) {//textField_InviteEmp
			String cnfM = Config.getString("Message.textField_InviteEmp");
			Bot.sendMessage(up, cnfM,utils.getKeyBoard("Отменить", null));
			us.setStatus(200);
			Users.save(us);
		}
		break;
	}
	case "предложить":{
		if(!Users.check(userId)){
			return;
		}
	
		User us = Users.conUserName(userId);
		if(us.getStatus()==1) {
			String cnfM = Config.getString("Message.textField_SendEmp");
			Bot.sendMessage(up, cnfM,utils.getKeyBoard("Отменить", null));
			us.setStatus(100);
			Users.save(us);
		}
		break;
	}//Закрыть
	case "закрыть":{
		if(!Users.check(userId)){
			return;
		}
		User us = Users.conUserName(userId);
		if(us.getStatus()>=1) {

			
			if(respons.containsKey(userId)) {
				Respon res  = respons.get(userId);
			res.close();
			respons.remove(userId);
			us.setStatus(0);
			Users.save(us);
			Bot.sendMessage(up, "Запрос закрыт",utils.getKeyBoard("Начать заново", null));
			return;
			}
			Bot.sendMessage(up, "У вас нет активного запроса",utils.getKeyBoard("Начать", null));
			return;
		}
		break;
	}
	case "отменить":{
		if(!Users.check(userId)){
			return;
		}
		User us = Users.conUserName(userId);
		if(us.getStatus()>=1) {
			us.setStatus(0);
			Users.save(us);
			Bot.sendMessage(up, "Запрос отменён",utils.getKeyBoard("Начать заново", null));
		}
		break;
	}

	default:
		if(!Users.check(userId)){
			return;
		}
		User us = Users.conUserName(userId);
		int st = us.getStatus();
		if(us.getStatus() == 0) {
		Bot.sendMessage(up, "Если тебе нужно найти того, кому можно высказаться, нажми на кнопку ниже.",utils.getKeyboardMarkup("Начать;start".split("@")));
		Bot.sendMessage(up, "",utils.getKeyBoard("Начать", ""));
		}
		if(st==300) {
			String[] spl = msg.split("");
			int i = spl.length;
			if(i<15 || i>600) {
				Bot.sendMessage(up, "Ты ввел некорректный запрос, необходимо ввести хотя бы 15, но не более 600 символов.");
				return;
			}
			Respon res = new Respon(msg, new Long(Config.getString("GroupId")), us, up.message().chat().id());
			respons.put(userId, res);
			Bot.sendMessage(up, "Укажи сколько времени будет актуален твой запрос в часах (от 1 до 24).");
			us.setStatus(301);
			return;
		}
		if(st==100) {
			String[] spl = msg.split("");
			int i = spl.length;
			if(i<15 || i>600) {
				Bot.sendMessage(up, "Ты ввел некорректный запрос, необходимо ввести хотя бы 15, но не более 600 символов.");
				return;
			}
			Respon res = new Respon(msg, new Long(Config.getString("GroupId")), us, up.message().chat().id());
			respons.put(userId, res);
			Bot.sendMessage(up, "Укажи сколько времени будет актуален твой запрос в часах (от 1 до 24).");
			us.setStatus(101);
			return;
		}
		if(st==200) {
			String[] spl = msg.split("");
			int i = spl.length;
			if(i<15 || i>600) {
				Bot.sendMessage(up, "Ты ввел некорректный запрос, необходимо ввести хотя бы 15, но не более 600 символов.");
				return;
			}
			Respon res = new Respon(msg, new Long(Config.getString("GroupId")), us, up.message().chat().id());
			respons.put(userId, res);
			Bot.sendMessage(up, "Укажи сколько времени будет актуален твой запрос в часах (от 1 до 24).");
			us.setStatus(201);
			return;
		}
		if(st==101) {
			String[] spl = msg.split(" ");
			int i = spl.length;
			if(i!=1) {
				Bot.sendMessage(up, "Ты ввел некорректное значение. 🧐");
				return;
			}
			int time = new Integer(spl[0]);
			if(time<1 || time>24) {
				Bot.sendMessage(up, "Ты ввел некорректное число. 🧐");
				return;
			}
			Respon res = respons.get(userId);
			res.setTime(time);
			respons.remove(userId);
			respons.put(userId, res);
			String profit = Config.getString("Message.Profit");
			Bot.sendMessage(up, profit, utils.getKeyBoard("Закрыть запрос", null));
			us.setStatus(102);
			Users.save();
			sendResponse(res, us, up);
		}
		if(st==201) {
			String[] spl = msg.split(" ");
			int i = spl.length;
			if(i!=1) {
				Bot.sendMessage(up, "Ты ввел некорректное значение. 🧐");
				return;
			}
			int time = new Integer(spl[0]);
			if(time<1 || time>24) {
				Bot.sendMessage(up, "Ты ввел некорректное число. 🧐");
				return;
			}
			Respon res = respons.get(userId);
			res.setTime(time);
			respons.remove(userId);
			respons.put(userId, res);
			String profit = Config.getString("Message.Profit");
			Bot.sendMessage(up, profit, utils.getKeyBoard("Закрыть запрос", null));
			us.setStatus(202);
			Users.save();
			sendResponse(res, us, up);
		}
		if(st==301) {
			String[] spl = msg.split(" ");
			int i = spl.length;
			if(i!=1) {
				Bot.sendMessage(up, "Ты ввел некорректное значение. 🧐");
				return;
			}
			int time = new Integer(spl[0]);
			if(time<1 || time>24) {
				Bot.sendMessage(up, "Ты ввел некорректное число. 🧐");
				return;
			}
			Respon res = respons.get(userId);
			res.setTime(time);
			respons.remove(userId);
			respons.put(userId, res);
			String profit = Config.getString("Message.Profit");
			Bot.sendMessage(up, profit, utils.getKeyBoard("Закрыть запрос", null));
			us.setStatus(302);
			Users.save();
			sendResponse(res, us, up);
		}
		if(us.getRed() != null) {
			Config.set(us.getRed(), msg);
			Bot.sendMessage(up, "Значение установленно!");
			String[] buts = ("Редактировать;"+(us.getRed().replace(".", ":"))).split("@");
			us.setRed(null);
			us.setRedMsgId(0);
			Bot.editCallBackQuery(up.message().chat().id(), us.getRedMsgId(), utils.getKeyboardMarkup(buts));
		}
		break;
	}
//	Bot.deleteMessage(up.message().chat().id(), up.message().messageId());
	}
	private void feedBack(String message, String username) {
		@SuppressWarnings("removal")
		long chatid = new Long(Config.getString("GroupId"));
			List<ChatMember> res =	Bot.getAdministrators(chatid).administrators();
		for(ChatMember mem:res) {
			
			Bot.sendMessage(mem.user().id(), "FeedBack(@"+username+"): "+message);
		}
	}
	private void sendResponse(Respon res, User us, Update up) {
		int st = us.getStatus();
		if(st==102) {
			String gender = "";
			if(us.getGender()) {
				gender = "👦";
			}else {
				gender = "👩";
			}
			String firstName = up.message().from().firstName();
			
		String meeees = Config.getString("ChatSend_SendEmp").replace("%message%", res.getMessage()).replace("%gender%", gender).replace("%name%", firstName).replace("%username%", "@"+us.getUserName());
		res.open(meeees);
		respon(us, up, res);
		return;
		}
		if(st==202) {
			String gender = "";
			if(us.getGender()) {
				gender = "👦";
			}else {
				gender = "👩";
			}
			String firstName = up.message().from().firstName();
			
			String meeees = Config.getString("ChatSend_InviteEmp").replace("%message%", res.getMessage()).replace("%gender%", gender).replace("%name%", firstName).replace("%username%", "@"+us.getUserName());
			
		res.open(meeees);
		respon(us, up, res);
		return;
		}
		if(st==302) {
			String gender = "";
			if(us.getGender()) {
				gender = "👦";
			}else {
				gender = "👩";
			}
			String firstName = up.message().from().firstName();
			String meeees = Config.getString("ChatSend_Companion").replace("%message%", res.getMessage()).replace("%gender%", gender).replace("%name%", firstName).replace("%username%", "@"+us.getUserName());
			
		res.open(meeees);
		respon(us, up, res);
		return;
		}
		Bot.sendMessage(up, "Я вас не понимаю :|");
		return;
	}
	private void statusM1(Update up, User u, boolean b) {
		u.setGender(b);
		u.setStatus(0);
		Users.save(u);
		start(up);
	}
	private void respon(User us, Update up, Respon res) {
		respons.remove(us.getId());
		respons.put(us.getId(), res);
		Long time = (long) ((60000*60)*res.getTime());
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(!respons.containsValue(res)) {
					return;
				}
			    us.setStatus(0);
			    Users.save(us);
			   res.close();
			   Bot.editCallBackQuery(res.getChatId(), res.getMessageId(), new InlineKeyboardMarkup());
			   respons.remove(us.getId());
			   
				Bot.sendMessage(up, "Твой запрос был закрыт, т.к. истекло время, на которое он был опубликован и не нашлось тех, кто откликнулся. Либо ты никого не выбрал. 😔", utils.getKeyBoard("Начать заного", null));
			}
		}).start();
	}
	public void start(Update up) {
		if(up.message().chat().id()<1) return;
		Long userId = up.message().from().id();
		if(!Users.check(userId)){
			if(up.message().from().username()== null) {
				Bot.sendMessage(up, "Для того, чтобы люди могли откликнуться, для начала заполни поле username в настройках Telegram.");
				return;
			}
			Bot.sendMessage(up, Config.getString("Message.welcome"));
			System.out.println(up.message().from());
			User user = new User(userId, up.message().from().username(), -2, up.message().from().firstName());
			Users.addUser(user);
		}
		User u = Users.conUserName(userId);
		if(u.getStatus() == -2) {
			if(up.message().from().username()== null) {
				Bot.sendMessage(up, "Для того, чтобы люди могли откликнуться, для начала заполни поле username в настройках Telegram.");
				return;
			}
			Bot.sendMessage(up, "Укажи свой пол.", utils.getKeyBoard("👨 Мужчина","👩 Женщина"));
			u.setStatus(-1);
			Users.save(u);
			return;
		}
		
			int status = u.getStatus();
			if(status != 0) {
				if(status >= 1) {
					Bot.sendMessage(up, "У тебя есть незавершенный запрос, нажми на кнопку Отменить, если передумал.");
					return;
				}
				if(status == -1) {
					if(up.message().from().username()== null) {
						Bot.sendMessage(up, "Для того, чтобы люди могли откликнуться, для начала заполни поле username в настройках Telegram.");
						return;
					}
					Bot.sendMessage(up, "Укажи свой пол.", utils.getKeyBoard("👨 Мужчина","👩 Женщина"));
					return;
				}
			}
	//"Предложить;putInvite@Запросить;callInvite@Просто пообщаться;justInvite@Отменить;cancel".split("@")
		Bot.sendMessage(up, "Ты хочешь предложить эмпатию, запросить или просто пообщаться?", utils.getKeyBoard("Предложить","Запросить", "Просто пообщаться", "Отменить"));
		u.setStatus(1);
		Users.save(u);
	}
	public static void setRespons(HashMap<Long, Respon> hash) {
		onNewMessage.respons = hash; 
		
	}
	public static void newRespons(HashMap<Long, Respon> hash) {
		respons = hash;
		
	}
	
}
