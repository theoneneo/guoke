package com.lbs.guoke;

import com.lbs.guoke.controller.CellModuleManager;
import com.lbs.guoke.controller.RemindModuleManager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class tempDialogActivity extends Activity {
	private TextView text;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_alert);
		initUI();
	}
	
	private void initUI(){
		text = (TextView)findViewById(R.id.event_desc);
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < CellModuleManager.instance().getCellInfos().size(); i++){
			buf.append(CellModuleManager.instance().getCellInfos().get(i).cellid);
			buf.append(";");
		}
		text.setText(buf.toString());
	}
}
