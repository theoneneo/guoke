package com.lbs.guoke;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pad_go.loka.R;

public class SplashActivity extends BaseActivity {
	private Timer mTimer = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert);
		initUI();
	}
	
	private void initUI(){
		RelativeLayout view = (RelativeLayout)findViewById(R.id.title);
		view.setVisibility(View.INVISIBLE);
		
		mTimer = new Timer();
		setTimerTask();
	}
	
	private void setTimerTask() {
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				sHandler.sendEmptyMessage(1);
			}
		}, 1000);
	}
	
	private Handler sHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				goToMainActivity();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	private void goToMainActivity() {
		Intent i = new Intent(this, MainActivity.class);
		
		startActivity(i);
	}
}
