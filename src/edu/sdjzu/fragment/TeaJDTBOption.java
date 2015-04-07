package edu.sdjzu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.adapter.JDTBAdapter;
import edu.sdjzu.adapter.JDTBAdapter.DeleteJDTBOnClick;
import edu.sdjzu.kqsystem_teacher.TeaIndexAct;
import edu.sdjzu.model.TeachProgress;
import edu.sdjzu.teatools.TeaTool;

public class TeaJDTBOption extends Fragment {
	private ListView tipListView;
	private Button delBtn, cancelBtn, ignoreBtn, submitBtn;
	private JDTBAdapter jdtbAdapter;
	private List<HashMap<String, String>> listM;
	private List<TeachProgress> proList = new ArrayList<TeachProgress>();
	private Activity act;
	private List<Integer> listJno = new ArrayList<Integer>();
	private TeaTool tool;
	private ProgressDialog progressDialog;
	private TJKQSuccessReceiver tjKQSuccessReceiver;

	public TeaJDTBOption(List<TeachProgress> ls) {
		this.proList = ls;
	}

	private void initProgressDialog() {
		progressDialog = new ProgressDialog(act);
		progressDialog.setTitle(getString(R.string.kq_submit_title));
		progressDialog.setMessage(getString(R.string.kq_submiting));
		progressDialog.setCanceledOnTouchOutside(false);
	}

	private void initReceiver() {
		tjKQSuccessReceiver = new TJKQSuccessReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(getString(R.string.net_submit_kq_action));
		filter.addAction(getString(R.string.network_error_action));
		act.registerReceiver(tjKQSuccessReceiver, filter);
	}

	private void initJdtb() {
		listM = new ArrayList<HashMap<String, String>>();
		for (TeachProgress tp : proList) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(JDTBAdapter.jdtbKey.classTime, tp.getProgressJTime());
			map.put(JDTBAdapter.jdtbKey.courseName, tp.getCourseName());
			map.put(JDTBAdapter.jdtbKey.week, tp.getProgressWeek());
			map.put(JDTBAdapter.jdtbKey.jno, String.valueOf(tp.getProgressNo()));
			listM.add(map);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		act = getActivity();
		tool = new TeaTool(act);
		initJdtb();
		initLayout();
		initProgressDialog();
		initReceiver();
		super.onActivityCreated(savedInstanceState);
	}

	private void initLayout() {
		tipListView = (ListView) getView().findViewById(R.id.jdtb_notsaved_listview);
		delBtn = (Button) getView().findViewById(R.id.jdtb_notsaved_sure);
		cancelBtn = (Button) getView().findViewById(R.id.jdtb_notsaved_cancel);
		ignoreBtn = (Button) getView().findViewById(R.id.jdtb_notsaved_ignore);
		submitBtn = (Button) getView().findViewById(R.id.jdtb_notsaved_submit);
		jdtbAdapter = new JDTBAdapter(getActivity(), listM);
		tipListView.setAdapter(jdtbAdapter);
		jdtbAdapter.setDeleteJDTBOnClickListener(new DeleteJDTBOnClick() {
			@Override
			public void deleteOnClick(List<Integer> list) {
				listJno = list;
				delBtn.setVisibility(View.VISIBLE);
				cancelBtn.setVisibility(View.VISIBLE);
				submitBtn.setVisibility(View.VISIBLE);
				ignoreBtn.setVisibility(View.GONE);
			}
		});
		delBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				tool.deleteLocalProgress(listJno);
				updateBtn();
				proList = tool.existSavedTProgress();
				initJdtb();
				jdtbAdapter.setData(listM);
				jdtbAdapter.notifyDataSetChanged();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateBtn();
			}
		});
		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != progressDialog && listJno.size() > 0) {
					progressDialog.show();
				}
				if (listJno.size() > 0) {
					new Thread() {
						public void run() {
							tool.TJKQresultLocal(listJno);
						};
					}.start();
					updateBtn();
				}
			}
		});
		ignoreBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(act, TeaIndexAct.class);
				act.startActivity(intent);
				act.finish();
			}
		});
	}

	private void updateBtn() {
		jdtbAdapter.setDeleteArror(false);
		jdtbAdapter.notifyDataSetChanged();
		delBtn.setVisibility(View.GONE);
		cancelBtn.setVisibility(View.GONE);
		submitBtn.setVisibility(View.GONE);
		ignoreBtn.setVisibility(View.VISIBLE);
		listJno = new ArrayList<Integer>();
	}

	public void Vibrate(final Activity activity, long milliseconds) {
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.act_jdtb_saved, null);
	}

	class TJKQSuccessReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (null != progressDialog) {
				progressDialog.dismiss();
			}
		}
	}

	@Override
	public void onDestroy() {
		if (null != tjKQSuccessReceiver) {
			act.unregisterReceiver(tjKQSuccessReceiver);
			tjKQSuccessReceiver = null;
		}
		super.onDestroy();
	};

}
