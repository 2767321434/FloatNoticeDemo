package com.floatnotice.demo;

import java.lang.reflect.Field;

import android.animation.ObjectAnimator;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PackView extends LinearLayout {

    private int mScreenWidth;
    /**
	 * 记录小悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录小悬浮窗的高度
	 */
	public static int viewHeight;

	/**
	 * 记录系统状态栏的高度
	 */
	 private static int statusBarHeight;

	/**
	 * 用于更新小悬浮窗的位置
	 */
	private WindowManager windowManager;

	/**
	 * 小悬浮窗的参数
	 */
	private WindowManager.LayoutParams mParams;

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
	private Drawable pkg_icon ;
	private int id;

	public PackView(Context context,int id) {
		super(context);
		this.id=id;
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.pack_layout, this);
		View view = findViewById(R.id.pack_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
		DisplayMetrics dm=context.getResources().getDisplayMetrics();
		mScreenWidth=dm.widthPixels;
		
		ImageView largeImage=(ImageView) findViewById(R.id.large_icon);
		ImageView smallImage=(ImageView) findViewById(R.id.small_icon);
		StatusBarNotification sbn=MyWindowManager.getInfo(id);
		Bundle ext=sbn.getNotification().extras;
		Bitmap large_icon=ext.getParcelable(Notification.EXTRA_LARGE_ICON);
		
		String pkg_name=sbn.getPackageName();
		PackageManager pm=context.getPackageManager();
		try {
		    pkg_icon =pm.getApplicationIcon(pkg_name);
		} catch (NameNotFoundException e1) {
		    // TODO 自动生成的 catch 块
		    e1.printStackTrace();
		}
		if(pkg_icon!=null)
		{
		    Log.d("pkg_icon","pkg图标");
		    largeImage.setImageDrawable(pkg_icon);
		}
		if(large_icon!=null)
		{
		    Log.d("large_icon","large_icon图标--"+large_icon);
		    largeImage.setImageBitmap(large_icon);
		    smallImage.setImageDrawable(pkg_icon);
		}
		Log.d("large_icon","large_icon图标是null--"+large_icon);
		
		    
		
		

	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			// 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
			xInView = event.getX();
			yInView = event.getY();
			xDownInScreen = event.getRawX();
			yDownInScreen = event.getRawY() - getStatusBarHeight();
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			break;
		case MotionEvent.ACTION_MOVE:
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			// 手指移动的时候更新小悬浮窗的位置
			updateViewPosition();
			break;
		case MotionEvent.ACTION_UP:
		    
			// 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
			if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
				openBigWindow();
			}
			fixedPosition();
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
	 * 
	 * @param params
	 *            小悬浮窗的参数
	 */
	public void setParams(WindowManager.LayoutParams params) {
		mParams = params;
	}

	/**
	 * 更新小悬浮窗在屏幕中的位置。
	 */
	private void updateViewPosition() {
	    

		//mParams.x=mScreenWidth-viewWidth;

		mParams.x = (int) (xInScreen - xInView);
		mParams.y = (int) (yInScreen - yInView);
		windowManager.updateViewLayout(this, mParams);
	}
	private void fixedPosition()
	{
	    int x;
	    if(xInScreen-xInView>mScreenWidth/2)
	    {
		x=mScreenWidth-viewWidth;
	    }else
	    {
		x=0;
	    }
		mParams.x = x;
		
		mParams.y = (int) (yInScreen - yInView);
		ObjectAnimator.ofFloat(this, "parmX", xInScreen,x).setDuration(300).start();
		
	}
	public void setParmX(float x)
	{
	    mParams.x =(int) x;
	    windowManager.updateViewLayout(this, mParams);
	}
	/**
	 * 打开大悬浮窗，同时关闭小悬浮窗。
	 */
	private void openBigWindow() {
	    Log.d("pack_id",id+"---");
		MyWindowManager.createBigWindow(getContext().getApplicationContext(),id);
		MyWindowManager.removeSmallWindow(getContext().getApplicationContext(),id);
	}


	/**
	 * 用于获取状态栏的高度。
	 * 
	 * @return 返回状态栏高度的像素值。
	 */
	private int getStatusBarHeight() {
		if (statusBarHeight == 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}

}
