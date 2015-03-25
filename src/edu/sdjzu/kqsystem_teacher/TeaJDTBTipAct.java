package edu.sdjzu.kqsystem_teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.adapter.JDTBAdapter;
import edu.sdjzu.adapter.JDTBAdapter.JDTBDetailOnClick;
import edu.sdjzu.attr.TeacherAttr;
import edu.sdjzu.fragment.TeaJDTBOption;
import edu.sdjzu.fragment.TeaJDTBOptionDetail;
import edu.sdjzu.localtool.InternetStatus;
import edu.sdjzu.model.TeachProgress;
import edu.sdjzu.teatools.TeaLoginTool;
import edu.sdjzu.teatools.TeaTool;

public class TeaJDTBTipAct extends FragmentActivity implements JDTBDetailOnClick {
	private List<TeachProgress> proList = new ArrayList<TeachProgress>();
	private Fragment teaJDTBOption;
	private Fragment teaJDTBDetail;
	private TeaTool loginClass;
	private FragmentManager fm;
	private AlertDialog.Builder dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_jdtb);
		JDTBAdapter.setJDTBDetailOnClick(this);
		initActionBar();
		initJdtb();
		initFrag();
	}

	private void showDialog() {
		dialog = new AlertDialog.Builder(this);
		dialog.setPositiveButton(getString(R.string.btn_sure), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				TeaJDTBTipAct.this.finish();
			}
		});
		dialog.setNegativeButton(getString(R.string.btn_cancel), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.setTitle(getString(R.string.exit_tip));
		dialog.show();
	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("");
	}

	private void showBackKey() {
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(R.drawable.manager_kqresult_jump);
	}

	private void hideBackKey() {
		getActionBar().setHomeButtonEnabled(false);
		getActionBar().setLogo(R.drawable.ic_launcher);
	}

	private void initJdtb() {
		loginClass = new TeaTool(this);
		proList = (List<TeachProgress>) getIntent().getSerializableExtra(TeacherAttr.progressSavedKey);
	}

	private void initFrag() {
		fm = getSupportFragmentManager();
		teaJDTBDetail = new TeaJDTBOptionDetail();
		teaJDTBOption = new TeaJDTBOption(proList);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.jdtb_frag, teaJDTBOption);
		// transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void getProgressNo(int jno, String courseName, String courseWeek, String courseJtime) {
		goJDTBDetail(jno, courseName, courseWeek, courseJtime);

	}

	private void goJDTBDetail(int jno, String courseName, String courseWeek, String courseJtime) {
		showBackKey();
		FragmentTransaction transaction = fm.beginTransaction();
		Bundle b = new Bundle();
		b.putSerializable(TeacherAttr.jdtbKQStuKey, (Serializable) loginClass.getKQStuByJno(jno));
		b.putString(TeacherAttr.jnoKey, String.valueOf(jno));
		b.putString(TeacherAttr.jdtbCourseNameKey, courseName);
		b.putString(TeacherAttr.jdtbCourseWeekKey, courseWeek);
		b.putString(TeacherAttr.jdtbCourseClassTimeKey, courseJtime);
		teaJDTBDetail.setArguments(b);
		transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
		transaction.replace(R.id.jdtb_frag, teaJDTBDetail);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (event.getAction() == event.ACTION_UP && event.getKeyCode() == event.KEYCODE_BACK) {
			backKeyAction();
		}
		return true;
	}

	private void backKeyAction() {
		if (fm.getBackStackEntryCount() > 0) {
			hideBackKey();
			fm.popBackStack();
		} else {
			showDialog();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			backKeyAction();
		}
		return super.onOptionsItemSelected(item);
	}

}
