package com.lbs.guoke.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.lbs.guoke.db.DataBase.ADDRESS_DATA_DB;
import com.lbs.guoke.db.DataBase.CELL_DATA_DB;
import com.lbs.guoke.db.DataBase.REMIND_DATA_DB;
import com.lbs.guoke.structure.CellInfo;

public class DBTools {
	private static DBTools mInstance;
	private static Context mContext;

	private DBTools(Context context) {
		mContext = context;
	}

	public static DBTools instance(Context context) {
		synchronized (DBTools.class) {
			if (mInstance == null) {
				mInstance = new DBTools(context);
			}
			return mInstance;
		}
	}

	public static DBTools instance() {
		return mInstance;
	}

	public static void closeDB() {
		DBContentProvider.closeDB();
	}

	public static Cursor getAllCellInfos() {
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(
					CELL_DATA_DB.CONTENT_URI, null, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				return cursor;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}

	public static Cursor getAllRemindInfos() {
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(
					REMIND_DATA_DB.CONTENT_URI, null, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				return cursor;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}

	public static Cursor getAllSiteInfos() {
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(
					ADDRESS_DATA_DB.CONTENT_URI, null, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				return cursor;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}

	public static Cursor getSiteInfo(String key) {
		String selection = ADDRESS_DATA_DB.KEY + "='" + toValidRs(key) + "'";
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(
					ADDRESS_DATA_DB.CONTENT_URI, null, selection, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				return cursor;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}

	public static Cursor getRemindInfo(String key) {
		String selection = REMIND_DATA_DB.KEY + "='" + toValidRs(key) + "'";
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(
					REMIND_DATA_DB.CONTENT_URI, null, selection, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				return cursor;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}

	public void insertSiteData(String key, String name, String address,
			int type, String imageLink, String mark,
			ArrayList<CellInfo> cellInfos) {
		if (cellInfos == null || name == null || address == null)
			return;

		ContentValues value = new ContentValues();
		value.put("key", toValidRs(key));
		value.put("name", toValidRs(name));
		value.put("address", toValidRs(address));
		value.put("type", type);
		value.put("image", toValidRs(imageLink));
		value.put("mark", toValidRs(mark));
		mContext.getContentResolver()
				.insert(ADDRESS_DATA_DB.CONTENT_URI, value);

		for (int i = 0; i < cellInfos.size(); i++) {
			insertCellData(key, cellInfos.get(i).isCDMA,
					cellInfos.get(i).cellid, cellInfos.get(i).lac,
					cellInfos.get(i).mnc, cellInfos.get(i).mcc);
		}
	}

	public void insertCellData(String key, int iscdma, int cellid, int lac,
			int mnc, int mcc) {
		ContentValues value = new ContentValues();
		value.put("key", toValidRs(key));
		value.put("iscdma", iscdma);
		value.put("cellid", cellid);
		value.put("lac", lac);
		value.put("mnc", mnc);
		value.put("mcc", mcc);
		mContext.getContentResolver().insert(CELL_DATA_DB.CONTENT_URI, value);
	}

	public void insertRemindData(String key, String title, String message,
			int isremind, int isvibrate, int remindmusic) {
		ContentValues value = new ContentValues();
		value.put("key", toValidRs(key));
		value.put("title", toValidRs(title));
		value.put("message", toValidRs(message));
		value.put("isremind", isremind);
		value.put("isvibrate", isvibrate);
		value.put("remindmusic", remindmusic);
		mContext.getContentResolver().insert(REMIND_DATA_DB.CONTENT_URI, value);
	}

	public void updateSiteInfo(String key, String name, String address,
			int type, String imageLink, String mark) {
		String selection = ADDRESS_DATA_DB.KEY + "='" + toValidRs(key) + "'";
		ContentValues value = new ContentValues();
		value.put("name", toValidRs(name));
		value.put("address", toValidRs(address));
		value.put("type", type);
		value.put("image", toValidRs(imageLink));
		value.put("mark", toValidRs(mark));
		mContext.getContentResolver().update(ADDRESS_DATA_DB.CONTENT_URI,
				value, selection, null);
	}

	public void updateRemindInfo(String key, String title, String message,
			int isremind, int isvibrate, int remindmusic) {
		String selection = REMIND_DATA_DB.KEY + "='" + toValidRs(key) + "'";
		ContentValues value = new ContentValues();
		value.put("title", toValidRs(title));
		value.put("message", toValidRs(message));
		if(isremind != -1)
			value.put("isremind", isremind);
		value.put("isvibrate", isvibrate);
		value.put("remindmusic", remindmusic);
		mContext.getContentResolver().update(REMIND_DATA_DB.CONTENT_URI,
				value, selection, null);
	}

	public static String toValidRs(String obj) {
		if (obj == null)
			return "@*@";
		else if (obj.indexOf("'") != -1) {
			return obj.replace("'", "*@*");
		} else
			return obj;
	}

	public static String getUnvalidFormRs(String obj) {
		if (obj == null)
			return null;
		else if (obj.equals("@*@"))
			return null;
		else if (obj.indexOf("*@*") != -1) {
			return obj.replace("*@*", "'");
		} else
			return obj;
	}
}
