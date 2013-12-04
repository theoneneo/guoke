package com.lbs.guoke.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataBase {
    public static final class CELL_DATA_COLLECT_DB implements BaseColumns {
	public static final Uri CONTENT_URI = Uri.parse("content://com.lbs.guoke.db.provider/cell_data_collect");
	public static final String _ID = "_id";
	public static final String CELLID = "cellid";
	public static final String NAME = "name";
	public static final String INTRODUCTION = "introduction";
	public static final String THUMIMG = "thumimg";

	public static final String CREATE_TABLE = "CREATE TABLE cell_data_collect(_id INTEGER PRIMARY KEY AUTOINCREMENT"
	    + ", cellid INTEGER, name TEXT, introduction TEXT, thumimg TEXT);";
    }
}
