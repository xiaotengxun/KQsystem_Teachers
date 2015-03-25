package edu.sdjzu.broadcast;

import com.example.kqsystem_teachers.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetSubmitKQReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		int choice = intent.getIntExtra("toast", -1);
		if (choice == 0) {
			Toast.makeText(context, context.getString(R.string.net_submit_kq_nokq), 2000).show();
		} else if (choice == 1) {
			Toast.makeText(context, context.getString(R.string.net_submit_kq_success), 2000).show();
		}
	}

}
