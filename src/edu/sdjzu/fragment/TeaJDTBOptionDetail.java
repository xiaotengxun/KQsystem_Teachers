package edu.sdjzu.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kqsystem_teachers.R;

import edu.sdjzu.adapter.JDTBDetailAdapter;
import edu.sdjzu.attr.TeacherAttr;
import edu.sdjzu.model.Students;

public class TeaJDTBOptionDetail extends Fragment {
	private ListView listView;
	private List<Students> listStu = new ArrayList<Students>();;
	private JDTBDetailAdapter jdtbDetailAdapter;
	private TextView courseName, courseWeek, courseJtime;
	private String courseNameS, courseWeekS, courseJtimeS;
	private Button deleteBtn;
	private static DeleteJdtbOnClick deleteJdtbOnClick=null;
	private int jno=-1;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initView();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.act_jdtb_saved_detail, null);
	}

	private void initView() {
		courseJtime = (TextView) getView().findViewById(R.id.jdtb_detail_course_jtime);
		courseName = (TextView) getView().findViewById(R.id.jdtb_detail_coursename);
		courseWeek = (TextView) getView().findViewById(R.id.jdtb_detail_course_week);
		courseJtime.setText(courseJtimeS);
		courseName.setText(courseNameS);
		courseWeek.setText(courseWeekS);
		deleteBtn = (Button) getView().findViewById(R.id.jdtb_detail_delete);
		deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(deleteJdtbOnClick != null){
					deleteJdtbOnClick.delete(jno);
				}
			}
		});
		listView = (ListView) getView().findViewById(R.id.jdtb_detail_listv);
		jdtbDetailAdapter = new JDTBDetailAdapter(getActivity(), listStu);
		listView.setAdapter(jdtbDetailAdapter);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		listStu = (List<Students>) getArguments().getSerializable(TeacherAttr.jdtbKQStuKey);
		courseNameS = getArguments().getString(TeacherAttr.jdtbCourseNameKey, "");
		courseWeekS = getArguments().getString(TeacherAttr.jdtbCourseWeekKey, "");
		courseWeekS = String.format(getString(R.string.tea_look_weekformat), courseWeekS);
		courseJtimeS = getArguments().getString(TeacherAttr.jdtbCourseClassTimeKey, "");
		jno=Integer.valueOf(getArguments().getString(TeacherAttr.jnoKey));
		super.onCreate(savedInstanceState);
	}
	public static void setDeleteJdtbOnClick(DeleteJdtbOnClick dj){
		deleteJdtbOnClick=dj;
	}
	public interface DeleteJdtbOnClick{
		public void delete(int jno);
	};
}
