package ru.danko.moty;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

class MyProperties {

    private File mFile;
    private ArrayList<String> keys = new ArrayList<String>(), values = new ArrayList<String>();
    private String path = "config.properties";

    MyProperties() throws Throwable {
    	FileInputStream fileInputStream;
   	 Properties pr = new Properties();

   	try {
   	    fileInputStream = new FileInputStream(path);
   	    pr.load(fileInputStream);

   	} catch (IOException e) {
   	    System.out.println("Ошибка в программе: файл " + path + " не обнаружено");
   	    e.printStackTrace();
   	}
   for(Object key:pr.keySet()) {
	   keys.add((String) key);
	   values.add(pr.getProperty((String) key));
   }
   	
    }

    synchronized ArrayList<String> keySet() {
        return this.keys;
    }

    synchronized String getProperty(String key, String defaultValue) {
        for (int i1 = 0; i1 < this.keys.size(); i1++) {
            if (this.keys.get(i1).equals(key)) {
                return this.values.get(i1);
            }
        }
        return defaultValue;
    }

    synchronized void setProperty(String key, String value) throws Throwable {
        this.values.set(this.keys.indexOf(key), value);
        DataOutputStream out = new DataOutputStream(new FileOutputStream(this.mFile));
        for (int i1 = 0; i1 < this.keys.size(); i1++) {
            out.writeBytes(this.keys.get(i1) + "=" + this.values.get(i1));
        }
        out.flush();
        out.close();
    }

}
