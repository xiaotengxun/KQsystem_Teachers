package edu.sdjzu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import edu.sdjzu.attr.TeacherAttr;
import edu.sdjzu.localtool.InternetStatus;

public class NetService extends Service {
	private ThreadGroup threadGroup = new ThreadGroup(TeacherAttr.SERVICE_TAG);
	private Runnable runnable;
	private boolean isRunning = false;
	private boolean isNetConnected = false;
	private InternetStatus internetStatus = null;
	private Thread netThread = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		netThread.start();
		return 0;
	}

	private void init() {
		isRunning = true;
		internetStatus = new InternetStatus(NetService.this);
		runnable = new Runnable() {
			@Override
			public void run() {
				while (isRunning) {
					try {
						boolean isConnected = getNetWorkState();
						if (!isNetConnected && isConnected) {
							sendBroadcast(new Intent(TeacherAttr.BROAD_ACTION_NETWORK_CONNECTED));
							Thread.sleep(TeacherAttr.netWorkGetPerTime);
						}
						isNetConnected = isConnected;
					} catch (InterruptedException e) {
						isNetConnected = false;
						e.printStackTrace();
					} catch (Error ex) {
						isNetConnected = false;
					}

				}

			}
		};
		netThread = new Thread(threadGroup, runnable);
	}

	private boolean getNetWorkState() {
//		if (internetStatus.isNetworkConnected()) {
//			Log.i("chen", "net connected");
//		} else {
//			Log.i("chen", "net   not   connected");
//		}
		try {
			return internetStatus.isNetworkConnected();

		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void onCreate() {
		init();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.i("chen", "service onDestroy");
		super.onDestroy();
	}

}
