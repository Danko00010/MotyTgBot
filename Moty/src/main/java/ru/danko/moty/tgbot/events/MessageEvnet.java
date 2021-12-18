package ru.danko.moty.tgbot.events;

import java.util.ArrayList;
import com.pengrad.telegrambot.model.Update;

public class MessageEvnet  {
	private static ArrayList<onNewChatMessageListener> listeners = new ArrayList<onNewChatMessageListener>();
	public interface onNewChatMessageListener {
		   public void onCommandEvent(Update up,String msg);
		}
	public static void addListener(onNewChatMessageListener clazz){
		listeners.add(clazz);
		return;
	}
	public static void removeAll(){
		listeners = new ArrayList<onNewChatMessageListener>();
		return;
	}
	public void send(Update up, String msg){
		for(onNewChatMessageListener tralal:listeners){
			tralal.onCommandEvent(up, msg);
		}
	}
}
