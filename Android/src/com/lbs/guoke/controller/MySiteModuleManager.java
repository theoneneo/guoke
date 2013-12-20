package com.lbs.guoke.controller;

import java.util.ArrayList;

import android.database.Cursor;

import com.lbs.guoke.GuoKeApp;
import com.lbs.guoke.MainActivity;
import com.lbs.guoke.db.DBTools;
import com.lbs.guoke.structure.CellInfo;

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
				Cursor c = DBTools.getAllSiteInfos();
				if(c == null)
					return;
				for (int i = 0; i < c.getCount(); i++) {
					SiteInfo siteInfo = new SiteInfo();
					siteInfo.key = c.getString(c.getColumnIndex("key"));
					siteInfo.siteName = c.getString(c.getColumnIndex("name"));
					siteInfo.siteAddress = c.getString(c.getColumnIndex("address"));
					siteInfo.siteType = c.getInt(c.getColumnIndex("type"));
					siteInfo.siteImageLink = c.getString(c.getColumnIndex("image"));
					siteInfo.siteMark = c.getString(c.getColumnIndex("mark"));
					mSiteInfos.add(siteInfo);
				}
				
				if(GuoKeApp.mainHandler != null){
					GuoKeApp.mainHandler.sendEmptyMessage(GuoKeApp.GUOKE_SITE_UPDATE);
				}
			}
		};
		thread.start();
	}
	
	public void addSiteInfo(String name, String address, int type,
			String imageLink, String mark, ArrayList<CellInfo> cellInfos){
		 DBTools.instance().insertSiteData(name, address, type, imageLink, mark, CellModuleManager.instance().getCellInfos());
	}

	public class SiteInfo {
		public String key;
		public String siteName;
		public String siteAddress;
		public int siteType;
		public String siteImageLink;
		public String siteMark;
	}
}
