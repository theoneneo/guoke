package com.lbs.guoke.controller;

import java.util.ArrayList;

import android.content.Intent;

import com.lbs.guoke.GuoKeApp;
import com.lbs.guoke.cell.CellModule;
import com.lbs.guoke.cell.CellModule.CellInfo;
import com.lbs.guoke.listener.CellModuleListenerAbility;
import com.lbs.guoke.listener.ListenerAbility;

public class CellModuleManager {
    private GuoKeApp app;
    private static CellModule cellModule;
    private static CellModuleManager instance;
    private static CellModuleListenerAbility cellModuleLA;
    public static ArrayList<CellInfo> mCellInfos = new ArrayList<CellInfo>();   
    
    public CellModuleManager(GuoKeApp app) {
	this.app = app;
	cellModule = new CellModule(app.getApplicationContext());
	cellModule.enable();
	cellModuleLA = new CellModuleListenerAbility();
    }
    
    public void destoryCellModuleManager(){
	if(cellModule != null)
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
    
    public ArrayList<CellInfo> getCellInfos(){
	return mCellInfos;
    }
    
    public CellModuleListenerAbility getCellModuleListenerAbility(){
	return cellModuleLA;
    }
    
    public void UpdateCellData(){
	cellModuleLA.notifyCellChangeListener();
    }
}
