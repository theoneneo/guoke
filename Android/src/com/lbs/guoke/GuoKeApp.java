package com.lbs.guoke;

import java.util.ArrayList;

import com.lbs.guoke.cell.CellModule.CellInfo;
import com.lbs.guoke.controller.CellModuleManager;
import com.lbs.guoke.controller.GuoKeService;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
public class GuoKeApp extends Application {
    private static Handler MainHandler;
    private static CellModuleManager mCellManager;
    
    private static GuoKeApp app;
    @Override
    public void onCreate() {
	super.onCreate();
	app = this;
	mCellManager = CellModuleManager.instance(this);
	mCellManager.startService();
    }
    
    public static GuoKeApp getApplication() {
        return app;
    }
    
    public void updateCell(){
	if(MainHandler != null){
	    Message msg = Message.obtain();
	    MainHandler.sendEmptyMessage(0);
	}
    }
    
    public static void setMainHandler(Handler handler){
    	MainHandler = handler;
    }
}
