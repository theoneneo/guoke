package com.lbs.guoke.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataBase {
    public static final class CELL_DATA_DB implements BaseColumns {
	public static final Uri CONTENT_URI = Uri.parse("content://com.lbs.guoke.db.provider/cell_data");
	public static final String _ID = "_id";
	public static final String KEY = "key";
	public static final String ISCDMA = "iscdma";
	public static final String CELLID = "cellid";
	public static final String LAC = "lac";
	public static final String MNC = "mnc";
	public static final String MCC = "mcc";

	public static final String CREATE_TABLE = "CREATE TABLE cell_data(_id INTEGER PRIMARY KEY AUTOINCREMENT"
	    + ", key TEXT, iscdma INTEGER, cellid INTEGER, lac INTEGER, mnc INTEGER, mcc INTEGER);";
    }
    
    public static final class MESSAGE_DATA_DB implements BaseColumns {
	public static final Uri CONTENT_URI = Uri.parse("content://com.lbs.guoke.db.provider/message_data");
	public static final String _ID = "_id";
	public static final String KEY = "key";
	public static final String MESSAGE = "message";
	public static final String LINK = "link";

	public static final String CREATE_TABLE = "CREATE TABLE message_data(_id INTEGER PRIMARY KEY AUTOINCREMENT"
	    + ", key TEXT, message TEXT, link TEXT);";
    }
    
    public static final class REMIND_DATA_DB implements BaseColumns {
	public static final Uri CONTENT_URI = Uri.parse("content://com.lbs.guoke.db.provider/remind_data");
	public static final String _ID = "_id";
	public static final String REMINDID = "remindid";
	public static final String KEY = "key";
	public static final String TITLE = "title";
	public static final String MESSAGE = "message";
	public static final String ISREMIND = "isremind";
	public static final String ISVIBRATE = "isvibrate";
	public static final String REMINDTIME = "remindtime";

	public static final String CREATE_TABLE = "CREATE TABLE remind_data(_id INTEGER PRIMARY KEY AUTOINCREMENT"
	    + ", remindid TEXT, key TEXT, title TEXT, message TEXT, isremind INTEGER, isvibrate INTEGER, remindtime INTEGER);";
    }
    
    public static final class ADDRESS_DATA_DB implements BaseColumns {
	public static final Uri CONTENT_URI = Uri.parse("content://com.lbs.guoke.db.provider/address_data");
	public static final String _ID = "_id";
	public static final String KEY = "key";
	public static final String NAME = "name";
	public static final String ADDRESS = "address";
	public static final String TYPE = "type";
	public static final String IMAGE = "image";
	public static final String MARK = "mark";

	public static final String CREATE_TABLE = "CREATE TABLE address_data(_id INTEGER PRIMARY KEY AUTOINCREMENT"
	    + ", key TEXT, name TEXT, address TEXT, type INTEGER, image TEXT, mark TEXT);";
    }
}
