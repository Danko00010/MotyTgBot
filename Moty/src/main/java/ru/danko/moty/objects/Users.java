package ru.danko.moty.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.danko.moty.Config;



public class Users {
	private static HashMap<Long, User> users;
public Users() {
	
}
public static HashMap<Long, User> getMap() {
	return users;
}
public static boolean  check(long id) {
	if(users.containsKey(id)) {
		return true;
	}
	return false;
}
public static User addUser(User u) {
	users.put(u.getId(), u);
	save();
	List<String> list = Config.getStringListOld("Users");
	if(list == null) {
		list = new ArrayList<String>();
	}
	list.add(u.toString());
	Config.set("Users", list);
	load();
	return u;
}
public static void load() {
	List<String> list = Config.getStringListOld("Users");
	users = new HashMap<Long, User>();
	if(list == null) {
		return;
	}
	for(String ss:list) {
		User us  = new User(ss);
		if(users.containsKey(us.getId())) {
			System.out.println("Error: дублирование пользователя! Проверьте конфигурацию Users");
		}
		users.put(us.getId(),us);
	}
	System.out.println("LoadUsers: "+users.size());
}
public static void save(User us) {
	users.remove(us.getId());
	users.put(us.getId(), us);
	System.out.println("Status: "+us.getStatus());
	save();
}
public static void save() {
	List<String> list = new ArrayList<String>();
	System.out.println("2");
	for(User us:users.values()) {
		list.add(us.toString());
	}
	Config.set("Users", list);
for(String s:list) {
	System.out.println("3: "+s);
}
}
public static void delUser(User us) {
	users.remove(us.getId());
	save();
	
}

public static User conUserName(Long id) {
	if(users.containsKey(id)) {
		return users.get(id);
	}
	return null;
}

}
