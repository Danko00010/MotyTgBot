package ru.danko.moty.objects;

import java.util.ArrayList;
import java.util.List;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.response.SendResponse;

import ru.danko.moty.Config;
import ru.danko.moty.utils;
import ru.danko.moty.tgbot.Bot;

public class Respon {
	private String msg;
	private List<User> list;
	private int time;
	private int messageId;
	private boolean isOpen;
	private long chatId;
	private User us;
	private Long userChatId;
	public Respon(String msg, long chatId, User us, Long userChatId) {
		this.msg = msg;
		this.us = us;
		this.chatId = chatId;
		this.userChatId = userChatId;
		list = new ArrayList<User>();
	}
	public void setTime(int time) {
		this.time = time;
	}
	public long getUserChatId() {
		return userChatId;
	}
	public void addUser(User us) {
		list.add(us);
	}
	public long getChatId() {
		return chatId;
	}
	public List<User> getListUsers(){
		return list;
	}
	public User getUserList(long userAcceptId) {
		for(User us:list) {
			if(us.getId()==userAcceptId) {
				return us;
			}
		}
		return null;
	}
	public void accept(long userAcceptId) {
		User us = getUserList(userAcceptId);
		if(us==null) return;
		close();
		String msg = Config.getString("Message.Otc_accept").replace("%name%", us.getName()).replace("%username%", us.getUserName());
		Bot.sendMessage(userChatId, msg);
		us.setStatus(0);
		Users.save(us);
		InlineKeyboardMarkup str  = new InlineKeyboardMarkup();
		Bot.editCallBackQuery(chatId,messageId, str);
		
	}
	public int getCoutUsers() {
		return list.size();
	}
	public boolean checkUser(long userid) {
		if(getUserList(userid) != null) {
			return true;
		}
		return false;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void open(String mesg) {
		String[] buts = ("Откликнуться;out100_"+us.getId()).split("@");
		SendResponse msgRes = Bot.sendMessage(chatId, mesg, utils.getKeyboardMarkup(buts));
		messageId = msgRes.message().messageId();
		isOpen = true;
	}
	public void ontk(String username, String name, User us) {
		String msg = name+" @"+username+" откликнулся на твоё сообщение.";
		String[] buts = ("Принять;accept100_"+us.getId()).split("@");
		Bot.sendMessage(userChatId, msg, utils.getKeyboardMarkup(buts));
	}
	public void setMessageId(int id) {
		this.messageId = id;
	}
	public int getMessageId() {
		return messageId;
	}
	public String getMessage() {
		return this.msg;
	}
	public int getTime() {
		return this.time;
	}
	public void close() {
		  Bot.sendMessage(null, chatId,  "Запрос закрыт", null, null, messageId);
		isOpen = false;
	}
}
