package com.lbs.guoke.fragment;

import com.lbs.guoke.R;
import com.lbs.guoke.controller.CellModuleManager;
import com.lbs.guoke.controller.MySiteModuleManager;
import com.lbs.guoke.controller.RemindModuleManager;
import com.lbs.guoke.controller.RemindModuleManager.RemindInfo;
import com.lbs.guoke.fragment.MySiteListFragment.MySiteListFragmentListener;
import com.lbs.guoke.fragment.MySiteListFragment.SiteAdapter;
import com.lbs.guoke.structure.CellInfo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RemindListFragment extends ListFragment {
	private RemindAdapter adapter;
	private RemindListFragmentListener fListener;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
	}

	private void initUI() {
		adapter = new RemindAdapter(getActivity());
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View headView = (View) inflater.inflate(R.layout.view_add, null);
		getListView().addHeaderView(headView);
		Button btn_add = (Button) headView.findViewById(R.id.btn_add);
		btn_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				fListener.LoadAddRemindFragmentListener(
						AddRemindFragment.ADD_DATA_STATUS, null);
			}
		});
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				fListener.LoadAddRemindFragmentListener(
						AddRemindFragment.INFO_DATA_STATUS, RemindModuleManager
								.instance().getRemindInfos().get(arg2 - 1).key);
			}
		});
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			fListener = (RemindListFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FragmentListener");
		}
	}

	public interface RemindListFragmentListener {
		public void LoadAddRemindFragmentListener(int status, String key);
	}

	public void updateAdapter() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	public class RemindAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context mContext;

		public RemindAdapter(Context context) {
			mContext = context;
			inflater = LayoutInflater.from(mContext);
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			RemindViewHolder holder;
			if (convertView == null) {
				convertView = (View) inflater.inflate(R.layout.item_remind,
						parent, false);
				holder = new RemindViewHolder();
				holder.row_title = (TextView) convertView
						.findViewById(R.id.row_title);
				holder.row_menssage = (TextView) convertView
						.findViewById(R.id.row_message);
				holder.row_switch = (Switch) convertView
						.findViewById(R.id.row_switch);
				convertView.setTag(holder);
			} else {
				holder = (RemindViewHolder) convertView.getTag();
			}
			
			holder.row_title.setText(RemindModuleManager.instance()
					.getRemindInfos().get(position).remindTitle);
			holder.row_menssage.setText(RemindModuleManager.instance()
					.getRemindInfos().get(position).remindMessage);
			if (RemindModuleManager.instance().getRemindInfos().get(position).isRemind == 1)
				holder.row_switch.setChecked(true);
			else
				holder.row_switch.setChecked(false);
			holder.row_switch
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton arg0,
								boolean arg1) {
							// TODO Auto-generated method stub
							if (arg1)
								RemindModuleManager.instance().getRemindInfos()
										.get(position).isRemind = 1;
							else
								RemindModuleManager.instance().getRemindInfos()
										.get(position).isRemind = 0;
							
							RemindModuleManager.instance().matchRemindInfo();
						}
					});
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
		TextView row_title;
		TextView row_menssage;
		Switch row_switch;
	}
}
