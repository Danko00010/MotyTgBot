package ru.danko.moty;

import ru.danko.moty.objects.Users;
import ru.danko.moty.tgbot.Bot;
import ru.danko.moty.tgbot.events.CallbackQueryEvent;
import ru.danko.moty.tgbot.events.MessageEvnet;

public class Main {
public static void main(String[] args) {
	System.out.println("BotStart");
	
	Config.loadConfig();
	Users.load();
botStart();
MessageEvnet.addListener(new onNewMessage());
CallbackQueryEvent.addListener(new onCallBackQuery());

}

public static void reload() {
	Config.loadConfig();
}
private static void botStart() {
	new Thread(new Runnable() {
		
		@Override
		public void run() {
			new Bot(Config.getString("Token"));
			
		}
	}).start();
}
}
