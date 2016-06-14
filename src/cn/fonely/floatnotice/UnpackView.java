package cn.fonely.floatnotice;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.fonely.floatnotice.service.NoticService;
/**
 * 点击展开的界面
 * 
 */
public class UnpackView extends LinearLayout {
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

    public static int viewHeight; //view的高度
    private int nid;//通知的id
    private String key;//当前通知的key
    private String pkg_name;//发送通知应用包名
    private PendingIntent pi;//通知相应的intent
    private String tag;//通知的tag
    private Drawable pkg_icon;//应用的图标
    private Context mContext;
    private int statusBarHeight;
    private int id;//当前view的id

    @SuppressLint("NewApi")
    public UnpackView(final Context context, final int id) {
	super(context);
	this.mContext = context;
	this.id = id;
	LayoutInflater.from(context).inflate(R.layout.unpack_layout, this);
	View view = findViewById(R.id.unpack_layout);
	viewWidth = view.getLayoutParams().width;
	viewHeight = view.getLayoutParams().height;
	StatusBarNotification sbn = MyWindowManager.getInfo(id);
	Bundle ext = sbn.getNotification().extras;
	String title = ext.getString(Notification.EXTRA_TITLE);
	String text = ext.getString(Notification.EXTRA_TEXT);
	nid = sbn.getId();
	if (android.os.Build.VERSION.SDK_INT >= 21) {
	    key = sbn.getKey();
	}

	pkg_name = sbn.getPackageName();
	pi = sbn.getNotification().contentIntent;
	tag = sbn.getTag();
	
	  Log.d("pi==",pi+""); Log.d("this_id",nid+"");
	  Log.d("this_tag",key+""); Log.d("title",title+"");
	  Log.d("text",text+"");
	 //-------- 设置该view能接收的按键事件的关键，还是得看国外的网站靠谱
	  this.requestFocus();
	  this.setFocusableInTouchMode(true);
	//-----------  
	TextView title_view = (TextView) findViewById(R.id.nTitle);
	title_view.setText(title);
	TextView text_view = (TextView) findViewById(R.id.nText);
	text_view.setText(text);
	Bitmap icon = (Bitmap) ext.getParcelable(Notification.EXTRA_LARGE_ICON);
	ImageView icon_view = (ImageView) findViewById(R.id.nIcon);

	PackageManager pm = context.getPackageManager();

	try {
	    pkg_icon = pm.getApplicationIcon(pkg_name);
	} catch (NameNotFoundException e1) {
	    // TODO 自动生成的 catch 块
	    e1.printStackTrace();
	}
	if (icon != null) {
	    icon_view.setImageBitmap(icon);
	     Log.d("icon","icon不是null"+icon);
	} else {
	     Log.d("icon","icon是null");
	    if (pkg_icon != null) {
		icon_view.setImageDrawable(pkg_icon);
	    }
	}

	ImageButton close = (ImageButton) findViewById(R.id.del_btn);
	ImageButton back = (ImageButton) findViewById(R.id.back_btn);
	close.setOnClickListener(new OnClickListener() {
	    @SuppressWarnings("deprecation")
	    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
	    @Override
	    public void onClick(View v) {
		// 点击关闭悬浮窗的时候，移除所有悬浮窗
		
		NoticService ns = MyWindowManager.getNS();
		if (android.os.Build.VERSION.SDK_INT >= 21) {
		    ns.cancelNotification(key);
		} else {
		    ns.cancelNotification(pkg_name, tag, nid);
		}
		removeAll();
	    }
	});
	back.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		// 点击返回的时候，移除大悬浮窗，创建小悬浮窗
		 returnToSmall();
	    }
	});
	text_view.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO 自动生成的方法存根
		try {
		    pi.send();
		    removeAll();
		    NoticService ns = MyWindowManager.getNS();
				if (android.os.Build.VERSION.SDK_INT >= 21) {
				    ns.cancelNotification(key);
				} else {
				    ns.cancelNotification(pkg_name, tag, nid);
				}
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
	    if (xInView < 0 || yInView < 0 || xInView > viewWidth
		    || yInView > viewHeight) {
		 Log.d("unpack_id",id+"---");
		// 点击窗口外的区域返回，移除大悬浮窗，创建小悬浮窗
		 returnToSmall();
	    }
	    break;
	}
	return super.onTouchEvent(event);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
	if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
	    returnToSmall();
	    Log.d("按下返回键",""+keyCode);
            return true;
        }
       
        return super.onKeyDown(keyCode, event);
    }
   
    
    private void returnToSmall()
    {
	MyWindowManager.removeBigWindow(mContext, id);
	MyWindowManager.createSmallWindow(mContext, id);
    }
    private void removeAll()
    {
	  MyWindowManager.removeBigWindow(mContext, id);
	  MyWindowManager.removeSmallWindow(mContext, id);
    }
}
