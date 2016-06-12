package cn.fonely.floatnotice.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import cn.fonely.floatnotice.MyWindowManager;


public class NoticService extends NotificationListenerService {

    private NoticeReceiver myReceiver;
    private ArrayList<StatusBarNotification> sbnList;
    private Set<String> appList;

    @Override
    public void onCreate() {
	// TODO 自动生成的方法存根
	super.onCreate();
	appList=new HashSet<String>();
	SharedPreferences sharedPreferences= getSharedPreferences("app_list", Activity.MODE_PRIVATE); 
	appList=sharedPreferences.getStringSet("appList", new HashSet<String>());
	myReceiver = new NoticeReceiver();
	IntentFilter intentFilter = new IntentFilter();
	intentFilter.addAction(this.getPackageName() + "._notice");
	registerReceiver(myReceiver, intentFilter);
	MyWindowManager mWM = new MyWindowManager();
	mWM.registerService(this);

	Log.d("onCreate", "服务启动");
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

	sbnList = new ArrayList<StatusBarNotification>();
	for (StatusBarNotification sn : getActiveNotifications()) {
	    Log.d("sn.isClearable()", "" + sn.isClearable());
	
	      if(appList.contains(sn.getPackageName())){
	    
		   if (sn.isClearable()) {
			
			sbnList.add(sn);
		    }
	       }
	   }
	   
	
	Bundle ext = new Bundle();
	ext.putInt("thisRemove", -1);
	Intent intent = new Intent();
	intent.putExtras(ext);
	intent.setAction(this.getPackageName() + "._notice");
	sendBroadcast(intent);
	Log.d("onNotificationPosted", "mNotification不为null");

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
	// TODO 自动生成的方法存根
	int num = -1;
	if(sbn!=null)
	{
	for (int i = 0; i < sbnList.size(); i++) {
	    if ((sbnList.get(i).getPackageName()).equals(sbn.getPackageName())
		    & (sbnList.get(i).getId() == sbn.getId())) {
		Log.d("i==", i + "");
		num = i;
		break;
	    }
	}

	Bundle ext = new Bundle();
	ext.putInt("thisRemove", num);
	Intent intent = new Intent();
	intent.putExtras(ext);
	intent.setAction(this.getPackageName() + "._notice");
	sendBroadcast(intent);
	}
    }

    @Override
    public void onDestroy() {
	// TODO 自动生成的方法存根
	super.onDestroy();
	if (myReceiver != null) {
	    unregisterReceiver(myReceiver);
	}
	MyWindowManager mWM = new MyWindowManager();
	mWM.unregisterService();
	mWM = null;
    }

    public class NoticeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	    // TODO 自动生成的方法存根
	    Bundle extras = intent.getExtras();
	    int removeid = extras.getInt("thisRemove");
	    int updateList=extras.getInt("update_list");
	    if(updateList>0)
	    {
		SharedPreferences sharedPreferences= getSharedPreferences("app_list", Activity.MODE_PRIVATE); 
		appList=sharedPreferences.getStringSet("appList", new HashSet<String>());
		return;
	    }
	    Log.d("removeidid==", removeid + "");
	    if ((removeid) >= 0) {
		MyWindowManager.removeSmallWindow(getApplicationContext(),
			removeid);
		MyWindowManager.removeBigWindow(getApplicationContext(),
			removeid);
		return;
	    }
	    Log.d("收到了", "收到了");

	    if (sbnList != null) {
		for (int i = 0; i < sbnList.size(); i++) {
		    StatusBarNotification ext = sbnList.get(i);
		    MyWindowManager.setInfo(ext, i);
		    MyWindowManager.createSmallWindow(getApplicationContext(), i);
		}
	    }
	    
	}
    }
}
