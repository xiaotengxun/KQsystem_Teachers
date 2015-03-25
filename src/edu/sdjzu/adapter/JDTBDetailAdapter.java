package edu.sdjzu.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.kqsystem_teachers.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import edu.sdjzu.fragment.TeaJDTBOptionDetail;
import edu.sdjzu.fragment.TeaJDTBOptionDetail.DeleteJdtbOnClick;
import edu.sdjzu.model.Students;
import edu.sdjzu.teatools.TeaTool;

public class JDTBDetailAdapter extends BaseAdapter implements DeleteJdtbOnClick {
	private int jno = -1;
	private List<Students> listStu = new ArrayList<Students>();;
	private LayoutInflater mInflater;
	private List<String> deleteStu = new ArrayList<String>();
	private TeaTool loginClass;

	public JDTBDetailAdapter(Context ctx, List<Students> listS) {
		mInflater = LayoutInflater.from(ctx);
		listStu = listS;
		TeaJDTBOptionDetail.setDeleteJdtbOnClick(this);
		loginClass = new TeaTool(ctx);
	}

	public void setListData(List<Students> listS) {
		listStu = listS;
	}

	@Override
	public int getCount() {
		return listStu.size();
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
			convertView = mInflater.inflate(R.layout.jdtb_saved_detail_item, null);
			vh = new ViewHolder();
			vh.check = (CheckBox) convertView.findViewById(R.id.jdtb_checkbox);
			vh.name = (TextView) convertView.findViewById(R.id.jd_sname);
			vh.sno = (TextView) convertView.findViewById(R.id.jd_sno);
			vh.sclass = (TextView) convertView.findViewById(R.id.jd_sclass);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.name.setText(listStu.get(position).getStuName());
		vh.sclass.setText(listStu.get(position).getStuClass());
		vh.sno.setText(listStu.get(position).getStuNo());
		vh.check.setTag(position);
		vh.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int po = Integer.valueOf(buttonView.getTag().toString());
				if (isChecked) {
					deleteStu.add(listStu.get(po).getStuNo());
				} else {
					deleteStu.remove(listStu.get(po).getStuNo());
				}

			}
		});
		return convertView;
	}

	private class ViewHolder {
		CheckBox check;
		TextView name, sno, sclass;
	}

	@Override
	public void delete(int jn) {
		jno=jn;
		loginClass.deleteKQByStuJno(jno, deleteStu);
		refresh();
	};

	private void refresh() {
		listStu = loginClass.getKQStuByJno(jno);
		Log.i("chen","size="+listStu.size()+"   jno="+jno);
		JDTBDetailAdapter.this.notifyDataSetChanged();
	}
}
