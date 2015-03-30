package edu.sdjzu.teatools;

import java.io.IOException;

import edu.sdjzu.localtool.LocalSqlTool;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class TeaLoginTool {
	private Context context;
	private WebTool webTool;
	private String tag="chen";

	public TeaLoginTool(Context ctx) {
		context = ctx;
		try {
			webTool = new WebTool(context);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void firstLogin(String tno) {
		webTool.getAllUserInf();
		webTool.QueryTeachersByTno(tno);
		webTool.getAllJDTBbyTno(tno);
		webTool.QuerTeachTaskByTno(tno);
		webTool.getClassStuByTno(tno);
		webTool.getKQTBbyTno(tno);
		webTool.getStuPicAllByTno(tno);
	}
	/**
	 * �����û���¼ʱ���֮ǰ�û���������Ϣ
	 */
	public void clearCache(){
		LocalSqlTool sqlLocal=new LocalSqlTool(context);
		sqlLocal.clearCache();
	}

	public void secondLogin(String tno) {
		webTool.getKQTBbyTno(tno);
		webTool.getAllJDTBbyTno(tno);
		webTool.QuerTeachTaskByTno(tno);
	}
	/**
	 * ��������ύ���ڽ�����Զ����������ݿ������δ�ύ�Ŀ��ڽ��
	 */
	public void TJKQresult(int jno) {
			webTool.TJKQresult(jno);
			
	}
}
