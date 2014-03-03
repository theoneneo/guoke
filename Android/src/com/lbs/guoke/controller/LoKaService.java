package com.lbs.guoke.controller;

import com.lbs.guoke.cell.CellModule;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

public class LoKaService extends Service {
	private static CellModule cellModule = null;
	private IBinder mBinder = new CellBinder();

	public LoKaService() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// if (cellModule == null)//适配不同手机有可能无法保存回调函数
		cellModule = new CellModule(this);
		cellModule.enable();
		
		final IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(mBatInfoReceiver, filter);

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mBatInfoReceiver);
		if (cellModule != null)
			cellModule.disable();
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			final String action = intent.getAction();
			if (Intent.ACTION_SCREEN_ON.equals(action)) {
				cellModule.stopCellBroadCastSchedule();
			}
		}
	};

	public class CellBinder extends Binder {
		public LoKaService getService() {
			return LoKaService.this;
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
