package edu.sdjzu.kqsystem_teacher;

import java.io.Serializable;
import java.util.List;

import com.example.kqsystem_teachers.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import edu.sdjzu.attr.TeacherAttr;
import edu.sdjzu.model.TeachProgress;
import edu.sdjzu.service.NetService;
import edu.sdjzu.teatools.TeaTool;

public class LoginAct extends Activity {
	public static String userName = "";
	private EditText userNameTV = null;// �û��������
	private EditText userPassTV = null;// ���������
	private CheckBox remPassCB = null;// �����ס��
	private CheckBox autoLoginCB = null;// �Զ���½��
	private Button loginButton = null;// ��½��ť
	private Handler mHandler;
	private final static int LOGIN_FAILED = 1;
	private final static int LOGIN_SUCESS = 2;
	private final static int DIALOG_START = 3;
	private ProgressDialog progressDialog;
	private SharedPreferences sp;
	private Thread loginThread;
	private String name = "";
	private String pass = "";
	private TeaTool loginClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginClass = new TeaTool(LoginAct.this);
		sp = getSharedPreferences(TeacherAttr.sharePrefenceName, 0);
		progressDialog = new ProgressDialog(LoginAct.this);
		progressDialog.setTitle(getString(R.string.login_progress_tip1));
		progressDialog.setMessage(getString(R.string.login_progress_tip2));
		progressDialog.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
		initLayoutView();
		initLoginThread();
		getLastUser();
		loginAuto();
	}

	private void initLayoutView() {
		userNameTV = (EditText) this.findViewById(R.id.Login_UserName);
		userPassTV = (EditText) this.findViewById(R.id.Login_UserPass);
		remPassCB = (CheckBox) this.findViewById(R.id.Login_RemPass);
		autoLoginCB = (CheckBox) this.findViewById(R.id.Login_AutoLogin);
		loginButton = (Button) this.findViewById(R.id.Login_Button);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setClickable(false);
				loginBtnClick();
			}
		});
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				progressDialog.dismiss();
				switch (msg.what) {
				case LOGIN_FAILED:
					loginButton.setClickable(true);
					Toast.makeText(LoginAct.this, getString(R.string.login_error_tip2), 2000).show();
					break;
				case LOGIN_SUCESS:
					remenberPass();
					remenberAuto();
					loginButton.setClickable(true);
					userName = name;
					moveToAct();
					break;
				case DIALOG_START:
					progressDialog.show();
					break;
				}
				super.handleMessage(msg);
			}

		};

	}

	private void moveToAct() {
		Intent intent = new Intent();
		List<TeachProgress> list = loginClass.existSavedTProgress();
		if (list.size() > 0) {
			intent.putExtra(TeacherAttr.progressSavedKey, (Serializable) list);
			intent.setClass(LoginAct.this, TeaJDTBTipAct.class);
		} else {
			intent.setClass(LoginAct.this, TeaIndexAct.class);
		}
		startActivity(intent);
		LoginAct.this.finish();
	}

	private void StartService() {
		Intent intent = new Intent();
		intent.setClass(this, NetService.class);
		startService(intent);
	}

	private void initLoginThread() {
		loginThread = null;
		loginThread = new Thread() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(DIALOG_START);
				boolean bLogin = loginClass.LoginStartActivity(name, pass, sp);
				if (bLogin == true) {
					writePassAuto();
					mHandler.sendEmptyMessage(LOGIN_SUCESS);
				} else {
					mHandler.sendEmptyMessage(LOGIN_FAILED);
				}
			}
		};
	}

	private void loginBtnClick() {
		initLoginThread();
		name = userNameTV.getText().toString();
		pass = userPassTV.getText().toString();
		if (name == null || pass == null) {
			Toast.makeText(LoginAct.this, getString(R.string.login_error_tip1), 2000).show();
			mHandler.sendEmptyMessage(LOGIN_FAILED);
			return;
		}
		if (name.equals("") || pass.equals("")) {
			Toast.makeText(LoginAct.this, getString(R.string.login_error_tip1), 2000).show();
			mHandler.sendEmptyMessage(LOGIN_FAILED);
			return;
		}

		loginThread.start();
	}

	// �����һ�ε��û��������롢�Ƿ��Զ���½���Ƿ��ס����
	private void getLastUser() {
		if (sp.getBoolean(TeacherAttr.loginRemenberPassKey, false)) {
			remPassCB.setChecked(true);
			userNameTV.setText(sp.getString(TeacherAttr.loginUserName, ""));
			userPassTV.setText(sp.getString(TeacherAttr.loginUserPass, ""));
			name = userNameTV.getText().toString();
			pass = userPassTV.getText().toString();
		} else {
			remPassCB.setChecked(false);
		}
		if (sp.getBoolean(TeacherAttr.loginRemenberAutoKey, false)) {
			autoLoginCB.setChecked(true);
		} else {
			autoLoginCB.setChecked(false);
		}
	}

	// ������һ�ε�״̬�ж��Ƿ�Ҫ�Զ���½
	private void loginAuto() {
		if (remPassCB.isChecked() && autoLoginCB.isChecked()) {
			loginButton.setClickable(false);
			initLoginThread();
			loginThread.start();
		}
	}

	// ��½ʱ��¼ɾ���û���������
	private void writePassAuto() {
		if (remPassCB.isChecked()) {
			sp.edit().putString(TeacherAttr.loginUserName, userNameTV.getText().toString()).commit();
			sp.edit().putString(TeacherAttr.loginUserPass, userPassTV.getText().toString()).commit();
		} else {
			sp.edit().putString(TeacherAttr.loginUserName, "").commit();
			sp.edit().putString(TeacherAttr.loginUserPass, "").commit();
		}
	}

	// ��ѡ�������
	private void remenberPass() {
		if (remPassCB.isChecked()) {
			sp.edit().putBoolean(TeacherAttr.loginRemenberPassKey, true).commit();
		} else {
			sp.edit().putBoolean(TeacherAttr.loginRemenberPassKey, false).commit();
			sp.edit().putBoolean(TeacherAttr.loginRemenberAutoKey, false).commit();
			autoLoginCB.setChecked(false);
		}
	}

	// ��ѡ�Զ���½����
	private void remenberAuto() {
		if (autoLoginCB.isChecked()) {
			sp.edit().putBoolean(TeacherAttr.loginRemenberAutoKey, true).commit();
		} else {
			sp.edit().putBoolean(TeacherAttr.loginRemenberAutoKey, false);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}
