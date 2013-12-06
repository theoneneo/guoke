package com.lbs.guoke.controller;

import java.util.ArrayList;

import android.content.Intent;

import com.lbs.guoke.GuoKeApp;
import com.lbs.guoke.cell.CellModule.CellInfo;
import com.lbs.guoke.listener.ListenerAbility;

public class CellModuleManager {
    private GuoKeApp app;
    private static CellModuleManager instance;
    private ListenerAbility bb;
    public ArrayList<CellInfo> mCellInfos = new ArrayList<CellInfo>();   
    
    public CellModuleManager(GuoKeApp app) {
	this.app = app;
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
    
    public void startService(){
	Intent i = new Intent(app.getApplicationContext(), GuoKeService.class);
	app.getApplicationContext().startService(i);
    }
    
    public void stopService(){
	Intent i = new Intent(app.getApplicationContext(), GuoKeService.class);
	app.getApplicationContext().stopService(i);
    }  
    
    public ArrayList<CellInfo> getCellInfos(){
	return mCellInfos;
    }
    
//    public getAllCellInfo
}
