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
	 * ����Spinner��ѡ����,�û��������������ָ����Activity���������½��Ϣ
	 * 
	 * @param context
	 *            ��ǰAcitivity��������
	 * @param username
	 *            �û���
	 * @param password
	 *            �û�����
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
				if (isF) {// ��һ�λ���������ݲ���
					try {
						teaWebTool.firstLogin(username);
						isSuccess = true;
						sp.edit().putBoolean(TeacherAttr.isFirstLogin, false).commit();
					} catch (Exception e) {
						sp.edit().putBoolean(TeacherAttr.isFirstLogin, true);
					} catch (Error ex) {
						sp.edit().putBoolean(TeacherAttr.isFirstLogin, true);
					}
				} else {// �ǵ�һ�ε�½���������ݲ���
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
	 * �����û����ͽ��е�¼���ɹ��򷵻�true
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
	 * ����������״̬�´ӱ��ص�½
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	private boolean LocalLoginSuccess(String username, String password) {
		return new LocalSqlTool(context).localLogin(username, password);
	}

	/**
	 * ��ÿ����б���ʽ��ѧ����Ϣ
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> getStuList(int jno) {
		return new LocalSqlTool(context).getStuList(jno);
	}

	/**
	 * �����ʦ�Ͽεİ༶
	 * 
	 * @return
	 */
	public String[] getStuClass() {
		return new LocalSqlTool(context).getStuClass();
	}

	/**
	 * ���ÿ��ڸ���ѧ���༶���ѧ����Ϣ
	 * 
	 * @param cla
	 * @param isNormalKq
	 *            �Ƿ�����������
	 * @return
	 */
	public List<HashMap<String, String>> getStuListByClass(String cla, int jno,int filterType) {
		return new LocalSqlTool(context).getStuListByClass(cla, jno,filterType);
	}

	/**
	 * ��һ��Ԫ���ǽ���ֵ���ڶ���������ֵ�����ǵ�ǰʱ�䶼Ϊ-1
	 * 
	 * @return
	 */
	public int[] getCurrentProgress() {
		return new LocalSqlTool(context).getCurrentProgress();
	}

	/**
	 * ���ڸ��±��صĿ�����Ϣ
	 * 
	 * @param kq
	 */
	public void insertStuKq(KQresult kq) {
		new LocalSqlTool(context).insertStuKQ(kq, false);
	}

	/**
	 * ������ʦ��Ż����ʦ������
	 * 
	 * @param tno
	 * @return
	 */
	public String getTeaNameByTno(String tno) {
		return new LocalSqlTool(context).getTeaNameByTno(tno);
	}

	/**
	 * ����ѧ��ѧ�Ż��ѧ��ͼƬ
	 * 
	 * @param sno
	 * @return
	 */
	public Bitmap getPhotoBySno(String sno) {
		return new LocalSqlTool(context).getPhotoBySno(sno);
	}

	/**
	 * ������пγ�����
	 * 
	 * @return
	 */
	public List<String> getTeaAllCourse() {
		return new LocalSqlTool(context).getTeaAllCourse();
	}

	/**
	 * ����Ͽε����а༶
	 * 
	 * @return
	 */
	public List<String> getTeaAllClass() {
		return new LocalSqlTool(context).getTeaAllClass();
	}

	/**
	 * ����Ͽε������ܴ�
	 * 
	 * @return
	 */
	public List<String> getTeaAllWeek() {
		return new LocalSqlTool(context).getTeaAllWeek();
	}

	/**
	 * �����ʦ�Ͽε����нڴ�
	 * 
	 * @return
	 */
	public List<String> getTeaAllClassTime() {
		return new LocalSqlTool(context).getTeaAllClassTime();
	}

	/**
	 * �鿴������Ϣ����������
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
	 * ��ò�¼ʱ�Ľ��Ⱥź������
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
	 * �ж��Ƿ�����ѱ��浫δ�ύ�Ŀ��ڽ���
	 * 
	 * @return
	 */
	public List<TeachProgress> existSavedTProgress() {
		return new LocalSqlTool(context).existSavedTProgress();
	}

	/**
	 * ���ݽ��ȺŻ����Ӧ��δ�ύ�Ŀ�����Ϣ
	 * 
	 * @param jno
	 * @return
	 */
	public List<Students> getKQStuByJno(int jno) {
		return new LocalSqlTool(context).getKQStuByJno(jno);
	}

	/**
	 * ɾ��������Ϣ
	 * 
	 * @param jno
	 * @param stuSnoList
	 */
	public void deleteKQByStuJno(int jno, List<String> stuSnoList) {
		new LocalSqlTool(context).deleteKQByStuJno(jno, stuSnoList);
	}

	/**
	 * ɾ�����ػ����δ�ύ�Ľ���
	 * 
	 * @param listJno
	 */
	public void deleteLocalProgress(List<Integer> listJno) {
		new LocalSqlTool(context).deleteLocalProgress(listJno);
	}

	/**
	 * ���ݽ��Ⱥ��ύ��������ĳЩԭ��δ���ύ�Ŀ���
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
	 * ���ݽ��Ⱥ��жϵ�ǰ�����Ƿ��Ѿ��ύ
	 * 
	 * @param jno
	 * @return
	 */
	public boolean isJnoSubmit(int jno) {
		return new LocalSqlTool(context).isJnoSubmit(jno);
	}

	/*
	 * ������ʦ��Ż�����Ͽ�ѧ����ͼƬ�ļ�
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
