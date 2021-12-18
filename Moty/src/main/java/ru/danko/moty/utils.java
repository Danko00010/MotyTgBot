package ru.danko.moty;

import java.util.ArrayList;
import java.util.List;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;

public class utils {
	//down
public static ReplyKeyboardMarkup getKeyBoard(String oneline, String...strings) {
	
	ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(oneline);
	keyboard.selective(true);
	keyboard.resizeKeyboard(true);
	keyboard.oneTimeKeyboard(false);
	if(strings != null) {
	for(String s:strings){
		keyboard.addRow(s);
	}
	}
	return keyboard;
}

public static ReplyKeyboardRemove getKeyBoardRemove() {
	ReplyKeyboardRemove keyboard = new ReplyKeyboardRemove();
	return keyboard;
}

public static InlineKeyboardMarkup getKeyboardMarkup(String[] buttons) {
	InlineKeyboardMarkup board = new InlineKeyboardMarkup();
	List<InlineKeyboardButton> list = new ArrayList<>();
	InlineKeyboardButton[] buts = new InlineKeyboardButton[0];
	for(String s:buttons) {
		
		String[] b = s.split(";");
		String text = b[0];
		String name = b[1];
		InlineKeyboardButton button = new InlineKeyboardButton(text);
		button.callbackData(name);
		list.add(button);
	}
	buts = convertListToArray(list);
	System.out.println(buts.length);
	board.addRow(buts);
	return board;
}
private static InlineKeyboardButton[] convertListToArray(List<InlineKeyboardButton> list){
	InlineKeyboardButton[] ins = (InlineKeyboardButton[])list.toArray(new InlineKeyboardButton[list.size()]);
    return ins;
}
}
