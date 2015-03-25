package edu.sdjzu.fragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kqsystem_teachers.R;

public class TeaListView extends Fragment {
	private ListView popView;
	private List<String> popContent = new ArrayList<String>();
	private PopAdapter popAdapter;

	public TeaListView(List<String> popContent) {
		super();
		this.popContent = popContent;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initView();
		super.onActivityCreated(savedInstanceState);
	}

	private void initView() {
		popView = (ListView) getView().findViewById(R.id.listview);
		popAdapter = new PopAdapter();
		popView.setAdapter(popAdapter);
	}

	public void setListViewOnItemClick(OnItemClickListener onItemClick) {
		popView.setOnItemClickListener(onItemClick);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.act_listview, null);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	private class PopAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public PopAdapter() {
			mInflater = LayoutInflater.from(getActivity());
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return popContent.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.kq_left_slide_view_item, null);
			TextView tx = (TextView) convertView.findViewById(R.id.text1);
			tx.setText(popContent.get(position));
			tx.setTextColor(Color.BLACK);
			return convertView;
		}

	}

}
