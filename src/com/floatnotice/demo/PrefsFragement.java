package com.floatnotice.demo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

public class PrefsFragement extends PreferenceFragment{  
    Activity activity;
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);  
        activity=getActivity();
        addPreferencesFromResource(R.xml.preferences);  
        Preference prefence= getPreferenceScreen().findPreference("call_function");
        prefence.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	    
	    @Override
	    public boolean onPreferenceClick(Preference preference) {
		// TODO 自动生成的方法存根
		  NotificationManager  nm=(NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
			PendingIntent pendingIntent3 = PendingIntent.getActivity(activity, 0,  
		                    new Intent(activity, MainActivity.class), 0); 
			Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.circle_cyan);
			Notification notify3 = new Notification.Builder(activity)
		        .setSmallIcon(R.drawable.circle_cyan)  
		        .setTicker("TickerText:" + "您有新短消息，请注意查收！")  
		        .setContentTitle("Notification Title") 
		        .setLargeIcon(bm)
		        .setContentText("This is the notification message")
		        .setSmallIcon(R.drawable.circle_red)
		        .setContentIntent(pendingIntent3).setNumber(2).build();
			// notify3.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。  
			nm.notify(1, notify3);
	/*	Notification nb=new Notification.Builder(activity).setContentTitle("my title！").setContentText("my textinfo").setSubText("my subtext").build();
		nm.notify(1, nb);*/
		return true;
	    }
	});
        
      
    }  
} 
