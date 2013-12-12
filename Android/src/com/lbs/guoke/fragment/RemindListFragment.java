package com.lbs.guoke.fragment;

import com.lbs.guoke.R;
import com.lbs.guoke.controller.RemindModuleManager;
import com.lbs.guoke.controller.RemindModuleManager.RemindInfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RemindListFragment extends ListFragment {
    private RemindAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_list, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	adapter = new RemindAdapter(getActivity());
	setListAdapter(adapter);
    }

    public class RemindAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context mContext;

	public RemindAdapter(Context context) {
	    mContext = context;
	    inflater = LayoutInflater.from(mContext);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
	    RemindViewHolder holder;
	    if (convertView == null) {
		convertView = (View) inflater.inflate(R.layout.item_remind,
			parent, false);
		holder = new RemindViewHolder();
		holder.row_icon = (ImageView) convertView
			.findViewById(R.id.row_icon);
		holder.row_title = (TextView) convertView
			.findViewById(R.id.row_title);
		holder.row_menssage = (TextView) convertView
			.findViewById(R.id.row_message);
		convertView.setTag(holder);
	    } else {
		holder = (RemindViewHolder) convertView.getTag();
	    }
	    holder.row_icon.setImageResource(R.drawable.ic_launcher);
	    holder.row_title.setText(RemindModuleManager.instance()
		    .getRemindInfos().get(position).remindTitle);
	    holder.row_menssage.setText(RemindModuleManager.instance()
		    .getRemindInfos().get(position).remindMessage);
	    return convertView;
	}

	@Override
	public int getCount() {
	    // TODO Auto-generated method stub
	    return RemindModuleManager.instance().getRemindInfos().size();
	}

	@Override
	public Object getItem(int arg0) {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public long getItemId(int arg0) {
	    // TODO Auto-generated method stub
	    return 0;
	}
    }

    static class RemindViewHolder {
	ImageView row_icon;
	TextView row_title;
	TextView row_menssage;
    }
}
