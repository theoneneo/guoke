package com.lbs.guoke.db;

import java.util.ArrayList;

import com.lbs.guoke.cell.CellModule.CellInfo;
import com.lbs.guoke.db.DataBase.CELL_DATA_DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DBTools {
    private static DBTools mInstance;
    private static Context mContext;

    private DBTools() {
    }

    public static DBTools getInstance() {
	synchronized (DBTools.class) {
	    if (mInstance == null) {
		mInstance = new DBTools();
	    }
	    return mInstance;
	}
    }

    public static void initDBContext(Context context) {
	mContext = context;
    }

    public static void closeDB() {
	DBContentProvider.closeDB();
    }

    public static Cursor findCellData(ArrayList<CellInfo> cellids) {
	if (cellids == null)
	    return null;
	Cursor cursor = null;
	try {
	    int size = cellids.size();
	    StringBuffer buf = new StringBuffer();
	    for (int i = 0; i < size; i++) {
		buf.append(CELL_DATA_DB.CELLID + " = ");
		buf.append(String.valueOf(cellids.get(i)));
		if (i != size - 1)
		    buf.append(" or ");
	    }
	    cursor = mContext.getContentResolver().query(
		    CELL_DATA_DB.CONTENT_URI, null, buf.toString(), null, null);
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
	return null;
    }

    public static Cursor getAllSiteInfos() {
	return null;
    }

    public void insertSiteData(String name, String address, int type, String imageLink, String mark) {
	ContentValues value = new ContentValues();
	value.put("key", "site"+System.currentTimeMillis());
	value.put("name", toValidRs(name));
	value.put("address", toValidRs(address));
	value.put("type", type);
	value.put("image", toValidRs(imageLink));
	value.put("mark", toValidRs(mark));
	mContext.getContentResolver().insert(CELL_DATA_DB.CONTENT_URI, value);
    }
    
    public void insertCellData(String name, String address, int type, String imageLink, String mark) {
	ContentValues value = new ContentValues();
	value.put("key", "site"+System.currentTimeMillis());
	value.put("name", toValidRs(name));
	value.put("address", toValidRs(address));
	value.put("type", type);
	value.put("image", toValidRs(imageLink));
	value.put("mark", toValidRs(mark));
	mContext.getContentResolver().insert(CELL_DATA_DB.CONTENT_URI, value);
    }
    
    public void insertRemindData(String name, String address, int type, String imageLink, String mark) {
	ContentValues value = new ContentValues();
	value.put("key", "site"+System.currentTimeMillis());
	value.put("name", toValidRs(name));
	value.put("address", toValidRs(address));
	value.put("type", type);
	value.put("image", toValidRs(imageLink));
	value.put("mark", toValidRs(mark));
	mContext.getContentResolver().insert(CELL_DATA_DB.CONTENT_URI, value);
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
