package com.lbs.guoke;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.lbs.guoke.controller.RemindModuleManager;
import com.lbs.guoke.controller.RemindModuleManager.RemindInfo;
import com.neo.tools.RingTong;
import com.neo.tools.SystemTools;
import com.pad_go.loka.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class AlertDialogActivity extends BaseActivity {
	private ArrayList<String> remindids = new ArrayList<String>();
	private TextView remindTitle, remindMessage, ratio;
	private Button btn_confirm;
	private CheckBox check_remind;
	private int index,max;
	private Timer mTimer = null;
	private String remindid, message;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		setContentView(R.layout.fragment_alert);

		SystemTools.instance().wakeLockStart();
		saveRemindId();
		initUI();
		timer();
	}

	public void onPause() {
		super.onPause();
		SystemTools.instance().wakeLockStop();
		SystemTools.instance().stopVibrator();
	}

	public void onDestroy() {
		if(mTimer != null)
			mTimer.cancel();
		RingTong.stopRing();
		RemindModuleManager.instance().saveRemindTimer(remindids);
		super.onDestroy();
	}
	
	public void onBackPressed(){
		return;
	}

	private void saveRemindId() {
		boolean isVibrate = false;
		for (int i = 0; i < RemindModuleManager.instance()
				.getMatchRemindInfos().size(); i++) {
			remindids.add(RemindModuleManager.instance().getMatchRemindInfos()
					.get(i).remindid);
			if (!isVibrate) {
				if (RemindModuleManager.instance().getMatchRemindInfos().get(i).isVibrate == 1) {
					isVibrate = true;
				}
			}
		}
		if (isVibrate) {
			SystemTools.instance().startVibrator(this);
		}
		RingTong.systemNotificationRing(this, RemindModuleManager.instance().getMatchRemindInfos().get(0).ring);
	}

	private void initUI() {
		ImageView ring = (ImageView) findViewById(R.id.ring);
		AnimationDrawable anim = (AnimationDrawable) ring.getBackground();  
        anim.start(); 
        max = RemindModuleManager.instance().getMatchRemindInfos().size();
        index = 0;
        remindTitle = (TextView)findViewById(R.id.remind_title);
        remindTitle.setText("您已接近"+RemindModuleManager.instance().getMatchRemindInfos().get(0).remindTitle);
        ratio = (TextView)findViewById(R.id.ratio);
        ratio.setText(String.valueOf(index+1)+"/"+RemindModuleManager.instance().getMatchRemindInfos().size());
        remindMessage = (TextView)findViewById(R.id.remind_message);
        remindMessage.setText("\""+RemindModuleManager.instance().getMatchRemindInfos().get(0).remindMessage+"\"");
        remindid = RemindModuleManager.instance().getMatchRemindInfos().get(0).remindid;
        message = RemindModuleManager.instance().getMatchRemindInfos().get(0).remindMessage;
        btn_confirm = (Button)findViewById(R.id.confirm).findViewById(R.id.btn_add);
        btn_confirm.setText("知道了！");
        btn_confirm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				nextRemind();
			}   	
        });
        check_remind = (CheckBox)findViewById(R.id.check_remind);
        
	}
	
	private void nextRemind(){
		for(int i = 0; i < RemindModuleManager.instance().getRemindInfos().size(); i++){
			RemindInfo info = RemindModuleManager.instance().getRemindInfos().get(i);
			if(info.remindid.equals(remindids.get(index))){
				if(!check_remind.isChecked()){
					info.isRemind = 0;
				}
			}
		}
		check_remind.setChecked(false);
		
		if(max <= index+1){
			finish();
			return;
		}		
		index++;		

        remindTitle.setText("您已接近"+RemindModuleManager.instance().getMatchRemindInfos().get(index).remindTitle);
        ratio.setText(String.valueOf(index+1)+"/"+RemindModuleManager.instance().getMatchRemindInfos().size());
        remindMessage.setText(RemindModuleManager.instance().getMatchRemindInfos().get(index).remindMessage);
	}
	
	private void timer(){
		mTimer = new Timer();
		setTimerTask();
	}


	private void setTimerTask() {
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				sHandler.sendEmptyMessage(1);
			}
		}, 45000);
	}
	
	private Handler sHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				startAlertNotify(AlertDialogActivity.this, R.drawable.ic_launcher, "路佳提醒", "", 0,"您有错过的提醒！");
				finish();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	public void startAlertNotify(Context context, int iconId, String title,
			String tickerText, int defaults, String message) {
		Intent intent = new Intent(this, AddRemindActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("remindid", remindid);
		intent.putExtras(bundle);
		PendingIntent pd = PendingIntent.getActivity(context, 0, intent, 0);

		Notification.Builder builder = new Notification.Builder(context);
		Notification baseNF = builder.getNotification();
		baseNF.icon = iconId;
		baseNF.tickerText = title;
		baseNF.defaults = Notification.DEFAULT_ALL;
		builder.setAutoCancel(true);
		baseNF.setLatestEventInfo(context, title, message, pd);
		NotificationManager nm = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		nm.notify(0, baseNF);
	}
	
	private void go2AddRemindFragment(String remindid) {
		Intent i = new Intent(this, AddRemindActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("remindid", remindid);
		i.putExtras(bundle);
		startActivity(i);
		finish();
	}
}
