package com.lbs.guoke.controller;

import java.util.ArrayList;

import android.database.Cursor;

import com.lbs.guoke.GuoKeApp;
import com.lbs.guoke.db.DBTools;

public class RemindModuleManager {
	private GuoKeApp app;
	private static RemindModuleManager instance;
	public static ArrayList<RemindInfo> mRemindInfos = new ArrayList<RemindInfo>();

	public RemindModuleManager(GuoKeApp app) {
		this.app = app;
		getRemindInfosFromDB();
	}

	public static RemindModuleManager instance() {
		synchronized (RemindModuleManager.class) {
			if (instance == null) {
				instance = new RemindModuleManager(GuoKeApp.getApplication());
			}
			return instance;
		}
	}

	public ArrayList<RemindInfo> getRemindInfos() {
		return mRemindInfos;
	}

	private void getRemindInfosFromDB() {
		Thread thread = new Thread() {
			public void run() {
				Cursor c = DBTools.getAllRemindInfos();
				if (c == null)
					return;
				for (int i = 0; i < c.getCount(); i++) {
					RemindInfo remindInfo = new RemindInfo();
					remindInfo.key = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("key")));
					remindInfo.remindTitle = DBTools.getUnvalidFormRs(c
							.getString(c.getColumnIndex("title")));
					remindInfo.remindMessage = DBTools.getUnvalidFormRs(c
							.getString(c.getColumnIndex("message")));
					remindInfo.isRemind = c
							.getInt(c.getColumnIndex("isremind"));
					remindInfo.isVibrate = c.getInt(c
							.getColumnIndex("isvibrate"));
					remindInfo.remindMusic = c.getInt(c
							.getColumnIndex("remindmusic"));
					mRemindInfos.add(remindInfo);
					c.moveToNext();
				}
				c.close();

				if (GuoKeApp.mainHandler != null) {
					GuoKeApp.mainHandler
							.sendEmptyMessage(GuoKeApp.GUOKE_REMIND_UPDATE);
				}
			}
		};
		thread.start();
	}

	public void addRemindInfo(String remindTitle, String remindMessage,
			int isRemind, int isVibrate, int remindMusic) {
		String key = "site_" + System.currentTimeMillis();

		RemindInfo remindInfo = new RemindInfo();
		remindInfo.key = key;
		remindInfo.remindTitle = remindTitle;
		remindInfo.remindMessage = remindMessage;
		remindInfo.isRemind = isRemind;
		remindInfo.isVibrate = isVibrate;
		remindInfo.remindMusic = remindMusic;
		mRemindInfos.add(remindInfo);
		DBTools.instance().insertRemindData(key, remindTitle, remindMessage,
				isRemind, isVibrate, remindMusic);
	}

	public void modifySite(String key, String remindTitle, String remindMessage,
			int isRemind, int isVibrate, int remindMusic) {
		if (key == null || key == "")
			return;

		for (int i = 0; i < mRemindInfos.size(); i++) {
			RemindInfo remindInfo = mRemindInfos.get(i);
			if (remindInfo.key.equals(key)) {
				remindInfo.remindTitle = remindTitle;
				remindInfo.remindMessage = remindMessage;
				remindInfo.isRemind = isRemind;
				remindInfo.isVibrate = isVibrate;
				remindInfo.remindMusic = remindMusic;
				break;
			}
		}
		DBTools.instance().updateRemindInfo(key, remindTitle, remindMessage, isRemind, isVibrate,
				remindMusic);
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
