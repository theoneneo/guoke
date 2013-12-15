package com.lbs.guoke.controller;

import java.util.ArrayList;
import java.util.Vector;

import android.database.Cursor;

import com.lbs.guoke.GuoKeApp;
import com.lbs.guoke.cell.CellModule;
import com.lbs.guoke.db.DBTools;
import com.lbs.guoke.structure.CellInfo;

public class CellModuleManager {
	private GuoKeApp app;
	private static CellModule cellModule;
	private static CellModuleManager instance;
	// private static CellModuleListenerAbility cellModuleLA;
	public static ArrayList<CellInfo> mCellInfos = new ArrayList<CellInfo>();
	public static ArrayList<CellInfo> dbCellInfos = new ArrayList<CellInfo>();
	public static ArrayList<CellInfo> tempCellInfos = new ArrayList<CellInfo>();

	public CellModuleManager(GuoKeApp app) {
		this.app = app;
		getCellInfosFromDB();
		cellModule = new CellModule(app.getApplicationContext());
		cellModule.enable();
		// cellModuleLA = new CellModuleListenerAbility();
	}

	public void destoryCellModuleManager() {
		if (cellModule != null)
			cellModule.disable();
	}

	public static CellModuleManager instance(GuoKeApp app) {
		synchronized (CellModuleManager.class) {
			if (instance == null) {
				instance = new CellModuleManager(app);
			}
			return instance;
		}
	}

	public static CellModuleManager instance() {
		return instance;
	}

	public ArrayList<CellInfo> getCellInfos() {
		return mCellInfos;
	}

	// public CellModuleListenerAbility getCellModuleListenerAbility() {
	// return cellModuleLA;
	// }

	public void UpdateCellData() {
		// cellModuleLA.notifyCellChangeListener();
		// DBTools.instance(app.getApplicationContext()).insertSiteData("立水桥",
		// "明天第一城", 0, null, "家", mCellInfos);
		Thread thread = new Thread() {
			public void run() {
				if (mCellInfos == null)
					return;
				synchronized (mCellInfos) {
					for (int i = 0; i < mCellInfos.size(); i++) {
						CellInfo ci = mCellInfos.get(i);
						if (dbCellInfos == null)
							return;
						for (int m = 0; m < dbCellInfos.size(); m++) {
							CellInfo di = dbCellInfos.get(m);
						}
					}
				}
			}
		};
		thread.start();
	}

	private void getCellInfosFromDB() {
		Thread thread = new Thread() {
			public void run() {
				Cursor c = DBTools.getAllCellInfos();
				if (c == null)
					return;
				for (int i = 0; i < c.getCount(); i++) {
					CellInfo cellInfo = new CellInfo();
					cellInfo.key = c.getString(c.getColumnIndex("key"));
					cellInfo.isCDMA = c.getInt(c.getColumnIndex("iscdma"));
					cellInfo.cellid = c.getInt(c.getColumnIndex("cellid"));
					cellInfo.lac = c.getInt(c.getColumnIndex("lac"));
					cellInfo.mnc = c.getInt(c.getColumnIndex("mnc"));
					cellInfo.mcc = c.getInt(c.getColumnIndex("mcc"));
					dbCellInfos.add(cellInfo);
				}
			}
		};
		thread.start();
	}
}
