package edu.sdjzu.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.adapter.KqOrderAdapter;
import edu.sdjzu.kqsystem_teacher.LoginAct;
import edu.sdjzu.kqsystem_teacher.TeaIndexAct;
import edu.sdjzu.kqsystem_teacher.TeaStuOrderAct;
import edu.sdjzu.kqsystem_teacher.TeaStuOrderAct.onFilterChosePicStuListener;
import edu.sdjzu.kqsystem_teacher.TeaStuOrderAct.onPicClassListItemClick;
import edu.sdjzu.model.KQresult;
import edu.sdjzu.teatools.TeaTool;

public class TeaPicStuKq extends Fragment implements onPicClassListItemClick, onFilterChosePicStuListener {
	private List<HashMap<String, String>> listHash = new ArrayList<HashMap<String, String>>();
	private TeaTool loginClass;
	private ImageView picLeftBtn, picRightBtn;
	private ImageView picStu;
	private Button btnLate;
	private TextView stuStateTV, stuNameTV, stuSnoTV, stuClassTV;;
	private int picIndex = -1;
	private Handler picHandler;
	private Runnable picRunnable;
	private boolean isAllowPicNext = true;
	private static final int PIC_NETX = 0;
	private static final long PIC_PRESS_OFF_TIME = 2000;// 点击更新考勤按钮一秒钟之内切换一次图片

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.act_kq_stupic, null);
		return v;
	}

	private void initView() {
		stuStateTV = (TextView) getView().findViewById(R.id.kq_spic_stu_state);
		stuNameTV = (TextView) getView().findViewById(R.id.kq_pic_stu_name);
		stuSnoTV = (TextView) getView().findViewById(R.id.kq_pic_stu_sno);
		stuClassTV = (TextView) getView().findViewById(R.id.kq_pic_stu_class);
		btnLate = (Button) getView().findViewById(R.id.kq_spic_stu_late);
		btnLate.setOnClickListener(orderPicOnclick);
		picLeftBtn = (ImageView) getView().findViewById(R.id.kq_spic_stu_left);
		picRightBtn = (ImageView) getView().findViewById(R.id.kq_spic_stu_right);
		picLeftBtn.setOnClickListener(picLeftRightOnclick);
		picRightBtn.setOnClickListener(picLeftRightOnclick);
		picStu = (ImageView) getView().findViewById(R.id.kq_pic_stu_pic);
		picStu.setOnClickListener(stuPicOnClick);
		TeaStuOrderAct.setPicClassItemOnClickListener(this);
		TeaStuOrderAct.setOnFilterChoseListPicListener(this);
		loginClass = new TeaTool(getActivity());
		// listHash = loginClass.getStuList(TeaIndexAct.jno);
		listHash = TeaStuOrderAct.listHash;
		if (listHash.size() > 0) {
			picIndex = 0;
			updateStuPic(picIndex);
			changeViewShow(picIndex);
		}
		picHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == PIC_NETX) {
					changeViewShow(picIndex);
				}
				super.handleMessage(msg);
			}

		};
		picRunnable = new Runnable() {
			@Override
			public void run() {
				if (picIndex < listHash.size() - 1) {
					++picIndex;
					picHandler.sendEmptyMessage(PIC_NETX);
					isAllowPicNext = true;
				}
			}
		};

	}

	/*
	 * 更新界面学生信息
	 */
	private void changeViewShow(int position) {
		String picName = listHash.get(position).get(KqOrderAdapter.Constant.stuSnoKey);
		String picState = listHash.get(position).get(KqOrderAdapter.Constant.stuClassOderStateKey);
		Log.i("chen", "position=" + position + "   state=" + picState);
		String stuName = listHash.get(position).get(KqOrderAdapter.Constant.stuNameKey);
		String stuSno = listHash.get(position).get(KqOrderAdapter.Constant.stuSnoKey);
		String stuClass = listHash.get(position).get(KqOrderAdapter.Constant.stuClassKey);
		stuNameTV.setText(stuName);
		stuSnoTV.setText(stuSno);
		stuClassTV.setText(stuClass);

		if (picState.equals(getActivity().getString(R.string.tea_kq_state_normal))) {
			btnLate.setText(getActivity().getString(R.string.tea_kq_state_innormal));
			btnLate.setTextColor(Color.RED);
			stuStateTV.setTextColor(getActivity().getResources().getColor(R.color.color_black));
		} else {
			btnLate.setText(getActivity().getString(R.string.tea_kq_state_normal));
			btnLate.setTextColor(Color.BLACK);
			stuStateTV.setTextColor(getActivity().getResources().getColor(R.color.color_blue));
		}
		stuStateTV.setText(picState);
		Bitmap bitMap = loginClass.getPhotoBySno(picName);
		if (bitMap != null) {
			picStu.setImageBitmap(bitMap);
		}
	}

	/**
	 * 左右切换学生信息
	 */
	private View.OnClickListener picLeftRightOnclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.kq_spic_stu_left) {
				if (picIndex > 0) {
					--picIndex;
					updateStuPic(picIndex);
					changeViewShow(picIndex);
				}
			} else if (v.getId() == R.id.kq_spic_stu_right) {
				if (picIndex < listHash.size() - 1) {
					isAllowPicNext = true;
					++picIndex;
					updateStuPic(picIndex);
					changeViewShow(picIndex);
				}
			}

		}
	};

	private void updateStuPic(int index) {
		BitmapDrawable bitMap = new BitmapDrawable(getActivity().getCacheDir() + "/" + LoginAct.userName + "/"
				+ listHash.get(picIndex).get(KqOrderAdapter.Constant.stuSnoKey) + ".jpg");
		picStu.setImageDrawable(bitMap);
	}

	/**
	 * 更新学生考勤信息
	 */
	private View.OnClickListener orderPicOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (isAllowPicNext) {
				String state = getActivity().getString(R.string.tea_kq_state_normal);
				if (btnLate.getText().toString().equals(state)) {
					state = getActivity().getString(R.string.tea_kq_state_normal);
				} else {
					state = getActivity().getString(R.string.tea_kq_state_innormal);
				}

				updateStuKq(listHash.get(picIndex).get(KqOrderAdapter.Constant.stuSnoKey), state);
				listHash.get(picIndex).put(KqOrderAdapter.Constant.stuClassOderStateKey, state);
				changeViewShow(picIndex);
				isAllowPicNext = false;
				picHandler.removeCallbacks(picRunnable);
				picHandler.postDelayed(picRunnable, PIC_PRESS_OFF_TIME);
			}
		}
	};
	/**
	 * 点击学生图片进行切换到下一个学生
	 */
	private View.OnClickListener stuPicOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (picIndex < listHash.size() - 1) {
				++picIndex;
				updateStuPic(picIndex);
				changeViewShow(picIndex);
			}

		}
	};

	/**
	 * 更新本地的课堂考勤表
	 * 
	 * @param stuSno
	 * @param kState
	 */
	private void updateStuKq(String stuSno, String kState) {
		TeaTool lgClass = new TeaTool(getActivity());
		String teaName = lgClass.getTeaNameByTno(LoginAct.userName);
		String isSubmit = getActivity().getString(R.string.KQresult_notsubmit);
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initView();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (hidden == false) {
			changeViewShow(picIndex);
		}
		super.onHiddenChanged(hidden);
	}

	@Override
	public void choseClassPic(String cla) {
		listHash = TeaStuOrderAct.listHash;
		if (listHash.size() > 0) {
			picIndex = 0;
		}
		updateStuPic(picIndex);
		changeViewShow(picIndex);

	}

	@Override
	public void filterChosen(int type) {
		listHash = TeaStuOrderAct.listHash;
		if (listHash.size() > 0) {
			picIndex = 0;
		}
		updateStuPic(picIndex);
		changeViewShow(picIndex);
	}

}
