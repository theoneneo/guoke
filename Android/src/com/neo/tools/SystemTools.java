package com.neo.tools;

import com.lbs.guoke.GuoKeApp;
import com.lbs.guoke.controller.CellModuleManager;

import android.os.PowerManager;

public class SystemTools {
	private static SystemTools instance;
	private PowerManager.WakeLock mWakeLock;

	public SystemTools() {

	}

	public static SystemTools instance() {
		synchronized (CellModuleManager.class) {
			if (instance == null) {
				instance = new SystemTools();
			}
			return instance;
		}
	}

	public void wakeLockStart() {
		if (mWakeLock == null) {
			PowerManager pm = (PowerManager) GuoKeApp.getApplication()
					.getSystemService(GuoKeApp.getApplication().POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
					| PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
		}
		mWakeLock.acquire();
	}

	public void wakeLockStop() {
		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}
}
