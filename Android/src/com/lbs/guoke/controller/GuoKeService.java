package com.lbs.guoke.controller;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class GuoKeService extends Service {
    private IBinder mBinder = new LocalBinder();
    public GuoKeService() {
    }

    @Override
    public void onCreate() {
	super.onCreate();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
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
