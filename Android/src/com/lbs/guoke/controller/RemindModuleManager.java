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
					remindInfo.remindid = DBTools.getUnvalidFormRs(c
							.getString(c.getColumnIndex("remindid")));
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
					remindInfo.ring = DBTools.getUnvalidFormRs(c
							.getString(c.getColumnIndex("ring")));
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
			String remindMessage, int isRemind, int isVibrate, long remindTime, String ring) {
		String remindid = "remind_" + System.currentTimeMillis();
		RemindInfo remindInfo = new RemindInfo();
		remindInfo.remindid = remindid;
		remindInfo.key = key;
		remindInfo.remindTitle = remindTitle;
		remindInfo.remindMessage = remindMessage;
		remindInfo.isRemind = isRemind;
		remindInfo.isVibrate = isVibrate;
		remindInfo.remindTime = remindTime;
		remindInfo.ring = ring;
		mRemindInfos.add(remindInfo);
		DBTools.instance().insertRemindInfo(remindid, key, remindTitle,
				remindMessage, isRemind, isVibrate, remindTime, ring);
	}
	
	
	public void deleteRemindInfo(String remindid) {
		if (remindid == null || remindid == "")
			return;

		for (int i = 0; i < mRemindInfos.size(); i++) {
			if (mRemindInfos.get(i).remindid.equals(remindid)) {
				mRemindInfos.remove(i);
			}
		}
		DBTools.instance().deleteRemindInfo(remindid);
	}
	
	public void modifyRemindInfo(String remindid, String key,
			String remindTitle, String remindMessage, int isRemind,
			int isVibrate, long remindTime, String ring) {
		if (key == null || key == "")
			return;

		for (int i = 0; i < mRemindInfos.size(); i++) {
			if (mRemindInfos.get(i).remindid.equals(remindid)) {
				RemindInfo remindInfo = mRemindInfos.get(i);
				remindInfo.key = key;
				remindInfo.remindTitle = remindTitle;
				remindInfo.remindMessage = remindMessage;
				remindInfo.isRemind = isRemind;
				remindInfo.isVibrate = isVibrate;
				if (remindTime != -1)
					remindInfo.remindTime = remindTime;
				if(ring != null)
					remindInfo.ring = ring;
			}
		}
		DBTools.instance().updateRemindInfo(remindid, key, remindTitle,
				remindMessage, isRemind, isVibrate, remindTime, ring);
	}

	public void saveRemindData() {
		for (int i = 0; i < mRemindInfos.size(); i++) {
			RemindInfo remindInfo = mRemindInfos.get(i);
			DBTools.instance().updateRemindInfo(remindInfo.remindid,
					remindInfo.key, remindInfo.remindTitle,
					remindInfo.remindMessage, remindInfo.isRemind,
					remindInfo.isVibrate, remindInfo.remindTime, remindInfo.ring);
		}
	}
	
	public void saveRemindTimer(ArrayList<String> remindids) {
		for (int i = 0; i < mRemindInfos.size(); i++) {
			RemindInfo remindInfo = mRemindInfos.get(i);
			for(int m = 0; m < remindids.size(); m++){
				if(remindids.get(m).equals(remindInfo.remindid)){
					remindInfo.remindTime = System.currentTimeMillis();
					DBTools.instance().updateRemindInfo(remindInfo.remindid,
							remindInfo.key, remindInfo.remindTitle,
							remindInfo.remindMessage, remindInfo.isRemind,
							remindInfo.isVibrate, remindInfo.remindTime, remindInfo.ring);
					break;
				}
			}
		}
	}

	public void matchRemindInfo() {
		for (int i = 0; i < matchRemindInfos.size(); i++) {
			RemindInfo remindInfo = matchRemindInfos.get(i);
			if(System.currentTimeMillis() - remindInfo.remindTime < 30*60*1000){
				matchRemindInfos.remove(i);
			}
		}
		if(matchRemindInfos.size() != 0)
			go2AlertDialogActivity();
	}

	private void go2AlertDialogActivity() {
		Intent i = new Intent(app, AlertDialogActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(i);
	}

	public class RemindInfo {
		public String remindid;
		public String key;
		public String remindTitle;
		public String remindMessage;
		public String ring;
		public int isRemind;
		public int isVibrate;
		public long remindTime;
	}
}
