package com.floatnotice.demo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floatnotice.demo.service.NoticService;

public class UnpackView extends LinearLayout{
    /**
	 * 记录大悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录当前手指位置在屏幕上的横坐标值
	 */
	private float xInScreen;

	/**
	 * 记录当前手指位置在屏幕上的纵坐标值
	 */
	private float yInScreen;

	/**
	 * 记录手指按下时在屏幕上的横坐标的值
	 */
	private float xDownInScreen;

	/**
	 * 记录手指按下时在屏幕上的纵坐标的值
	 */
	private float yDownInScreen;

	/**
	 * 记录手指按下时在小悬浮窗的View上的横坐标的值
	 */
	private float xInView;

	/**
	 * 记录手指按下时在小悬浮窗的View上的纵坐标的值
	 */
	private float yInView;
	
	public static int viewHeight;
	private int nid;
	private String key;
	private String pkg_name;
	private PendingIntent pi;
	private String tag;
	private Drawable pkg_icon ;
	private Context mContext;
	private int statusBarHeight;
	private int id;
    @SuppressLint("NewApi")
    public UnpackView(final Context context,final int id) {
	super(context);
	this.mContext=context;
	this.id=id;
	LayoutInflater.from(context).inflate(R.layout.unpack_layout, this);
	View view = findViewById(R.id.unpack_layout);
	viewWidth = view.getLayoutParams().width;
	viewHeight = view.getLayoutParams().height;
	StatusBarNotification sbn=MyWindowManager.getInfo(id);
	Bundle ext=sbn.getNotification().extras;
	String title=ext.getString(Notification.EXTRA_TITLE);
	String text=ext.getString(Notification.EXTRA_TEXT);
	nid=sbn.getId();
	Log.d("sdk", android.os.Build.VERSION.SDK_INT+"");
	if(android.os.Build.VERSION.SDK_INT>=21){
	key=sbn.getKey();
	}
	
	pkg_name=sbn.getPackageName();
	pi=sbn.getNotification().contentIntent;
	tag=sbn.getTag();
	
	Log.d("pi==",pi+"");
	Log.d("this_id",nid+"");
	Log.d("this_tag",key+"");
	Log.d("title",title+"");
	Log.d("text",text+"");
	TextView title_view=(TextView) findViewById(R.id.nTitle);
	title_view.setText(title);
	TextView text_view=(TextView) findViewById(R.id.nText);
	text_view.setText(text);
	Bitmap icon=(Bitmap)ext.getParcelable(Notification.EXTRA_LARGE_ICON);
	ImageView icon_view=(ImageView) findViewById(R.id.nIcon);

	PackageManager pm=context.getPackageManager();
	
	try {
	    pkg_icon =pm.getApplicationIcon(pkg_name);
	} catch (NameNotFoundException e1) {
	    // TODO 自动生成的 catch 块
	    e1.printStackTrace();
	}
	if(icon!=null)
	{
	    icon_view.setImageBitmap(icon);
	    Log.d("icon","icon不是null"+icon);
	}else{
	    Log.d("icon","icon是null");
	    if(pkg_icon!=null)
		{
		    icon_view.setImageDrawable(pkg_icon);
		}
	}
	
	ImageButton close = (ImageButton) findViewById(R.id.del_btn);
	ImageButton back = (ImageButton) findViewById(R.id.back_btn);
	close.setOnClickListener(new OnClickListener() {
		@TargetApi(Build.VERSION_CODES.LOLLIPOP)
		@Override
		public void onClick(View v) {
			// 点击关闭悬浮窗的时候，移除所有悬浮窗
			MyWindowManager.removeBigWindow(context,id);
			MyWindowManager.removeSmallWindow(context,id);
			NoticService ns = MyWindowManager.getNS();
		if(android.os.Build.VERSION.SDK_INT>=21){
		    ns.cancelNotification(key);
		}
		else{
			ns.cancelNotification(pkg_name, tag, nid);
		}
		}
	});
	back.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// 点击返回的时候，移除大悬浮窗，创建小悬浮窗
			MyWindowManager.removeBigWindow(context,id);
			Log.d("unpack_id",id+"---");
			MyWindowManager.createSmallWindow(context,id);
			
		}
	});
	text_view.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		// TODO 自动生成的方法存根
		try {
		    pi.send();
		} catch (CanceledException e) {
		    // TODO 自动生成的 catch 块
		    e.printStackTrace();
		}
		
	    }
	});
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO 自动生成的方法存根
	switch (event.getAction()) {
	case MotionEvent.ACTION_DOWN:
	    xInView = event.getX();
	    Log.d("xInView",xInView+"--x");
		yInView = event.getY();
		 Log.d("yInView",yInView+"--y");

	    break;
	case MotionEvent.ACTION_MOVE:
	    break;
	case MotionEvent.ACTION_UP:
	    if(xInView<0||yInView<0||xInView>viewWidth||yInView>viewHeight)
	    {
		Log.d("unpack_id",id+"---");
		// 点击窗口外的区域返回，移除大悬浮窗，创建小悬浮窗
		MyWindowManager.removeBigWindow(mContext,id);
		MyWindowManager.createSmallWindow(mContext,id);
	    }
	    break;
	}
        return super.onTouchEvent(event);
    
    }
    
   
}
