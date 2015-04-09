package edu.sdjzu.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.kqsystem_teachers.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class KqLookListViewAdapter extends BaseAdapter {
	private List<HashMap<String, String>> hashMap = new ArrayList<HashMap<String, String>>();
	private LayoutInflater mInflater;

	public KqLookListViewAdapter(List<HashMap<String, String>> hashMap, Context context) {
		super();
		this.hashMap = hashMap;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return hashMap.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.act_kq_look_listitem, null);
		}
		vh = (ViewHolder) convertView.getTag();
		if (null == vh) {
			vh = new ViewHolder();
			vh.tvClass = (TextView) convertView.findViewById(R.id.bulu_stuclass);
			vh.tvClassState = (TextView) convertView.findViewById(R.id.bulu_stuState);
			vh.tvName = (TextView) convertView.findViewById(R.id.bulu_stuname);
			vh.tvSno = (TextView) convertView.findViewById(R.id.bulu_stusno);
			convertView.setTag(vh);
		}
		HashMap<String, String> hm = hashMap.get(position);
		vh.tvName.setText(hm.get(KqOrderAdapter.Constant.stuNameKey));
		vh.tvClass.setText(hm.get(KqOrderAdapter.Constant.stuClassKey));
		vh.tvClassState.setText(hm.get(KqOrderAdapter.Constant.stuClassOderStateKey));
		vh.tvSno.setText(hm.get(KqOrderAdapter.Constant.stuSnoKey));
		return convertView;
	}

	private class ViewHolder {
		TextView tvSno, tvName, tvClass, tvClassState;
	};

}
