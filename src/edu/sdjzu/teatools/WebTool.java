package edu.sdjzu.teatools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import com.example.kqsystem_teachers.R;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;
import edu.sdjzu.attr.TeacherAttr;
import edu.sdjzu.ksoap.tools.MyAndroidHttpTransport;
import edu.sdjzu.localtool.LocalSqlTool;
import edu.sdjzu.model.KQresult;
import edu.sdjzu.model.Students;
import edu.sdjzu.model.TeachProgress;
import edu.sdjzu.model.TeachTask;
import edu.sdjzu.model.Teachers;
import edu.sdjzu.model.UserInf;

public class WebTool {
	private LocalSqlTool localSqlTool;
	@SuppressWarnings("unused")
	private Context context;
	private String NAMESPACE = "http://chenshuwan.org/";
	private String METHOD_NAME = "";
	private String SOAP_ACTION = "";
	private String URL = "http://jsjzy.sdjzu.edu.cn/sdjzu/service1.asmx";
	private static List<Students> listStu = new ArrayList<Students>();;
	private MyAndroidHttpTransport ht;

	public WebTool(Context context) throws IOException {
		super();
		this.context = context;
		localSqlTool = new LocalSqlTool(context);
		ht = new MyAndroidHttpTransport(URL);
	}

	/**
	 * 根据用户类型进行登录，成功则返回true
	 * 
	 * @param userType
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean LoginSuccess(String userType, String username, String password) {
		boolean istrue = false;
		METHOD_NAME = "LoginSuccess";
		SOAP_ACTION = NAMESPACE + METHOD_NAME;
		SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
		rpc.addProperty("userType", userType);
		rpc.addProperty("username", username);
		rpc.addProperty("password", password);
		// AndroidHttpTransport ht = new AndroidHttpTransport(URL);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet = true;
		(new MarshalBase64()).register(envelope);
		envelope.setOutputSoapObject(rpc);
		try {
			ht.call(SOAP_ACTION, envelope);
		} catch (XmlPullParserException e1) {
			return false;
		} catch (IOException e) {
			return false;
		}
		try {
			if (envelope.getResponse() != null) {
				istrue = Boolean.valueOf(envelope.getResponse().toString());
				System.out.println("用户" + username + "是否登录成功？" + istrue);
			}
		} catch (SoapFault e) {
			e.printStackTrace();
		}
		return istrue;
	}

	/**
	 * 获得所有辅导员信息
	 * 
	 * @return
	 */
	public List<UserInf> getAllUserInf() {
		// getAllUserInf
		List<UserInf> tlist = new ArrayList<UserInf>();
		METHOD_NAME = "getAllUserInf";
		SOAP_ACTION = NAMESPACE + METHOD_NAME;
		SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
		// AndroidHttpTransport ht = new AndroidHttpTransport(URL);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet = true;
		(new MarshalBase64()).register(envelope);
		envelope.setOutputSoapObject(rpc);
		try {
			ht.call(SOAP_ACTION, envelope);
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
			return tlist;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return tlist;
		}
		try {
			if (envelope.getResponse() != null) {
				Object result = (Object) envelope.getResponse();
				SoapObject data = (SoapObject) result;
				int counts = data.getPropertyCount();
				for (int i = 0; i < counts; i++) {
					SoapObject s = (SoapObject) data.getProperty(i);
					UserInf user = new UserInf();
					user.setUserNo(s.getProperty("Uno").toString());
					user.setUserPassword(s.getProperty("Upwd").toString());
					user.setUserName(s.getProperty("Uname").toString());
					user.setUserSex(s.getProperty("Usex").toString());
					user.setUserSdept(s.getProperty("Usdept").toString());
					user.setUserClass(s.getProperty("Usclass").toString());
					user.setUserTel(s.getProperty("Utel").toString());
					user.setUserType(s.getProperty("Utype").toString());
					tlist.add(user);
					Log.i("chen", "getAllUserInf user.name=" + user.getUserName());
				}
			}
		} catch (SoapFault e) {
			e.printStackTrace();
		}
		for (UserInf userInf : tlist) {

			localSqlTool.insertUserInfByUno(userInf, userInf.getUserNo());
		}
		return tlist;
	}

