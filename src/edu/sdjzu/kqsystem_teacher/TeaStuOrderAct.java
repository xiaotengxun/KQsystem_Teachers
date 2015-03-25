package edu.sdjzu.kqsystem_teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.attr.TeacherAttr;
import edu.sdjzu.broadcast.NetErrorReceiver;
import edu.sdjzu.broadcast.NetSubmitKQReceiver;
import edu.sdjzu.fragment.TeaListStuKq;
import edu.sdjzu.fragment.TeaPicStuKq;
import edu.sdjzu.localtool.InternetStatus;
import edu.sdjzu.teatools.TeaLoginTool;
import edu.sdjzu.teatools.TeaTool;

/**
 * 学生课堂考勤
 * 
 * @author Administrator
 *
 */
public class TeaStuOrderAct extends FragmentActivity {
	public static List<HashMap<String, String>> listHash = new ArrayList<HashMap<String, String>>();
	private ListView slideListView;
	private TeaTool loginClass;
	private static onListClassListItemClick classListClick = null;
	private static onPicClassListItemClick classPicClick = null;
	private RadioButton radioList, radioPic;
	private final String stuListKey = "stuListKey", stuPicKey = "stuPicKey";
	private android.support.v4.app.Fragment stuListFrag, stuPicFrag;
	public static int currentJno = -1;
	public static int currentRno = -1;
	private String currentClass;// 补录考勤的班级
	private MyNetErrorReceiver netErrorReceiver = null;
	private MyNetSubmitKQReceiver netSubmitKQReceiver = null;
	private boolean isNormalKq = true;// 是否是正常考勤，而不是考勤查看或考勤补录，默认是正常考勤
	private ProgressDialog progressDialog;// 考勤提交时的进度条

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_kq_order);
		currentJno = Integer.valueOf(getIntent().getStringExtra(TeacherAttr.jnoKey));
		currentRno = Integer.valueOf(getIntent().getStringExtra(TeacherAttr.rnoKey));
		currentClass = getIntent().getStringExtra(TeacherAttr.buluClassKey);
		Log.i("chen", "currentJno and currentRno=" + currentJno + "      " + currentRno);
		initStuInfo();
		initView();
		setListener();
		if (currentClass == null) {
			Log.i("chen", "class null");
			isNormalKq = true;
		} else {
			// 非正常考勤情况下根据班级获得相应的学生
			isNormalKq = false;
			listHash = loginClass.getStuListByClass(currentClass, currentJno, isNormalKq);
		}
		initActionBar();
		registerReceiver();
		initProgressDialog();
	}

	private void initProgressDialog() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getString(R.string.kq_submit_title));
		progressDialog.setMessage(getString(R.string.kq_submiting));
		progressDialog.setCanceledOnTouchOutside(false);
	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setLogo(R.drawable.manager_kqresult_jump);
		actionBar.setTitle("");
		View view = getLayoutInflater().inflate(R.layout.kq_order_mode, null);
		ActionBar.LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		actionBar.setCustomView(view, lp);
		actionBar.setDisplayShowCustomEnabled(true);
		radioList = (RadioButton) view.findViewById(R.id.kq_order_mode_list);
		radioPic = (RadioButton) view.findViewById(R.id.kq_order_mode_pic);
		radioList.setOnClickListener(radioCheckListener);
		radioPic.setOnClickListener(radioCheckListener);
	}

	private void initView() {
		slideListView = (ListView) findViewById(R.id.left_drawer);
		TeaTool lc = new TeaTool(this);
		String[] stuClass = lc.getStuClass();
		slideListView.setAdapter(new ArrayAdapter<String>(this, R.layout.kq_left_slide_view_item, stuClass));
		stuListFrag = new TeaListStuKq();
		stuPicFrag = new TeaPicStuKq();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		Bundle args = new Bundle();
		args.putString(TeacherAttr.userKey, LoginAct.userName);
		stuListFrag.setArguments(args);
		stuPicFrag.setArguments(args);
		transaction.add(R.id.kq_container, stuListFrag, stuListKey);
		transaction.add(R.id.kq_container, stuPicFrag, stuPicKey);
		transaction.hide(stuPicFrag);
		transaction.commit();
	}

	/**
	 * 正常考勤情况下获得老师所交的所有学生
	 */
	private void initStuInfo() {
		loginClass = new TeaTool(this);
		listHash = loginClass.getStuList(currentJno);
	}

	private void registerReceiver() {
		netErrorReceiver = new MyNetErrorReceiver();
		registerReceiver(netErrorReceiver, new IntentFilter(getString(R.string.network_error_action)));
		netSubmitKQReceiver = new MyNetSubmitKQReceiver();
		registerReceiver(netSubmitKQReceiver, new IntentFilter(getString(R.string.net_submit_kq_action)));
	}

	private void setListener() {
		slideListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String cla = ((TextView) arg1).getText().toString();
				listHash = loginClass.getStuListByClass(cla, currentJno, isNormalKq);
				if (classListClick != null) {
					classListClick.choseClassList(cla);
				}
				if (classPicClick != null) {
					classPicClick.choseClassPic(cla);
				}
			}
		});
	}

	public static void setListClassItemOnClickListener(onListClassListItemClick c) {
		classListClick = c;
	}

	public static interface onListClassListItemClick {
		public void choseClassList(String cla);
	}

	public static void setPicClassItemOnClickListener(onPicClassListItemClick c) {
		classPicClick = c;
	}

	public static interface onPicClassListItemClick {
		public void choseClassPic(String cla);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		} else if (item.getItemId() == R.id.kq_order_save) {
			InternetStatus net = new InternetStatus(TeaStuOrderAct.this);
			if (null != progressDialog) {
				progressDialog.show();
			}
			new Thread() {
				@Override
				public void run() {
					TeaLoginTool web = new TeaLoginTool(TeaStuOrderAct.this);
					web.TJKQresult(currentJno);
				}
			}.start();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onPrepareOptionsMenu(menu);
	}

	private OnClickListener radioCheckListener = new OnClickListener() {

		@Override
		public void onClick(View buttonView) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			android.support.v4.app.Fragment fList = getSupportFragmentManager().findFragmentByTag(stuListKey);
			android.support.v4.app.Fragment fPic = getSupportFragmentManager().findFragmentByTag(stuPicKey);
			transaction.hide(fPic);
			transaction.hide(fList);
			if (buttonView.getId() == R.id.kq_order_mode_list) {
				transaction.show(fList);
			} else if (buttonView.getId() == R.id.kq_order_mode_pic) {
				transaction.show(fPic);
			}
			transaction.commit();
		}
	};

	public class MyNetSubmitKQReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int choice = intent.getIntExtra("toast", -1);
			Log.i("chen", "choice=" + choice);
			if (null != progressDialog) {
				progressDialog.dismiss();
			}
			if (choice == 0) {
				Toast.makeText(context, context.getString(R.string.net_submit_kq_nokq), 2000).show();
			} else if (choice == 1) {
				Toast.makeText(context, context.getString(R.string.net_submit_kq_success), 2000).show();
			}
		}

	}

	public class MyNetErrorReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (null != progressDialog) {
				progressDialog.dismiss();
			}
			Toast.makeText(context, context.getString(R.string.network_error_update_kq_tip), 2000).show();
		}

	}

	@Override
	protected void onPause() {
		if (netErrorReceiver != null) {
			unregisterReceiver(netErrorReceiver);
			netErrorReceiver = null;
		}
		if (netSubmitKQReceiver != null) {
			unregisterReceiver(netSubmitKQReceiver);
			netSubmitKQReceiver = null;

		}
		super.onPause();
	}

}
