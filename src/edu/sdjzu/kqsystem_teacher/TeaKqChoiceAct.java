package edu.sdjzu.kqsystem_teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.attr.TeacherAttr;
import edu.sdjzu.fragment.TeaKqChoiceOption;
import edu.sdjzu.fragment.TeaKqChoiceOption.OnLook;
import edu.sdjzu.fragment.TeaKqLook;
import edu.sdjzu.teatools.TeaTool;

public class TeaKqChoiceAct extends FragmentActivity implements OnLook {
	private Fragment choiceFrag;
	private Fragment lookFrag, buluFrag;
	private String choiceType = "";
	private TeaTool loginClass;
	private List<HashMap<String, String>> listHash;
	private Handler mHandler;
	private final int KQ_BULU = 0;
	private final int KQ_LOOK = 1;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_kq_extra_home);
		initActionBar();
		initData();
		initView();
	}

	private void updateActionBar(int chose) {
		View view = getLayoutInflater().inflate(R.layout.actionbar_customer, null);
		TextView tv = (TextView) view.findViewById(R.id.actionbar_center_tv);
		if (chose == KQ_BULU) {
			tv.setText(getString(R.string.home_kq_bulu));
		} else if (chose == KQ_LOOK) {
			tv.setText(getString(R.string.home_kq_lookback));
		}
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		actionBar.setCustomView(view, lp);
	}

	private ActionBar actionBar;

	private void initActionBar() {
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setLogo(R.drawable.manager_kqresult_jump);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setTitle("");

	}

	private void initData() {
		choiceType = getIntent().getStringExtra(TeacherAttr.kqExtraChoice);
		loginClass = new TeaTool(this);
		listHash = new ArrayList<HashMap<String, String>>();
		if (choiceType.equals(TeacherAttr.kqLookBackKey)) {
			updateActionBar(KQ_LOOK);
		} else if (choiceType.equals(TeacherAttr.kqBuluKey)) {
			updateActionBar(KQ_BULU);
		}
	}

	private void initView() {
		choiceFrag = new TeaKqChoiceOption();
		((TeaKqChoiceOption) choiceFrag).setOnLook(this);
		lookFrag = new TeaKqLook();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		Bundle b = new Bundle();
		b.putString(TeacherAttr.kqExtraChoice, choiceType);
		choiceFrag.setArguments(b);
		transaction.replace(R.id.kq_extra_home_fram, choiceFrag);
		transaction.commit();
	}

	@Override
	public void showLook(String course, String cla, String week, String claTime, String titleType) {
		//
		if (titleType.equals(TeacherAttr.kqLookBackKey)) {
			goLookFrag(course, cla, week, claTime);
		} else {
			goBuluFrag(course, week, claTime, cla);
		}
	}

	private void goLookFrag(String course, String cla, String week, String claTime) {
		listHash = loginClass.getLookKq(course, cla, week, claTime);
		lookFrag = new TeaKqLook();
		week = String.format(getString(R.string.tea_look_weekformat), week);
		ArrayList<String> sList = new ArrayList<String>();
		sList.add(course);
		sList.add(week);
		sList.add(claTime);
		sList.add(cla);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		Bundle b = new Bundle();
		b.putSerializable(TeacherAttr.kqLookDataKey, (Serializable) listHash);
		b.putStringArrayList(TeacherAttr.kqLookTitle, sList);
		lookFrag.setArguments(b);
		transaction.replace(R.id.kq_extra_home_fram, lookFrag);
		transaction.commit();
	}

	private void goBuluFrag(String course, String week, String claTime, String cla) {
		HashMap<String, String> hash = loginClass.getBuluJnoRno(course, week, claTime);
		if (loginClass.isAllowKQByJno(Integer.valueOf(hash.get(TeacherAttr.jnoKey)))
				&& !loginClass.isJnoSubmit(Integer.valueOf(hash.get(TeacherAttr.jnoKey)))) {
			Intent intent = new Intent();
			intent.putExtra(TeacherAttr.jnoKey, hash.get(TeacherAttr.jnoKey));
			intent.putExtra(TeacherAttr.rnoKey, hash.get(TeacherAttr.rnoKey));
			intent.putExtra(TeacherAttr.buluClassKey, cla);
			intent.setClass(TeaKqChoiceAct.this, TeaStuOrderAct.class);
			startActivity(intent);
		} else {
			Toast.makeText(TeaKqChoiceAct.this, getString(R.string.KQ_bulu_not_allow_tip1), 2000).show();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
