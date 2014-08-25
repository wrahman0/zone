package com.wasiur.database;
import java.sql.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wasiur.uwfood.MainActivity;

public class DBAdapterLocation {
	/*Stores the parsed info into this db. The parsed info is the following
	 */

	//Database columns
	public static final String KEY_ROWID = "_id";
	public static final String KEY_LOCATION = "location_raw"; //As a JSONObject to string

	//Database properties
	private static final String DATABASE_NAME = "Location.db";
	private static final String DATABASE_TABLE = "Location";
	private static final int DATABASE_VERSION = 2;
	
	private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("+ KEY_ROWID + " integer primary key autoincrement, " + 
			KEY_LOCATION + " text not null);";
	
	final Context context;
	
	DatabaseHelper DBHelper;
	SQLiteDatabase db;

	public DBAdapterLocation (Context ctx){
		this.context = ctx;
		DBHelper = new DatabaseHelper (context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper (Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(MainActivity.TAG,"Creating " + DATABASE_TABLE + ".");
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i(MainActivity.TAG,"Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE +";");
			onCreate(db);
		}
	}
	
	public DBAdapterLocation open() throws SQLException {
		Log.i(MainActivity.TAG, "OPENNING DB...");
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close(){
		Log.i(MainActivity.TAG, "CLOSING DB...");
		DBHelper.close();
	}
	
	public long insert(String location){
		ContentValues locationJSON = new ContentValues();
		locationJSON.put(KEY_LOCATION, location);
		return db.insert(DATABASE_TABLE, null, locationJSON);
	}
	
	public void delete(){
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE +";");
	}
	
	public Cursor getLocation(){
		Log.w(MainActivity.TAG, "GETTING LOCATION...");
		return  db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_LOCATION},null,null,null,null,null);
	}

}
