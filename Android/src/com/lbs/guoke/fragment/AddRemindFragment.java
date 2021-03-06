package com.lbs.guoke.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pad_go.loka.R;
import com.lbs.guoke.controller.MySiteModuleManager;
import com.lbs.guoke.controller.MySiteModuleManager.SiteInfo;
import com.lbs.guoke.controller.RemindModuleManager;
import com.lbs.guoke.controller.RemindModuleManager.RemindInfo;

public class AddRemindFragment extends Fragment {
	public final static int ADD_DATA_STATUS = 0;
	public final static int INFO_DATA_STATUS = 1;
	public final static int MODIFY_DATA_STATUS = 2;

	private TextView titleText, edit_remind;
	private EditText addressEdit;
	private Button btn_ring, btn_bottom_left, btn_bottom_right;
	private CheckBox remind, vibrate;

	private int mStatus;
	private String key = null, remindid = null, ring = null;

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

		edit_remind = (TextView) getActivity().findViewById(R.id.edit_remind);
		addressEdit = (EditText) getActivity().findViewById(R.id.edit_content);
		btn_ring = (Button) getActivity().findViewById(R.id.btn_ring);
		btn_ring.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onChooseSound();
			}
		});
		vibrate = (CheckBox) getActivity().findViewById(
				R.id.vibrate);
		remind = (CheckBox) getActivity().findViewById(R.id.remind);
		btn_bottom_left = (Button) getActivity().findViewById(R.id.bottom)
				.findViewById(R.id.btn_bottom_left);
		btn_bottom_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mStatus == INFO_DATA_STATUS) {
					deleteRemind();
				} else if (mStatus == ADD_DATA_STATUS) {
					getActivity().finish();
				} else if (mStatus == MODIFY_DATA_STATUS) {
					getActivity().finish();
				}
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
	
	protected void onChooseSound() {
		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
				RingtoneManager.TYPE_NOTIFICATION);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置通知铃声");

		startActivityForResult(intent, 0);
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
					edit_remind.setText(siteInfo.siteName);
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
								edit_remind.setText(siteInfo.siteName);
								key = siteInfo.key;
								break;
							}
						}
						addressEdit.setText(remindInfo.remindMessage);
						ring = remindInfo.ring;
						if (remindInfo.isVibrate == 0)
							vibrate.setChecked(false);
						else
							vibrate.setChecked(true);

						if(remindInfo.isRemind ==0)
							remind.setChecked(false);
						else
							remind.setChecked(true);
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
			addressEdit.setEnabled(true);
			btn_ring.setEnabled(true);
			vibrate.setEnabled(true);
			remind.setEnabled(true);
			btn_bottom_left.setText(R.string.cancel);
			btn_bottom_right.setText(R.string.save);
		}
			break;
		case INFO_DATA_STATUS: {
			titleText.setText(R.string.info_remind);
			addressEdit.setEnabled(false);
			btn_ring.setEnabled(false);
			vibrate.setEnabled(false);
			remind.setEnabled(false);
			btn_bottom_left.setText(R.string.delete);
			btn_bottom_right.setText(R.string.modify);
		}
			break;
		case MODIFY_DATA_STATUS: {
			titleText.setText(R.string.modify_remind);
			addressEdit.setEnabled(true);
			btn_ring.setEnabled(true);
			vibrate.setEnabled(true);
			remind.setEnabled(true);
			btn_bottom_left.setText(R.string.cancel);
			btn_bottom_right.setText(R.string.save);
		}
			break;
		default:
			break;
		}
	}

	private void saveRemindInfo() {
		boolean b = vibrate.isChecked();
		int isVibrate = 0;
		if (b) {
			isVibrate = 1;
		}
		boolean bRemind = remind.isChecked();
		int isRemind = 0;
		if(bRemind){
			isRemind = 1;
		}
		String musicUrl = Settings.System.getString(getActivity().getContentResolver(),
				Settings.System.NOTIFICATION_SOUND);
		RemindModuleManager.instance().addRemindInfo(key,
				edit_remind.getText().toString(),
				addressEdit.getText().toString(),
				isRemind, isVibrate, 0, musicUrl);
	}

	private void deleteRemind() {
		new AlertDialog.Builder(getActivity()).setTitle("提醒").setMessage("确认删除").setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				for (int i = 0; i < RemindModuleManager.instance().getRemindInfos()
						.size(); i++) {
					RemindInfo info = RemindModuleManager.instance().getRemindInfos()
							.get(i);
					if (info.key.equals(key)) {
						RemindModuleManager.instance().getRemindInfos().remove(i);
					}
				}
				
				RemindModuleManager.instance().deleteRemindInfo(remindid);
				getActivity().finish();//
			}
		}).create().show();
	}

	private void modifyRemindInfo() {
		boolean b = vibrate.isChecked();
		int isVibrate = 0;
		if (b) {
			isVibrate = 1;
		}
		boolean bRemind = remind.isChecked();
		int isRemind = 0;
		if(bRemind){
			isRemind = 1;
		}
		RemindModuleManager.instance().modifyRemindInfo(remindid, key,
				edit_remind.getText().toString(),
				addressEdit.getText().toString(),
				isRemind, isVibrate, -1, ring);
	}
	
	public void setRing(String ring){
		this.ring = ring;
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
