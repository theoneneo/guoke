package com.lbs.guoke.controller;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;

import com.lbs.guoke.GuoKeApp;
import com.lbs.guoke.controller.RemindModuleManager.RemindInfo;
import com.lbs.guoke.db.DBTools;
import com.lbs.guoke.structure.CellInfo;

public class CellModuleManager {
	private GuoKeApp app;
	private static CellModuleManager instance;
	private static ArrayList<CellInfo> mCellInfos = new ArrayList<CellInfo>();
	private static ArrayList<CellInfo> dbCellInfos = new ArrayList<CellInfo>();
	private static ArrayList<CellInfo> mBugCellInfos = new ArrayList<CellInfo>();
	public static boolean bAddToArray = false;

	private CellModuleManager(GuoKeApp app) {
		this.app = app;
		getCellInfosFromDB();
		startService();
	}

	public void destory() {
//		 stopService();
	}

	public static CellModuleManager instance() {
		synchronized (CellModuleManager.class) {
			if (instance == null) {
				instance = new CellModuleManager(GuoKeApp.getApplication());
			}
			return instance;
		}
	}

	public void startService() {
		Intent i = new Intent(app.getApplicationContext(), LoKaService.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.getApplicationContext().startService(i);
	}

	public void stopService() {
		Intent i = new Intent(app.getApplicationContext(), LoKaService.class);
		app.getApplicationContext().stopService(i);
	}

	public ArrayList<CellInfo> getCellInfos() {
		return mCellInfos;
	}
	
	public ArrayList<CellInfo> getBugCellInfos(){
		return mBugCellInfos;
	}
	
	public void setCellInfos(ArrayList<CellInfo> cellInfo){
		mCellInfos.clear();
		mCellInfos.addAll(cellInfo);
		UpdateCellData();
		if(bAddToArray)
			checkBugCellInfos(mCellInfos);
	}

	public ArrayList<CellInfo> getDBCellInfos() {
		return dbCellInfos;
	}

	public void UpdateCellData() {
		Thread thread = new Thread() {
			public void run() {
				if (mCellInfos == null || mCellInfos.size() == 0)
					return;
				ArrayList<CellInfo> tempCellInfos = new ArrayList<CellInfo>();
				synchronized (mCellInfos) {
					tempCellInfos.clear();
					for (int i = 0; i < mCellInfos.size(); i++) {// 取得匹配上的cell
						CellInfo ci = mCellInfos.get(i);
						if (dbCellInfos == null || dbCellInfos.size() == 0)
							return;
						for (int m = 0; m < dbCellInfos.size(); m++) {
							CellInfo di = dbCellInfos.get(m);
							if (ci.cellid == di.cellid
									&& ci.isCDMA == di.isCDMA
									&& ci.lac == di.lac && ci.mnc == di.mnc
									&& ci.mcc == di.mcc) {
								tempCellInfos.add(di);
							}
						}
					}
				}
				if (RemindModuleManager.instance().getRemindInfos() == null
						|| RemindModuleManager.instance().getRemindInfos()
								.size() == 0)
					return;
				RemindModuleManager.instance().getMatchRemindInfos().clear();
				for (int i = 0; i < RemindModuleManager.instance()
						.getRemindInfos().size(); i++) {
					RemindInfo remindInfo = RemindModuleManager.instance()
							.getRemindInfos().get(i);
					for (int m = 0; m < tempCellInfos.size(); m++) {
						CellInfo cell = tempCellInfos.get(m);
						if (cell.key.equals(remindInfo.key) && (remindInfo.isRemind == 1)) {
							RemindModuleManager.instance()
									.getMatchRemindInfos().add(remindInfo);
							break;
						}
					}
				}
				if (RemindModuleManager.instance().getMatchRemindInfos().size() != 0)
					app.eventAction(GuoKeApp.GUOKE_REMIND_MATCH);
			}
		};
		thread.start();
	}
	
	private void checkBugCellInfos(ArrayList<CellInfo> cellInfos){
		boolean bSave = false;
		for(int m = 0; m < cellInfos.size(); m++){
			CellInfo cell = cellInfos.get(m);
			for(int n = 0; n < mBugCellInfos.size(); n++){
				if(cell.cellid == mBugCellInfos.get(n).cellid){
					bSave = false;
					break;
				}
				bSave = true;
			}
			if(bSave){
				mBugCellInfos.add(cell);
			}
		}
	}

	private void getCellInfosFromDB() {
		Thread thread = new Thread() {
			public void run() {
				Cursor c = DBTools.getAllCellInfos();
				if (c == null)
					return;
				for (int i = 0; i < c.getCount(); i++) {
					CellInfo cellInfo = new CellInfo();
					cellInfo.key = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("key")));
					cellInfo.isCDMA = c.getInt(c.getColumnIndex("iscdma"));
					cellInfo.cellid = c.getInt(c.getColumnIndex("cellid"));
					cellInfo.lac = c.getInt(c.getColumnIndex("lac"));
					cellInfo.mnc = c.getInt(c.getColumnIndex("mnc"));
					cellInfo.mcc = c.getInt(c.getColumnIndex("mcc"));
					dbCellInfos.add(cellInfo);
					c.moveToNext();
				}
				c.close();
				app.eventAction(GuoKeApp.GUOKE_CELL_DB);
			}
		};
		thread.start();
	}

	public void setDBCellInfos(String key) {
		for (int i = 0; i < mBugCellInfos.size(); i++) {
			CellInfo cellInfo = new CellInfo();
			cellInfo.key = key;
			cellInfo.isCDMA = mBugCellInfos.get(i).isCDMA;
			cellInfo.cellid = mBugCellInfos.get(i).cellid;
			cellInfo.lac = mBugCellInfos.get(i).lac;
			cellInfo.mnc = mBugCellInfos.get(i).mnc;
			cellInfo.mcc = mBugCellInfos.get(i).mcc;
			dbCellInfos.add(cellInfo);
		}
	}
	
	public void clearBugCellInfos(){
		if(mBugCellInfos != null)
			mBugCellInfos.clear();
	}
}
