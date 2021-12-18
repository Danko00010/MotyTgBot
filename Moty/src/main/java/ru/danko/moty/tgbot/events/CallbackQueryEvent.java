package ru.danko.moty.tgbot.events;

import java.util.ArrayList;

import com.pengrad.telegrambot.model.Update;

public class CallbackQueryEvent {
	private static ArrayList<onCallBackQueryEvent> listeners = new ArrayList<onCallBackQueryEvent>();
	public interface onCallBackQueryEvent {
		   public void onCallBackQuery(Update up);
		}
	public static void addListener(onCallBackQueryEvent clazz){
		listeners.add(clazz);
		return;
	}
	public static void removeAll(){
		listeners = new ArrayList<onCallBackQueryEvent>();
		return;
	}
	public void send(Update up){
		for(onCallBackQueryEvent tralal:listeners){
			tralal.onCallBackQuery(up);
		}
	}
}
