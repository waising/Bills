package com.bosssoft.bills.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ���ݿ⹫���࣬�ṩ�������ݿ����
 * 
 * @author raymon
 * 
 */
public class DBHelper {
	// Ĭ�����ݿ�
	private static final String DB_NAME = "bills.db";

	// ���ݿ�汾
	private static final int DB_VERSION = 1;

	// ִ��open()�����ݿ�ʱ�����淵�ص����ݿ����
	private SQLiteDatabase mSQLiteDatabase = null;

	// ��SQLiteOpenHelper�̳й���
	private DatabaseHelper mDatabaseHelper = null;

	// ����Context����
	private Context mContext = null;
	
	private static DBHelper dbConn= null;
	
	// ��ѯ�α����
	private Cursor cursor;

	/**
	 * SQLiteOpenHelper�ڲ���
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			// ������getWritableDatabase()�� getReadableDatabase()����ʱ,����һ�����ݿ�
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE ad_record(id PRIMARY KEY NOT NULL, adUrl TEXT, apMac TEXT, createDate DATETIME);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS ad_record");
			onCreate(db);
		}
	}

	/**
	 * ���캯��
	 * 
	 * @param mContext
	 */
	private DBHelper(Context mContext) {
		super();
		this.mContext = mContext;
	}
	
	public static DBHelper getInstance(Context mContext){
		if (null == dbConn) {
			dbConn = new DBHelper(mContext);
		}
		return dbConn;
	}

	/**
	 * �����ݿ�
	 */
	public void open() {
		mDatabaseHelper = new DatabaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}

	/**
	 * �ر����ݿ�
	 */
	public void close() {
		if (null != mDatabaseHelper) {
			mDatabaseHelper.close();
		}
		if (null != cursor) {
			cursor.close();
		}
	}

	/**
	 * ��������
	 * @param tableName ����
	 * @param nullColumn null
	 * @param contentValues ��ֵ��
	 * @return �²������ݵ�ID�����󷵻�-1
	 * @throws Exception
	 */
	public long insert(String tableName, String nullColumn,
			ContentValues contentValues) throws Exception {
		try {
			return mSQLiteDatabase.insert(tableName, nullColumn, contentValues);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * ͨ������IDɾ������
	 * @param tableName ����
	 * @param key ������
	 * @param id ����ֵ
	 * @return ��Ӱ��ļ�¼��
	 * @throws Exception
	 */
	public long delete(String tableName, String key, int id) throws Exception {
		try {
			return mSQLiteDatabase.delete(tableName, key + " = " + id, null);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * ���ұ����������
	 * @param tableName ����
	 * @param columns ������������У�����null
	 * @return
	 * @throws Exception
	 */
	public Cursor findAll(String tableName, String [] columns) throws Exception{
		try {
			cursor = mSQLiteDatabase.query(tableName, columns, null, null, null, null, null);
			cursor.moveToFirst();
			return cursor;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * ����������������
	 * @param tableName ����
	 * @param key ������
	 * @param id  ����ֵ
	 * @param columns ������������У�����null
	 * @return Cursor�α�
	 * @throws Exception 
	 */
	public Cursor findById(String tableName, String key, int id, String [] columns) throws Exception {
		try {
			return mSQLiteDatabase.query(tableName, columns, key + " = " + id, null, null, null, null);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * ����������ѯ����
	 * @param tableName ����
	 * @param names ��ѯ����
	 * @param values ��ѯ����ֵ
	 * @param columns ������������У�����null
	 * @param orderColumn �������
	 * @param limit ���Ʒ�����
	 * @return Cursor�α�
	 * @throws Exception
	 */
	public Cursor find(String tableName, String [] names, String [] values, String [] columns, String orderColumn, String limit) throws Exception{
		try {
			StringBuffer selection = new StringBuffer();
			for (int i = 0; i < names.length; i++) {
				selection.append(names[i]);
				selection.append(" = ?");
				if (i != names.length - 1) {
					selection.append(",");
				}
			}
			cursor = mSQLiteDatabase.query(true, tableName, columns, selection.toString(), values, null, null, orderColumn, limit);
			cursor.moveToFirst();
			return cursor;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * @param tableName ����
	 * @param names ��ѯ����
	 * @param values ��ѯ����ֵ
	 * @param args ������-ֵ��
	 * @return true��false
	 * @throws Exception
	 */
	public boolean udpate(String tableName, String [] names, String [] values, ContentValues args) throws Exception{
		try {
			StringBuffer selection = new StringBuffer();
			for (int i = 0; i < names.length; i++) {
				selection.append(names[i]);
				selection.append(" = ?");
				if (i != names.length - 1) {
					selection.append(",");
				}
			}
			return mSQLiteDatabase.update(tableName, args, selection.toString(), values) > 0;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * ִ��sql��䣬����������ɾ��������
	 * 
	 * @param sql
	 */
	public void executeSql(String sql) {
		mSQLiteDatabase.execSQL(sql);
	}

}
//ת����ע��������http://forhope.iteye.com/blog/1461412
