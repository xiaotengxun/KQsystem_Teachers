package edu.sdjzu.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.kqsystem_teacher.LoginAct;
import edu.sdjzu.kqsystem_teacher.TeaStuOrderAct;
import edu.sdjzu.model.KQresult;
import edu.sdjzu.teatools.TeaTool;

public class KqOrderAdapter extends BaseAdapter {
	public static class Constant {
		public static String stuNameKey = "stuNameKey";
		public static String stuSnoKey = "stuSnoKey";
		public static String stuClassKey = "stuClassKey";
		public static String stuDisabsenceCountsKey = "stuDisabsenceCounts";
		// public static String stuClassStateKey = "stuClassStateKey";
		public static String stuClassOderStateKey = "stuClassOderStateKey";
		public static String stuKqInTimeKey = "stuKqInTimeKey";

	};

	private LayoutInflater mInflater;
	private Context ctx;
	private List<HashMap<String, String>> listHash = new ArrayList<HashMap<String, String>>();
	private String stateNormal = "";
	private String stateInNormal = "";

	public KqOrderAdapter(Context ctx, List<HashMap<String, String>> listHash) {
		super();
		mInflater = LayoutInflater.from(ctx);
		this.ctx = ctx;
		this.listHash = listHash;
		stateInNormal = ctx.getString(R.string.tea_kq_state_innormal);
		stateNormal = ctx.getString(R.string.tea_kq_state_normal);
	}

	public void setStuList(List<HashMap<String, String>> listHash) {
		this.listHash = listHash;
	}

