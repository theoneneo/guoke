package com.lbs.guoke.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
	
	private TextView titleText;
	private Spinner spin_address;
	private EditText edit_remind;
	private Button btn_ring, btn_bottom_left, btn_bottom_right;
	private Switch switch_vibrate;
	
	private int mStatus;
	private String key, remindText;
	private String siteName[];
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_add_remind, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mStatus = getArguments().getInt("status", 0);
		key = getArguments().getString("key");
		if (INFO_DATA_STATUS == mStatus) {
			for (int i = 0; i < RemindModuleManager.instance().getRemindInfos()
					.size(); i++) {
				RemindInfo remindInfo = RemindModuleManager.instance()
						.getRemindInfos().get(i);
				if (remindInfo.key.equals(key)) {
					
					break;
				}
			}
		}
		initUI();
	}

	private void initUI() {
		titleText = (TextView) getActivity().findViewById(R.id.title)
				.findViewById(R.id.text_title);
		ImageButton btn_back = (ImageButton) titleText
				.findViewById(R.id.btn_left);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		
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
			}
		});
	}
}
