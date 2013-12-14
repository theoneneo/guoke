package com.lbs.guoke.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.lbs.guoke.R;

public class AddRemindFragment extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_add_remind, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
	}

	private void initUI() {
		View title = getActivity().findViewById(R.id.title);
		Button btn_back = (Button) title.findViewById(R.id.btn_left);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
	}
}