	/**
	 * 根据老师编号获取老师信息
	 * 
	 * @param Tno
	 * @return
	 */
	public Teachers QueryTeachersByTno(String Tno) {
		Teachers teacher = null;
		METHOD_NAME = "getTeacherByTno";
		SOAP_ACTION = NAMESPACE + METHOD_NAME;
		SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
		rpc.addProperty("tno", Tno);
		// AndroidHttpTransport ht = new AndroidHttpTransport(URL);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet = true;
		(new MarshalBase64()).register(envelope);
		envelope.setOutputSoapObject(rpc);
		try {
			ht.call(SOAP_ACTION, envelope);
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
			return teacher;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return teacher;
		}
		try {
			if (envelope.getResponse() != null) {
				teacher = new Teachers();
				Object result = (Object) envelope.getResponse();
				SoapObject data = (SoapObject) result;
				teacher.setTeaNo((data.getProperty("Tno").toString()));
				teacher.setTeaPassword(data.getProperty("Tpwd").toString());
				teacher.setTeaName(data.getProperty("Tname").toString());
				teacher.setTeaSex(data.getProperty("Tsex").toString());
				teacher.setTeaSdept(data.getProperty("Tsdept").toString());
				teacher.setTeaEmail(data.getProperty("Temail").toString());
				teacher.setTeaTel(data.getProperty("Ttel").toString());
				Log.i("chen", "QueryTeachersByTno teacher.name=" + teacher.getTeaName());
			}
		} catch (SoapFault e) {
			e.printStackTrace();
		}
		if (teacher != null)
			localSqlTool.insertTeachersByTno(teacher, Tno);
		return teacher;
	}

	/**
	 * 根据老师姓名获得他/她的教学进度表
	 * 
	 * @param tname
	 * @return
	 */
	public List<TeachProgress> getAllJDTBbyTno(String tno) {

		List<TeachProgress> lisTP = new ArrayList<TeachProgress>();
		METHOD_NAME = "getAllJDTBbyTno";
		SOAP_ACTION = NAMESPACE + METHOD_NAME;
		SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
		System.out.println("	rpc.addProperty   begin");
		rpc.addProperty("tno", tno);
		System.out.println("	rpc.addProperty   over");
		// AndroidHttpTransport ht = new AndroidHttpTransport(URL);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet = true;
		(new MarshalBase64()).register(envelope);
		envelope.setOutputSoapObject(rpc);
		try {
			ht.call(SOAP_ACTION, envelope);
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
			return lisTP;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return lisTP;
		}
		try {
			if (envelope.getResponse() != null) {
				Object result = (Object) envelope.getResponse();
				SoapObject data = (SoapObject) result;
				int counts = data.getPropertyCount();
				// System.out.println("老师"+tno+"的教学进度表如下:");
				for (int i = 0; i < counts; i++) {
					SoapObject s = (SoapObject) data.getProperty(i);
					TeachProgress t = new TeachProgress();
					t.setProgressNo(Integer.valueOf(s.getProperty("Jno").toString()));
					t.setTeaName(s.getProperty("Tname").toString());
					t.setProgressJTime(s.getProperty("Jtime").toString());
					t.setCourseName(s.getProperty("Cname").toString());
					t.setProgressAddress(s.getProperty("Jaddress").toString());
					t.setTaskNo(Integer.valueOf(s.getProperty("Rno").toString()));
					t.setProgressClass(s.getProperty("Jclass").toString());
					t.setProgressWeek(s.getProperty("Jweek").toString());
					t.setStartTime(s.getProperty("StartTime").toString());
					t.setEndTime(s.getProperty("EndTime").toString());
					t.setIsKQ(s.getProperty("IsKQ").toString());
					t.setInMan(s.getProperty("InMan").toString());
					t.setInTime(s.getProperty("InTime").toString());
					lisTP.add(t);
					Log.i("chen", "getAllJDTBbyTno TeachProgress.prgress time=" + t.getEndTime());
				}
			}
		} catch (SoapFault e) {
			e.printStackTrace();
		}
		localSqlTool.insertAllJDTBbyTname(lisTP, tno);
		return lisTP;
	}

