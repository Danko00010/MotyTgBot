package ru.danko.moty.objects;

public class User {
private Long id;
private Boolean gender;
private String stat;
private int status;
private String username;
private String name;
private String red = null;
private int redmsgid;

public User(Long id, String username, int status, String name) {
	this.id = id;
	this.username = username;
	this.status = status;
	this.name = name;
	this.stat="0:0:0:0:0";
}
@SuppressWarnings("removal")
public User(String s) {
	try {
	String[] spl = s.split(";");
	this.id = new Long(spl[0]);
	this.username = spl[1];
	this.gender = true;
	if(spl[2].equals("false")) {
		this.gender = false;
	}
	this.stat = spl[3];
	this.status = new Integer(spl[4]);
	this.name = spl[5];
	}catch(Exception ex) {
		System.out.println("Ошибка загрузки конфигураций!");
		ex.printStackTrace();
		System.exit(0);
	}
}
public Long getId() {
	return id;
}
public String getUserName() {
	return username;
}
@SuppressWarnings("removal")
public int getSt(int i) {
	return new Integer(getStatistic().split(":")[i]);
}
public void setSt(int i, int st) {
	String[] spl = stat.split(":");
	String save = "";
	if(i==1) {
		save = st+":"+spl[1]+":"+spl[2]+":"+spl[3];
	}
	if(i==2) {
		save = spl[0]+":"+st+":"+spl[2]+":"+spl[3];
	}
if(i==3) {
	save = spl[0]+":"+spl[1]+":"+st+":"+spl[3];
	}
if(i==4) {
	save = spl[0]+":"+spl[1]+":"+spl[2]+":"+st;
}

this.stat = save;
	return;
}
public String getName() {
	return name;
}
public void setGender(Boolean gender) {
	this.gender = gender;
}
public boolean getGender() {
	return gender;
}
public void setStatistic(String stat) {
	this.stat = stat;
}
public void setRed(String s) {
	this.red = s;
}
public String getRed() {
	return this.red;
}
public void setRedMsgId(int id) {
	this.redmsgid = id;
}
public int getRedMsgId() {
	return this.redmsgid;
}
public String getStatistic() {
	return stat;
}
public void setStatus(int i) {
 this.status = i;
}
public int summStatic() {
	return (getSt(0)+getSt(1)+getSt(2)+getSt(3));
}
public int getStatus() {
	return status;
}
public String toString() {
	if(stat==null || stat.equals("")) {
		stat="0:0:0:0:0";
	}
	
	return id+";"+username+";"+gender+";"+stat+";"+status+";"+name;
}

}
