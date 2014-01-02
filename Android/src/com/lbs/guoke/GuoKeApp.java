package com.lbs.guoke;

import com.lbs.guoke.controller.CellModuleManager;
import com.lbs.guoke.controller.GuoKeService;
import com.lbs.guoke.controller.MySiteModuleManager;
import com.lbs.guoke.controller.RemindModuleManager;
import com.lbs.guoke.db.DBTools;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public class GuoKeApp extends Application {
	public static final int GUOKE_SITE_UPDATE = 10000;
	public static final int GUOKE_REMIND_UPDATE = GUOKE_SITE_UPDATE + 1;

	public static Handler mainHandler;
	private static CellModuleManager mCellManager;
	private static RemindModuleManager mRemindManager;
	private static MySiteModuleManager mMySiteManager;
	private static GuoKeApp app;

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		DBTools.instance(this);
		mCellManager = CellModuleManager.instance();
		mRemindManager = RemindModuleManager.instance();
		mMySiteManager = MySiteModuleManager.instance();
	}

	public void onTerminate() {
		super.onTerminate();
		mCellManager.destoryCellModuleManager();
	}

	public static GuoKeApp getApplication() {
		return app;
	}

	public static void setMainHandler(Handler handler) {
		mainHandler = handler;
	}

	public void eventAction(int eventType, Object obj) {
		// TODO Auto-generated method stub
		Message msg = Message.obtain();
		msg.what = eventType;
		msg.obj = obj;
		GuoKeHandler.sendMessage(msg);
	}

	private Handler GuoKeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void startService() {
		Intent i = new Intent(app.getApplicationContext(), GuoKeService.class);
		app.getApplicationContext().startService(i);
	}

	public void stopService() {
		Intent i = new Intent(app.getApplicationContext(), GuoKeService.class);
		app.getApplicationContext().stopService(i);
	}
}
