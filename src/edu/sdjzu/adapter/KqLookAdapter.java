package edu.sdjzu.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.model.KQLookInfo;

public class KqLookAdapter extends BaseAdapter {
	private List<KQLookInfo> list = new ArrayList<KQLookInfo>();
	private Context context;
	private LayoutInflater mInflater;

	public KqLookAdapter(Context ctx, List<KQLookInfo> list) {
		this.list = list;
		context = ctx;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
		ViewHolder vh = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.act_kq_look_listview, null);
		}
		vh = (ViewHolder) convertView.getTag();
		if (null == vh) {
			vh = new ViewHolder();
			vh.listView = (MyListView) convertView.findViewById(R.id.kq_look_titile_lsv);
			vh.title = (TextView) convertView.findViewById(R.id.kq_look_title);
			convertView.setTag(vh);
		}
		KQLookInfo info = list.get(position);
		String titleContent = String.format(context.getString(R.string.tea_look_weekformat), info.getWeek())
				+ "       " + info.getClassTime() + "        " + info.getDescription();
		if (!info.getDescription().equals("")) {
			int bstart = titleContent.indexOf("未提交");
			int bend = "未提交".length();
			SpannableStringBuilder style = new SpannableStringBuilder(titleContent);
			style.setSpan(new ForegroundColorSpan(Color.RED), bstart, bstart + bend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			vh.title.setText(style);
		} else {
			vh.title.setText(titleContent);
		}
		KqLookListViewAdapter adapter = new KqLookListViewAdapter(info.getHashMap(), context);
		vh.listView.setAdapter(adapter);
		return convertView;
	}

	private class ViewHolder {
		MyListView listView;
		TextView title;

	};

}
