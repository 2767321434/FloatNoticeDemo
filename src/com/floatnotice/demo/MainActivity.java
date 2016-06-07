package com.floatnotice.demo;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.floatnotice.demo.service.NoticService;


public class MainActivity extends Activity {

    Button btn;
    Context ctx;
    private static final int NOTIFICATION_FLAG = 1; 
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx=getApplicationContext();
        tv=(TextView) findViewById(R.id.tv1);
        btn=(Button) findViewById(R.id.button1);
        Button btn2 = (Button) findViewById(R.id.button2);
        Intent intent = new Intent(MainActivity.this, NoticService.class);  
        startService(intent);
        btn.setOnClickListener(new OnClickListener() {
   	 
   	 //启动FloatViewService  
            
        
	    @Override
	    public void onClick(View v) {
		// TODO 自动生成的方法存根
		/*if(!isServiceRunning(ctx,"com.example.floatviewdemo.service.FloatViewService"))
		{
		    Intent intent = new Intent(MainActivity.this, FloatViewService.class);  
		    startService(intent); 
		}else
		{
		    Intent intent = new Intent(MainActivity.this, FloatViewService.class);  
		        //终止FloatViewService  
		        stopService(intent);
		}*/
		NotificationManager  nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent pendingIntent3 = PendingIntent.getActivity(getApplicationContext(), 0,  
	                    new Intent(ctx, MainActivity.class), 0); 
		Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.circle_cyan);
		Notification notify3 = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.circle_cyan)  
                .setTicker("TickerText:" + "您有新短消息，请注意查收！")  
                .setContentTitle("Notification Title") 
                .setLargeIcon(bm)
                .setContentText("This is the notification message")
                .setSmallIcon(R.drawable.circle_red)
                .setContentIntent(pendingIntent3).setNumber(1).build();
		// notify3.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。  
		nm.notify(NOTIFICATION_FLAG, notify3);
/*	Notification nb=new Notification.Builder(getApplicationContext()).setContentTitle("my title！").setContentText("my textinfo").setSubText("my subtext").build();
	nm.notify(NOTIFICATION_FLAG, nb);*/
	    }
	});
        btn2.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
		    startActivity(intent);
	    }
	});
    }
    
    /*@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		 Intent intent = new Intent(MainActivity.this, FloatViewService.class);  
         //启动FloatViewService  
         startService(intent);  
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		// 销毁悬浮窗
		Intent intent = new Intent(MainActivity.this, FloatViewService.class);  
        //终止FloatViewService  
        stopService(intent); 
        super.onStop();
	}*/
	/**
	        * 用来判断服务是否运行.
	        * @param context
	        * @param className 判断的服务名字
	        * @return true 在运行 false 不在运行
	        */
	       public static boolean isServiceRunning(Context mContext,String className) {
	           boolean isRunning = false;
	ActivityManager activityManager = (ActivityManager)
	mContext.getSystemService(Context.ACTIVITY_SERVICE); 
	           List<ActivityManager.RunningServiceInfo> serviceList  = activityManager.getRunningServices(30);
	          if (!(serviceList.size()>0)) {
	               return false;
	           }
	           for (int i=0; i<serviceList.size(); i++) {
	               Log.d("services==",serviceList.get(i).service.getClassName());
	               if (serviceList.get(i).service.getClassName().equals(className) == true) {
	                   isRunning = true;
	                   break;
	               }
	           }
	           return isRunning;
	       }
	       
	       
	       public void execCommand(String command) throws IOException {
		    // start the ls command running
		    //String[] args =  new String[]{"sh", "-c", command};
		    Runtime runtime = Runtime.getRuntime();  
		    Process proc = runtime.exec(command);        //这句话就是shell与高级语言间的调用
		        //如果有参数的话可以用另外一个被重载的exec方法
		        //实际上这样执行时启动了一个子进程,它没有父进程的控制台
		        //也就看不到输出,所以我们需要用输出流来得到shell执行后的输出
		        InputStream inputstream = proc.getInputStream();
		        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
		        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
		        // read the ls output
		        String line = "";
		        StringBuilder sb = new StringBuilder(line);
		        while ((line = bufferedreader.readLine()) != null) {
		            //System.out.println(line);
		                sb.append(line);
		                sb.append('\n');
		        }
		        tv.setText("out---"+sb.toString());
		        //使用exec执行不会等执行成功以后才返回,它会立即返回
		        //所以在某些情况下是很要命的(比如复制文件的时候)
		        //使用wairFor()可以等待命令执行完成以后才返回
		        try {
		            if (proc.waitFor() != 0) {
		                System.err.println("exit value = " + proc.exitValue());
		                tv.setText("in---"+sb.toString());
		            }
		        }
		        catch (InterruptedException e) {  
		            System.err.println(e);
		        }
		    }
		
}

