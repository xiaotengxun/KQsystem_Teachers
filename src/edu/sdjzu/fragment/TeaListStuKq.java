package edu.sdjzu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.adapter.KqOrderAdapter;
import edu.sdjzu.kqsystem_teacher.TeaIndexAct;
import edu.sdjzu.kqsystem_teacher.TeaStuOrderAct;
import edu.sdjzu.kqsystem_teacher.TeaStuOrderAct.onListClassListItemClick;
import edu.sdjzu.teatools.TeaTool;

public class TeaListStuKq extends Fragment implements onListClassListItemClick {
	private List<HashMap<String, String>> listHash = new ArrayList<HashMap<String, String>>();
	private ListView stuListView;
	private TeaTool loginClass;
	private KqOrderAdapter kqOrderAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	private void initView() {
		loginClass = new TeaTool(getActivity());
		// listHash = loginClass.getStuList(TeaIndexAct.jno);
		listHash = TeaStuOrderAct.listHash;
		stuListView = (ListView) getView().findViewById(R.id.tea_kq_listview);
		kqOrderAdapter = new KqOrderAdapter(getActivity(), listHash);
		stuListView.setAdapter(kqOrderAdapter);
		TeaStuOrderAct.setListClassItemOnClickListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.act_kq_stulist, null);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (hidden == false) {
			kqOrderAdapter.setStuList(listHash);
			kqOrderAdapter.notifyDataSetChanged();
		}
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initView();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void choseClassList(String cla) {
		listHash = TeaStuOrderAct.listHash;
		kqOrderAdapter.setStuList(listHash);
		kqOrderAdapter.notifyDataSetChanged();
	}

}