	/**
	 * 通过老师编号查教学任务
	 * 
	 * @param TaskNo
	 * @return
	 */
	public List<TeachTask> QuerTeachTaskByTno(String tno) {

		List<TeachTask> tlist = new ArrayList<TeachTask>();
		METHOD_NAME = "getRWTByTno";
		SOAP_ACTION = NAMESPACE + METHOD_NAME;
		SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
		rpc.addProperty("tno", tno);
		// AndroidHttpTransport ht = new AndroidHttpTransport(URL);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet = true;
		(new MarshalBase64()).register(envelope);
		envelope.setOutputSoapObject(rpc);
		try {
			ht.call(SOAP_ACTION, envelope);
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
			return tlist;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return tlist;
		}
		try {
			if (envelope.getResponse() != null) {
				Object result = (Object) envelope.getResponse();
				SoapObject data = (SoapObject) result;
				int counts = data.getPropertyCount();
				for (int i = 0; i < counts; i++) {
					SoapObject s = (SoapObject) data.getProperty(i);
					TeachTask task = new TeachTask();
					task.setCourseName(s.getProperty("Cname").toString());
					task.setCourseNo(s.getProperty("Cno").toString());
					task.setCourseType(s.getProperty("Ctype").toString());
					task.setTaskClass(s.getProperty("Rclass").toString());
					task.setTaskNo(Integer.valueOf(s.getProperty("Rno").toString()));
					task.setTaskTerms(s.getProperty("Rterms").toString());
					task.setTaskWeek(s.getProperty("Rweek").toString());
					task.setTeaName(s.getProperty("Tname").toString());
					tlist.add(task);
					Log.i("chen", "QuerTeachTaskByTno    task.CourseName------------=" + task.getCourseName());
				}

			}
		} catch (SoapFault e) {
			e.printStackTrace();
		}
		for (TeachTask teachTask : tlist) {

			localSqlTool.insertTeachTaskByTaskNo(teachTask);
		}
		return tlist;
	}

	/**
	 * 根据老师的编号查找一个教师上课的全部学生
	 * 
	 * @param rno
	 * @return
	 */
	public List<Students> getClassStuByTno(String tno) {

		listStu = new ArrayList<Students>();
		METHOD_NAME = "getClassStuByTno";
		SOAP_ACTION = NAMESPACE + METHOD_NAME;
		SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
		rpc.addProperty("tno", tno);
		// AndroidHttpTransport ht = new AndroidHttpTransport(URL);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet = true;
		(new MarshalBase64()).register(envelope);
		envelope.setOutputSoapObject(rpc);
		try {
			ht.call(SOAP_ACTION, envelope);
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
			return listStu;
		} catch (IOException e) {
			e.printStackTrace();
			return listStu;
		}
		try {
			if (envelope.getResponse() != null) {
				Object result = (Object) envelope.getResponse();
				SoapObject data = (SoapObject) result;
				int counts = data.getPropertyCount();
				for (int i = 0; i < counts; i++) {
					SoapObject s = (SoapObject) data.getProperty(i);
					Students st = new Students();
					st.setStuClass(s.getProperty("Sclass").toString());
					st.setStuNo(s.getProperty("Sno").toString());
					st.setStuPic(s.getProperty("Spic").toString());
					st.setStuSex(s.getProperty("Ssex").toString());
					st.setStuName(s.getProperty("Sname").toString());
					st.setStuState(s.getProperty("Sstate").toString());
					st.setStuId(s.getProperty("Sid").toString());
					st.setStuPassword(s.getProperty("Spwd").toString());
					st.setParPassword(s.getProperty("Ppwd").toString());
					st.setStuSdept(s.getProperty("Ssdept").toString());
					st.setParTel(s.getProperty("Ptel").toString());
					st.setStuTel(s.getProperty("Stel").toString());
					listStu.add(st);
					Log.i("chen", "stu.name=" + st.getStuName());

				}
			}
		} catch (SoapFault e) {
			e.printStackTrace();
		}
		localSqlTool.insertClassStuByRno(listStu);
		return listStu;
	}

	/**
	 * 根据老师编号获得其学生的考勤信息
	 * 
	 * @param tno
	 * @return
	 */
	public List<KQresult> getKQTBbyTno(String tno) {
		// getAllKQTBbyTno
		List<KQresult> klist = new ArrayList<KQresult>();
		METHOD_NAME = "getAllKQTBbyTno";
		SOAP_ACTION = NAMESPACE + METHOD_NAME;
		SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
		rpc.addProperty("tno", String.valueOf(tno));
		// AndroidHttpTransport ht = new AndroidHttpTransport(URL);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet = true;
		(new MarshalBase64()).register(envelope);
		envelope.setOutputSoapObject(rpc);
		try {
			ht.call(SOAP_ACTION, envelope);
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
			return klist;
		} catch (IOException e) {
			e.printStackTrace();
			return klist;
		}
		try {
			if (envelope.getResponse() != null) {
				Object result = (Object) envelope.getResponse();
				SoapObject data = (SoapObject) result;
				int counts = data.getPropertyCount();
				for (int i = 0; i < counts; i++) {
					SoapObject s = (SoapObject) data.getProperty(i);
					KQresult k = new KQresult();
					k.setKqNo(Integer.valueOf(s.getProperty("Kno").toString()));
					k.setTaskNo(Integer.valueOf(s.getProperty("Rno").toString()));
					k.setProgressNo(Integer.valueOf(s.getProperty("Jno").toString()));
					k.setStuNo(s.getProperty("Sno").toString());
					k.setKqState(s.getProperty("Kstate").toString());
					k.setKqMark(s.getProperty("Kmarks").toString());
					k.setIsSubmit(s.getProperty("IsSubmin").toString());
					k.setInMan(s.getProperty("InMan").toString());
					k.setInTime(s.getProperty("InTime").toString());
					klist.add(k);
					Log.i("chen", "getKQTBbyTno    sno=" + k.getStuNo() + "         name=" + k.getsName()
							+ "    kqsult=" + k.getKqState());
				}
			}
		} catch (SoapFault e) {
			e.printStackTrace();
		}
		localSqlTool.updateKQresult(klist, true);
		return klist;
	}

