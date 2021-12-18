package ru.danko.moty;

import java.util.HashMap;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import ru.danko.moty.objects.Respon;
import ru.danko.moty.objects.User;
import ru.danko.moty.objects.Users;
import ru.danko.moty.tgbot.Bot;
import ru.danko.moty.tgbot.events.CallbackQueryEvent.onCallBackQueryEvent;

public class onCallBackQuery implements onCallBackQueryEvent {

	@SuppressWarnings("removal")
	@Override
	public void onCallBackQuery(Update up) {
		String data = up.callbackQuery().data();
		Long chatId = up.callbackQuery().message().chat().id();
		CallbackQuery callback = up.callbackQuery();
		String[] dd = data.split(":");
		System.out.println(dd[0]);
		if(dd[0].equals("cancel_option")) {
			User us = Users.conUserName(up.callbackQuery().from().id());
			String[] buts = ("Редактировать;"+dd[1]+":"+dd[2]).split("@");
			Bot.editCallBackQuery(chatId, up.callbackQuery().message().messageId(), utils.getKeyboardMarkup(buts));
			us.setRed(null);
			us.setRedMsgId(0);
			return;
		}
		if(dd[0].equalsIgnoreCase("Message")) {
			Bot.sendMessage(up, "Введите новое значение.");
			String[] buts = ("Отменить;cancel_option:"+data).split("@");
			Bot.editCallBackQuery(chatId, up.callbackQuery().message().messageId(), utils.getKeyboardMarkup(buts));
			User us = Users.conUserName(up.callbackQuery().from().id());
			//String save = data.replace(":", ".");
			us.setRed(data);
			us.setRedMsgId(up.callbackQuery().message().messageId());
			System.out.println(data);
			return;
		}
		if(data.equals("start")) {
			if(chatId<1) return;
			Long userId = up.callbackQuery().message().from().id();
			InlineKeyboardMarkup str  = new InlineKeyboardMarkup();
			Bot.editCallBackQuery(chatId,up.callbackQuery().message().messageId(), str);
			if(!Users.check(userId)){
				if(up.callbackQuery().from().username()== null) {
					Bot.sendMessage(up, "Для того, чтобы люди могли откликнуться, для начала заполни поле username в настройках Telegram.");
					return;
				}
				Bot.sendMessage(up, Config.getString("welcome"));
				User user = new User(userId, up.callbackQuery().from().username(), -2, up.callbackQuery().from().firstName());
				Users.addUser(user);
			}
			User u = Users.conUserName(userId);
			if(u.getStatus() == -2) {
				if(up.callbackQuery().from().username()== null) {
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
						if(up.callbackQuery().message().from().username()== null) {
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
			return;
		}
		if(data.split("_")[0].equals("accept100")) {
			Long userid = new Long(data.split("_")[1]);
			HashMap<Long, Respon> hash = onNewMessage.getRespons();
			if(!hash.containsKey(up.callbackQuery().from().id())) {
				Bot.sendAnswerCallBackQuery(callback, false, "Запрос был закрыт.");
				return;
			}
			
			Respon res = hash.get(up.callbackQuery().from().id());
			hash.remove(up.callbackQuery().from().id());
			onNewMessage.newRespons(hash);
			res.accept(new Long(data.split("_")[1]));
			InlineKeyboardMarkup str  = new InlineKeyboardMarkup();
			Bot.editCallBackQuery(chatId,up.callbackQuery().message().messageId(), str);
		
			User us1 = Users.conUserName(up.callbackQuery().message().chat().id());
			int stu = us1.getStatus();
			int in = 0;
			int iin = 0;
			if(stu==101||stu==102) {
				in = 2;
				iin = 1;
			}
			if(stu==201||stu==202) {
				in = 3;
				iin = 2;
			}
			if(stu==301||stu==302) {
				in = 4;
				iin = 3;
			}
			int acceptSt1 = us1.getSt(iin);
			us1.setSt(in, acceptSt1+2);
			Users.save(us1);
			int summ1 = us1.summStatic();
			String cnfMessage1 = Config.getString("Message.add_point_out").replace("%summ%", ""+summ1);
			Bot.sendMessage(us1.getId(), cnfMessage1);
			User us = Users.conUserName(userid);
			int acceptSt = us.getSt(0);
			us.setSt(1, acceptSt+1);
			Users.save(us);
			int summ = us.summStatic();
	
			String cnfMessage = Config.getString("Message.add_point_otk_accept").replace("%summ%", ""+summ);//add_point_otk_accept
			Bot.sendMessage(us.getId(), cnfMessage);
			return;
		}
		if(data.split("_")[0].equals("out100")) {
		Long userid = new Long(data.split("_")[1]);
		String ss = up.callbackQuery().from().id()+"";
		String ss2 = userid+"";
		if(ss.equalsIgnoreCase(ss2)) {
			Bot.sendAnswerCallBackQuery(callback, false, "Нельзя откликнуться на свой запрос.");
			return;
		}
		HashMap<Long, Respon> hash = onNewMessage.getRespons();
		if(!hash.containsKey(userid)) {
			Bot.sendAnswerCallBackQuery(callback, false, "Запрос был закрыт.");
			return;
		}
		Respon res = hash.get(userid);
		if(res.checkUser(up.callbackQuery().from().id())) {
			Bot.sendAnswerCallBackQuery(callback, false, "Вы уже откликнулись на этот запрос.");
			return;
		}
		int i = res.getCoutUsers();
		i++;
		User us = Users.conUserName(up.callbackQuery().from().id());
		res.addUser(us);
		hash.remove(userid);
		hash.put(userid, res);
		onNewMessage.setRespons(hash);
		String[] buts = ("Откликнуться +"+i+";out100_"+userid).split("@");
		Bot.editCallBackQuery(chatId ,up.callbackQuery().message().messageId(), utils.getKeyboardMarkup(buts));
		res.ontk(us.getUserName(), up.callbackQuery().from().firstName(), us);
		int acceptSt = us.getSt(0);
		acceptSt++;
		us.setSt(1, acceptSt);
		int summ = us.summStatic();//add_point_otk_put
		String cnfMessage = Config.getString("Message.add_point_otk_put").replace("%summ%", ""+summ);//add_point_otk_accept
		Bot.sendMessage(us.getId(), cnfMessage);
		}
		
	}

}
