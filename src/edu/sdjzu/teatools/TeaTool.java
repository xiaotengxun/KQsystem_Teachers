package edu.sdjzu.teatools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import edu.sdjzu.attr.TeacherAttr;
import edu.sdjzu.localtool.DatabaseManager;
import edu.sdjzu.localtool.InternetStatus;
import edu.sdjzu.localtool.LocalSqlTool;
import edu.sdjzu.model.KQLookInfo;
import edu.sdjzu.model.KQresult;
import edu.sdjzu.model.Students;
import edu.sdjzu.model.TeachProgress;

public class TeaTool {
	private Context context;

	/**
	 * 根据Spinner的选择项,用户名密码和来启动指定的Activity，并保存登陆信息
	 * 
	 * @param context
	 *            当前Acitivity的上下文
	 * @param username
	 *            用户名
	 * @param password
	 *            用户密码
	 */
	public boolean LoginStartActivity(String username, String password, SharedPreferences sp) {
		boolean isSuccess = false;
		InternetStatus is = new InternetStatus(context);
		boolean isF = sp.getBoolean(TeacherAttr.isFirstLogin, true);
		String lastUserName = sp.getString(TeacherAttr.loginUserName, null);
		boolean isCacheClear = false;
		TeaLoginTool teaWebTool = new TeaLoginTool(context);
		if (null != lastUserName && !lastUserName.endsWith(username)) {
			isCacheClear = true;
			teaWebTool.clearCache();
			isF = false;
		}
		if (is.isNetworkConnected()) {
			if (WebLoginSuccess(username, password)) {
				if (isF) {// 第一次获得所有数据操作
					try {
						teaWebTool.firstLogin(username);
						isSuccess = true;
						sp.edit().putBoolean(TeacherAttr.isFirstLogin, false).commit();
					} catch (Exception e) {
						sp.edit().putBoolean(TeacherAttr.isFirstLogin, true);
					} catch (Error ex) {
						sp.edit().putBoolean(TeacherAttr.isFirstLogin, true);
					}
				} else {// 非第一次登陆，更新数据操作
					try {
						teaWebTool.secondLogin(username);
						isSuccess = true;
					} catch (Error ex) {
					}
				}
			}
		} else {
			return LocalLoginSuccess(username, password);
		}
		return isSuccess;
	}

	public TeaTool(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 根据用户类型进行登录，成功则返回true
	 * 
	 * @param userType
	 * @param username
	 * @param password
	 * @return
	 */
	private boolean WebLoginSuccess(String username, String password) {
		WebTool web = null;
		try {
			web = new WebTool(context);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (web != null) {
			return web.LoginSuccess(TeacherAttr.userType, username, password);
		} else
			return false;
	}

	/**
	 * 无网络连接状态下从本地登陆
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	private boolean LocalLoginSuccess(String username, String password) {
		return new LocalSqlTool(context).localLogin(username, password);
	}

	/**
	 * 获得考勤列表形式的学生信息
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> getStuList(int jno) {
		return new LocalSqlTool(context).getStuList(jno);
	}

	/**
	 * 获得老师上课的班级
	 * 
	 * @return
	 */
	public String[] getStuClass() {
		return new LocalSqlTool(context).getStuClass();
	}

	/**
	 * 课堂考勤根据学生班级获得学生信息
	 * 
	 * @param cla
	 * @param isNormalKq
	 *            是否是正常考勤
	 * @return
	 */
	public List<HashMap<String, String>> getStuListByClass(String cla, int jno,int filterType) {
		return new LocalSqlTool(context).getStuListByClass(cla, jno,filterType);
	}

	/**
	 * 第一个元素是进度值，第二个是任务值，不是当前时间都为-1
	 * 
	 * @return
	 */
	public int[] getCurrentProgress() {
		return new LocalSqlTool(context).getCurrentProgress();
	}

	/**
	 * 考勤更新本地的考勤信息
	 * 
	 * @param kq
	 */
	public void insertStuKq(KQresult kq) {
		new LocalSqlTool(context).insertStuKQ(kq, false);
	}

	/**
	 * 根据老师编号获得老师的姓名
	 * 
	 * @param tno
	 * @return
	 */
	public String getTeaNameByTno(String tno) {
		return new LocalSqlTool(context).getTeaNameByTno(tno);
	}

	/**
	 * 根据学生学号获得学生图片
	 * 
	 * @param sno
	 * @return
	 */
	public Bitmap getPhotoBySno(String sno) {
		return new LocalSqlTool(context).getPhotoBySno(sno);
	}

	/**
	 * 获得所有课程名称
	 * 
	 * @return
	 */
	public List<String> getTeaAllCourse() {
		return new LocalSqlTool(context).getTeaAllCourse();
	}

	/**
	 * 获得上课的所有班级
	 * 
	 * @return
	 */
	public List<String> getTeaAllClass() {
		return new LocalSqlTool(context).getTeaAllClass();
	}

	/**
	 * 获得上课的所有周次
	 * 
	 * @return
	 */
	public List<String> getTeaAllWeek() {
		return new LocalSqlTool(context).getTeaAllWeek();
	}

	/**
	 * 获得老师上课的所有节次
	 * 
	 * @return
	 */
	public List<String> getTeaAllClassTime() {
		return new LocalSqlTool(context).getTeaAllClassTime();
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

	public List<KQLookInfo> getLookKq(String course, String cla, String week, String claTime) {
		return new LocalSqlTool(context).getLookKq(course, cla, week, claTime);
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
		return new LocalSqlTool(context).getBuluJnoRno(course, week, claTime);
	}

	/**
	 * 判断是否存在已保存但未提交的考勤进度
	 * 
	 * @return
	 */
	public List<TeachProgress> existSavedTProgress() {
		return new LocalSqlTool(context).existSavedTProgress();
	}

	/**
	 * 根据进度号获得相应的未提交的考勤信息
	 * 
	 * @param jno
	 * @return
	 */
	public List<Students> getKQStuByJno(int jno) {
		return new LocalSqlTool(context).getKQStuByJno(jno);
	}

	/**
	 * 删除考勤信息
	 * 
	 * @param jno
	 * @param stuSnoList
	 */
	public void deleteKQByStuJno(int jno, List<String> stuSnoList) {
		new LocalSqlTool(context).deleteKQByStuJno(jno, stuSnoList);
	}

	/**
	 * 删除本地缓存的未提交的进度
	 * 
	 * @param listJno
	 */
	public void deleteLocalProgress(List<Integer> listJno) {
		new LocalSqlTool(context).deleteLocalProgress(listJno);
	}

	/**
	 * 根据进度号提交本地由于某些原因未能提交的考勤
	 * 
	 * @param listJno
	 */
	public void TJKQresultLocal(List<Integer> listJno) {
		WebTool web = null;
		try {
			web = new WebTool(context);
			web.TJKQresultLocal(listJno);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据进度号判断当前进度是否已经提交
	 * 
	 * @param jno
	 * @return
	 */
	public boolean isJnoSubmit(int jno) {
		return new LocalSqlTool(context).isJnoSubmit(jno);
	}

	/*
	 * 根据老师编号获得其上课学生的图片文件
	 */
	public void getStuPicZipByTno(String tno) {
		try {
			new WebTool(context).getStuPicZipByTno(tno);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	
}
