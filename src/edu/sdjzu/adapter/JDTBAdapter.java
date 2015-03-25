package edu.sdjzu.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kqsystem_teachers.R;

public class JDTBAdapter extends BaseAdapter {

	private Context context;
	private List<HashMap<String, String>> listMap;
	private LayoutInflater mInflater;
	private static JDTBDetailOnClick jdtbDetailOnClick = null;
	private boolean isArrorVisible = false;
	private DeleteJDTBOnClick deleteJDTBOnClick;
	private List<Integer> listSelectedJno = new ArrayList<Integer>();

	public static class jdtbKey {
		public static String courseName = "courseName";
		public static String week = "week";
		public static String classTime = "classTime";
		public static String jno = "jno";
	};

	public JDTBAdapter(Context context, List<HashMap<String, String>> listM) {
		super();
		this.context = context;
		listMap = listM;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listMap.size();
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
		ViewHold vh;
		convertView = mInflater.inflate(R.layout.jdtb_listview_item, null);
		vh = new ViewHold();
		vh.btnLook = (Button) convertView.findViewById(R.id.jdtb_look);
		vh.classTime = (TextView) convertView.findViewById(R.id.jdtb_classtime);
		vh.week = (TextView) convertView.findViewById(R.id.jdtb_week);
		vh.courseName = (TextView) convertView.findViewById(R.id.jdtb_coursename);
		vh.arrorSelect = (CheckBox) convertView.findViewById(R.id.jdtb_checkbox);
		convertView.setTag(vh);
		
		vh.btnLook.setTag(position);
		Map map = listMap.get(position);
		vh.classTime.setText((String) map.get(jdtbKey.classTime));
		vh.courseName.setText((String) map.get(jdtbKey.courseName));
		vh.week.setText(String.format(context.getString(R.string.tea_look_weekformat), (String) map.get(jdtbKey.week)));
		vh.arrorSelect.setTag(map.get(jdtbKey.jno));
		if (isArrorVisible) {
			vh.arrorSelect.setVisibility(View.VISIBLE);
		} else {
			vh.arrorSelect.setVisibility(View.INVISIBLE);
		}
		vh.btnLook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int po = Integer.valueOf(v.getTag().toString());
				if (jdtbDetailOnClick != null) {
					HashMap<String, String> hash = listMap.get(po);
					jdtbDetailOnClick.getProgressNo(Integer.valueOf(hash.get(jdtbKey.jno)),
							hash.get(jdtbKey.courseName), hash.get(jdtbKey.week), hash.get(jdtbKey.classTime));
				}
			}
		});
		vh.arrorSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					listSelectedJno.add(Integer.valueOf(buttonView.getTag().toString()));
					Log.i("chen", "add jno=" + buttonView.getTag());
				} else {
					listSelectedJno.remove(Integer.valueOf(buttonView.getTag().toString()));
					Log.i("chen", "remove jno=" + buttonView.getTag());
				}
			}
		});
		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Vibrate(context, 200);
				isArrorVisible = true;
				notifyDataSetChanged();
				if (deleteJDTBOnClick != null) {
					deleteJDTBOnClick.deleteOnClick(listSelectedJno);
				}
				return false;
			}
		});
		return convertView;
	}

	public void setDeleteArror(boolean isVisible) {
		this.isArrorVisible = isVisible;
	}

	public void Vibrate(final Context activity, long milliseconds) {
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	class ViewHold {
		TextView courseName;
		TextView week;
		TextView classTime;
		Button btnLook;
		CheckBox arrorSelect;
		View touchView;
	};

	public interface DeleteJDTBOnClick {
		public void deleteOnClick(List<Integer> listJno);
	};

	public void setDeleteJDTBOnClickListener(DeleteJDTBOnClick deleteOnClick) {
		this.deleteJDTBOnClick = deleteOnClick;
	}

	public static void setJDTBDetailOnClick(JDTBDetailOnClick jc) {
		jdtbDetailOnClick = jc;
	}

	public interface JDTBDetailOnClick {
		public void getProgressNo(int jno, String courseName, String courseWeek, String courseJtime);
	};

}
