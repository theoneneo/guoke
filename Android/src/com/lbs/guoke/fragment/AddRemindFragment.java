package com.lbs.guoke.fragment;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.lbs.guoke.R;
import com.lbs.guoke.controller.MySiteModuleManager;
import com.lbs.guoke.controller.MySiteModuleManager.SiteInfo;
import com.lbs.guoke.controller.RemindModuleManager;
import com.lbs.guoke.controller.RemindModuleManager.RemindInfo;

public class AddRemindFragment extends Fragment {
	public final static int ADD_DATA_STATUS = 0;
	public final static int INFO_DATA_STATUS = 1;
	public final static int MODIFY_DATA_STATUS = 2;

	private TextView titleText, addressText;
	// private Spinner spin_name;
	private EditText edit_remind;
	private Button btn_ring, btn_bottom_left, btn_bottom_right;
	private Switch switch_vibrate;

	private int mStatus;
	private String key = null, remindid = null, remindMessage = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_add_remind, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
		initData();
	}

	private void initUI() {
		titleText = (TextView) getActivity().findViewById(R.id.title)
				.findViewById(R.id.text_title);
		ImageButton btn_back = (ImageButton) getActivity().findViewById(
				R.id.title).findViewById(R.id.btn_left);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});

		// spin_name = (Spinner) getActivity().findViewById(R.id.spin_address);
		// spin_name.setAdapter(new SiteNameAdapter());
		// spin_name.setOnItemSelectedListener(new OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// // TODO Auto-generated method stub
		// key = MySiteModuleManager.instance().getSiteInfos().get(arg2).key;
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		addressText = (TextView) getActivity().findViewById(
				R.id.text_address_message);
		edit_remind = (EditText) getActivity().findViewById(R.id.edit_remind);
		btn_ring = (Button) getActivity().findViewById(R.id.btn_ring);
		btn_ring.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		switch_vibrate = (Switch) getActivity().findViewById(
				R.id.switch_vibrate);

		btn_bottom_left = (Button) getActivity().findViewById(R.id.bottom)
				.findViewById(R.id.btn_bottom_left);
		btn_bottom_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		btn_bottom_right = (Button) getActivity().findViewById(R.id.bottom)
				.findViewById(R.id.btn_bottom_right);
		btn_bottom_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mStatus == INFO_DATA_STATUS) {
					setStatus(MODIFY_DATA_STATUS);
				} else if (mStatus == ADD_DATA_STATUS) {
					saveRemindInfo();
					getActivity().setResult(getActivity().RESULT_OK, null);
					getActivity().finish();
				} else if (mStatus == MODIFY_DATA_STATUS) {
					modifyRemindInfo();
					setStatus(INFO_DATA_STATUS);
				}
			}
		});
	}

	private void initData() {
		mStatus = ADD_DATA_STATUS;
		key = getArguments().getString("key");
		remindid = getArguments().getString("remindid");

		if (remindid == null) {
			for (int m = 0; m < MySiteModuleManager.instance()
					.getSiteInfos().size(); m++) {
				SiteInfo siteInfo = MySiteModuleManager.instance()
						.getSiteInfos().get(m);
				if (siteInfo.key.equals(key)) {
					addressText.setText(siteInfo.siteName);
					key = siteInfo.key;
					break;
				}
			}
		} else {
			for (int i = 0; i < RemindModuleManager.instance().getRemindInfos()
					.size(); i++) {
				RemindInfo remindInfo = RemindModuleManager.instance()
						.getRemindInfos().get(i);
				if (remindid != null) {
					if (remindInfo.remindid.equals(remindid)) {
						for (int m = 0; m < MySiteModuleManager.instance()
								.getSiteInfos().size(); m++) {
							SiteInfo siteInfo = MySiteModuleManager.instance()
									.getSiteInfos().get(m);
							if (siteInfo.key.equals(remindInfo.key)) {
								addressText.setText(siteInfo.siteName);
								key = siteInfo.key;
								break;
							}
						}
						edit_remind.setText(remindInfo.remindTitle);
						if (remindInfo.isVibrate == 0)
							switch_vibrate.setChecked(false);
						else
							switch_vibrate.setChecked(true);

						mStatus = INFO_DATA_STATUS;
						break;
					}
				}
			}
		}
		setStatus(mStatus);
	}

	public void setStatus(int status) {
		mStatus = status;
		switch (mStatus) {
		case ADD_DATA_STATUS: {
			titleText.setText(R.string.add_remind);
			// spin_name.setEnabled(true);
			edit_remind.setEnabled(true);
			btn_ring.setEnabled(true);
			switch_vibrate.setEnabled(true);
			btn_bottom_left.setVisibility(View.VISIBLE);
			btn_bottom_right.setText(R.string.save);
		}
			break;
		case INFO_DATA_STATUS: {
			titleText.setText(R.string.info_remind);
			// spin_name.setEnabled(false);
			edit_remind.setEnabled(false);
			btn_ring.setEnabled(false);
			switch_vibrate.setEnabled(false);
			btn_bottom_left.setVisibility(View.GONE);
			btn_bottom_right.setText(R.string.modify);
		}
			break;
		case MODIFY_DATA_STATUS: {
			titleText.setText(R.string.modify_remind);
			// spin_name.setEnabled(true);
			edit_remind.setEnabled(true);
			btn_ring.setEnabled(true);
			switch_vibrate.setEnabled(true);
			btn_bottom_left.setVisibility(View.VISIBLE);
			btn_bottom_right.setText(R.string.save);
		}
			break;
		default:
			break;
		}
	}

	private void saveRemindInfo() {
		boolean b = switch_vibrate.isChecked();
		int isVibrate = 0;
		if (b) {
			isVibrate = 1;
		}
		RemindModuleManager.instance().addRemindInfo(key,
				edit_remind.getText().toString(),
				addressText.getText().toString(),
				0, isVibrate, 0);
	}

	private void modifyRemindInfo() {
		boolean b = switch_vibrate.isChecked();
		int isVibrate = 0;
		if (b) {
			isVibrate = 1;
		}
		RemindModuleManager.instance().modifyRemindInfo(remindid, key,
				edit_remind.getText().toString(),
				addressText.getText().toString(),
				-1, isVibrate, -1);
	}

	public class SiteNameAdapter implements SpinnerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return MySiteModuleManager.instance().getSiteInfos().size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return MySiteModuleManager.instance().getSiteInfos().get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = new TextView(getActivity());
			}
			((TextView) convertView).setText(MySiteModuleManager.instance()
					.getSiteInfos().get(position).siteName);
			return convertView;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub

		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub

		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			return getView(position, convertView, parent);
		}
	}
}
