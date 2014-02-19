package com.lbs.guoke;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class tempDialogActivity extends BaseActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert);
		initUI();
	}
	
	private void initUI(){
		TextView titleText = (TextView) findViewById(R.id.title)
				.findViewById(R.id.text_title);
		titleText.setText("关于路佳");
		ImageButton btn_back = (ImageButton)findViewById(
				R.id.title).findViewById(R.id.btn_left);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
