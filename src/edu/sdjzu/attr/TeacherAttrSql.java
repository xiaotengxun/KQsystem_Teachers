package edu.sdjzu.attr;

public class TeacherAttrSql {// UserInf
	public static final String SQL_INSERT_TO_USERINFO = "insert into UserInf(Uno, Upwd, Uname, Usex, Usdept, "
			+ "Uclass, Utel, Utype) values(?,?,?,?,?,?,?,?)";// �򱾵ز������е�Ա��Ϣ
	public static final String SQL_DELETE_FROM_USERINFO = "delete from UserInf where Uno=?";// ɾ������ĳ����Ա����Ϣ
	public static final String SQL_DELETE_FROM_TEACHERS = "delete from Teachers where Tno=?";// ɾ������ĳ����ʦ����Ϣ
	public static final String SQL_INSERT_TO_TEACHERS = "insert into Teachers (Tno ,Tpwd  ,Tname,Tsex "
			+ ",Tsdept ,Temail,Tel) values(?,?,?,?,?,?,?)";// �򱾵ز���ĳ����ʦ����Ϣ
	public static final String SQL_DELETE_FROM_TEACHPROGRESS_ALL = "delete from TeachProgress ";// ɾ�����ؽ��ȱ�
	public static final String SQL_INSERT_TO_TEACHPROGRESS_ALL = "insert into TeachProgress  (Jno ,Rno,Cname,Tname,Jclass,Jweek,"
			+ "Jtime,Jaddress,StartTime,EndTime,IsKQ ,InMan  ,InTime) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";// �򱾵ز�����ȱ�
	public static final String SQL_INSERT_TO_TEACH_TASK = "insert into TeachTask(Rno,Cno,Cname,Tname,Rclass,Ctype,"
			+ "Rweek,Rterms) values(?,?,?,?,?,?,?,?)";// �򱾵ز����ѧ���ȱ�
	public static final String SQL_DELETE_FROM_TEACH_TASK = "delete from TeachTask";// ɾ�����ص������
	public static final String SQL_INSERT_TO_STUDENTS = "insert into Students(Sid,Sno,Spwd,Ppwd,Sname,Ssex,Ssdept,"
			+ "Sclass,Sstate,Stel,Ptel,Spic) values(?,?,?,?,?,?,?,?,?,?,?,?)";// �򱾵ز���W����Ϣ
	public static final String SQL_DELETE_FROM_STUDENTS = "delete from Students";
	public static final String SQL_SELECT_FROM_KQRESULT_EXISTS = "select * from KQresult where Jno=? and Sno=?";// �жϷ������ϵ�kqresult�ڱ�����û��
	public static final String SQL_SELECT_FROM_KQRESULTLOCAL_ALL = "select * from KQresultLocal where Jno=?";// �жϱ��ػ���KQresultLocal��û��
	public static final String SQL_SELECT_FROM_KQRESULTLOCAL_BY_CLASS = "select K.Sno as Sno,K.InTime as InTime,K.Kstate as Kstate from KQresultLocal K,Students S where Jno=? and S.Sclass=?";// �жϱ��ػ���KQresultLocal��û��
	public static final String SQL_DELETE_FROM_KQRESULTLOCAL = "delete from KQresultLocal where Jno=? and Sno=?";// ���ݽ��Ⱥ�ѧ��ɾ��kqresult
	public static final String SQL_DELETE_FROM_KQRESULT_ALL="delete from KQresult";
	public static final String SQL_INSERT_TO_KQRESULT = "insert into KQresult(Rno,Jno,Sno,Kstate,"
			+ "Kmarks,IsSubmin,InMan,InTime) values(?,?,?,?,?,?,?,?)";// ���뿼����Ϣ
	public static final String SQL_INSERT_TO_KQRESULTLocal = "insert into KQresultLocal(Rno,Jno,Sno,Kstate,"
			+ "Kmarks,IsSubmin,InMan,InTime) values(?,?,?,?,?,?,?,?)";// ���뿼����Ϣ

	public static final String SQL_SELECT_FROM_TEACHERS_LOGIN = "select * from Teachers where Tno=? and Tpwd=?";// ���û���½��б��ص�½

	public static final String SQL_SELECT_FROM_KQRESULT_DISABLENCE_CONTS = "select * from KQresult "
			+ "where Kstate<>? and Sno=?";// ����ѧ��ѧ�Ż��ѧ����ȱ�ڴ���
	public static final String SQL_SELECT_FROM_STUDENTS_ALL = "select *  from Students";// �������ѧ���б�
	public static final String SQL_SELECT_FROM_TEACHTASK = "select * from TeachTask";
	public static final String SQL_SELECT_FROM_TEACHPROGRESS = "select * from TeachProgress "
			+ "where ? between StartTime and EndTime";// �ȶ������Ƿ����Ͽ�ʱ��

