package com.lbs.guoke.controller;

import java.util.ArrayList;

import android.database.Cursor;

import com.lbs.guoke.GuoKeApp;
import com.lbs.guoke.controller.RemindModuleManager.RemindInfo;
import com.lbs.guoke.db.DBTools;

public class MySiteModuleManager {
	private GuoKeApp app;
	private static MySiteModuleManager instance;
	private static ArrayList<SiteInfo> mSiteInfos = new ArrayList<SiteInfo>();

	public MySiteModuleManager(GuoKeApp app) {
		this.app = app;
		getSiteInfosFromDB();
	}

	public static MySiteModuleManager instance() {
		synchronized (MySiteModuleManager.class) {
			if (instance == null) {
				instance = new MySiteModuleManager(GuoKeApp.getApplication());
			}
			return instance;
		}
	}

	public ArrayList<SiteInfo> getSiteInfos() {
		return mSiteInfos;
	}

	private void getSiteInfosFromDB() {
		Thread thread = new Thread() {
			public void run() {
				Cursor c = DBTools.getAllSiteInfos();
				if (c == null)
					return;
				for (int i = 0; i < c.getCount(); i++) {
					SiteInfo siteInfo = new SiteInfo();
					siteInfo.key = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("key")));
					siteInfo.siteName = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("name")));
					siteInfo.siteAddress = DBTools.getUnvalidFormRs(c
							.getString(c.getColumnIndex("address")));
					siteInfo.siteType = c.getInt(c.getColumnIndex("type"));
					siteInfo.siteImageLink = DBTools.getUnvalidFormRs(c
							.getString(c.getColumnIndex("image")));
					siteInfo.siteMark = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("mark")));
					mSiteInfos.add(siteInfo);
					c.moveToNext();
				}
				c.close();
				app.eventAction(GuoKeApp.GUOKE_SITE_UPDATE);
			}
		};
		thread.start();
	}

	public void addSiteInfo(String name, String address, int type,
			String imageLink, String mark) {
		String key = "site_" + System.currentTimeMillis();

		SiteInfo siteInfo = new SiteInfo();
		siteInfo.key = key;
		siteInfo.siteName = name;
		siteInfo.siteAddress = address;
		siteInfo.siteType = type;
		siteInfo.siteImageLink = imageLink;
		siteInfo.siteMark = mark;
		mSiteInfos.add(siteInfo);

		CellModuleManager.instance().setDBCellInfos(key);

		DBTools.instance().insertSiteInfo(key, name, address, type, imageLink,
				mark, CellModuleManager.instance().getCellInfos());
	}
	
	public void deleteSiteInfo(String key){
		for (int i = 0; i < mSiteInfos.size(); i++) {
			if (mSiteInfos.get(i).key.equals(key)) {
				mSiteInfos.remove(i);
				DBTools.instance().deleteSiteInfo(key);
				return;
			}
		}
	}

	public void modifySiteInfo(String key, String name, String address, int type,
			String imageLink, String mark) {
		if (key == null || key == "")
			return;
		for (int i = 0; i < mSiteInfos.size(); i++) {
			if (mSiteInfos.get(i).key.equals(key)) {
				SiteInfo siteInfo = mSiteInfos.get(i);
				siteInfo.siteName = name;
				siteInfo.siteAddress = address;
				siteInfo.siteType = type;
				siteInfo.siteImageLink = imageLink;
				siteInfo.siteMark = mark;
			}
		}
		DBTools.instance().updateSiteInfo(key, name, address, type, imageLink,
				mark);
		SyncRemindInfos();
	}

	private void SyncRemindInfos() {
		for (int i = 0; i < RemindModuleManager.instance().getRemindInfos()
				.size(); i++) {
			RemindInfo remindInfo = RemindModuleManager.instance()
					.getRemindInfos().get(i);
			for (int m = 0; m < MySiteModuleManager.instance().getSiteInfos()
					.size(); m++) {
				SiteInfo siteInfo = MySiteModuleManager.instance()
						.getSiteInfos().get(m);
				if (siteInfo.key.equals(remindInfo.key)) {
					remindInfo.remindMessage = siteInfo.siteName;
					break;
				}
			}
		}
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
