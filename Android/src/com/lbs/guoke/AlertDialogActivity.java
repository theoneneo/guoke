package com.lbs.guoke;

import com.lbs.guoke.controller.RemindModuleManager;
import com.neo.tools.RingTong;
import com.neo.tools.SystemTools;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AlertDialogActivity extends BaseActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		setContentView(R.layout.fragment_alert);
		
		SystemTools.instance().wakeLockStart();
		SystemTools.instance().startVibrator(this);
		RingTong.systemNotificationRing(this);

		initUI();
	}

	public void onPause() {
		super.onPause();
		SystemTools.instance().wakeLockStop();
		SystemTools.instance().stopVibrator();
	}

	private void initUI() {
		ListView list = (ListView) findViewById(R.id.list);
		RemindAdapter adapter = new RemindAdapter(this);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				go2AddRemindFragment(RemindModuleManager.instance()
						.getMatchRemindInfos().get(arg2).remindid);
			}
		});
	}

	private void go2AddRemindFragment(String remindid) {
		Intent i = new Intent(this, AddRemindActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("remindid", remindid);
		i.putExtras(bundle);
		startActivity(i);
		finish();
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
				convertView = (View) inflater.inflate(R.layout.item_remind_pop,
						parent, false);
				holder = new RemindViewHolder();
				holder.row_message = (TextView) convertView
						.findViewById(R.id.row_message);
				convertView.setTag(holder);
			} else {
				holder = (RemindViewHolder) convertView.getTag();
			}
			holder.row_message.setText(RemindModuleManager.instance()
					.getMatchRemindInfos().get(position).remindTitle);
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return RemindModuleManager.instance().getMatchRemindInfos().size();
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

	static class RemindViewHolder {
		TextView row_message;
	}
}
