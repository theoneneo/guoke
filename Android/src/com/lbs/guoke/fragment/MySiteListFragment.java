package com.lbs.guoke.fragment;

import com.lbs.guoke.R;
import com.lbs.guoke.controller.MySiteModuleManager;
import com.lbs.guoke.controller.MySiteModuleManager.SiteInfo;
import com.lbs.guoke.fragment.RemindListFragment.RemindViewHolder;

import android.app.Activity;
import android.content.Context;
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
	private MySiteListFragmentListener fListener;
	private SiteAdapter adapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
	}

	private void initUI() {
		adapter = new SiteAdapter(getActivity());
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View headView = (View) inflater.inflate(R.layout.view_add, null);
		getListView().addHeaderView(headView);
		Button btn_add = (Button) headView.findViewById(R.id.btn_add);
		btn_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				fListener.LoadAddSiteFragmentListener(
						AddSiteFragment.ADD_DATA_STATUS, null);
			}
		});
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				fListener.LoadAddSiteFragmentListener(
						AddSiteFragment.INFO_DATA_STATUS, MySiteModuleManager
								.instance().getSiteInfos().get(arg2 - 1).key);
			}
		});
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			fListener = (MySiteListFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FragmentListener");
		}
	}
	
	public interface MySiteListFragmentListener {
		public void LoadAddSiteFragmentListener(int status, String key);
		public void LoadAddRemindFragmentListener(int status, String key);
	}

	public void updateAdapter() {
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
				holder.row_button = (Button) convertView.findViewById(R.id.row_button);
				convertView.setTag(holder);
			} else {
				holder = (SiteViewHolder) convertView.getTag();
			}
			
			final SiteInfo sInfo = MySiteModuleManager.instance().getSiteInfos().get(position);
			holder.row_icon.setImageResource(R.drawable.ic_launcher);
			holder.row_name.setText(sInfo.siteName);
			holder.row_detail.setText(sInfo.siteAddress);
			holder.row_button.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					fListener.LoadAddRemindFragmentListener(AddRemindFragment.INFO_DATA_STATUS, sInfo.key);
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
