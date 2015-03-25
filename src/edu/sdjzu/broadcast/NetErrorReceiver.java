package edu.sdjzu.broadcast;

import com.example.kqsystem_teachers.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NetErrorReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, context.getString(R.string.network_error_update_kq_tip), 2000).show();;
	}

}
