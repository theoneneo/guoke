package com.lbs.guoke.controller;

import java.util.ArrayList;

import android.app.Notification;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.lbs.guoke.AddRemindActivity;
import com.lbs.guoke.AlertDialogActivity;
import com.lbs.guoke.GuoKeApp;
import com.lbs.guoke.MainActivity;
import com.lbs.guoke.R;
import com.lbs.guoke.db.DBTools;
import com.neo.tools.NotificationTools;

public class RemindModuleManager {
	private GuoKeApp app;
	private static RemindModuleManager instance;
	private static ArrayList<RemindInfo> mRemindInfos = new ArrayList<RemindInfo>();
	private static ArrayList<RemindInfo> matchRemindInfos = new ArrayList<RemindInfo>();

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

	private void getRemindInfosFromDB() {
		Thread thread = new Thread() {
			public void run() {
				Cursor c = DBTools.getAllRemindInfos();
				if (c == null)
					return;
				for (int i = 0; i < c.getCount(); i++) {
					RemindInfo remindInfo = new RemindInfo();
					remindInfo.remindid = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("remindid")));
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
					remindInfo.remindTime = c.getInt(c
							.getColumnIndex("remindtime"));
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
			String remindMessage, int isRemind, int isVibrate, long remindTime) {
		String remindid = "remind_" + System.currentTimeMillis();
		RemindInfo remindInfo = new RemindInfo();
		remindInfo.remindid = remindid;
		remindInfo.key = key;
		remindInfo.remindTitle = remindTitle;
		remindInfo.remindMessage = remindMessage;
		remindInfo.isRemind = isRemind;
		remindInfo.isVibrate = isVibrate;
		remindInfo.remindTime = remindTime;
		mRemindInfos.add(remindInfo);
		DBTools.instance().insertRemindData(remindid, key, remindTitle, remindMessage,
				isRemind, isVibrate, remindTime);
	}

	public void modifyRemindInfo(String remindid, String key, String remindTitle,
			String remindMessage, int isRemind, int isVibrate, long remindTime) {
		if (key == null || key == "")
			return;

		for (int i = 0; i < mRemindInfos.size(); i++) {
			if (mRemindInfos.get(i).remindid.equals(remindid)) {
				RemindInfo remindInfo = mRemindInfos.get(i);
				remindInfo.key = key;
				remindInfo.remindTitle = remindTitle;
				remindInfo.remindMessage = remindMessage;
				remindInfo.isVibrate = isVibrate;
				remindInfo.remindTime = remindTime;
			}
		}
		DBTools.instance().updateRemindInfo(remindid, key, remindTitle, remindMessage,
				isRemind, isVibrate, remindTime);
	}

	public void saveRemindData() {
		for (int i = 0; i < mRemindInfos.size(); i++) {
			RemindInfo remindInfo = mRemindInfos.get(i);
			modifyRemindInfo(remindInfo.remindid, remindInfo.key, remindInfo.remindTitle,
					remindInfo.remindMessage, remindInfo.isRemind,
					remindInfo.isVibrate, remindInfo.remindTime);
		}
	}

	public void matchRemindInfo() {
		go2AddRemindFragment();
//		if(curRemindInfos.size() != 0){
//			int defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS;
//			StringBuffer buf = new StringBuffer(); 
//			for(int i = 0; i < curRemindInfos.size(); i++){
//				RemindInfo remindInfo = curRemindInfos.get(i);
//				if(remindInfo.isVibrate == 1)
//					defaults = Notification.DEFAULT_ALL;
//				buf.append(remindInfo.remindTitle);
//				buf.append(";");
//			}
//			
//			NotificationTools.startAlertNotify(app.getApplicationContext(), R.drawable.ic_launcher, "过客", "过客提醒", defaults, buf.toString());
//		}
	}
	
	private void go2AddRemindFragment() {
		Intent i = new Intent(app, AlertDialogActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(i);
	}

	public class RemindInfo {
		public String remindid;
		public String key;
		public String remindTitle;
		public String remindMessage;
		public int isRemind;
		public int isVibrate;
		public long remindTime;
	}
}
