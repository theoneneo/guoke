package com.lbs.guoke;

import java.util.ArrayList;

import com.lbs.guoke.cell.CellModule.CellInfo;
import com.lbs.guoke.controller.GuoKeService;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
public class GuoKeApp extends Application {
    
    public ArrayList<CellInfo> mCellInfos = new ArrayList<CellInfo>();
    private static Handler MainHandler;
    
    private static GuoKeApp app;
    @Override
    public void onCreate() {
	super.onCreate();
	app = this;
	startService();
    }
    
    public static GuoKeApp getApplication() {
        return app;
    }
    
    public ArrayList<CellInfo> getCellInfos(){
	return mCellInfos;
    }
    
    private void startService(){
	Intent i = new Intent(this, GuoKeService.class);
	startService(i);
    }
    
    public void stopService(){
	Intent i = new Intent(this, GuoKeService.class);
	stopService(i);
    }
    
    public void updateCell(){
	if(MainHandler != null){
	    Message msg = Message.obtain();
	    MainHandler.sendEmptyMessage(what);
	}
    }
    
    public static void setMainHandler(Handler handler){
    	MainHandler = handler;
    }
}
