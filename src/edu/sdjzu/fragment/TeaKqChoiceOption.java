package edu.sdjzu.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.attr.TeacherAttr;
import edu.sdjzu.teatools.TeaTool;

public class TeaKqChoiceOption extends Fragment {
	private List<String> courseList, classList, weekList, classTimeList;
	private TeaTool loginClass;
	private Spinner spinnerCourse, spinnerClass, spinnerWeek, spinnerClassTime;
	private String courseString, classString, weekString, classTimeString;
	private String titleType;
	private TextView titleTV;
	private Button btnSure;
	private static OnLook onLook;

	public static void setOnLook(OnLook look) {
		onLook = look;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		initData();
		initView();
		setSpinnerListener();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.act_kq_extra, null);
	}

	private void initData() {
		loginClass = new TeaTool(getActivity());
		classList = loginClass.getTeaAllClass();
		classTimeList = loginClass.getTeaAllClassTime();
		courseList = loginClass.getTeaAllCourse();
		weekList = loginClass.getTeaAllWeek();
	}

	private void initView() {
		titleTV = (TextView) getView().findViewById(R.id.kq_extra_title);
		if (titleType.equals(TeacherAttr.kqBuluKey)) {
			titleTV.setText(getString(R.string.home_kq_bulu));
		} else {
			titleTV.setText(getString(R.string.home_kq_lookback));
			List<String> list = new ArrayList<String>(weekList);
			weekList.clear();
			weekList.add(getString(R.string.kq__all));
			weekList.addAll(list);

			list.clear();
			list = new ArrayList<String>(classTimeList);
			classTimeList.clear();
			classTimeList.add(getString(R.string.kq__all));
			classTimeList.addAll(list);
		}
		btnSure = (Button) getView().findViewById(R.id.kq_extra_sure);

		spinnerCourse = (Spinner) getView().findViewById(R.id.kq_extra_course);
		spinnerClass = (Spinner) getView().findViewById(R.id.kq_extra_class);
		spinnerWeek = (Spinner) getView().findViewById(R.id.kq_extra_week);
		spinnerClassTime = (Spinner) getView().findViewById(R.id.kq_extra_classtime);

		spinnerCourse.setAdapter(new SpinAdapter(getActivity(), courseList));
		spinnerClass.setAdapter(new SpinAdapter(getActivity(), classList));
		spinnerWeek.setAdapter(new SpinAdapter(getActivity(), weekList));
		spinnerClassTime.setAdapter(new SpinAdapter(getActivity(), classTimeList));

		btnSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (courseString != null && classString != null && weekString != null && classTimeString != null) {
					if (courseString.length() > 0 && classString.length() > 0 && weekString.length() > 0
							&& classTimeString.length() > 0) {
						if (onLook != null) {
							onLook.showLook(courseString, classString, weekString, classTimeString, titleType);
						}
					}
				}
			}
		});
	}

	private void setSpinnerListener() {
		spinnerCourse.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				courseString = parent.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		spinnerClass.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				classString = parent.getSelectedItem().toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		spinnerWeek.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				weekString = parent.getSelectedItem().toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		spinnerClassTime.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				classTimeString = parent.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 查看回调接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnLook {
		public void showLook(String course, String cla, String week, String claTime, String kqKey);
	};

	private class SpinAdapter extends BaseAdapter implements SpinnerAdapter {
		private LayoutInflater mInflater;
		private List<String> list;
		private Context ctx;

		public SpinAdapter(Context context, List<String> list) {
			ctx = context;
			mInflater = LayoutInflater.from(ctx);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.act_kq_extra_spinner_item, null);
			TextView tv = (TextView) convertView.findViewById(R.id.kq_spinner_tv);
			tv.setText(list.get(position));
			return convertView;
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		titleType = getArguments().getString(TeacherAttr.kqExtraChoice);
		super.onCreate(savedInstanceState);
	}

}