	public static final String SQL_SELECT_FROM_TEACHERS_FOR_NAME = "select * from Teachers where Tno=?";// ������ʦ��Ż����ʦ��Ϣ
	public static final String SQL_SELECT_FROM_TEACHPROGRESS_ALL = "select * from TeachProgress";// ������н��ȱ���Ϣ
	public static final String SQL_SELECT_FROM_TEACHPROGRESS_ALL_COURSE = "select distinct Cname from TeachProgress";// ��ý��ȱ�������пγ�������Ϣ
	public static final String SQL_SELECT_FROM_TEACHPROGRESS_ALL_WEEK = "select distinct Jweek from TeachProgress";// ��ý��ȱ����������
	public static final String SQL_SELECT_FROM_TEACHPROGRESS_ALL_CLASSTIME = "select distinct Jtime from TeachProgress";// ��ý��ȱ���������Ͽνڴ���Ϣ
	public static final String SQL_SELECT_LOOK_KQ_PER_CLASS = "select S.Sname as sname,S.Sno as sno,S.Sclass as sclass,"
			+ "KQ.Kstate as state from TeachProgress as J,KQresult as KQ,Students as S where J.Cname=? and J.Jweek=?"
			+ " and J.Jtime=? and S.Sclass=? and J.Jno=KQ.Jno and KQ.Sno=S.Sno";// �����ض��༶�鿴
	public static final String SQL_SELECT_LOOK_KQ_ALL_CLASS = "select S.Sname as sname,S.Sno as sno,S.Sclass as sclass,KQ.Kstate as state from TeachProgress as J,KQresult as KQ,Students as S where J.Cname=? and J.Jweek=?"
			+ " and J.Jtime=?  and J.Jno=KQ.Jno and KQ.Sno=S.Sno";// ����ȫ���༶�鿴
	public static final String SQL_SELECT_BULU_RNO_JNO = "select * from TeachProgress where Cname=? and "
			+ "Jweek=? and Jtime=?";// ���ڲ�¼��ѯ���Ⱥź������

	public static final String SQL_SELECT_FROM_KQRESULT_NOT_SUBMIT = "select K.Kno as Kno,K.Rno as Rno,K.Jno as Jno,K.Sno as Sno,K.Kstate as Kstate,"
			+ "K.IsSubmin as IsSubmin,K.Kmarks as Kmarks,"
			+ "K.InMan as InMan,K.InTime as InTime, S.Sname as Sname,S.Sclass as Sclass"
			+ " from KQresult K,Students S where K.IsSubmin=? " + "and K.Jno=? and K.Sno=S.Sno";// ��ѯδ�ύ�Ŀ�����Ϣ
	public static final String SQL_SELECT_FROM_KQRESULTLOCAL_NOT_SUBMIT = "select K.Kno as Kno,K.Rno as Rno,K.Jno as Jno,K.Sno as Sno,K.Kstate as Kstate,"
			+ "K.IsSubmin as IsSubmin,K.Kmarks as Kmarks,"
			+ "K.InMan as InMan,K.InTime as InTime, S.Sname as Sname,S.Sclass as Sclass"
			+ " from KQresultLocal K,Students S where K.IsSubmin=? " + "and K.Jno=? and K.Sno=S.Sno";// ��ѯδ�ύ�Ŀ�����Ϣ

	public static final String SQL_UPDATE_KQRESULTLocal_BY_JNO = "update KQresultLocal set IsSubmin=? where Jno=?";// ���ݽ��ȺŸ��¿���״̬Ϊ���ύ

	public static final String SQL_DELETE_KQRESULTLOCAL_BY_JNO="update  KQresultLocal set IsSubmin=? where Jno=?";
	public static final String SQL_Delete_TEACHLocalPROGRESS_JNO = "delete from TeachLocalProgress where jno=?";// ���ݽ��ȺŸ��±��صĽ��ȱ�Ϊ���ύ
	public static final String SQL_UPDATE_TEACHPROGRESS_SAVE = "update TeachProgress set IsSaved=? where Jno=?";// ���ݽ��Ⱥ���û�������������ñ���״̬Ϊ����
	public static final String SQL_EXIST_SAVED_TEACHLocalPROGRESS = "select * from TeachLocalProgress where IsSaved=? and IsKQ=?";// �����Ƿ������������ԭ���δ�ύ�Ľ���

	public static final String SQL_KQ_NOT_SUBMIT_STU = "select S.Sno as sno,S.Sname as sname,S.Sclass as sclass from "
			+ "Students as S,KQresultLocal as KQ where KQ.Jno=? and KQ.Sno=S.Sno and KQ.Kstate<>?";// ���δ�ύ�Ŀ�����Ϣ

	public static final String SQL_SELECT_FROM_TEACHERPROGRESS_BY_JNO = "select * from TeachProgress where jno=?";
	public static final String SQL_SELECT_FROM_TEACHLOCALPROGRESS_BY_JNO = "select * from TeachLocalProgress where jno=?";
	public static final String SQL_INSERT_TO_TEACHLOCALPROGRESS = "insert into TeachLocalProgress"
			+ "(jno,rno,cname,jweek,jtime,IsKQ,IsSaved) values(?,?,?,?,?,?,?)";
	public static final String SQL_UPDATE_TEACHLOCALPROGRESS = "update  TeachLocalProgress set IsKq=? and IsSaved=? where jno=?";

}
