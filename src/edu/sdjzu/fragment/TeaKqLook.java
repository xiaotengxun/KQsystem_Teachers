package edu.sdjzu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.adapter.KqLookAdapter;
import edu.sdjzu.attr.TeacherAttr;

public class TeaKqLook extends Fragment {
	private ListView lookListView;
	private KqLookAdapter kqLookAdapter;
	private List<HashMap<String, String>> listHash = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> listTitle = new ArrayList<String>();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initView();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.act_kq_look, null);
	}

	private void initView() {
		lookListView = (ListView) getView().findViewById(R.id.look_listview);
		kqLookAdapter = new KqLookAdapter(getActivity(), listHash);
		lookListView.setAdapter(kqLookAdapter);
		if (listTitle.size() > 0) {
			((TextView) getView().findViewById(R.id.kq_look_course)).setText(listTitle.get(0));
			((TextView) getView().findViewById(R.id.kq_look_week)).setText(listTitle.get(1));
			((TextView) getView().findViewById(R.id.kq_look_classtime)).setText(listTitle.get(2));
			((TextView) getView().findViewById(R.id.kq_look_class)).setText(listTitle.get(3));
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		listHash = (List<HashMap<String, String>>) getArguments().getSerializable(TeacherAttr.kqLookDataKey);
		listTitle = getArguments().getStringArrayList(TeacherAttr.kqLookTitle);
		super.onCreate(savedInstanceState);
	}

}
