package com.lbs.guoke.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataBase {
    public static final class CELL_DATA_DB implements BaseColumns {
	public static final Uri CONTENT_URI = Uri.parse("content://com.lbs.guoke.db.provider/cell_data");
	public static final String _ID = "_id";
	public static final String KEY = "key";
	public static final String CELLID = "cellid";
	public static final String LAC = "lac";
	public static final String MNC = "mnc";
	public static final String MCC = "mcc";
	public static final String ADDRESS = "address";
	public static final String INTRODUCTION = "introduction";
	public static final String THUMIMG = "thumimg";

	public static final String CREATE_TABLE = "CREATE TABLE cell_data_collect(_id INTEGER PRIMARY KEY AUTOINCREMENT"
	    + ", key TEXT, cellid INTEGER, lac INTEGER, mnc INTEGER, mcc INTEGER, address TEXT, introduction TEXT, thumimg TEXT);";
    }
    
    public static final class MESSAGE_DATA_DB implements BaseColumns {
	public static final Uri CONTENT_URI = Uri.parse("content://com.lbs.guoke.db.provider/message_data");
	public static final String _ID = "_id";
	public static final String KEY = "key";
	public static final String MESSAGE = "message";
	public static final String LINK = "link";

	public static final String CREATE_TABLE = "CREATE TABLE cell_data_collect(_id INTEGER PRIMARY KEY AUTOINCREMENT"
	    + ", key TEXT, message TEXT, mcc link);";
    }
}
