package edu.sdjzu.localtool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "sdjzu.db";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		// CursorFactory����Ϊnull,ʹ��Ĭ��ֵ
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE  UserInf  ( Uno  varchar(10) primary key, Upwd  varchar(20), Uname  varchar(20), "
				+ "Usex  varchar(1), Usdept  varchar(20), Uclass  varchar(100), Utel  varchar(15), Utype  varchar(5))");
		db.execSQL("CREATE TABLE  Students  (Sid  varchar(18) DEFAULT '��',Sno  varchar(15),Spwd  varchar(20),Ppwd  varchar(20),"
				+ "Sname  varchar(10),Ssex  varchar(1),Ssdept  varchar(10),Sclass  varchar(10),Sstate  varchar(2) DEFAULT '��У',"
				+ "Stel  varchar(11) DEFAULT 'δ��д',Ptel  varchar(11) DEFAULT 'δ��д',Spic  varchar(3) DEFAULT 'δ�ϴ�')");
		db.execSQL("CREATE TABLE  Teachers  (Tno  varchar(10) primary key,Tpwd  varchar(20),Tname  varchar(20),Tsex  varchar(1),Tsdept  varchar(10),"
				+ "Temail  varchar(50) DEFAULT 'δ��д',Tel  varchar(11) DEFAULT 'δ��д')");
		db.execSQL("CREATE TABLE  TeachTask  (Rno  int primary key,Cno  varchar(10),Cname  varchar(30),Tname  varchar(30),Rclass  varchar(30),Ctype  varchar(4),"
				+ "Rweek  varchar(5) DEFAULT '',Rterms  varchar(11) DEFAULT '')");
		db.execSQL("CREATE TABLE  TeachProgress  (Jno  int primary key ,Rno  int,Cname  varchar(50),Tname  varchar(30),Jclass  varchar(30),Jweek  varchar(2),"
				+ "Jtime  varchar(10),Jaddress  varchar(15),StartTime  datetime,EndTime  datetime,IsKQ  varchar(3) DEFAULT 'δ�ύ',InMan  varchar(20),InTime  datetime)");
		db.execSQL("CREATE TABLE  TeachLocalProgress  (Jno  int primary key ,Rno  int,Cname  varchar(50),Jweek  varchar(2),"
				+ "Jtime  varchar(10),IsKQ  varchar(3) DEFAULT 'δ�ύ',IsSaved varchar(3) default '��')");
		db.execSQL("CREATE TABLE  KQresult  (Kno integer primary key AUTOINCREMENT,Rno  int,Jno  int ,Sno  varchar(15) ,Kstate  varchar(3),"
				+ "Kmarks  varchar(20) DEFAULT '��',IsSubmin  varchar(3) DEFAULT 'δ�ύ',InMan  varchar(20),InTime  datetime)");
		db.execSQL("CREATE TABLE  KQresultLocal  (Kno integer primary key AUTOINCREMENT,Rno  int,Jno  int ,Sno  varchar(15) ,Kstate  varchar(3),"
				+ "Kmarks  varchar(20) DEFAULT '��',IsSubmin  varchar(3) DEFAULT 'δ�ύ',InMan  varchar(20),InTime  datetime)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

}
