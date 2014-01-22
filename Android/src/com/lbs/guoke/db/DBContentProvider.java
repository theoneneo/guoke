package com.lbs.guoke.db;

import java.util.HashMap;

import com.lbs.guoke.db.DataBase.ADDRESS_DATA_DB;
import com.lbs.guoke.db.DataBase.CELL_DATA_DB;
import com.lbs.guoke.db.DataBase.MESSAGE_DATA_DB;
import com.lbs.guoke.db.DataBase.REMIND_DATA_DB;

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
	private static final String DATABASE_NAME = "guoke_data.db";
	private static final int DATABASE_VERSION = 1;

	private static final String TB_CELL_DATA = "cell_data";
	private static final String TB_MESSAGE_DATA = "message_data";
	private static final String TB_REMIND_DATA = "remind_data";
	private static final String TB_ADDRESS_DATA = "address_data";

	private static final int CELL_DATA = 1;
	private static final int CELL_DATA_ID = 2;
	private static final int MESSAGE_DATA = 3;
	private static final int MESSAGE_DATA_ID = 4;
	private static final int REMIND_DATA = 5;
	private static final int REMIND_DATA_ID = 6;
	private static final int ADDRESS_DATA = 7;
	private static final int ADDRESS_DATA_ID = 8;

	private static HashMap<String, String> cellDataMap;
	private static HashMap<String, String> messageDataMap;
	private static HashMap<String, String> remindDataMap;
	private static HashMap<String, String> addressDataMap;

	private static final UriMatcher sUriMatcher;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		sUriMatcher.addURI(URI_AUTHORITY, "cell_data", CELL_DATA);
		sUriMatcher.addURI(URI_AUTHORITY, "cell_data/#", CELL_DATA_ID);
		sUriMatcher.addURI(URI_AUTHORITY, "message_data", MESSAGE_DATA);
		sUriMatcher.addURI(URI_AUTHORITY, "message_data/#", MESSAGE_DATA_ID);
		sUriMatcher.addURI(URI_AUTHORITY, "remind_data", REMIND_DATA);
		sUriMatcher.addURI(URI_AUTHORITY, "remind_data/#", REMIND_DATA_ID);
		sUriMatcher.addURI(URI_AUTHORITY, "address_data", ADDRESS_DATA);
		sUriMatcher.addURI(URI_AUTHORITY, "address_data/#", ADDRESS_DATA_ID);

		cellDataMap = new HashMap<String, String>();
		cellDataMap.put(CELL_DATA_DB._ID, CELL_DATA_DB._ID);
		cellDataMap.put(CELL_DATA_DB.KEY, CELL_DATA_DB.KEY);
		cellDataMap.put(CELL_DATA_DB.ISCDMA, CELL_DATA_DB.ISCDMA);
		cellDataMap.put(CELL_DATA_DB.CELLID, CELL_DATA_DB.CELLID);
		cellDataMap.put(CELL_DATA_DB.LAC, CELL_DATA_DB.LAC);
		cellDataMap.put(CELL_DATA_DB.MNC, CELL_DATA_DB.MNC);
		cellDataMap.put(CELL_DATA_DB.MCC, CELL_DATA_DB.MCC);

		messageDataMap = new HashMap<String, String>();
		messageDataMap.put(MESSAGE_DATA_DB._ID, MESSAGE_DATA_DB._ID);
		messageDataMap.put(MESSAGE_DATA_DB.KEY, MESSAGE_DATA_DB.KEY);
		messageDataMap.put(MESSAGE_DATA_DB.MESSAGE, MESSAGE_DATA_DB.MESSAGE);
		messageDataMap.put(MESSAGE_DATA_DB.LINK, MESSAGE_DATA_DB.LINK);

		remindDataMap = new HashMap<String, String>();
		remindDataMap.put(REMIND_DATA_DB._ID, REMIND_DATA_DB._ID);
		remindDataMap.put(REMIND_DATA_DB.REMINDID, REMIND_DATA_DB.REMINDID);
		remindDataMap.put(REMIND_DATA_DB.KEY, REMIND_DATA_DB.KEY);
		remindDataMap.put(REMIND_DATA_DB.TITLE, REMIND_DATA_DB.TITLE);
		remindDataMap.put(REMIND_DATA_DB.MESSAGE, REMIND_DATA_DB.MESSAGE);
		remindDataMap.put(REMIND_DATA_DB.ISREMIND, REMIND_DATA_DB.ISREMIND);
		remindDataMap.put(REMIND_DATA_DB.ISVIBRATE, REMIND_DATA_DB.ISVIBRATE);
		remindDataMap.put(REMIND_DATA_DB.REMINDTIME, REMIND_DATA_DB.REMINDTIME);

		addressDataMap = new HashMap<String, String>();
		addressDataMap.put(ADDRESS_DATA_DB._ID, ADDRESS_DATA_DB._ID);
		addressDataMap.put(ADDRESS_DATA_DB.KEY, ADDRESS_DATA_DB.KEY);
		addressDataMap.put(ADDRESS_DATA_DB.NAME, ADDRESS_DATA_DB.NAME);
		addressDataMap.put(ADDRESS_DATA_DB.ADDRESS, ADDRESS_DATA_DB.ADDRESS);
		addressDataMap.put(ADDRESS_DATA_DB.TYPE, ADDRESS_DATA_DB.TYPE);
		addressDataMap.put(ADDRESS_DATA_DB.IMAGE, ADDRESS_DATA_DB.IMAGE);
		addressDataMap.put(ADDRESS_DATA_DB.MARK, ADDRESS_DATA_DB.MARK);
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
			qb.setTables(TB_CELL_DATA);
			qb.setProjectionMap(cellDataMap);
			break;
		case CELL_DATA_ID:
			qb.setTables(TB_CELL_DATA);
			qb.setProjectionMap(cellDataMap);
			qb.appendWhere(CELL_DATA_DB._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		case MESSAGE_DATA:
			qb.setTables(TB_MESSAGE_DATA);
			qb.setProjectionMap(messageDataMap);
			break;
		case MESSAGE_DATA_ID:
			qb.setTables(TB_MESSAGE_DATA);
			qb.setProjectionMap(messageDataMap);
			qb.appendWhere(MESSAGE_DATA_DB._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		case REMIND_DATA:
			qb.setTables(TB_REMIND_DATA);
			qb.setProjectionMap(remindDataMap);
			break;
		case REMIND_DATA_ID:
			qb.setTables(TB_REMIND_DATA);
			qb.setProjectionMap(remindDataMap);
			qb.appendWhere(REMIND_DATA_DB._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		case ADDRESS_DATA:
			qb.setTables(TB_ADDRESS_DATA);
			qb.setProjectionMap(addressDataMap);
			break;
		case ADDRESS_DATA_ID:
			qb.setTables(TB_ADDRESS_DATA);
			qb.setProjectionMap(addressDataMap);
			qb.appendWhere(ADDRESS_DATA_DB._ID + "="
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
			retval = db.insert(TB_CELL_DATA, CELL_DATA_DB._ID, values);
			myURI = CELL_DATA_DB.CONTENT_URI;
			break;
		case MESSAGE_DATA:
			retval = db.insert(TB_MESSAGE_DATA, MESSAGE_DATA_DB._ID, values);
			myURI = MESSAGE_DATA_DB.CONTENT_URI;
			break;
		case REMIND_DATA:
			retval = db.insert(TB_REMIND_DATA, REMIND_DATA_DB._ID, values);
			myURI = REMIND_DATA_DB.CONTENT_URI;
			break;
		case ADDRESS_DATA:
			retval = db.insert(TB_ADDRESS_DATA, ADDRESS_DATA_DB._ID, values);
			myURI = ADDRESS_DATA_DB.CONTENT_URI;
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
			retval = db.update(TB_CELL_DATA, values, selection, selectionArgs);
			break;
		case CELL_DATA_ID:
			retval = db.update(TB_CELL_DATA, values,
					CELL_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		case MESSAGE_DATA:
			retval = db.update(TB_MESSAGE_DATA, values, selection,
					selectionArgs);
			break;
		case MESSAGE_DATA_ID:
			retval = db.update(TB_MESSAGE_DATA, values,
					MESSAGE_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		case REMIND_DATA:
			retval = db
					.update(TB_REMIND_DATA, values, selection, selectionArgs);
			break;
		case REMIND_DATA_ID:
			retval = db.update(TB_REMIND_DATA, values,
					REMIND_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		case ADDRESS_DATA:
			retval = db.update(TB_ADDRESS_DATA, values, selection,
					selectionArgs);
			break;
		case ADDRESS_DATA_ID:
			retval = db.update(TB_ADDRESS_DATA, values,
					MESSAGE_DATA_DB._ID
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
			retval = db.delete(TB_CELL_DATA, selection, selectionArgs);
			break;
		case CELL_DATA_ID:
			retval = db.delete(TB_CELL_DATA,
					CELL_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		case MESSAGE_DATA:
			retval = db.delete(TB_MESSAGE_DATA, selection, selectionArgs);
			break;
		case MESSAGE_DATA_ID:
			retval = db.delete(TB_MESSAGE_DATA,
					MESSAGE_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		case REMIND_DATA:
			retval = db.delete(TB_REMIND_DATA, selection, selectionArgs);
			break;
		case REMIND_DATA_ID:
			retval = db.delete(TB_REMIND_DATA,
					REMIND_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		case ADDRESS_DATA:
			retval = db.delete(TB_ADDRESS_DATA, selection, selectionArgs);
			break;
		case ADDRESS_DATA_ID:
			retval = db.delete(TB_ADDRESS_DATA,
					ADDRESS_DATA_DB._ID
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
	private static final String TB_CELL_DATA = "cell_data";
	private static final String TB_MESSAGE_DATA = "message_data";
	private static final String TB_REMIND_DATA = "remind_data";
	private static final String TB_ADDRESS_DATA = "address_data";

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
		db.execSQL(CELL_DATA_DB.CREATE_TABLE);
		db.execSQL(MESSAGE_DATA_DB.CREATE_TABLE);
		db.execSQL(REMIND_DATA_DB.CREATE_TABLE);
		db.execSQL(ADDRESS_DATA_DB.CREATE_TABLE);
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
		db.execSQL("DROP TABLE IF EXISTS " + TB_CELL_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TB_MESSAGE_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TB_REMIND_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TB_ADDRESS_DATA);
		onCreate(db);
	}
}
