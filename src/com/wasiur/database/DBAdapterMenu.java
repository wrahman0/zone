package com.wasiur.database;
import java.sql.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wasiur.uwfood.MainActivity;

public class DBAdapterMenu {
	/*Stores the parsed infor into this db. The parsed info is the following
	 */

	//Database columns
	public static final String KEY_ROWID = "_id";
	public static final String KEY_MENU = "menu_date"; //As a JSONObject to string
	public static final String KEY_INFO = "menu_raw"; //As a JSONObject to string

	//Database properties
	private static final String DATABASE_NAME = "Menu.db";
	private static final String DATABASE_TABLE = "Menu";
	private static final int DATABASE_VERSION = 2;
	
	private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("+ KEY_ROWID + " integer primary key autoincrement, " + 
			KEY_MENU + " text not null, " +
			KEY_INFO + " text not null);";
	
	final Context context;
	
	DatabaseHelper DBHelper;
	SQLiteDatabase db;

	public DBAdapterMenu (Context ctx){
		this.context = ctx;
		DBHelper = new DatabaseHelper (context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper (Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(MainActivity.sTAG,"Creating " + DATABASE_TABLE + ".");
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i(MainActivity.sTAG,"Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE +";");
			onCreate(db);
		}
	}
	
	public DBAdapterMenu open() throws SQLException {
		Log.i(MainActivity.sTAG, "OPENNING DB...");
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close(){
		Log.i(MainActivity.sTAG, "CLOSING DB...");
		DBHelper.close();
	}
	
	public long insertMenu(String menu, String data){
		ContentValues menuJSON = new ContentValues();
		menuJSON.put(KEY_MENU, menu);
		menuJSON.put(KEY_INFO, data);
		return db.insert(DATABASE_TABLE, null, menuJSON);
	}
	
	public Cursor getMenu(){
		Log.w(MainActivity.sTAG, "GETTING LOCATION...");
		return  db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_MENU, KEY_INFO},null,null,null,null,null);
	}

}
