package cn.fonely.floatnotice;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
/**
 * 主activity
 */
public class MainActivity extends Activity {

    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	ctx = getApplicationContext();

    }

   /* *//**
     * 用来判断服务是否运行.
     * 
     * @param context
     * @param className
     *            判断的服务名字
     * @return true 在运行 false 不在运行
     *//*
    public static boolean isServiceRunning(Context mContext, String className) {
	boolean isRunning = false;
	ActivityManager activityManager = (ActivityManager) mContext
		.getSystemService(Context.ACTIVITY_SERVICE);
	List<ActivityManager.RunningServiceInfo> serviceList = activityManager
		.getRunningServices(30);
	if (!(serviceList.size() > 0)) {
	    return false;
	}
	for (int i = 0; i < serviceList.size(); i++) {
	    Log.d("services==", serviceList.get(i).service.getClassName());
	    if (serviceList.get(i).service.getClassName().equals(className) == true) {
		isRunning = true;
		break;
	    }
	}
	return isRunning;
    }

    *//**
     * 执行shell命令，留着备用
     * @param command
     * @throws IOException
     *//*
    public void execCommand(String command) throws IOException {
	// start the ls command running
	// String[] args = new String[]{"sh", "-c", command};
	Runtime runtime = Runtime.getRuntime();
	Process proc = runtime.exec(command); // 这句话就是shell与高级语言间的调用
	// 如果有参数的话可以用另外一个被重载的exec方法
	// 实际上这样执行时启动了一个子进程,它没有父进程的控制台
	// 也就看不到输出,所以我们需要用输出流来得到shell执行后的输出
	InputStream inputstream = proc.getInputStream();
	InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
	BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
	// read the ls output
	String line = "";
	StringBuilder sb = new StringBuilder(line);
	while ((line = bufferedreader.readLine()) != null) {
	    // System.out.println(line);
	    sb.append(line);
	    sb.append('\n');
	}
	tv.setText("out---" + sb.toString());
	// 使用exec执行不会等执行成功以后才返回,它会立即返回
	// 所以在某些情况下是很要命的(比如复制文件的时候)
	// 使用wairFor()可以等待命令执行完成以后才返回
	try {
	    if (proc.waitFor() != 0) {
		System.err.println("exit value = " + proc.exitValue());
		tv.setText("in---" + sb.toString());
	    }
	} catch (InterruptedException e) {
	    System.err.println(e);
	}
    }*/

}