	@Override
	public int getCount() {
		return listHash.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.act_kq_stulist_item, null);
			vh = new ViewHolder();
			vh.stuName = (TextView) convertView.findViewById(R.id.kq_stu_name);
			vh.stuSno = (TextView) convertView.findViewById(R.id.kq_stu_sno);
			//vh.stuClass = (TextView) convertView.findViewById(R.id.kq_stu_class);
			vh.stuDisAbsenceTimes = (TextView) convertView.findViewById(R.id.kq_stu_disabsent_times);
//			vh.stuClassState = (TextView) convertView.findViewById(R.id.kq_stu_class_state);
			vh.stuOrderState = (Button) convertView.findViewById(R.id.kq_stu_order_state);
			vh.stuOrderState.setTag(vh);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if (position % 2 == 1) {
			convertView.setBackgroundColor(ctx.getResources().getColor(R.color.kq_order_stu_background_one));
		} else {
			convertView.setBackgroundColor(ctx.getResources().getColor(R.color.kq_order_stu_background_two));
		}
		HashMap<String, String> hM = listHash.get(position);
		vh.position = position;
		vh.stuName.setText(hM.get(Constant.stuNameKey));
		vh.stuSno.setText(hM.get(Constant.stuSnoKey));
	//	vh.stuClass.setText(hM.get(Constant.stuClassKey));
		vh.stuDisAbsenceTimes.setText(hM.get(Constant.stuDisabsenceCountsKey));
		setOrderBtnOnClick(vh);
		if (hM.get(Constant.stuClassOderStateKey).equals(stateNormal)) {
			vh.stuOrderState.setText(stateInNormal);
			vh.stuOrderState.setTextColor(Color.RED);
//			vh.stuClassState.setText(stateNormal);
//			vh.stuClassState.setTextColor(Color.BLACK);
		} else {
			vh.stuOrderState.setText(stateNormal);
			vh.stuOrderState.setTextColor(Color.BLACK);
//			vh.stuClassState.setText(stateInNormal);
//			vh.stuClassState.setTextColor(Color.RED);
		}
		return convertView;
	}

	private void setOrderBtnOnClick(ViewHolder vh) {
		vh.stuOrderState.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewHolder vHolder = (ViewHolder) v.getTag();
				int lateTimes = Integer.valueOf(listHash.get(vHolder.position).get(Constant.stuDisabsenceCountsKey));
				if (vHolder.stuOrderState.getText().equals(stateInNormal)) {// 缺勤
					++lateTimes;
					vHolder.stuOrderState.setText(stateNormal);
					vHolder.stuOrderState.setTextColor(Color.BLACK);
//					vHolder.stuClassState.setText(stateInNormal);
//					vHolder.stuClassState.setTextColor(Color.RED);
					updateStuKq(vHolder.stuSno.getText().toString(), stateInNormal);

				} else if (vHolder.stuOrderState.getText().equals(stateNormal)) {
					--lateTimes;
					vHolder.stuOrderState.setText(stateInNormal);
					vHolder.stuOrderState.setTextColor(Color.RED);
//					vHolder.stuClassState.setText(stateNormal);
//					vHolder.stuClassState.setTextColor(Color.BLACK);
					updateStuKq(vHolder.stuSno.getText().toString(), stateNormal);
				}
				listHash.get(vHolder.position).put(Constant.stuDisabsenceCountsKey, String.valueOf(lateTimes));
				vHolder.stuDisAbsenceTimes.setText(String.valueOf(lateTimes));
			}
		});
	}

	// private void setSpinnerOnClickListener(ViewHolder vh) {
	// vh.stuOrderState.setOnItemSelectedListener(new OnItemSelectedListener() {
	// @Override
	// public void onItemSelected(AdapterView<?> parent, View view, int
	// position, long id) {
	// ViewHolder vHolder = (ViewHolder) parent.getTag();
	// String lastClassState=vHolder.stuClassState.getText().toString();
	// // int lateTimes =
	// //
	// Integer.valueOf(listHash.get(vHolder.position).get(Constant.stuDisabsenceCountsKey));
	// if (position == 1) {
	// // ++lateTimes;
	// ((TextView) view).setTextColor(Color.RED);
	// vHolder.stuClassState.setTextColor(Color.RED);
	// vHolder.stuClassState.setText(ctx.getString(R.string.tea_kq_state_innormal));
	// listHash.get(vHolder.position).put(Constant.stuClassOderStateKey,
	// ctx.getString(R.string.tea_kq_state_innormal));
	// if(lastClassState.equals(ctx.getString(R.string.tea_kq_state_normal))){
	// Log.i("chen", "1111");
	// updateStuKq(vHolder.stuSno.getText().toString(),
	// ctx.getString(R.string.tea_kq_state_innormal));
	// }
	// } else {
	// // --lateTimes;
	// vHolder.stuClassState.setTextColor(Color.BLACK);
	// vHolder.stuClassState.setText(ctx.getString(R.string.tea_kq_state_normal));
	// listHash.get(vHolder.position).put(Constant.stuClassOderStateKey,
	// ctx.getString(R.string.tea_kq_state_normal));
	// listHash.get(vHolder.position).put(Constant.stuClassOderStateKey,
	// ctx.getString(R.string.tea_kq_state_normal));
	// if(lastClassState.equals(ctx.getString(R.string.tea_kq_state_innormal))){
	// Log.i("chen", "2222");
	// updateStuKq(vHolder.stuSno.getText().toString(),
	// ctx.getString(R.string.tea_kq_state_normal));
	// }
	// }
	// // listHash.get(vHolder.position).put(Constant.stuDisabsenceCountsKey,
	// // String.valueOf(lateTimes));
	// // vHolder.stuDisAbsenceTimes.setText(String.valueOf(lateTimes));
	// }
	//
	// @Override
	// public void onNothingSelected(AdapterView<?> parent) {
	// }
	// });
	// }

	/**
	 * 更新本地的课堂考勤表
	 * 
	 * @param stuSno
	 * @param kState
	 */
	private void updateStuKq(String stuSno, String kState) {
		TeaTool lgClass = new TeaTool(ctx);
		String teaName = lgClass.getTeaNameByTno(LoginAct.userName);
		String isSubmit = ctx.getString(R.string.KQresult_notsubmit);
		KQresult kq = new KQresult();
		kq.setTaskNo(TeaStuOrderAct.currentRno);
		kq.setProgressNo(TeaStuOrderAct.currentJno);
		kq.setStuNo(stuSno);
		kq.setKqState(kState);
		kq.setKqMark("");
		kq.setIsSubmit(isSubmit);
		kq.setInMan(teaName);
		kq.setInTime(getTime());
		lgClass.insertStuKq(kq);
	}

	private String getTime() {
		Calendar ca = Calendar.getInstance();
		Date nowTime = ca.getTime();
		SimpleDateFormat datetime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String datetimeString = datetime.format(nowTime);
		return datetimeString;
	}

	public class ViewHolder {
		int position;
		TextView stuName, stuSno;// stuClass;
		TextView stuDisAbsenceTimes;// stuClassState;
		Button stuOrderState;
	};

}
