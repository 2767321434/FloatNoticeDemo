package cn.fonely.floatnotice;

import android.content.Context;
import android.graphics.PixelFormat;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import cn.fonely.floatnotice.service.NoticService;

/**
 * 自定义的WindowManager，实现大小悬浮窗的切换
 */
public class MyWindowManager implements ServiceListener {

    private static NoticService service;


    /**
     * 大悬浮窗View的实例
     */
    private static UnpackView bigWindow;



    /**
     * 大悬浮窗View的参数
     */
    private static LayoutParams bigWindowParams;

    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;
    /**
     * 用于存放获得的StatusBarNotification对象
     */
   private static SparseArray<StatusBarNotification> sbns=new SparseArray<StatusBarNotification>();
    
   // private static Map<Integer, StatusBarNotification> sbns = new HashMap<Integer, StatusBarNotification>();
   private static SparseArray<PackView> sView=new SparseArray<PackView>();
    //private static Map<Integer, PackView> sView = new HashMap<Integer, PackView>();
   // private static Map<Integer, UnpackView> lView = new HashMap<Integer, UnpackView>();
   private static SparseArray<UnpackView> lView =new SparseArray<UnpackView>();
  //  private static Map<Integer, LayoutParams> sLayoutList=new HashMap<Integer, LayoutParams>();
   private static SparseArray<LayoutParams> sLayoutList=new SparseArray<WindowManager.LayoutParams>();
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
	PackView smallWindow=sView.get(id);
	 LayoutParams smallWindowParams=sLayoutList.get(id);
	 Log.d("newId===",id+"");
	 Log.d("newSmallOut",sView.get(id)+"");
	if (smallWindow == null) {
	    
	    smallWindow = new PackView(context, id);
	    smallWindow.setAlpha(0.7f);
	    if(smallWindowParams==null){
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
	
	    sView.put(id, smallWindow);
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
	    sLayoutList.put(id,(LayoutParams) sView.get(id).getLayoutParams());
	    WindowManager windowManager = getWindowManager(context);
	    windowManager.removeView(sView.get(id));
	    sView.remove(id);
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
	bigWindow=lView.get(id);
	Log.d("bigView-out",bigWindow+"");
		
	if (bigWindow== null) {
	    bigWindow = new UnpackView(context, id);
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
	    bigWindow.setLayoutParams(bigWindowParams);
	    lView.put(id, bigWindow);
	}
	windowManager.addView(bigWindow, bigWindowParams);
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
	   lView.remove(id);
	}
    }
    /**
     * 移除通知时移除全部界面
     * @param context
     * @param id
     */
    public static void removeAllView(Context context,int id)
    {	
	if (lView.get(id) != null){
	    WindowManager windowManager = getWindowManager(context);
	    windowManager.removeView(lView.get(id));
	    lView.remove(id);
	}
	if (sView.get(id) != null) {
	    WindowManager windowManager = getWindowManager(context);
	    windowManager.removeView(sView.get(id));
	    sView.remove(id);
	}
    }
    /*
     * 获取对应id的StatusBarNotification对象
     */
    public static StatusBarNotification getInfo(int id) {
	return sbns.get(id);
    }
    /**
     * 设置StatusBarNotification对象及其id
     * @param ext
     * @param id
     */
    public static void setInfo(StatusBarNotification ext, int id) {
	MyWindowManager.sbns.put(id, ext);
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
