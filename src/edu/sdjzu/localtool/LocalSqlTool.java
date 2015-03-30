package edu.sdjzu.localtool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.adapter.KqOrderAdapter;
import edu.sdjzu.attr.TeacherAttr;
import edu.sdjzu.attr.TeacherAttrSql;
import edu.sdjzu.model.KQresult;
import edu.sdjzu.model.Students;
import edu.sdjzu.model.TeachProgress;
import edu.sdjzu.model.TeachTask;
import edu.sdjzu.model.Teachers;
import edu.sdjzu.model.UserInf;

public class LocalSqlTool {
	private Context context;
	private DatabaseManager db;
	private static List<HashMap<String, String>> stuListHashMap = new ArrayList<HashMap<String, String>>();
	private String kstateNormal = "";
	private String kstateInNormal = "";

	public LocalSqlTool(Context context) {
		super();
		this.context = context;
		kstateInNormal = context.getString(R.string.sql_Kresult_Kstate_inabsence);
		kstateNormal = context.getString(R.string.sql_Kqresult_Kstate_absence);
	}

	public boolean localLogin(String username, String password) {
		db = DatabaseManager.getInstance(context);
		String sql = TeacherAttrSql.SQL_SELECT_FROM_TEACHERS_LOGIN;
		Cursor cursor = db.Query(sql, new String[] { username, password });
		if (cursor.getCount() > 0) {
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}

	/**
	 * 向本地插入辅导员信息
	 * 
	 * @param u
	 */
	public void insertUserInfByUno(UserInf u, String uno) {
		db = DatabaseManager.getInstance(context);
		String sql = new String(TeacherAttrSql.SQL_DELETE_FROM_USERINFO);
		db.execSQL(sql, new String[] { uno });
		sql = new String(TeacherAttrSql.SQL_INSERT_TO_USERINFO);
		String[] arg = { u.getUserNo(), u.getUserPassword(), u.getUserName(), u.getUserSex(), u.getUserSdept(),
				u.getUserClass(), u.getUserTel(), u.getUserType() };
		db.execSQL(sql, arg);
		// //db.closeDB();
	}

	/**
	 * 老师编号获取老师信息
	 * 
	 * @param t
	 */
	public void insertTeachersByTno(Teachers t, String tno) {
		db = DatabaseManager.getInstance(context);
		String sql = TeacherAttrSql.SQL_DELETE_FROM_TEACHERS;
		db.execSQL(sql, new String[] { tno });
		sql = TeacherAttrSql.SQL_INSERT_TO_TEACHERS;
		String[] arg = { t.getTeaNo(), t.getTeaPassword(), t.getTeaName(), t.getTeaSex(), t.getTeaSdept(),
				t.getTeaEmail(), t.getTeaTel() };
		db.execSQL(sql, arg);
	}

	/**
	 * 向本地插入教学进度表
	 */
	public void insertAllJDTBbyTname(List<TeachProgress> lit, String tno) {
		if (lit.size() == 0)
			return;
		db = DatabaseManager.getInstance(context);
		String sql = TeacherAttrSql.SQL_DELETE_FROM_TEACHPROGRESS_ALL;
		db.execSQL(sql);
		sql = TeacherAttrSql.SQL_INSERT_TO_TEACHPROGRESS_ALL;
		db.beginTransaction();
		try {

			for (int i = 0; i < lit.size(); i++) {
				TeachProgress t = lit.get(i);
				String[] arg = { String.valueOf(t.getProgressNo()), String.valueOf(t.getTaskNo()), t.getCourseName(),
						t.getTeaName(), t.getProgressClass(), t.getProgressWeek(), t.getProgressJTime(),
						t.getProgressAddress(), t.getStartTime(), t.getEndTime(), t.getIsKQ(), t.getInMan(),
						t.getInTime() };
				db.execSQL(sql, arg);

			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * 向本地插入教学任务
	 * 
	 * @param t
	 */
	public void insertTeachTaskByTaskNo(List<TeachTask> tList) {
		db = DatabaseManager.getInstance(context);
		String sql = TeacherAttrSql.SQL_DELETE_FROM_TEACH_TASK;
		db.execSQL(sql);
		db.beginTransaction();
		try {
			for (int i = 0; i < tList.size(); i++) {
				TeachTask t = tList.get(i);
				sql = TeacherAttrSql.SQL_INSERT_TO_TEACH_TASK;
				String[] arg = { String.valueOf(t.getTaskNo()), t.getCourseNo(), t.getCourseName(), t.getTeaName(),
						t.getTaskClass(), t.getCourseType(), t.getTaskWeek(), t.getTaskTerms() };
				db.execSQL(sql, arg);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
		} finally {

		}
	}

	/**
	 * 向本地插入教师上课的全部学生
	 * 
	 * @param stuList
	 */
	public void insertClassStuByRno(List<Students> stuList) {
		if (stuList.size() == 0)
			return;
		db = DatabaseManager.getInstance(context);
		String sql = TeacherAttrSql.SQL_DELETE_FROM_STUDENTS;
		db.execSQL(sql);
		sql = TeacherAttrSql.SQL_INSERT_TO_STUDENTS;
		db.beginTransaction();
		try {
			for (int i = 0; i < stuList.size(); i++) {
				Students stu = stuList.get(i);
				String[] arg = { stu.getStuId(), stu.getStuNo(), stu.getStuPassword(), stu.getParPassword(),
						stu.getStuName(), stu.getStuSex(), stu.getStuSdept(), stu.getStuClass(), stu.getStuState(),
						stu.getStuTel(), stu.getParTel(), stu.getStuPic() };
				db.execSQL(sql, arg);
			}
			db.setTransactionSuccessful();

		} catch (Exception e) {

		} finally {
			db.endTransaction();
		}
	}

	/**
	 * 更新表KQresult,更新本地存在的，插入本地不存在的，保留本地有而服务器上的数据没有的
	 * 
	 * @param klist
	 */
	public void updateKQresult(List<KQresult> klist, boolean isFromServer) {
		if (klist.size() == 0)
			return;
		db = DatabaseManager.getInstance(context);
		String sql = TeacherAttrSql.SQL_DELETE_FROM_KQRESULT_ALL;
		db.execSQL(sql);
		for (KQresult kq : klist) {
			insertStuKQ(kq, true);
		}
	}

	private int getStuDisabsenceCountsBySno(String sno) {
		int counts = 0;
		String sql = TeacherAttrSql.SQL_SELECT_FROM_KQRESULT_DISABLENCE_CONTS;
		db = DatabaseManager.getInstance(context);
		Cursor cursor = db.Query(sql,
				new String[] { context.getResources().getString(R.string.sql_Kqresult_Kstate_absence), sno });
		while (cursor.moveToNext()) {
			counts = cursor.getCount();
		}
		counts = cursor.getCount();
		cursor.close();
		// //db.closeDB();
		return counts;
	}

	/**
	 * 获得考勤列表形式的学生信息
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> getStuList(int jno) {
		List<HashMap<String, String>> listHash = new ArrayList<HashMap<String, String>>();
		// if (stuListHashMap.size() > 0) {
		// listHash = new ArrayList<HashMap<String, String>>(stuListHashMap);
		// return listHash;
		// }
		String sql = TeacherAttrSql.SQL_SELECT_FROM_STUDENTS_ALL;
		db = DatabaseManager.getInstance(context);
		Cursor cursor = db.Query(sql, null);
		while (cursor.moveToNext()) {
			String sno = cursor.getString(cursor.getColumnIndex("Sno"));
			int disabsenceCounts = getStuDisabsenceCountsBySno(sno);
			HashMap<String, String> hash = new HashMap<String, String>();
			hash.put(KqOrderAdapter.Constant.stuNameKey, cursor.getString(cursor.getColumnIndex("Sname")));
			hash.put(KqOrderAdapter.Constant.stuSnoKey, sno);
			hash.put(KqOrderAdapter.Constant.stuClassKey, cursor.getString(cursor.getColumnIndex("Sclass")));
			hash.put(KqOrderAdapter.Constant.stuDisabsenceCountsKey, String.valueOf(disabsenceCounts));
			hash.put(KqOrderAdapter.Constant.stuClassOderStateKey, context.getString(R.string.tea_kq_state_normal));
			listHash.add(hash);
		}
		cursor.close();
		db = DatabaseManager.getInstance(context);
		sql = TeacherAttrSql.SQL_SELECT_FROM_KQRESULT_EXISTS;
		for (HashMap<String, String> hashMap : listHash) {
			cursor = db
					.Query(sql, new String[] { String.valueOf(jno), hashMap.get(KqOrderAdapter.Constant.stuSnoKey) });
			if (cursor.moveToNext()) {
				hashMap.put(KqOrderAdapter.Constant.stuClassOderStateKey,
						context.getString(R.string.tea_kq_state_innormal));
				hashMap.put(KqOrderAdapter.Constant.stuKqInTimeKey, cursor.getString(cursor.getColumnIndex("InTime")));

			} else {
				hashMap.put(KqOrderAdapter.Constant.stuKqInTimeKey, "00/00/00 00:00:00");
			}
			cursor.close();
		}
		stuListHashMap = new ArrayList<HashMap<String, String>>(listHash);
		return listHash;
	}

	// 将文件数据保存到SDCard
	public void saveToSDCard(String content, String fileName) throws Exception {
		File file = new File(Environment.getExternalStorageDirectory(), fileName);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(content.getBytes());
		fileOutputStream.close();
	}

	// 从SDCard读取文件数据
	@SuppressWarnings("resource")
	private String readFromSDCard(String fileName) throws Exception {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		File SDFile = Environment.getExternalStorageDirectory();// 获取外存储设备的文件目录
		File file = new File(SDFile.getAbsolutePath() + File.separator + fileName);// 构建读取文件所需目录的文件对象
		FileInputStream inputStream = new FileInputStream(file);
		int len = 0;
		byte[] buffer = new byte[1024];
		while ((len = inputStream.read(buffer)) != -1) {
			byteArrayOutputStream.write(buffer, 0, len);// 写入内存中
		}
		byte[] data = byteArrayOutputStream.toByteArray();

		return new String(data);
	}

	@SuppressWarnings("resource")
	private List<String> parseJson(String Js, String sno) throws IOException {
		List<String> sjs = new ArrayList<String>();
		// String Js="[{\"name\":\"chen\",\"age\":21}]";
		JsonReader reader = new JsonReader(new StringReader(Js));
		reader.beginArray();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals(sno)) {
				sjs.add(reader.nextString());
				break;
			}
		}
		reader.endObject();
		reader.endArray();

		return sjs;
	}

	/**
	 * 根据学生学号获得学生图片
	 * 
	 * @param sno
	 * @return
	 */
	public Bitmap getPhotoBySno(String sno) {
		String fileName = sno + ".txt";
		Bitmap bitmap = null;
		String js = "";
		try {
			js = readFromSDCard(fileName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (js.equals(""))
			return null;
		List<String> slist = new ArrayList<String>();
		try {
			slist = parseJson(js, sno);
		} catch (IOException e) {
		}
		if (slist.size() == 0)
			return null;
		byte[] bit = Base64.decode(slist.get(0), Base64.DEFAULT);
		bitmap = BitmapFactory.decodeByteArray(bit, 0, bit.length);
		return bitmap;
	}

	/**
	 * 获得老师上课的所有班级,在添加“全部”选项
	 * 
	 * @return
	 */
	public String[] getStuClass() {
		ArrayList<String> classList = new ArrayList<String>();
		String sql = TeacherAttrSql.SQL_SELECT_FROM_TEACHTASK;
		db = DatabaseManager.getInstance(context);
		Cursor cursor = db.Query(sql, null);
		if (cursor.getCount() > 0) {
			classList.add(context.getString(R.string.tea_class_all));
		}
		while (cursor.moveToNext()) {
			String cla = cursor.getString(cursor.getColumnIndex("Rclass"));
			String[] clas = cla.split("、");
			for (int i = 0; i < clas.length; i++) {
				classList.add(clas[i]);
			}
		}
		cursor.close();
		// //db.closeDB();
		return classList.toArray(new String[classList.size()]);

	}

	/**
	 * 课堂考勤根据学生班级获得学生信息
	 * 
	 * @param cla
	 * @param isNormalKq
	 *            是否是正常考勤
	 * @return
	 */
	public List<HashMap<String, String>> getStuListByClass(String cla, int jno, boolean isNormalKq) {
		List<HashMap<String, String>> stuHash = new ArrayList<HashMap<String, String>>();
		getStuList(jno);
		if (cla.equals(context.getString(R.string.tea_class_all)) && isNormalKq) {
			return new ArrayList<HashMap<String, String>>(stuListHashMap);
		} else if (cla.equals(context.getString(R.string.tea_class_all)) && !isNormalKq) {
			stuHash = new ArrayList<HashMap<String, String>>(stuListHashMap);
		} else if (!cla.equals(context.getString(R.string.tea_class_all))) {
			for (HashMap<String, String> hashMap : stuListHashMap) {
				if (hashMap.get(KqOrderAdapter.Constant.stuClassKey).equalsIgnoreCase(cla)) {
					HashMap<String, String> hm = new HashMap<String, String>(hashMap);
					stuHash.add(hm);
				}
			}
		}
		if (false == isNormalKq) {
			List<HashMap<String, String>> stuHashLocal = new ArrayList<HashMap<String, String>>();
			String sql = "";
			Cursor cursor = null;
			db = DatabaseManager.getInstance(context);
			if (cla.equals(context.getString(R.string.tea_class_all))) {
				sql = TeacherAttrSql.SQL_SELECT_FROM_KQRESULTLOCAL_ALL;
				cursor = db.Query(sql, new String[] { String.valueOf(jno) });
			} else {
				sql = TeacherAttrSql.SQL_SELECT_FROM_KQRESULTLOCAL_BY_CLASS;
				cursor = db.Query(sql, new String[] { String.valueOf(jno), cla });
			}
			if (null != cursor) {
				while (cursor.moveToNext()) {
					HashMap<String, String> localHashMap = new HashMap<String, String>();
					localHashMap.put(KqOrderAdapter.Constant.stuSnoKey, cursor.getString(cursor.getColumnIndex("Sno")));
					localHashMap.put(KqOrderAdapter.Constant.stuKqInTimeKey,
							cursor.getString(cursor.getColumnIndex("InTime")));
					localHashMap.put(KqOrderAdapter.Constant.stuClassOderStateKey,
							cursor.getString(cursor.getColumnIndex("Kstate")));
					stuHashLocal.add(localHashMap);
				}
				cursor.close();
				for (int i = 0; i < stuHashLocal.size(); i++) {// //////111111
					HashMap<String, String> hashL = stuHashLocal.get(i);
					for (int j = 0; j < stuHash.size(); j++) {// ///222222
						HashMap<String, String> hashS = stuHash.get(j);
						String inTimeS = hashS.get(KqOrderAdapter.Constant.stuKqInTimeKey);
						String inTimeL = hashL.get(KqOrderAdapter.Constant.stuKqInTimeKey);
						String kstateS = hashS.get(KqOrderAdapter.Constant.stuClassOderStateKey);
						String kstateL = hashL.get(KqOrderAdapter.Constant.stuClassOderStateKey);
						if (null != inTimeS
								&& hashL.get(KqOrderAdapter.Constant.stuSnoKey).equals(
										hashS.get(KqOrderAdapter.Constant.stuSnoKey))) {// //////33333
							Log.i("chen", "find");
							int compare = inTimeS.compareToIgnoreCase(inTimeL);
							if (compare > 0) {// 服务器的时间最新
								sql = "update KQresultLocal set InTime=?,Kstate=? where Sno=? and Jno=?";
								db.execSQL(sql,
										new String[] { inTimeS, kstateS, hashS.get(KqOrderAdapter.Constant.stuSnoKey),
												String.valueOf(jno) });
							} else if (compare < 0) {// 本地缓存的时间最新
								stuHash.get(j).put(KqOrderAdapter.Constant.stuClassOderStateKey, kstateL);
								int disAbsenceCounts = Integer.valueOf(hashS
										.get(KqOrderAdapter.Constant.stuDisabsenceCountsKey));

								if (kstateS.equals(kstateNormal) && kstateL.equals(kstateInNormal)) {
									++disAbsenceCounts;
								} else if (kstateS.equals(kstateInNormal) && kstateL.equals(kstateNormal)) {
									--disAbsenceCounts;
								}
								stuHash.get(j).put(KqOrderAdapter.Constant.stuDisabsenceCountsKey,
										String.valueOf(disAbsenceCounts));
							}
						}// //////33333
					}// ////222222
				}// ////////////////////111111
			}
		}
		return stuHash;
	}

	/**
	 * 第一个元素是进度值，第二个是任务值，不是当前时间都为-1
	 * 
	 * @return
	 */
	public int[] getCurrentProgress() {
		int[] jdbt = { -1, -1 };
		String time = getTime();
		String currentTime = forMatTime(time);
		String sql = TeacherAttrSql.SQL_SELECT_FROM_TEACHPROGRESS;
		db = DatabaseManager.getInstance(context);
		Cursor cursor = db.Query(sql, new String[] { currentTime });
		while (cursor.moveToNext()) {
			jdbt[0] = Integer.valueOf(cursor.getString(cursor.getColumnIndex("Jno")));
			jdbt[1] = Integer.valueOf(cursor.getString(cursor.getColumnIndex("Rno")));
		}
		cursor.close();
		return jdbt;
	}

	public String forMatTime(String time) {
		String[] s1 = time.split("/");
		String[] s2 = s1[2].split(" ");
		String[] s3 = s2[1].split(":");
		String result = "";
		result = String.valueOf(Integer.valueOf(s1[0])) + "/" + String.valueOf(Integer.valueOf(s1[1])) + "/"
				+ String.valueOf(Integer.valueOf(s2[0])) + " ";
		result = result + String.valueOf(Integer.valueOf(s3[0])) + ":" + s3[1] + ":" + s3[2];
		return result;
	}

	private String getTime() {
		Calendar ca = Calendar.getInstance();
		Date nowTime = ca.getTime();
		SimpleDateFormat datetime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String datetimeString = datetime.format(nowTime);
		return datetimeString;
	}

	/**
	 * 
	 课堂考勤更新本地考勤信息
	 * 
	 * @param k
	 * @param isFromServer
	 */
	public void insertStuKQ(KQresult k, boolean isFromServer) {
		db = DatabaseManager.getInstance(context);
		String sql = "";
		String subState = "";
		if (isFromServer) {
			sql = TeacherAttrSql.SQL_INSERT_TO_KQRESULT;
			subState = context.getResources().getString(R.string.KQresult_submit);
		} else {
			sql = TeacherAttrSql.SQL_DELETE_FROM_KQRESULTLOCAL;
			db.execSQL(sql, new String[] { String.valueOf(k.getProgressNo()), k.getStuNo() });
			sql = TeacherAttrSql.SQL_INSERT_TO_KQRESULTLocal;
			subState = k.getIsSubmit();
		}
		String[] arg = { String.valueOf(k.getTaskNo()), String.valueOf(k.getProgressNo()), k.getStuNo(),
				k.getKqState(), k.getKqMark(), subState, k.getInMan(), k.getInTime() };
		db.execSQL(sql, arg);
	}

	/**
	 * 根据老师编号获得老师的姓名
	 * 
	 * @param tno
	 * @return
	 */
	public String getTeaNameByTno(String tno) {
		String sql = TeacherAttrSql.SQL_SELECT_FROM_TEACHERS_FOR_NAME;
		db = DatabaseManager.getInstance(context);
		Cursor cursor = db.Query(sql, new String[] { tno });
		String teaName = "";
		if (cursor.moveToNext()) {
			teaName = cursor.getString(cursor.getColumnIndex("Tname"));
		}
		cursor.close();
		// //db.closeDB();
		return teaName;
	}

	/**
	 * 获得所有课程名称
	 * 
	 * @return
	 */
	public List<String> getTeaAllCourse() {
		List<String> courseList = new ArrayList<String>();
		String sql = TeacherAttrSql.SQL_SELECT_FROM_TEACHPROGRESS_ALL_COURSE;
		db = DatabaseManager.getInstance(context);
		Cursor cursor = db.Query(sql, null);
		while (cursor.moveToNext()) {
			String cn = cursor.getString(cursor.getColumnIndex("Cname"));
			courseList.add(cn);
		}
		cursor.close();
		// ////db.closeDB();
		return courseList;
	}

	/**
	 * 获得上课的所有班级
	 * 
	 * @return
	 */
	public List<String> getTeaAllClass() {
		String[] cla = getStuClass();
		List<String> classList = new ArrayList<String>();
		if (cla.length > 0) {
			classList = Arrays.asList(cla);
		}
		return classList;
	}

	/**
	 * 获得上课的所有周次
	 * 
	 * @return
	 */
	public List<String> getTeaAllWeek() {
		List<String> weekList = new ArrayList<String>();
		String sql = TeacherAttrSql.SQL_SELECT_FROM_TEACHPROGRESS_ALL_WEEK;
		db = DatabaseManager.getInstance(context);
		Cursor cursor = db.Query(sql, null);
		while (cursor.moveToNext()) {
			String cn = cursor.getString(cursor.getColumnIndex("Jweek"));
			weekList.add(cn);
		}
		cursor.close();
		// //db.closeDB();
		return weekList;
	}

	/**
	 * 获得老师上课的所有节次
	 * 
	 * @return
	 */
	public List<String> getTeaAllClassTime() {
		List<String> classTimeList = new ArrayList<String>();
		String sql = TeacherAttrSql.SQL_SELECT_FROM_TEACHPROGRESS_ALL_CLASSTIME;
		db = DatabaseManager.getInstance(context);
		Cursor cursor = db.Query(sql, null);
		while (cursor.moveToNext()) {
			String cn = cursor.getString(cursor.getColumnIndex("Jtime"));
			classTimeList.add(cn);
		}
		cursor.close();
		// //db.closeDB();
		return classTimeList;
	}

	/**
	 * 查看考勤信息获得相关数据
	 * 
	 * @param course
	 * @param cla
	 * @param week
	 * @param claTime
	 * @return
	 */

	public List<HashMap<String, String>> getLookKq(String course, String cla, String week, String claTime) {
		List<HashMap<String, String>> ltHashMap = new ArrayList<HashMap<String, String>>();
		String sql = TeacherAttrSql.SQL_SELECT_LOOK_KQ_PER_CLASS;
		Cursor cursor = null;
		db = DatabaseManager.getInstance(context);
		if (cla.equals(context.getString(R.string.tea_class_all))) {
			sql = TeacherAttrSql.SQL_SELECT_LOOK_KQ_ALL_CLASS;
			cursor = db.Query(sql, new String[] { course, week, claTime });
		} else {
			cursor = db.Query(sql, new String[] { course, week, claTime, cla });
		}
		while (cursor != null && cursor.moveToNext()) {
			HashMap<String, String> h = new HashMap<String, String>();
			h.put(KqOrderAdapter.Constant.stuNameKey, cursor.getString(cursor.getColumnIndex("sname")));
			h.put(KqOrderAdapter.Constant.stuSnoKey, cursor.getString(cursor.getColumnIndex("sno")));
			h.put(KqOrderAdapter.Constant.stuClassKey, cursor.getString(cursor.getColumnIndex("sclass")));
			h.put(KqOrderAdapter.Constant.stuClassOderStateKey, cursor.getString(cursor.getColumnIndex("state")));
			ltHashMap.add(h);
		}
		cursor.close();
		// //db.closeDB();
		return ltHashMap;
	}

	/**
	 * 获得补录时的进度号和任务号
	 * 
	 * @param course
	 * @param week
	 * @param claTime
	 * @return
	 */
	public HashMap<String, String> getBuluJnoRno(String course, String week, String claTime) {
		HashMap<String, String> hash = new HashMap<String, String>();
		String sql = TeacherAttrSql.SQL_SELECT_BULU_RNO_JNO;
		db = DatabaseManager.getInstance(context);
		Cursor cursor = db.Query(sql, new String[] { course, week, claTime });
		if (cursor.moveToNext()) {
			hash.put(TeacherAttr.jnoKey, cursor.getString(cursor.getColumnIndex("Jno")));
			hash.put(TeacherAttr.rnoKey, cursor.getString(cursor.getColumnIndex("Rno")));
		} else {
			hash.put(TeacherAttr.jnoKey, "-1");
			hash.put(TeacherAttr.rnoKey, "-1");
		}
		return hash;
	}

	/**
	 * 从当前进度号下本地缓存中获得所有未提交的考勤信息
	 * 
	 * @return
	 */
	public List<KQresult> getAllKQByJno(int jno) {
		List<KQresult> klist = new ArrayList<KQresult>();
		db = DatabaseManager.getInstance(context);
		String sql = TeacherAttrSql.SQL_SELECT_FROM_KQRESULTLOCAL_NOT_SUBMIT;
		Cursor cursor = db.Query(sql,
				new String[] { context.getString(R.string.KQresult_notsubmit), String.valueOf(jno) });
		while (cursor.moveToNext()) {
			KQresult kq = new KQresult();
			kq.setKqNo(Integer.valueOf(cursor.getString(cursor.getColumnIndex("Kno"))));
			kq.setTaskNo(Integer.valueOf(cursor.getString(cursor.getColumnIndex("Rno"))));
			kq.setProgressNo(Integer.valueOf(cursor.getString(cursor.getColumnIndex("Jno"))));
			kq.setStuNo(cursor.getString(cursor.getColumnIndex("Sno")));
			kq.setKqState(cursor.getString(cursor.getColumnIndex("Kstate")));
			kq.setKqMark(cursor.getString(cursor.getColumnIndex("Kmarks")));
			kq.setIsSubmit(cursor.getString(cursor.getColumnIndex("IsSubmin")));
			kq.setInMan(cursor.getString(cursor.getColumnIndex("InMan")));
			kq.setInTime(cursor.getString(cursor.getColumnIndex("InTime")));
			kq.setsName(cursor.getString(cursor.getColumnIndex("Sname")));
			kq.setsClass(cursor.getString(cursor.getColumnIndex("Sclass")));
			klist.add(kq);
			Log.i("chen", kq.getsName() + "    " + kq.getStuNo() + "    " + kq.getIsSubmit());
		}
		cursor.close();
		return klist;
	}

	/**
	 * 根据进度号更新本地的考勤状态为已提交
	 * 
	 * @param jno
	 */
	public void upLocalKQByJno(int jno) {
		String sql = TeacherAttrSql.SQL_DELETE_KQRESULTLOCAL_BY_JNO;
		db = DatabaseManager.getInstance(context);
		db.execSQL(sql, new String[] { context.getString(R.string.sql_Kqresult_Kstate_submit), String.valueOf(jno) });
	}

	/**
	 * 根据进度号删除本地的之前没有提交进度表
	 * 
	 * @param jno
	 */
	public void upLocalJno(int jno) {
		String sql = TeacherAttrSql.SQL_Delete_TEACHLocalPROGRESS_JNO;
		db = DatabaseManager.getInstance(context);
		db.execSQL(sql, new String[] { String.valueOf(jno) });
	}

	/**
	 * 在无网络的情况下保存本地的进度状态为保存状态
	 * 
	 * @param jno
	 */
	public void upTeachProgressSave(int jno) {
		db = DatabaseManager.getInstance(context);
		String sql = "";
		String sJno = String.valueOf(jno);
		String sRno = "";
		String sCname = "";
		String sJweek = "";
		String sJtime = "";
		String sIsKq = context.getString(R.string.sql_teachprogress_no);
		String sIsSaved = "是";

		sql = TeacherAttrSql.SQL_SELECT_FROM_TEACHERPROGRESS_BY_JNO;
		Cursor cursor = db.Query(sql, new String[] { sJno });

		while (cursor.moveToNext()) {
			sRno = cursor.getString(cursor.getColumnIndex("Rno"));
			sCname = cursor.getString(cursor.getColumnIndex("Cname"));
			sJweek = cursor.getString(cursor.getColumnIndex("Jweek"));
			sJtime = cursor.getString(cursor.getColumnIndex("Jtime"));
		}
		sql = TeacherAttrSql.SQL_SELECT_FROM_TEACHLOCALPROGRESS_BY_JNO;
		cursor = null;
		cursor = db.Query(sql, new String[] { sJno });
		if (cursor.getCount() <= 0) {
			sql = TeacherAttrSql.SQL_INSERT_TO_TEACHLOCALPROGRESS;
			db.execSQL(sql, new String[] { sJno, sRno, sCname, sJweek, sJtime, sIsKq, sIsSaved });
		} else {
			sql = TeacherAttrSql.SQL_UPDATE_TEACHLOCALPROGRESS;
			db.execSQL(sql, new String[] { sIsKq, sIsSaved, sJno });
		}
		cursor.close();
	}

	/**
	 * 判断是否存在已保存但未提交的考勤进度
	 * 
	 * @return
	 */
	public List<TeachProgress> existSavedTProgress() {
		String sql = TeacherAttrSql.SQL_EXIST_SAVED_TEACHLocalPROGRESS;
		List<TeachProgress> list = new ArrayList<TeachProgress>();
		db = DatabaseManager.getInstance(context);
		Cursor cursor = db.Query(
				sql,
				new String[] { context.getString(R.string.sql_Kqresult_Kstate_saved),
						context.getString(R.string.sql_teachprogress_no) });
		while (cursor.moveToNext()) {
			TeachProgress tp = new TeachProgress();
			tp.setCourseName(cursor.getString(cursor.getColumnIndex("Cname")));
			tp.setTaskNo(Integer.valueOf(cursor.getString(cursor.getColumnIndex("Rno"))));
			tp.setProgressNo(Integer.valueOf(cursor.getString(cursor.getColumnIndex("Jno"))));
			tp.setProgressJTime(cursor.getString(cursor.getColumnIndex("Jtime")));
			tp.setProgressWeek(cursor.getString(cursor.getColumnIndex("Jweek")));
			list.add(tp);
		}
		cursor.close();
		return list;
	}

	/**
	 * 根据进度号获得相应的未提交的考勤信息
	 * 
	 * @param jno
	 * @return
	 */
	public List<Students> getKQStuByJno(int jno) {
		String sql = TeacherAttrSql.SQL_KQ_NOT_SUBMIT_STU;
		db = DatabaseManager.getInstance(context);
		Cursor cursor = db.Query(sql,
				new String[] { String.valueOf(jno), context.getString(R.string.sql_Kqresult_Kstate_absence) });
		List<Students> listStu = new ArrayList<Students>();
		while (cursor.moveToNext()) {
			Students stu = new Students();
			stu.setStuName(cursor.getString(cursor.getColumnIndex("sname")));
			stu.setStuNo(cursor.getString(cursor.getColumnIndex("sno")));
			stu.setStuClass(cursor.getString(cursor.getColumnIndex("sclass")));
			listStu.add(stu);
		}
		cursor.close();
		return listStu;
	}

	/**
	 * 删除考勤信息
	 * 
	 * @param jno
	 * @param stuSnoList
	 */
	public void deleteKQByStuJno(int jno, List<String> stuSnoList) {
		String sql = "delete  from KQresultLocal where Jno=? and ( Sno=";
		if (stuSnoList.size() > 0) {
			for (int i = 0; i < stuSnoList.size() - 1; i++) {
				String sno = stuSnoList.get(i);
				sql += sno + " or Sno=";
			}
			sql += stuSnoList.get(stuSnoList.size() - 1) + ")";
			db = DatabaseManager.getInstance(context);
			db.execSQL(sql, new String[] { String.valueOf(jno) });
		}
	}

	/**
	 * 删除本地缓存的未提交的进度
	 * 
	 * @param listJno
	 */
	public void deleteLocalProgress(List<Integer> listJno) {
		String sql = "delete from TeachLocalProgress where";
		int jnoCounts = listJno.size();
		if (jnoCounts > 0) {
			for (int i = 0; i < jnoCounts - 1; i++) {
				sql += " Jno=" + listJno.get(i) + " or";
			}
			sql += " Jno=" + listJno.get(jnoCounts - 1);
			db = DatabaseManager.getInstance(context);
			db.execSQL(sql);
		}
	}

	/**
	 * 根据进度号获得本地缓存的考勤信息
	 * 
	 * @param listJno
	 * @return
	 */
	public List<KQresult> getKQresultLocalByJno(List<Integer> listJno) {
		List<KQresult> listKQ = new ArrayList<KQresult>();
		String sql = "select K.Kno as Kno,K.Rno as Rno,K.Jno as Jno,K.Sno as Sno,K.Kstate as Kstate,"
				+ "K.IsSubmin as IsSubmin,K.Kmarks as Kmarks,"
				+ "K.InMan as InMan,K.InTime as InTime, S.Sname as Sname,S.Sclass as Sclass"
				+ " from KQresultLocal K,Students S where K.IsSubmin=? and K.Sno=S.Sno and (";// 查询未提交的考勤信息
		int jnoCounts = listJno.size();
		Log.i("chen", "jnoSize=" + jnoCounts);
		if (jnoCounts > 0) {
			for (int i = 0; i < jnoCounts - 1; i++) {
				sql += " K.Jno=" + listJno.get(i) + " or";
			}
			sql += " K.Jno=" + listJno.get(jnoCounts - 1) + " )";
			db = DatabaseManager.getInstance(context);
			Cursor cursor = db.Query(sql, new String[] { context.getString(R.string.KQresult_notsubmit) });
			while (cursor.moveToNext()) {
				KQresult kq = new KQresult();
				kq.setKqNo(Integer.valueOf(cursor.getString(cursor.getColumnIndex("Kno"))));
				kq.setTaskNo(Integer.valueOf(cursor.getString(cursor.getColumnIndex("Rno"))));
				kq.setProgressNo(Integer.valueOf(cursor.getString(cursor.getColumnIndex("Jno"))));
				kq.setStuNo(cursor.getString(cursor.getColumnIndex("Sno")));
				kq.setKqState(cursor.getString(cursor.getColumnIndex("Kstate")));
				kq.setKqMark(cursor.getString(cursor.getColumnIndex("Kmarks")));
				kq.setIsSubmit(cursor.getString(cursor.getColumnIndex("IsSubmin")));
				kq.setInMan(cursor.getString(cursor.getColumnIndex("InMan")));
				kq.setInTime(cursor.getString(cursor.getColumnIndex("InTime")));
				kq.setsName(cursor.getString(cursor.getColumnIndex("Sname")));
				kq.setsClass(cursor.getString(cursor.getColumnIndex("Sclass")));
				Log.i("chen", "kq.isSubmint=" + kq.getIsSubmit());
				listKQ.add(kq);
			}
			cursor.close();
		}
		return listKQ;

	}
	/**
	 * 其它用户登录时清除之前用户的所有信息
	 */
	public void clearCache(){
		db = DatabaseManager.getInstance(context);
		String sql="delete from UserInf";
		db.execSQL(sql);
		sql="delete from Teachers";
		db.execSQL(sql);
		sql="delete from TeachProgress";
		db.execSQL(sql);
		sql="delete from TeachTask";
		db.execSQL(sql);
		sql="delete from TeachLocalProgress";
		db.execSQL(sql);
		sql="delete from KQresult";
		db.execSQL(sql);
		sql="delete from KQresultLocal";
		db.execSQL(sql);
		sql="delete from Students";
		db.execSQL(sql);
	}
	

}
