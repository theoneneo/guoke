package com.lbs.guoke.controller;

import java.util.ArrayList;

import com.lbs.guoke.cell.CellModule;
import com.lbs.guoke.structure.CellInfo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class GuoKeService extends Service {
	private static CellModule cellModule;
	
	private IBinder mBinder = new CellBinder();

	public GuoKeService() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		cellModule = new CellModule(this);
		cellModule.enable();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (cellModule != null)
			cellModule.disable();		
		super.onDestroy();
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