	/**
	 * 根据老师编号获得其所有学生的图片
	 * 
	 * @return
	 */

	public void getStuPicAllByTno(String tno) {
		for (Students stu : listStu) {
			try {
				getPhoto(stu.getStuNo());
			} catch (Exception e) {
				// TODO: handle exception
			} catch (Error ex) {
			}
		}
	}

	@SuppressWarnings("unused")
	private void getPhoto(String sno) {
		List<Bitmap> bitmap = new ArrayList<Bitmap>();
		METHOD_NAME = "getPicBySno";
		SOAP_ACTION = NAMESPACE + METHOD_NAME;
		SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
		rpc.addProperty("sno", sno);
		// AndroidHttpTransport ht = new AndroidHttpTransport(URL);
		ht.debug = true;

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet = true;
		(new MarshalBase64()).register(envelope);
		envelope.setOutputSoapObject(rpc);
		try {
			ht.call(SOAP_ACTION, envelope);
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		try {
			if (envelope.getResponse() != null) {

				String result = envelope.getResponse().toString();
				if (result.equals(""))
					return;
				try {
					String fileName = sno + ".txt";
					Log.i("chen", "pic =" + sno + "    " + result);
					localSqlTool.saveToSDCard(result, fileName);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (SoapFault e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向服务器提交考勤结果，自动到本地数据库里查找未提交的考勤结果
	 */
	public void TJKQresult(int jno) {
		List<KQresult> klist = new ArrayList<KQresult>();
		klist = localSqlTool.getAllKQByJno(jno);
		if (klist.size() == 0) {
			Intent intents = new Intent(context.getString(R.string.net_submit_kq_action));
			intents.putExtra("toast", 0);
			context.sendBroadcast(intents);
			return;
		}
		String js = "[";
		for (int i = 0; i < klist.size() - 1; i++) {
			KQresult k = klist.get(i);
			js += "{'Kno':'" + k.getKqNo() + "','Rno':'" + k.getTaskNo() + "','Jno':'" + k.getProgressNo()
					+ "','Sno':'" + k.getStuNo() + "','Kstate':'" + k.getKqState() + "','Kmarks':'" + k.getKqMark()
					+ "','IsSubmin':'" + k.getIsSubmit() + "','InMan':'" + k.getInMan() + "','InTime':'"
					+ k.getInTime() + "','Sname':'" + k.getsName() + "','Sclass':'" + k.getsClass() + "'},";
		}
		KQresult k = klist.get(klist.size() - 1);
		js += "{'Kno':'" + k.getKqNo() + "','Rno':'" + k.getTaskNo() + "','Jno':'" + k.getProgressNo() + "','Sno':'"
				+ k.getStuNo() + "','Kstate':'" + k.getKqState() + "','Kmarks':'" + k.getKqMark() + "','IsSubmin':'"
				+ k.getIsSubmit() + "','InMan':'" + k.getInMan() + "','InTime':'" + k.getInTime() + "','Sname':'"
				+ k.getsName() + "','Sclass':'" + k.getsClass() + "'}";
		js += "]";
		Log.i("chen", "js=" + js);
		METHOD_NAME = "UpKQTB";
		SOAP_ACTION = NAMESPACE + METHOD_NAME;
		SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
		rpc.addProperty("js", js);
		// AndroidHttpTransport ht = new AndroidHttpTransport(URL);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet = true;
		(new MarshalBase64()).register(envelope);
		envelope.setOutputSoapObject(rpc);
		try {
			ht.call(SOAP_ACTION, envelope);
			Log.i("chen", "ht.call(SOAP_ACTION, envelope)");
			UpdateProgress(jno);
			localSqlTool.upLocalJno(jno);
			localSqlTool.upLocalKQByJno(jno);
			Intent intent = new Intent(context.getString(R.string.net_submit_kq_action));
			intent.putExtra("toast", 1);
			context.sendBroadcast(intent);
		} catch (XmlPullParserException e1) {
			Log.i("chen", "" + e1);
			localSqlTool.upTeachProgressSave(jno);
			context.sendBroadcast(new Intent(context.getString(R.string.network_error_action)));
		} catch (IOException e) {
			Log.i("chen", "" + e);
			localSqlTool.upTeachProgressSave(jno);
			context.sendBroadcast(new Intent(context.getString(R.string.network_error_action)));
		}
	}

	/**
	 * 根据进度号提交本地由于某些原因未能提交的考勤
	 * 
	 * @param listJno
	 */
	public void TJKQresultLocal(List<Integer> listJno) {
		List<KQresult> klist = new ArrayList<KQresult>();
		klist = localSqlTool.getKQresultLocalByJno(listJno);
		int kqCounts = klist.size();
		Log.i("chen", "kqResultLocal size()=" + kqCounts);
		if (0 == kqCounts) {
			return;
		}
		String js = "[";
		for (int i = 0; i < klist.size() - 1; i++) {
			KQresult k = klist.get(i);
			js += "{'Kno':'" + k.getKqNo() + "','Rno':'" + k.getTaskNo() + "','Jno':'" + k.getProgressNo()
					+ "','Sno':'" + k.getStuNo() + "','Kstate':'" + k.getKqState() + "','Kmarks':'" + k.getKqMark()
					+ "','IsSubmin':'" + k.getIsSubmit() + "','InMan':'" + k.getInMan() + "','InTime':'"
					+ k.getInTime() + "','Sname':'" + k.getsName() + "','Sclass':'" + k.getsClass() + "'},";
		}
		KQresult k = klist.get(klist.size() - 1);
		js += "{'Kno':'" + k.getKqNo() + "','Rno':'" + k.getTaskNo() + "','Jno':'" + k.getProgressNo() + "','Sno':'"
				+ k.getStuNo() + "','Kstate':'" + k.getKqState() + "','Kmarks':'" + k.getKqMark() + "','IsSubmin':'"
				+ k.getIsSubmit() + "','InMan':'" + k.getInMan() + "','InTime':'" + k.getInTime() + "','Sname':'"
				+ k.getsName() + "','Sclass':'" + k.getsClass() + "'}";
		js += "]";
		Log.i("chen", "js=" + js);
		METHOD_NAME = "UpKQTB";
		SOAP_ACTION = NAMESPACE + METHOD_NAME;
		SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
		rpc.addProperty("js", js);
		// AndroidHttpTransport ht = new AndroidHttpTransport(URL);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet = true;
		(new MarshalBase64()).register(envelope);
		envelope.setOutputSoapObject(rpc);
		try {
			ht.call(SOAP_ACTION, envelope);
			Log.i("chen", "ht.call(SOAP_ACTION, envelope)");
			Intent intent = new Intent(context.getString(R.string.net_submit_kq_action));
			intent.putExtra("toast", 1);
			localSqlTool.deleteLocalProgress(listJno);
			context.sendBroadcast(intent);
		} catch (XmlPullParserException e1) {
			Log.i("chen", "" + e1);
			context.sendBroadcast(new Intent(context.getString(R.string.network_error_action)));
		} catch (IOException e) {
			Log.i("chen", "" + e);
			context.sendBroadcast(new Intent(context.getString(R.string.network_error_action)));
		}
	}

	/**
	 * 根据进度号更新服务器的进度状态为已提交
	 * 
	 * @param jno
	 */
	public void UpdateProgress(int jno) {
		// UpdateJDTBbyJno
		List<Integer> jlist = new ArrayList<Integer>();
		jlist.add(jno);
		METHOD_NAME = "UpdateJDTBbyJno";
		SOAP_ACTION = NAMESPACE + METHOD_NAME;
		SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
		rpc.addProperty("jno", jno);
		// AndroidHttpTransport ht = new AndroidHttpTransport(URL);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		envelope.dotNet = true;
		(new MarshalBase64()).register(envelope);
		envelope.setOutputSoapObject(rpc);
		try {
			ht.call(SOAP_ACTION, envelope);
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
