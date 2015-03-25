package edu.sdjzu.attr;

public class TeacherAttrSql {// UserInf
	public static final String SQL_INSERT_TO_USERINFO = "insert into UserInf(Uno, Upwd, Uname, Usex, Usdept, "
			+ "Uclass, Utel, Utype) values(?,?,?,?,?,?,?,?)";// 向本地插入所有导员信息
	public static final String SQL_DELETE_FROM_USERINFO = "delete from UserInf where Uno=?";// 删除本地某个导员的信息
	public static final String SQL_DELETE_FROM_TEACHERS = "delete from Teachers where Tno=?";// 删除本地某个老师的信息
	public static final String SQL_INSERT_TO_TEACHERS = "insert into Teachers (Tno ,Tpwd  ,Tname,Tsex "
			+ ",Tsdept ,Temail,Tel) values(?,?,?,?,?,?,?)";// 向本地插入某个老师的信息
	public static final String SQL_DELETE_FROM_TEACHPROGRESS_ALL = "delete from TeachProgress ";// 删除本地进度表
	public static final String SQL_INSERT_TO_TEACHPROGRESS_ALL = "insert into TeachProgress  (Jno ,Rno,Cname,Tname,Jclass,Jweek,"
			+ "Jtime,Jaddress,StartTime,EndTime,IsKQ ,InMan  ,InTime) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";// 向本地插入进度表
	public static final String SQL_INSERT_TO_TEACH_TASK = "insert into TeachTask(Rno,Cno,Cname,Tname,Rclass,Ctype,"
			+ "Rweek,Rterms) values(?,?,?,?,?,?,?,?)";// 向本地插入教学进度表
	public static final String SQL_DELETE_FROM_TEACH_TASK = "delete from TeachTask";// 删除本地的任务表
	public static final String SQL_INSERT_TO_STUDENTS = "insert into Students(Sid,Sno,Spwd,Ppwd,Sname,Ssex,Ssdept,"
			+ "Sclass,Sstate,Stel,Ptel,Spic) values(?,?,?,?,?,?,?,?,?,?,?,?)";// 向本地插入學生信息
	public static final String SQL_DELETE_FROM_STUDENTS = "delete from Students";
	public static final String SQL_SELECT_FROM_KQRESULT_EXISTS = "select * from KQresult where Jno=? and Sno=?";// 判断服务器上的kqresult在本地有没有
	public static final String SQL_SELECT_FROM_KQRESULTLOCAL_ALL = "select * from KQresultLocal where Jno=?";// 判断本地缓存KQresultLocal有没有
	public static final String SQL_SELECT_FROM_KQRESULTLOCAL_BY_CLASS = "select K.Sno as Sno,K.InTime as InTime,K.Kstate as Kstate from KQresultLocal K,Students S where Jno=? and S.Sclass=?";// 判断本地缓存KQresultLocal有没有
	public static final String SQL_DELETE_FROM_KQRESULTLOCAL = "delete from KQresultLocal where Jno=? and Sno=?";// 根据进度号学号删除kqresult
	public static final String SQL_DELETE_FROM_KQRESULT_ALL="delete from KQresult";
	public static final String SQL_INSERT_TO_KQRESULT = "insert into KQresult(Rno,Jno,Sno,Kstate,"
			+ "Kmarks,IsSubmin,InMan,InTime) values(?,?,?,?,?,?,?,?)";// 插入考勤信息
	public static final String SQL_INSERT_TO_KQRESULTLocal = "insert into KQresultLocal(Rno,Jno,Sno,Kstate,"
			+ "Kmarks,IsSubmin,InMan,InTime) values(?,?,?,?,?,?,?,?)";// 插入考勤信息

	public static final String SQL_SELECT_FROM_TEACHERS_LOGIN = "select * from Teachers where Tno=? and Tpwd=?";// 检测没网下进行本地登陆

	public static final String SQL_SELECT_FROM_KQRESULT_DISABLENCE_CONTS = "select * from KQresult "
			+ "where Kstate<>? and Sno=?";// 根据学生学号获得学生的缺勤次数
	public static final String SQL_SELECT_FROM_STUDENTS_ALL = "select *  from Students";// 获得所有学生列表
	public static final String SQL_SELECT_FROM_TEACHTASK = "select * from TeachTask";
	public static final String SQL_SELECT_FROM_TEACHPROGRESS = "select * from TeachProgress "
			+ "where ? between StartTime and EndTime";// 比对现在是否是上课时间

