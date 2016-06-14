package cn.fonely.floatnotice;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import cn.fonely.floatnotice.AppListAdapter.OnShowItemClickListener;

public class AppListActivity extends Activity implements OnShowItemClickListener {

    private Button sAllBtn,cAllBtn;
    private  ListView appListView;
    private  List<ItemBean> dataList;
    private List<ItemBean> selectedList;
    private  AppListAdapter myAdapter;
    private ProgressDialog progressDialog = null;
    private Set<String> appList;
    private final Handler mHandler = new MyHandler(this);  
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
	appList=new HashSet<String>();
	SharedPreferences sharedPreferences= getSharedPreferences("app_list", Activity.MODE_PRIVATE); 
	appList=sharedPreferences.getStringSet("appList", new HashSet<String>());
	
	setContentView(R.layout.activity_app_list);
	sAllBtn=(Button) findViewById(R.id.select_all);
	cAllBtn=(Button) findViewById(R.id.cancel_all);
	appListView= (ListView) findViewById(R.id.applist_view);
	dataList=new ArrayList<ItemBean>();
	selectedList=new ArrayList<ItemBean>();
	progressDialog=new ProgressDialog(this);
	progressDialog.setCancelable(false);
	progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	progressDialog.setProgress(0);
	progressDialog.show();
	
	  new Thread(new Runnable() {
		    
		    @Override
		    public void run() {
			// TODO 自动生成的方法存根
			 Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			        List<ResolveInfo> mAllApps = getPackageManager().queryIntentActivities(mainIntent, 0);
			        	 
			                	PackageManager pManager = getApplicationContext().getPackageManager();
			                	//List<ApplicationInfo> pList=pManager.getInstalledApplications(0);
			                	progressDialog.setMax(mAllApps.size());
			                	Log.d("size=",mAllApps.size()+"");
			                	for(int j=0;j<mAllApps.size();j++)
			                	{
			                	    
			                	    ResolveInfo resolve = mAllApps.get(j);
			                	    ItemBean item=new ItemBean();
			                	    item.setAppIcon(resolve.loadIcon(pManager));
			                	    item.setAppName(resolve.loadLabel(pManager).toString());
			                	    item.setPkgName(resolve.activityInfo.packageName);
			                	    Log.d("app_pkg_name",resolve.activityInfo.packageName);
			                	    if(appList.contains(resolve.activityInfo.packageName))
			                	    {
			                		 Log.d("select_app_pkg_name",resolve.activityInfo.packageName);
			                		item.setChecked(true);
			                	    }
			                	    item.setId(j);
			                	    dataList.add(item);
			                	    if(item.isChecked())
			                	    {
			                		selectedList.add(item);
			                	    }
			                	    progressDialog.incrementProgressBy(j);  
			                	    Log.d("name--"+j,resolve.loadLabel(pManager).toString());
			                /*	    try {
							Thread.sleep(50);
						    } catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						    }*/
			                	}
			               progressDialog.dismiss();  
			               mHandler.sendEmptyMessage(1234);
		    }
		}).start();
	/*  Log.d("dataList.size",""+dataList.size());
          myAdapter=new AppListAdapter(dataList, AppListActivity.this);
          appListView.setAdapter(myAdapter);
          myAdapter.setOnShowItemClickListener(AppListActivity.this);*/
	  appListView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		    // TODO 自动生成的方法存根

	            ItemBean item = dataList.get(position);
	            boolean isChecked = item.isChecked();
	            if (isChecked) {
	                item.setChecked(false);
	            } else {
	                item.setChecked(true);
	            }
	            myAdapter.notifyDataSetChanged();
	            Log.d("select",selectedList.size()+"");
		}

	
	    });

	    sAllBtn.setOnClickListener(new OnClickListener() {
	        
	        @Override
	        public void onClick(View v) {
	    	// TODO 自动生成的方法存根
	            for (ItemBean bean : dataList) {
	                if (!bean.isChecked()) {
	                    bean.setChecked(true);
	                    if (!selectedList.contains(bean)) {
	                        selectedList.add(bean);
	                    }
	                }
	                
	        }
	            myAdapter.notifyDataSetChanged();
	        }
	    });
	    cAllBtn.setOnClickListener(new OnClickListener() {
	        
	        @Override
	        public void onClick(View v) {
	    	// TODO 自动生成的方法存根
	            for (ItemBean bean : dataList) {
	                if (bean.isChecked()) {
	                    bean.setChecked(false);
	                    if (selectedList.contains(bean)) {
	                        selectedList.remove(bean);
	                    }
	                }
	        }
	            myAdapter.notifyDataSetChanged();
	        }
	    });
	    
	  
    }
    
    private void updataList()
    {
	 Log.d("dataList.size",""+dataList.size());
         myAdapter=new AppListAdapter(dataList, AppListActivity.this);
         appListView.setAdapter(myAdapter);
         myAdapter.setOnShowItemClickListener(AppListActivity.this);
    }
    
    private  static class MyHandler extends Handler{

	private final WeakReference<AppListActivity> mActivity;  
	  
        public MyHandler(AppListActivity activity) {  
            mActivity = new WeakReference<AppListActivity>(activity);  
        }  
  
	public void handleMessage(android.os.Message msg) {
	    if(msg.what==1234)
	    {
		if (mActivity.get() == null) {  
	                return;  
	            }  
	            mActivity.get().updataList();
	    }
	}
    }
    

    @Override
    public void onShowItemClick(ItemBean bean) {
	// TODO 自动生成的方法存根
	 if (bean.isChecked() && !selectedList.contains(bean)) {
	        selectedList.add(bean);
	    } else if (!bean.isChecked() && selectedList.contains(bean)) {
	        selectedList.remove(bean);
	    }
    }

    @Override
    protected void onDestroy() {
        // TODO 自动生成的方法存根
        super.onDestroy();
        Set<String> list=new HashSet<String>();
        Log.d("s_size==",selectedList.size()+"");
        for(int i=0;i<selectedList.size();i++)
        {
            Log.d("s_list_"+i,selectedList.get(i).getPkgName());
            list.add(selectedList.get(i).getPkgName());
        }
        SharedPreferences mySharedPreferences= getSharedPreferences("app_list", Activity.MODE_PRIVATE); 
        SharedPreferences.Editor editor = mySharedPreferences.edit(); 
        editor.clear();
        editor.putStringSet("appList", list);
        editor.putInt("app_list_size", list.size());
        editor.commit(); 
        editor.apply();
        Bundle ext=new Bundle();
	Intent intent = new Intent();
	ext.putInt("update_list", 2);
	intent.putExtras(ext);
	intent.setAction(this.getPackageName() + "._notice");
	sendBroadcast(intent);
	
	if(myAdapter!=null)
	{
	    myAdapter.clearObjects();
	}
	if(dataList!=null)
	{
	    dataList=null;
	}
	if(selectedList!=null)
	{
	    selectedList=null;
	}
	if(progressDialog!=null)
	{
	    progressDialog=null;
	}
	if(appListView!=null)
	{
	    appListView=null;
	}
	mHandler.removeCallbacksAndMessages(null);
	
    }

   
}
