package ru.danko.moty.tgbot.events;

import java.util.ArrayList;

import com.pengrad.telegrambot.model.Update;

public class postCannelEvent {
	private static ArrayList<onPostCannel> listeners = new ArrayList<onPostCannel>();
	public interface onPostCannel {
		   public void onPostCannelEvent(Update up);
		}
	public static void addListener(onPostCannel clazz){
		listeners.add(clazz);
		return;
	}
	public static void removeAll(){
		listeners = new ArrayList<onPostCannel>();
		return;
	}
	public void send(Update up){
		for(onPostCannel tralal:listeners){
			tralal.onPostCannelEvent(up);
		}
	}
}