	public static final String SQL_SELECT_FROM_TEACHERS_FOR_NAME = "select * from Teachers where Tno=?";// 根据老师编号获得老师信息
	public static final String SQL_SELECT_FROM_TEACHPROGRESS_ALL = "select * from TeachProgress";// 获得所有进度表信息
	public static final String SQL_SELECT_FROM_TEACHPROGRESS_ALL_COURSE = "select distinct Cname from TeachProgress";// 获得进度表里的所有课程名称信息
	public static final String SQL_SELECT_FROM_TEACHPROGRESS_ALL_WEEK = "select distinct Jweek from TeachProgress";// 获得进度表里的所有周
	public static final String SQL_SELECT_FROM_TEACHPROGRESS_ALL_CLASSTIME = "select distinct Jtime from TeachProgress";// 获得进度表里的所有上课节次信息
	public static final String SQL_SELECT_LOOK_KQ_PER_CLASS = "select S.Sname as sname,S.Sno as sno,S.Sclass as sclass,"
			+ "KQ.Kstate as state from TeachProgress as J,KQresult as KQ,Students as S where J.Cname=? and J.Jweek=?"
			+ " and J.Jtime=? and S.Sclass=? and J.Jno=KQ.Jno and KQ.Sno=S.Sno";// 考勤特定班级查看
	public static final String SQL_SELECT_LOOK_KQ_ALL_CLASS = "select S.Sname as sname,S.Sno as sno,S.Sclass as sclass,KQ.Kstate as state from TeachProgress as J,KQresult as KQ,Students as S where J.Cname=? and J.Jweek=?"
			+ " and J.Jtime=?  and J.Jno=KQ.Jno and KQ.Sno=S.Sno";// 考勤全部班级查看
	public static final String SQL_SELECT_BULU_RNO_JNO = "select * from TeachProgress where Cname=? and "
			+ "Jweek=? and Jtime=?";// 考勤补录查询进度号和任务号

	public static final String SQL_SELECT_FROM_KQRESULT_NOT_SUBMIT = "select K.Kno as Kno,K.Rno as Rno,K.Jno as Jno,K.Sno as Sno,K.Kstate as Kstate,"
			+ "K.IsSubmin as IsSubmin,K.Kmarks as Kmarks,"
			+ "K.InMan as InMan,K.InTime as InTime, S.Sname as Sname,S.Sclass as Sclass"
			+ " from KQresult K,Students S where K.IsSubmin=? " + "and K.Jno=? and K.Sno=S.Sno";// 查询未提交的考勤信息
	public static final String SQL_SELECT_FROM_KQRESULTLOCAL_NOT_SUBMIT = "select K.Kno as Kno,K.Rno as Rno,K.Jno as Jno,K.Sno as Sno,K.Kstate as Kstate,"
			+ "K.IsSubmin as IsSubmin,K.Kmarks as Kmarks,"
			+ "K.InMan as InMan,K.InTime as InTime, S.Sname as Sname,S.Sclass as Sclass"
			+ " from KQresultLocal K,Students S where K.IsSubmin=? " + "and K.Jno=? and K.Sno=S.Sno";// 查询未提交的考勤信息

	public static final String SQL_UPDATE_KQRESULTLocal_BY_JNO = "update KQresultLocal set IsSubmin=? where Jno=?";// 根据进度号跟新考勤状态为已提交

	public static final String SQL_DELETE_KQRESULTLOCAL_BY_JNO="update  KQresultLocal set IsSubmin=? where Jno=?";
	public static final String SQL_Delete_TEACHLocalPROGRESS_JNO = "delete from TeachLocalProgress where jno=?";// 根据进度号更新本地的进度表为已提交
	public static final String SQL_UPDATE_TEACHPROGRESS_SAVE = "update TeachProgress set IsSaved=? where Jno=?";// 根据进度号在没网络的情况下设置保存状态为保存
	public static final String SQL_EXIST_SAVED_TEACHLocalPROGRESS = "select * from TeachLocalProgress where IsSaved=? and IsKQ=?";// 查找是否存在由于网络原因而未提交的进度

	public static final String SQL_KQ_NOT_SUBMIT_STU = "select S.Sno as sno,S.Sname as sname,S.Sclass as sclass from "
			+ "Students as S,KQresultLocal as KQ where KQ.Jno=? and KQ.Sno=S.Sno and KQ.Kstate<>?";// 获得未提交的考勤信息

	public static final String SQL_SELECT_FROM_TEACHERPROGRESS_BY_JNO = "select * from TeachProgress where jno=?";
	public static final String SQL_SELECT_FROM_TEACHLOCALPROGRESS_BY_JNO = "select * from TeachLocalProgress where jno=?";
	public static final String SQL_INSERT_TO_TEACHLOCALPROGRESS = "insert into TeachLocalProgress"
			+ "(jno,rno,cname,jweek,jtime,IsKQ,IsSaved) values(?,?,?,?,?,?,?)";
	public static final String SQL_UPDATE_TEACHLOCALPROGRESS = "update  TeachLocalProgress set IsKq=? and IsSaved=? where jno=?";

}
