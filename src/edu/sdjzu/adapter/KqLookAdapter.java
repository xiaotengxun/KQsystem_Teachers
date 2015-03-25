package edu.sdjzu.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kqsystem_teachers.R;

public class KqLookAdapter extends BaseAdapter {
	private List<HashMap<String, String>> listHash = new ArrayList<HashMap<String, String>>();
	private Context context;
	private LayoutInflater mInflater;

	public KqLookAdapter(Context ctx, List<HashMap<String, String>> listHash) {
		this.listHash = listHash;
		context = ctx;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listHash.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.act_kq_look_listitem, null);
		((TextView) convertView.findViewById(R.id.bulu_stuname)).setText(listHash.get(position).get(
				KqOrderAdapter.Constant.stuNameKey));
		((TextView) convertView.findViewById(R.id.bulu_stuclass)).setText(listHash.get(position).get(
				KqOrderAdapter.Constant.stuClassKey));
		((TextView) convertView.findViewById(R.id.bulu_stusno)).setText(listHash.get(position).get(
				KqOrderAdapter.Constant.stuSnoKey));
		((TextView) convertView.findViewById(R.id.bulu_stuState)).setText(listHash.get(position).get(
				KqOrderAdapter.Constant.stuClassOderStateKey));
		return convertView;
	}

}
