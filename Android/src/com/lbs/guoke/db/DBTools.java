package com.lbs.guoke.db;

import java.util.ArrayList;

import com.lbs.guoke.db.DataBase.CELL_DATA_COLLECT_DB;

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

    public static Cursor findCellData(ArrayList<Integer> cellids) {
	if(cellids == null)
	    return null;
	Cursor cursor = null;
	try {
	    int size = cellids.size();
	    StringBuffer buf = new StringBuffer();
	    for(int i = 0; i < size; i++){
		buf.append(CELL_DATA_COLLECT_DB.CELLID+ " = ");
		buf.append(String.valueOf(cellids.get(i)));
		if(i != size - 1)
		    buf.append(" or ");
	    }
	    cursor = mContext.getContentResolver().query(
		    CELL_DATA_COLLECT_DB.CONTENT_URI, null, buf.toString(), null, null);
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

//    public static Cursor findCellDataItem(String time) {
//	Cursor cursor = null;
//	try {
//	    cursor = mContext.getContentResolver().query(
//		    CELL_DATA_COLLECT_DB.CONTENT_URI, null, "time = " + time, null,
//		    null);
//	    if (cursor != null) {
//		cursor.moveToFirst();
//		return cursor;
//	    } else {
//		return null;
//	    }
//	} catch (Exception e) {
//	    e.printStackTrace();
//	}
//	return cursor;
//    }

    public void insertCellData(int cellid, String name, String introduction, String thumimg) {
	ContentValues value = new ContentValues();
	value.put("cellid", cellid);
	value.put("name", toValidRs(name));
	value.put("introduction", toValidRs(introduction));
	value.put("thumimg", toValidRs(thumimg));
	mContext.getContentResolver().insert(CELL_DATA_COLLECT_DB.CONTENT_URI, value);
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
