package edu.sdjzu.localtool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * DatabaseManager���ݿ������
 * 
 * @author Administrator
 * 
 */
public class DatabaseManager {

	private static DBHelper helper;
	private static SQLiteDatabase db = null;
	private Context context = null;
	private static DatabaseManager dm = null;

	/**
	 * DatabaseManager���캯��
	 * 
	 * @param context
	 *            ����������
	 */
	public DatabaseManager(Context context) {
		this.context = context;
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	public static DatabaseManager getInstance(Context ctx) {
		if (dm == null) {
			dm = new DatabaseManager(ctx);
		}
		return dm;
	}

	/**
	 * ���뺯��
	 * 
	 * @param tableName
	 *            ���ݱ���
	 * @param nullValue
	 *            �������ֵ
	 * @param values
	 *            �¼�¼ֵ��ɵ�Values
	 * @return ����long���͵��½���¼�к�
	 */
	public long Insert(String tableName, String nullValue, ContentValues values) {
		return db.insert(tableName, nullValue, values);
	}

	/**
	 * ���º���
	 * 
	 * @param tableName
	 *            ����
	 * @param values
	 *            �޸ĵ�����ɵ�Values
	 * @param whereClause
	 *            ���µ��������
	 * @param whereArgs
	 *            �����������Ĳ�������
	 * @return ����int���͵�Ӱ������
	 */
	public int Upadte(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
		return db.update(tableName, values, whereClause, whereArgs);
	}

	/**
	 * ɾ������
	 * 
	 * @param tableName
	 *            ����
	 * @param whereClause
	 *            ɾ�����������
	 * @param whereArgs
	 *            ɾ���������Ĳ�������
	 * @return ɾ��Ӱ�������
	 */
	public int Delete(String tableName, String whereClause, String[] whereArgs) {
		return db.delete(tableName, whereClause, whereArgs);
	}

	/**
	 * Execִ��insert��delete��update��CREATE TABLE֮���и�����Ϊ��SQL���
	 * 
	 * @param sql
	 *            SQL���
	 */
	public void execSQL(String sql) {
		db.execSQL(sql);
	}

	/**
	 * Execִ��insert��delete��update��CREATE TABLE֮���и�����Ϊ��SQL���
	 * 
	 * @param sql
	 *            ��ֵSQL���
	 * @param bindArgs
	 */
	public void execSQL(String sql, Object[] bindArgs) {
		db.execSQL(sql, bindArgs);
	}

	/**
	 * ִ��select���
	 * 
	 * @param sql
	 *            SQL��ѯ���
	 * @param selectionArgs
	 * @return ����SQL���
	 */
	public Cursor Query(String sql, String[] selectionArgs) {
		return db.rawQuery(sql, selectionArgs);
	}

	/**
	 * �ر�database����
	 */
	public void closeDB() {
		db.close();
		helper.close();
	}

	public void beginTransaction() {
		db.beginTransaction();
	}

	public void setTransactionSuccessful() {
		db.setTransactionSuccessful();
	}

	public void endTransaction() {
		db.endTransaction();
	}

}
