package com.lbs.guoke.controller;

import com.lbs.guoke.cell.CellModule;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class GuoKeService extends Service {
	private static CellModule cellModule = null;
	
	private IBinder mBinder = new CellBinder();

	public GuoKeService() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		if (cellModule == null)//适配不同手机有可能无法保存回调函数
		cellModule = new CellModule(this);
		cellModule.enable();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (cellModule != null)
			cellModule.disable();		
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public class CellBinder extends Binder {
		public GuoKeService getService() {
			return GuoKeService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		if (null == mBinder)
			mBinder = new CellBinder();
		return mBinder;
	}
}
