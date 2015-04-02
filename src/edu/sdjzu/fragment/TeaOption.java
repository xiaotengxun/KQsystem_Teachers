package edu.sdjzu.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.attr.TeacherAttr;
import edu.sdjzu.kqsystem_teacher.TeaIndexAct;
import edu.sdjzu.kqsystem_teacher.TeaKqChoiceAct;
import edu.sdjzu.kqsystem_teacher.TeaStuOrderAct;
import edu.sdjzu.teatools.TeaTool;

public class TeaOption extends Fragment {
	private String userName = "";
	Button btnLookBack;
	Button btnBulu;
	Button btnOrder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.act_kq_home, null);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView() {
		btnLookBack = (Button) getView().findViewById(R.id.home_kq_lookback);
		btnBulu = (Button) getView().findViewById(R.id.home_kq_bulu);
		btnOrder = (Button) getView().findViewById(R.id.home_kq_order);
		btnLookBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), TeaKqChoiceAct.class);
				intent.putExtra(TeacherAttr.kqExtraChoice, TeacherAttr.kqLookBackKey);
				startActivity(intent);
			}
		});
		btnBulu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), TeaKqChoiceAct.class);
				intent.putExtra(TeacherAttr.kqExtraChoice, TeacherAttr.kqBuluKey);
				startActivity(intent);
			}
		});
		btnOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isClassNow();
			}
		});
	}

	private void isClassNow() {
		TeaTool lgClass = new TeaTool(getActivity());
		int[] jrno = lgClass.getCurrentProgress();
		if (jrno[0] != -1) {
			int jno = jrno[0];
			int rno = jrno[1];
			Intent intent = new Intent();
			intent.setClass(getActivity(), TeaStuOrderAct.class);
			intent.putExtra(TeacherAttr.userKey, userName);
			intent.putExtra(TeacherAttr.kqExtraChoice, TeacherAttr.kqOrderKey);
			intent.putExtra(TeacherAttr.jnoKey, String.valueOf(jno));
			intent.putExtra(TeacherAttr.rnoKey, String.valueOf(rno));
			startActivity(intent);
		} else {
			Toast.makeText(getActivity(), getString(R.string.forclass_tip), 2000).show();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		userName = (String) getArguments().get(TeacherAttr.userKey);
		super.onAttach(activity);
	}

}
