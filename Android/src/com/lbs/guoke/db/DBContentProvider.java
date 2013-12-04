package com.lbs.guoke.db;

import java.util.HashMap;

import com.lbs.guoke.db.DataBase.CELL_DATA_COLLECT_DB;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.text.TextUtils;

public class DBContentProvider extends ContentProvider {
    private static DBProviderHelper dbHelper;

    private static final String URI_AUTHORITY = "com.lbs.guoke.db.provider";
    private static final String DATABASE_NAME = "cell_data_collect.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TB_CELL_DATA_COLLECT = "cell_data_collect";

    private static final int CELL_DATA = 1;
    private static final int CELL_DATA_ID = 2;

    private static HashMap<String, String> cellDataCoolectMap;

    private static final UriMatcher sUriMatcher;
    static {
	sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	sUriMatcher.addURI(URI_AUTHORITY, "cell_data_collect", CELL_DATA);
	sUriMatcher.addURI(URI_AUTHORITY, "cell_data_collect/#", CELL_DATA_ID);

	cellDataCoolectMap = new HashMap<String, String>();
	cellDataCoolectMap.put(CELL_DATA_COLLECT_DB._ID, CELL_DATA_COLLECT_DB._ID);
	cellDataCoolectMap.put(CELL_DATA_COLLECT_DB.CELLID, CELL_DATA_COLLECT_DB.CELLID);
	cellDataCoolectMap.put(CELL_DATA_COLLECT_DB.NAME, CELL_DATA_COLLECT_DB.NAME);
	cellDataCoolectMap.put(CELL_DATA_COLLECT_DB.INTRODUCTION, CELL_DATA_COLLECT_DB.INTRODUCTION);
	cellDataCoolectMap.put(CELL_DATA_COLLECT_DB.THUMIMG, CELL_DATA_COLLECT_DB.THUMIMG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#onCreate()
     */
    @Override
    public boolean onCreate() {
	dbHelper = new DBProviderHelper(this.getContext(), DATABASE_NAME, null,
		DATABASE_VERSION);
	return true;
    }

    public static void closeDB() {
	dbHelper.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#query(android.net.Uri,
     * java.lang.String[], java.lang.String, java.lang.String[],
     * java.lang.String)
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
	    String[] selectionArgs, String sortOrder) {
	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

	switch (sUriMatcher.match(uri)) {
	case CELL_DATA:
	    qb.setTables(TB_CELL_DATA_COLLECT);
	    qb.setProjectionMap(cellDataCoolectMap);
	    break;
	case CELL_DATA_ID:
	    qb.setTables(TB_CELL_DATA_COLLECT);
	    qb.setProjectionMap(cellDataCoolectMap);
	    qb.appendWhere(CELL_DATA_COLLECT_DB._ID + "="
		    + uri.getPathSegments().get(1));
	    break;
	default:
	    throw new IllegalArgumentException("Unsupported URI: " + uri);
	}
	getContext().getContentResolver().notifyChange(uri, null);
	SQLiteDatabase db = dbHelper.getReadableDatabase();
	Cursor cursor = qb.query(db, projection, selection, selectionArgs,
		null, null, sortOrder);
	cursor.setNotificationUri(getContext().getContentResolver(), uri);

	return cursor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#insert(android.net.Uri,
     * android.content.ContentValues)
     */
    @Override
    public Uri insert(Uri uri, ContentValues initValues) {
	ContentValues values = null;
	if (initValues == null) {
	    return null;
	} else {
	    values = new ContentValues(initValues);
	}

	SQLiteDatabase db = dbHelper.getWritableDatabase();

	Uri myURI;
	long retval = 0;
	switch (sUriMatcher.match(uri)) {
	case CELL_DATA:
	    retval = db
		    .insert(TB_CELL_DATA_COLLECT, CELL_DATA_COLLECT_DB._ID, values);
	    myURI = CELL_DATA_COLLECT_DB.CONTENT_URI;
	    break;
	default:
	    throw new IllegalArgumentException("Unknown URI " + uri);
	}

	if (retval > 0) {
	    Uri retUri = ContentUris.withAppendedId(myURI, retval);
	    retUri.buildUpon().build();
	    getContext().getContentResolver().notifyChange(retUri, null);

	    return retUri;
	}
	throw new SQLException("Failed to insert row into " + uri);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#update(android.net.Uri,
     * android.content.ContentValues, java.lang.String, java.lang.String[])
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
	    String[] selectionArgs) {
	int retval = 0;
	SQLiteDatabase db = dbHelper.getWritableDatabase();

	switch (sUriMatcher.match(uri)) {
	case CELL_DATA:
	    retval = db.update(TB_CELL_DATA_COLLECT, values, selection,
		    selectionArgs);
	    break;
	case CELL_DATA_ID:
	    retval = db.update(TB_CELL_DATA_COLLECT, values,
		    CELL_DATA_COLLECT_DB._ID
			    + "="
			    + uri.getPathSegments().get(1)
			    + (!TextUtils.isEmpty(selection) ? " AND ("
				    + selection + ')' : ""), selectionArgs);
	    break;

	default:
	    break;

	}

	getContext().getContentResolver().notifyChange(uri, null);
	return retval;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#delete(android.net.Uri,
     * java.lang.String, java.lang.String[])
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
	int retval = 0;
	SQLiteDatabase db = dbHelper.getWritableDatabase();

	switch (sUriMatcher.match(uri)) {
	case CELL_DATA:
	    retval = db.delete(TB_CELL_DATA_COLLECT, selection, selectionArgs);
	    break;
	case CELL_DATA_ID:
	    retval = db.delete(TB_CELL_DATA_COLLECT,
		    CELL_DATA_COLLECT_DB._ID
			    + "="
			    + uri.getPathSegments().get(1)
			    + (!TextUtils.isEmpty(selection) ? " AND ("
				    + selection + ')' : ""), selectionArgs);
	    break;
	default:
	    break;
	}

	getContext().getContentResolver().notifyChange(uri, null);
	return retval;
    }

    @Override
    public String getType(Uri uri) {
	// TODO Auto-generated method stub
	return null;
    }

    public SQLiteDatabase getHelper() {
	return dbHelper.getWritableDatabase();
    }

}

/**
 * @author jeff
 * 
 */
class DBProviderHelper extends SQLiteOpenHelper {
    /**
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    private static final String TB_CELL_DATA_COLLECT = "cell_data_collect";

    public DBProviderHelper(Context context, String name,
	    CursorFactory factory, int version) {
	super(context, name, factory, version);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
     * .SQLiteDatabase)
     */
    public void onCreate(SQLiteDatabase db) {
	db.execSQL(CELL_DATA_COLLECT_DB.CREATE_TABLE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
     * .SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS " + TB_CELL_DATA_COLLECT);
	onCreate(db);
    }
}
