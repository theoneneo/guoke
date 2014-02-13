package com.lbs.guoke.fragment;

import com.lbs.guoke.AddRemindActivity;
import com.lbs.guoke.AddSiteActivity;
import com.lbs.guoke.MainActivity;
import com.lbs.guoke.R;
import com.lbs.guoke.tempDialogActivity;
import com.lbs.guoke.controller.CellModuleManager;
import com.lbs.guoke.controller.MySiteModuleManager;
import com.lbs.guoke.controller.RemindModuleManager;
import com.lbs.guoke.controller.MySiteModuleManager.SiteInfo;
import com.lbs.guoke.controller.RemindModuleManager.RemindInfo;
import com.lbs.guoke.fragment.RemindListFragment.RemindViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MySiteListFragment extends ListFragment {
	private SiteAdapter adapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list_add, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
	}

	private void startTemp() {
		Intent i = new Intent(getActivity(), tempDialogActivity.class);
		startActivity(i);
	}

	private void initUI() {
		Button btn_add = (Button)getActivity().findViewById(R.id.add).findViewById(R.id.btn_add);
		btn_add.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				go2AddSiteFragment(null);
			}	
		});
		adapter = new SiteAdapter(getActivity());
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				go2AddSiteFragment(MySiteModuleManager.instance()
						.getSiteInfos().get(arg2 - 1).key);
			}
		});
		adapter.notifyDataSetChanged();
	}

	private void go2AddSiteFragment(String key) {
		int status = 0;
		if (key == null) {
			status = AddSiteFragment.ADD_DATA_STATUS;
		} else {
			status = AddSiteFragment.INFO_DATA_STATUS;
		}
		// TODO Auto-generated method stub
		Intent i = new Intent(getActivity(), AddSiteActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("status", status);
		bundle.putString("key", key);
		i.putExtras(bundle);
		startActivityForResult(i, MainActivity.REQUEST_ADD_SITE);
	}
	
	private void go2AddRemindFragment(String key){
		String remindid = null;
		for(int i = 0; i < RemindModuleManager.instance().getRemindInfos().size(); i++){
			RemindInfo remindInfo = RemindModuleManager.instance().getRemindInfos().get(i);
			if(remindInfo.key.equals(key)){
				remindid = remindInfo.remindid;
				break;
			}
		}
		Intent i = new Intent(getActivity(), AddRemindActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("key", key);
		bundle.putString("remindid", remindid);
		i.putExtras(bundle);
		startActivityForResult(i, MainActivity.REQUEST_ADD_REMIND);
	}

	public void updateAdapter() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	public class SiteAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context mContext;

		public SiteAdapter(Context context) {
			mContext = context;
			inflater = LayoutInflater.from(mContext);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			SiteViewHolder holder;
			if (convertView == null) {
				convertView = (View) inflater.inflate(R.layout.item_site,
						parent, false);
				holder = new SiteViewHolder();
				holder.row_icon = (ImageView) convertView
						.findViewById(R.id.row_icon);
				holder.row_name = (TextView) convertView
						.findViewById(R.id.row_name);
				holder.row_detail = (TextView) convertView
						.findViewById(R.id.row_detail);
				holder.row_button = (Button) convertView
						.findViewById(R.id.row_button);
				convertView.setTag(holder);
			} else {
				holder = (SiteViewHolder) convertView.getTag();
			}

			final SiteInfo sInfo = MySiteModuleManager.instance()
					.getSiteInfos().get(position);
			holder.row_icon.setImageResource(R.drawable.ic_launcher);
			holder.row_name.setText(sInfo.siteName);
			holder.row_detail.setText(sInfo.siteAddress);
			holder.row_button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					go2AddRemindFragment(sInfo.key);
				}
			});
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return MySiteModuleManager.instance().getSiteInfos().size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	static class SiteViewHolder {
		ImageView row_icon;
		TextView row_name;
		TextView row_detail;
		Button row_button;
	}
}
