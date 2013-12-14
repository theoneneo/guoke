package com.lbs.guoke.fragment;

import com.lbs.guoke.R;
import com.lbs.guoke.controller.MySiteModuleManager;
import com.lbs.guoke.fragment.RemindListFragment.RemindViewHolder;

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

public class MySiteListFragment extends ListFragment {
	private SiteAdapter adapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new SiteAdapter(getActivity());
		setListAdapter(adapter);
	}
	
	public void updateAdapter(){
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
				holder.row_title = (TextView) convertView
						.findViewById(R.id.row_title);
				convertView.setTag(holder);
			} else {
				holder = (SiteViewHolder) convertView.getTag();
			}
			holder.row_icon.setImageResource(R.drawable.ic_launcher);
			holder.row_title.setText(MySiteModuleManager.instance()
					.getSiteInfos().get(position).siteName);
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
		TextView row_title;
	}
}
