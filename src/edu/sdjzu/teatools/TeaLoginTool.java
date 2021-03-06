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
	private String tag = "chen";

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
		webTool.getStuPicZipByTno(tno);
		// webTool.getStuPicAllByTno(tno);

	}

	/**
	 * 其它用户登录时清除之前用户的所有信息
	 */
	public void clearCache() {
		LocalSqlTool sqlLocal = new LocalSqlTool(context);
		sqlLocal.clearCache();
	}

	public void secondLogin(String tno) {
		webTool.getKQTBbyTno(tno);
		webTool.getAllJDTBbyTno(tno);
		webTool.QuerTeachTaskByTno(tno);
	}

	/**
	 * 向服务器提交考勤结果，自动到本地数据库里查找未提交的考勤结果
	 */
	public void TJKQresult(int jno) {
		webTool.TJKQresult(jno);

	}
}
