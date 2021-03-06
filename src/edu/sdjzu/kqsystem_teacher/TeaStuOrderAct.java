package edu.sdjzu.kqsystem_teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.attr.TeacherAttr;
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
	private static onFilterChoseListStuListener filterChoseListStuListener = null;
	private static onFilterChosePicStuListener filterChosePicStuListener = null;
	private RadioButton radioList, radioPic;
	private final String stuListKey = "stuListKey", stuPicKey = "stuPicKey";
	private android.support.v4.app.Fragment stuListFrag, stuPicFrag;
	public static int currentJno = -1;
	public static int currentRno = -1;
	private String currentClass;// 补录考勤的班级
	private MyNetErrorReceiver netErrorReceiver = null;
	private MyNetSubmitKQReceiver netSubmitKQReceiver = null;
	private ProgressDialog progressDialog;// 考勤提交时的进度条
	private Handler mHandler;
	private final int PROGRESS_CANCEL = 0;
	private PopupWindow popWindowFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_kq_order);
		currentJno = Integer.valueOf(getIntent().getStringExtra(TeacherAttr.jnoKey));
		currentRno = Integer.valueOf(getIntent().getStringExtra(TeacherAttr.rnoKey));
		currentClass = getIntent().getStringExtra(TeacherAttr.buluClassKey);
		initStuInfo();
		initView();
		setListener();
		if (currentClass == null) {
			currentClass = getString(R.string.tea_class_all);
		} else {
			// 非正常考勤情况下根据班级获得相应的学生
		}
		listHash = loginClass.getStuListByClass(currentClass, currentJno, filterType);
		initActionBar();
		registerReceiver();
		initProgressDialog();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case PROGRESS_CANCEL:
					if (null != progressDialog) {
						progressDialog.dismiss();
						progressDialog = null;
					}
					break;
				}
			}
		};

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
		RelativeLayout filterBtn = (RelativeLayout) view.findViewById(R.id.kq_order_filter);
		filterBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!popWindowFilter.isShowing()) {
					popWindowFilter.showAtLocation(anchor, Gravity.BOTTOM, 0, 0);
				}
			}
		});
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
		View view = getLayoutInflater().inflate(R.layout.kq_filter, null);
		popWindowFilter = new PopupWindow(view, android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		anchor = findViewById(R.id.filter_container);
		popWindowFilter.setAnimationStyle(R.style.PopupAnimation);
		filterDefault = (CheckBox) view.findViewById(R.id.kq_order_filter_default);
		filterUp = (CheckBox) view.findViewById(R.id.kq_order_filter_up);
		filterDown = (CheckBox) view.findViewById(R.id.kq_order_filter_down);
		filterBtnFinish = (Button) view.findViewById(R.id.kq_order_filter_finish);
		filterBtnFinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popWindowFilter.dismiss();
				if (lastFilterType != filterType) {
					listHash = loginClass.getStuListByClass(currentClass, currentJno, filterType);
					if (null != filterChoseListStuListener) {
						filterChoseListStuListener.filterChosen(filterType);
						lastFilterType = filterType;
					}
					if (null != filterChosePicStuListener) {
						filterChosePicStuListener.filterChosen(filterType);
						lastFilterType = filterType;
					}
				}
			}
		});
		filterDefault.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				filterDefault.setChecked(true);
				filterDown.setChecked(false);
				filterUp.setChecked(false);
				filterType = 0;
			}
		});
		filterDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				filterDefault.setChecked(false);
				filterDown.setChecked(true);
				filterUp.setChecked(false);
				filterType = 2;
			}
		});
		filterUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				filterDefault.setChecked(false);
				filterDown.setChecked(false);
				filterUp.setChecked(true);
				filterType = 1;
			}
		});
	}

	private int lastFilterType = 0;
	private int filterType = 0;// 筛选类型，0：默认缺勤排序，1：增序，2：降序
	private View anchor;
	private CheckBox filterDefault = null;
	private CheckBox filterUp = null;
	private CheckBox filterDown = null;
	private Button filterBtnFinish = null;

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
				listHash = loginClass.getStuListByClass(cla, currentJno, filterType);
				if (classListClick != null) {
					classListClick.choseClassList(cla);
				}
				if (classPicClick != null) {
					classPicClick.choseClassPic(cla);
				}
			}
		});
	}

	public static void setOnFilterChoseListStuListener(onFilterChoseListStuListener listen) {
		filterChoseListStuListener = listen;
	}

	public static interface onFilterChoseListStuListener {
		public void filterChosen(int type);
	};

	public static void setOnFilterChoseListPicListener(onFilterChosePicStuListener listen) {
		filterChosePicStuListener = listen;
	}

	public static interface onFilterChosePicStuListener {
		public void filterChosen(int type);
	};

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
			if (null == progressDialog) {
				initProgressDialog();
			}
			progressDialog.show();
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
			mHandler.sendEmptyMessage(PROGRESS_CANCEL);
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
