package com.lbs.guoke.controller;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;

import com.lbs.guoke.AlertDialogActivity;
import com.lbs.guoke.GuoKeApp;
import com.lbs.guoke.db.DBTools;

public class RemindModuleManager {
	private GuoKeApp app;
	private static RemindModuleManager instance;
	private static ArrayList<RemindInfo> mRemindInfos = new ArrayList<RemindInfo>();
	private static ArrayList<RemindInfo> matchRemindInfos = new ArrayList<RemindInfo>();
	private static ArrayList<RemindInfo> curRemindInfos = new ArrayList<RemindInfo>();

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

	public void destory() {
		saveRemindData();
	}

	public ArrayList<RemindInfo> getRemindInfos() {
		return mRemindInfos;
	}

	public ArrayList<RemindInfo> getMatchRemindInfos() {
		return matchRemindInfos;
	}

	public ArrayList<RemindInfo> getCurRemindInfos() {
		return curRemindInfos;
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
				app.eventAction(GuoKeApp.GUOKE_REMIND_UPDATE);
			}
		};
		thread.start();
	}

	public void addRemindInfo(String key, String remindTitle,
			String remindMessage, int isRemind, int isVibrate, int remindMusic) {
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

	public void modifySite(String key, String remindTitle,
			String remindMessage, int isRemind, int isVibrate, int remindMusic) {
		if (key == null || key == "")
			return;

		for(int i = 0; i < mRemindInfos.size(); i++){
			if(mRemindInfos.get(i).key.equals(key)){
				RemindInfo remindInfo = mRemindInfos.get(i);
				remindInfo.remindTitle = remindTitle;
				remindInfo.remindMessage = remindMessage;
				remindInfo.isVibrate = isVibrate;
				remindInfo.remindMusic = remindMusic;
			}				
		}
		DBTools.instance().updateRemindInfo(key, remindTitle, remindMessage,
				isRemind, isVibrate, remindMusic);
	}

	public void saveRemindData() {
		for (int i = 0; i < mRemindInfos.size(); i++) {
			RemindInfo remindInfo = mRemindInfos.get(i);
			modifySite(remindInfo.key, remindInfo.remindTitle,
					remindInfo.remindMessage, remindInfo.isRemind,
					remindInfo.isVibrate, remindInfo.remindMusic);
		}
	}

	public void matchRemindInfo() {
		curRemindInfos.clear();
		for (int i = 0; i < matchRemindInfos.size(); i++) {
			RemindInfo remindInfo = matchRemindInfos.get(i);
			if (remindInfo.isRemind == 1)
				curRemindInfos.add(remindInfo);
		}
		if(curRemindInfos.size() != 0)
			startAlertActivity();
	}

	private void startAlertActivity() {
		Intent i = new Intent(app, AlertDialogActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(i);
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
