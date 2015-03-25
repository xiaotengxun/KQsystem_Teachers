package edu.sdjzu.kqsystem_teacher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.attr.TeacherAttr;

public class SpashAct extends Activity {
	private Runnable delayRun;
	private Handler handler = null;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		delayRun = new Runnable() {
			@Override
			public void run() {
				Intent mainIntent = new Intent(SpashAct.this, LoginAct.class);
				SpashAct.this.startActivity(mainIntent);
				handler.removeCallbacks(delayRun);
				SpashAct.this.finish();
			}
		};
		handler = new Handler();
		handler.postDelayed(delayRun, TeacherAttr.SPASH_TIME);
	}

	private void test() {
		Intent email = new Intent(android.content.Intent.ACTION_SEND);
		email.setType("text/plain");
		// 设置邮件默认地址
		// email.putExtra(android.content.Intent.EXTRA_EMAIL, "1");
		// 设置邮件默认标题
		email.putExtra(android.content.Intent.EXTRA_SUBJECT, "我是邮件的标题");
		// 设置要默认发送的内容
		email.putExtra(android.content.Intent.EXTRA_TEXT, "我是分享的内容");
		// 调用系统的邮件系统
		startActivity(Intent.createChooser(email, "分享方式"));
	}

	@Override
	protected void onPause() {
		finish();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
