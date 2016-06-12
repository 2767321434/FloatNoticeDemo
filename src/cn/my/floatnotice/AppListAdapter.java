package cn.my.floatnotice;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;



public class AppListAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private List<ItemBean> items;
    private ItemBean item;
    private OnShowItemClickListener onShowItemClickListener;
    
    public AppListAdapter(List<ItemBean> list,Context context)
    {
	 items=list;
	 inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
	// TODO 自动生成的方法存根
	return items.size();
    }

    @Override
    public Object getItem(int position) {
	// TODO 自动生成的方法存根
	return items.get(position);
    }

    @Override
    public long getItemId(int position) {
	// TODO 自动生成的方法存根
	return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	// TODO 自动生成的方法存根
	 ViewHolder holder;
	 if(convertView==null)
	 {
	     holder=new ViewHolder();
	     convertView=inflater.inflate(R.layout.app_item, null);
	     holder.appIconView=(ImageView) convertView.findViewById(R.id.app_icon_view);
	     holder.appNameView=(TextView) convertView.findViewById(R.id.app_name_view);
	     holder.cb=(CheckBox) convertView.findViewById(R.id.app_check);
	     convertView.setTag(holder);
	 }
	 else
	 {
	     holder=(ViewHolder) convertView.getTag();
	 }
	 
	 item=items.get(position);
	 holder.appIconView.setImageDrawable(item.getAppIcon());
	 holder.appNameView.setText(item.getAppName());
	 
	 holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	        @Override
	        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	        if(isChecked)
	        {
	            item.setChecked(true);
	        }
	        else
	        {
	            item.setChecked(false);
	        }
	        //回调方法，讲item加入已选择的
	        onShowItemClickListener.onShowItemClick(item);
	        }
	    });
	 	holder.cb.setChecked(item.isChecked());
		return convertView;
	 
    }

    static class ViewHolder
    {
    ImageView appIconView;
    CheckBox cb;
    TextView appNameView;

    }
    public interface OnShowItemClickListener {
	    public void onShowItemClick(ItemBean bean);
	    }

	    public void setOnShowItemClickListener(OnShowItemClickListener onShowItemClickListener) {
	    this.onShowItemClickListener = onShowItemClickListener;
	}
}
