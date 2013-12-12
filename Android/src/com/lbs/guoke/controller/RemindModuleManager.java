package com.lbs.guoke.controller;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;

import com.lbs.guoke.GuoKeApp;
import com.lbs.guoke.cell.CellModule;
import com.lbs.guoke.cell.CellModule.CellInfo;
import com.lbs.guoke.db.DBTools;
import com.lbs.guoke.listener.CellModuleListenerAbility;
import com.lbs.guoke.listener.ListenerAbility;

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
		Cursor cursor = DBTools.getInstance().getAllRemindInfos();
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); i++) {

		}
	    }
	};
	thread.start();
    }

    public class RemindInfo {
	public boolean isRemind = false;
	public String remindTitle;
	public String remindMessage;
	public int remindMusic;
	public boolean isVibrate = true;
    }
}
