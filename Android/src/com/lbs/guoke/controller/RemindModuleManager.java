package com.lbs.guoke.controller;

import java.util.ArrayList;

import android.database.Cursor;

import com.lbs.guoke.GuoKeApp;
import com.lbs.guoke.MainActivity;
import com.lbs.guoke.db.DBTools;

public class RemindModuleManager {
	private GuoKeApp app;
	private static RemindModuleManager instance;
	public static ArrayList<RemindInfo> mRemindInfos = new ArrayList<RemindInfo>();

	public RemindModuleManager(GuoKeApp app) {
		this.app = app;
		getRemindInfosFromDB();
	}

	public static RemindModuleManager instance(GuoKeApp app) {
		synchronized (RemindModuleManager.class) {
			if (instance == null) {
				instance = new RemindModuleManager(app);
			}
			return instance;
		}
	}

	public static RemindModuleManager instance() {
		return instance;
	}

	public ArrayList<RemindInfo> getRemindInfos() {
		return mRemindInfos;
	}

	private void getRemindInfosFromDB() {
		Thread thread = new Thread() {
			public void run() {
				Cursor c = DBTools.getAllRemindInfos();
				if(c == null)
					return;
				for (int i = 0; i < c.getCount(); i++) {
					RemindInfo remindInfo = new RemindInfo();
					remindInfo.key = DBTools.getUnvalidFormRs(c.getString(c.getColumnIndex("key")));
					remindInfo.remindTitle = DBTools.getUnvalidFormRs(c.getString(c.getColumnIndex("title")));
					remindInfo.remindMessage = DBTools.getUnvalidFormRs(c.getString(c.getColumnIndex("message")));
					remindInfo.isRemind = c.getInt(c.getColumnIndex("isremind"));
					remindInfo.isVibrate = c.getInt(c.getColumnIndex("isvibrate"));
					remindInfo.remindMusic = c.getInt(c.getColumnIndex("remindmusic"));
					mRemindInfos.add(remindInfo);
				}
				c.close();

				if(GuoKeApp.mainHandler != null){
					GuoKeApp.mainHandler.sendEmptyMessage(GuoKeApp.GUOKE_REMIND_UPDATE);
				}
			}
		};
		thread.start();
	}

	public class RemindInfo {
		public String key;
		public String remindTitle;
		public String remindMessage;
		public int isRemind;
		public int isVibrate;
		public int remindMusic;
	}
}
