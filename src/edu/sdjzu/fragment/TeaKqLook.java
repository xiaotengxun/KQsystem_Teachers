package edu.sdjzu.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.adapter.KqLookAdapter;
import edu.sdjzu.attr.TeacherAttr;
import edu.sdjzu.model.KQLookInfo;

public class TeaKqLook extends Fragment {
	private ListView lookListView;
	private KqLookAdapter kqLookAdapter;
	private List<KQLookInfo> listKqInfo;

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
		listKqInfo = (List<KQLookInfo>) getArguments().get(TeacherAttr.kqLookDataKey);
		kqLookAdapter=new KqLookAdapter(getActivity(), listKqInfo);
		lookListView = (ListView) getView().findViewById(R.id.kq_look_lsv);
		lookListView.setAdapter(kqLookAdapter);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

}
