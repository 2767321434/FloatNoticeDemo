package com.floatnotice.demo.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.floatnotice.demo.MyWindowManager;

@SuppressLint("NewApi")
public class NoticService extends NotificationListenerService{
   
    private PendingIntent pendingIntent;
    private NoticeReceiver myReceiver;
    private int count;
    ArrayList<StatusBarNotification> sbnList;
   // Handler handler=new Handler();
    //定义浮动窗口布局  
//    private LinearLayout mpackFloatLayout;  
//    private LinearLayout munpackFloatLayout;
//    private WindowManager.LayoutParams wmParams;  
//    //创建浮动窗口设置布局参数的对象  
//
//    private View mFloatView;  
      @Override
    public void onCreate() {
        // TODO 自动生成的方法存根
        super.onCreate();
      //  flag=false;
   //     wmParams = new WindowManager.LayoutParams();  
        //通过getApplication获取的是WindowManagerImpl.CompatModeWrapper  
        count=0;
        
     //mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);  
        //Toast.makeText(NoticService.this, "启动", Toast.LENGTH_SHORT).show(); 
        myReceiver = new NoticeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("notice");
        registerReceiver(myReceiver, intentFilter);
        MyWindowManager mWM=new MyWindowManager();
        mWM.registerService(this);
        
        Log.d("onCreate","服务启动");
    }
    @SuppressLint("NewApi")
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
	// TODO 自动生成的方法存根
	Notification mNotification=sbn.getNotification();
	
	Log.d("onNotificationPosted",mNotification.toString());
	sbnList=new ArrayList<StatusBarNotification>();
	for(StatusBarNotification sn:getActiveNotifications())
	{
	    Log.d("sn.isClearable()",""+sn.isClearable());
	    if(sn.isClearable()){
	    sbnList.add(sn);
	    }
	}
	Bundle ext=new Bundle();
	ext.putInt("thisRemove", -1);
	Intent intent = new Intent();
	    intent.putExtras(ext);
        intent.setAction("notice");
        sendBroadcast(intent);
        Log.d("onNotificationPosted","mNotification不为null");
       /* if (mNotification!=null){
            if(mNotification.flags==1||mNotification.flags==0x10||mNotification.flags==0)
            {
          Log.d("flag",mNotification.flags+"---");
          Bundle extras = mNotification.extras;
          int id=sbn.getId();
  	if(android.os.Build.VERSION.SDK_INT>21){
	    String key=sbn.getKey();
	    extras.putString("this_key", key);
	    Log.d("key==",key);
	}
  Bitmap licon=(Bitmap)extras.getParcelable(Notification.EXTRA_LARGE_ICON);
  Log.d("licon==",licon+"----");
          PendingIntent pi= mNotification.contentIntent;
          extras.putParcelable("pi", pi);
          extras.putInt("this_id", id);
          extras.putString("this_tag",sbn.getTag());
          Log.d("this_tag==",sbn.getTag()+"----");
          extras.putString("pkg_name", sbn.getPackageName());
          Log.d("pkg_name==",sbn.getPackageName());
        
          Log.d("largeIcon==",mNotification.largeIcon+"---large");
          
          //通过以下方式可以获取Notification的详细信息
          
           * Bundle extras = sbn.getNotification().extras; String
           * notificationTitle = extras.getString(Notification.EXTRA_TITLE);
           * Bitmap notificationLargeIcon = ((Bitmap)
           * extras.getParcelable(Notification.EXTRA_LARGE_ICON)); Bitmap
           * notificationSmallIcon = ((Bitmap)
           * extras.getParcelable(Notification.EXTRA_SMALL_ICON)); CharSequence
           * notificationText = extras.getCharSequence(Notification.EXTRA_TEXT);
           * CharSequence notificationSubText =
           * extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
           * Log.i("SevenNLS", "notificationTitle:"+notificationTitle);
           * Log.i("SevenNLS", "notificationText:"+notificationText);
           * Log.i("SevenNLS", "notificationSubText:"+notificationSubText);
           * Log.i("SevenNLS",
           * "notificationLargeIcon is null:"+(notificationLargeIcon == null));
           * Log.i("SevenNLS",
           * "notificationSmallIcon is null:"+(notificationSmallIcon == null));
           
          Log.d("nowNoticf===","----"+getActiveNotifications().length);
         // extras.putInt("ncount", count);
          
          
          Log.d("count", count+"---");
            Intent intent = new Intent();
	    intent.putExtras(extras);
            intent.setAction("gengxin");
            sendBroadcast(intent);
            Log.d("onNotificationPosted","mNotification不为null");
            }
        }*/
    }
    
    
   
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
	// TODO 自动生成的方法存根
	int num=-1;
	for(int i=0;i<sbnList.size();i++)
	{
	    if((sbnList.get(i).getPackageName()).equals(sbn.getPackageName())&(sbnList.get(i).getId()==sbn.getId()))
	    {
		Log.d("i==",i+"");
		num=i;
		break;
	    }
	}
	
	
	Bundle ext=new Bundle();
	ext.putInt("thisRemove", num);
	Intent intent = new Intent();
	intent.putExtras(ext);
        intent.setAction("notice");
        sendBroadcast(intent);
	
    }
    @Override
    public void onDestroy() {
        // TODO 自动生成的方法存根
        super.onDestroy();
        if(myReceiver!=null){
            unregisterReceiver(myReceiver);
        }
        MyWindowManager mWM=new MyWindowManager();
        mWM.unregisterService();
       
    }

public class NoticeReceiver extends BroadcastReceiver {
    
    
    @Override
    public void onReceive(Context context, Intent intent) {
	// TODO 自动生成的方法存根
	Bundle extras=intent.getExtras();
	int removeid=extras.getInt("thisRemove");
	Log.d("removeidid==",removeid+"");
	if((removeid)>=0){
	    MyWindowManager.removeSmallWindow(getApplicationContext(), removeid);
	    MyWindowManager.removeBigWindow(getApplicationContext(),removeid);
	    return;
	}
	Log.d("收到了","收到了");
	Log.d("list===",sbnList+"");
	if(sbnList!=null)
	{
	for(int i=0;i<sbnList.size();i++)
	{
	    StatusBarNotification ext=sbnList.get(i);
	    
	    MyWindowManager.setInfo(ext,i);
	    MyWindowManager.createSmallWindow(getApplicationContext(),i);
	}
	}
	
    }
    
}

private final void saveImage(Bitmap paramBitmap, String paramString, long paramLong)
{
  try
  {
   // removeBitmap(paramString, paramLong);
    if (paramBitmap != null)
    {
      if ((paramBitmap.getWidth() < 100) && (paramBitmap.getHeight() < 100)) {
        return;
      }
      File localFile = new File(Environment.getExternalStorageDirectory().toString() + "/NoticeTEST", "/" + paramString + "_" + paramLong + ".pg");
      if (!localFile.getParentFile().exists()) {
        localFile.getParentFile().mkdir();
      }
      FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
      paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, localFileOutputStream);
      localFileOutputStream.flush();
      localFileOutputStream.close();
      return;
    }
  }
  catch (FileNotFoundException localFileNotFoundException)
  {
    return;
  }
  catch (IOException localIOException)
  {
    return;
  }
  catch (Exception localException)
  {
  }
}


}
