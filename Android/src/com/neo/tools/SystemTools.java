package com.neo.tools;

import com.lbs.guoke.GuoKeApp;
import com.lbs.guoke.controller.CellModuleManager;

import android.content.Context;
import android.os.PowerManager;
import android.os.Vibrator;

public class SystemTools {
	private static SystemTools instance;
	private PowerManager.WakeLock mWakeLock;
	private Vibrator vibrator;

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

	public void startVibrator(Context context) {
		/*
		 * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
		 */
		vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 1000, 400, 1000, 400 }; // 停止 开启 停止 开启
		vibrator.vibrate(pattern, -2); // 重复两次上面的pattern 如果只想震动一次，index设为-1
	}
	
	public void stopVibrator(){
		if(vibrator != null)
			vibrator.cancel();
	}
}
