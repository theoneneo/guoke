package com.lbs.guoke.controller;

import com.lbs.guoke.cell.CellModule;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class GuoKeService extends Service {
    private CellModule cellModule = null;
    private IBinder mBinder = new LocalBinder();
    public GuoKeService() {
    }

    @Override
    public void onCreate() {
	super.onCreate();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
	Thread cellThread = new Thread(){
	    public void run() {
		cellModule = new CellModule(getApplicationContext());
		cellModule.enable();
	    }
	};
	cellThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
	cellModule.disable();
	super.onDestroy();
    }

    public class LocalBinder extends Binder {
	public GuoKeService getService() {
	    return GuoKeService.this;
	}
    }

    @Override
    public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	if (null == mBinder)
	    mBinder = new LocalBinder();
	return mBinder;
    }
}
