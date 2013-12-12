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

public class MySiteModuleManager {
    private GuoKeApp app;
    private static MySiteModuleManager instance;
    public static ArrayList<SiteInfo> mSiteInfos = new ArrayList<SiteInfo>();

    public MySiteModuleManager(GuoKeApp app) {
	this.app = app;
	getSiteInfosFromDB();
    }

    public static MySiteModuleManager instance(GuoKeApp app) {
	synchronized (MySiteModuleManager.class) {
	    if (instance == null) {
		instance = new MySiteModuleManager(app);
	    }
	    return instance;
	}
    }

    public static MySiteModuleManager instance() {
	return instance;
    }

    public ArrayList<SiteInfo> getSiteInfos() {
	return mSiteInfos;
    }

    private void getSiteInfosFromDB() {
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

    public class SiteInfo {
	public String siteName;
	public String siteAddress;
	public int siteType;
	public String siteImageLink;
	public String siteMark;
    }
}
