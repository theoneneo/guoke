package com.lbs.guoke;

import java.util.ArrayList;

import com.lbs.guoke.controller.CellModuleManager;
import com.lbs.guoke.controller.MySiteModuleManager;
import com.lbs.guoke.controller.RemindModuleManager;
import com.lbs.guoke.db.DBTools;
import com.neo.tools.SystemTools;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
//TODO 已经提醒了，是否还需要提醒  半小时提醒
//TODO 是否要关闭cell service 进程

public class GuoKeApp extends Application {
	public static final int GUOKE_SITE_UPDATE = 10000;
	public static final int GUOKE_REMIND_UPDATE = GUOKE_SITE_UPDATE + 1;
	public static final int GUOKE_REMIND_MATCH = GUOKE_REMIND_UPDATE + 1;
	public static final int GUOKE_CELL_DB = GUOKE_REMIND_MATCH + 1;
	public static final int GUOKE_EXIT = GUOKE_CELL_DB + 1;

	public static Handler mainHandler;
	private static CellModuleManager mCellManager;
	private static RemindModuleManager mRemindManager;
	private static MySiteModuleManager mMySiteManager;
	private ArrayList<Activity> list = new ArrayList<Activity>();
	private static GuoKeApp app;
	private boolean isCellDB = false, isSiteDB = false, isRemindDB = false;

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
	}

	public static GuoKeApp getApplication() {
		return app;
	}

	public static Context getAppContext() {
		return app.getApplicationContext();
	}

	public void addActivity(Activity activity) {
		list.add(activity);
	}

	public void exit() {
		mRemindManager.destory();
		mCellManager.destory();
		for (Activity activity : list) {
			activity.finish();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
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

	public void eventAction(int eventType) {
		// TODO Auto-generated method stub
		Message msg = Message.obtain();
		msg.what = eventType;
		GuoKeHandler.sendMessage(msg);
	}

	private Handler GuoKeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GUOKE_REMIND_MATCH:
				mRemindManager.matchRemindInfo();
				break;
			case GUOKE_CELL_DB:
				isCellDB = true;
				if (isSiteDB && isRemindDB)
					CellModuleManager.instance().UpdateCellData();
				break;
			case GUOKE_SITE_UPDATE:
				isSiteDB = true;
				if (isCellDB && isRemindDB)
					CellModuleManager.instance().UpdateCellData();
				if (mainHandler != null)
					mainHandler.sendEmptyMessage(msg.what);
				break;
			case GUOKE_REMIND_UPDATE:
				isRemindDB = true;
				if (isSiteDB && isCellDB)
					CellModuleManager.instance().UpdateCellData();
				if (mainHandler != null)
					mainHandler.sendEmptyMessage(msg.what);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
}
