package edu.sdjzu.kqsystem_teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.attr.TeacherAttr;
import edu.sdjzu.fragment.TeaKqChoiceOption;
import edu.sdjzu.fragment.TeaKqChoiceOption.OnLook;
import edu.sdjzu.fragment.TeaKqLook;
import edu.sdjzu.localtool.InternetStatus;
import edu.sdjzu.teatools.TeaLoginTool;
import edu.sdjzu.teatools.TeaTool;

public class TeaKqChoiceAct extends FragmentActivity implements OnLook {
	private Fragment choiceFrag;
	private Fragment lookFrag, buluFrag;
	private String choiceType = "";
	private TeaTool loginClass;
	private List<HashMap<String, String>> listHash;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_kq_extra_home);
		initData();
		initView();
		initActionBar();
	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setLogo(R.drawable.manager_kqresult_jump);
		actionBar.setTitle("");
	}

	private void initData() {
		choiceType = getIntent().getStringExtra(TeacherAttr.kqExtraChoice);
		loginClass = new TeaTool(this);
		listHash = new ArrayList<HashMap<String, String>>();
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
		Intent intent = new Intent();
		intent.putExtra(TeacherAttr.jnoKey, hash.get(TeacherAttr.jnoKey));
		intent.putExtra(TeacherAttr.rnoKey, hash.get(TeacherAttr.rnoKey));
		intent.putExtra(TeacherAttr.buluClassKey, cla);
		intent.setClass(TeaKqChoiceAct.this, TeaStuOrderAct.class);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
