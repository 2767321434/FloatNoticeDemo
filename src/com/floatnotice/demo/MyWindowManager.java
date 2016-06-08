package com.floatnotice.demo;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.PixelFormat;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.floatnotice.demo.service.NoticService;

public class MyWindowManager implements ServiceListener {

    private static NoticService service;
    /**
     * 小悬浮窗View的实例
     */
    private static PackView smallWindow;

    /**
     * 大悬浮窗View的实例
     */
    private static UnpackView bigWindow;

    /**
     * 小悬浮窗View的参数
     */
    private static LayoutParams smallWindowParams;

    /**
     * 大悬浮窗View的参数
     */
    private static LayoutParams bigWindowParams;

    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;

    private static Map<Integer, StatusBarNotification> exts = new HashMap<Integer, StatusBarNotification>();
    private static Map<Integer, View> sView = new HashMap<Integer, View>();
    private static Map<Integer, View> lView = new HashMap<Integer, View>();

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     * 
     * @param context
     *            必须为应用程序的Context.
     */
    public static void createSmallWindow(Context context, int id) {
	WindowManager windowManager = getWindowManager(context);
	int screenWidth = windowManager.getDefaultDisplay().getWidth();
	int screenHeight = windowManager.getDefaultDisplay().getHeight();
	if (sView.get(id) == null) {
	    smallWindow = new PackView(context, id);
	    smallWindow.setAlpha(0.7f);
	    sView.put(id, smallWindow);
	    if (smallWindowParams == null) {
		smallWindowParams = new LayoutParams();
		smallWindowParams.type = LayoutParams.TYPE_TOAST;
		smallWindowParams.format = PixelFormat.RGBA_8888;
		smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
			| LayoutParams.FLAG_NOT_FOCUSABLE;
		smallWindowParams.gravity = Gravity.START | Gravity.TOP;
		smallWindowParams.width = PackView.viewWidth;
		smallWindowParams.height = PackView.viewHeight;
		smallWindowParams.x = screenWidth;
		smallWindowParams.y = screenHeight / 2;
	    }
	    smallWindow.setParams(smallWindowParams);
	    windowManager.addView(smallWindow, smallWindowParams);
	}
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     * 
     * @param context
     *            必须为应用程序的Context.
     */
    public static void removeSmallWindow(Context context, int id) {

	if (sView.get(id) != null) {
	    WindowManager windowManager = getWindowManager(context);
	    windowManager.removeView(sView.get(id));
	    sView.put(id, null);
	}
    }

    /**
     * 创建一个大悬浮窗。位置为屏幕正中间。
     * 
     * @param context
     *            必须为应用程序的Context.
     */
    public static void createBigWindow(Context context, int id) {
	WindowManager windowManager = getWindowManager(context);
	int screenWidth = windowManager.getDefaultDisplay().getWidth();
	int screenHeight = windowManager.getDefaultDisplay().getHeight();
	if (lView.get(id) == null) {
	    bigWindow = new UnpackView(context, id);

	    lView.put(id, bigWindow);
	    if (bigWindowParams == null) {
		bigWindowParams = new LayoutParams();
		bigWindowParams.x = screenWidth / 2 - UnpackView.viewWidth / 2;
		bigWindowParams.y = screenHeight / 2 - UnpackView.viewHeight / 2;
		bigWindowParams.type = LayoutParams.TYPE_TOAST;
		bigWindowParams.format = PixelFormat.RGBA_8888;
		bigWindowParams.gravity = Gravity.START | Gravity.TOP;
		bigWindowParams.width = UnpackView.viewWidth;
		bigWindowParams.height = UnpackView.viewHeight;

	    }
	    windowManager.addView(bigWindow, bigWindowParams);
	}
    }

    /**
     * 将大悬浮窗从屏幕上移除。
     * 
     * @param context
     *            必须为应用程序的Context.
     */
    public static void removeBigWindow(Context context, int id) {
	if (lView.get(id) != null) {
	    Log.d("removeBig", lView.get(id) + "---");
	    WindowManager windowManager = getWindowManager(context);
	    windowManager.removeView(lView.get(id));
	    lView.put(id, null);
	}
    }
    /*
     * 获取对应id的StatusBarNotification对象
     */
    public static StatusBarNotification getInfo(int id) {
	return exts.get(id);
    }
    /**
     * 设置StatusBarNotification对象及其id
     * @param ext
     * @param id
     */
    public static void setInfo(StatusBarNotification ext, int id) {
	MyWindowManager.exts.put(id, ext);
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     * 
     * @param context
     *            必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private static WindowManager getWindowManager(Context context) {
	if (mWindowManager == null) {
	    mWindowManager = (WindowManager) context
		    .getSystemService(Context.WINDOW_SERVICE);
	}
	return mWindowManager;
    }
    /**
     * 注册服务，获取通知服务实例
     */
    public void registerService(NoticService service) {
	// TODO 自动生成的方法存根
	this.service = service;
    }

    /**
     * 反注册服务
     */
    public void unregisterService() {
	// TODO 自动生成的方法存根
	service = null;
    }
    
    /**
     * 让其他类获取通知服务实例
     * @return
     */
    public static NoticService getNS() {
	return service;
    }

}
