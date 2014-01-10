package com.lbs.guoke;

import com.lbs.guoke.controller.CellModuleManager;
import com.lbs.guoke.controller.RemindModuleManager;
import com.lbs.guoke.structure.CellInfo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AlertDialogActivity extends Activity {
	private TextView text;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_alert);
		initUI();
	}
	
	private void initUI(){
		text = (TextView)findViewById(R.id.event_desc);
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < RemindModuleManager.instance().getCurRemindInfos().size(); i++){
			buf.append(RemindModuleManager.instance().getCurRemindInfos().get(i).remindMessage);
//			for(int m = 0; m < CellModuleManager.instance().getCellInfos().size(); m++){
//				CellInfo cellInfo = CellModuleManager.instance().getCellInfos().get(m);
//				if(cellInfo.key.equals(RemindModuleManager.instance()
//					.getCurRemindInfos().get(i).key)){
//					buf.append(cellInfo.cellid);
//					buf.append(";");
//				}
//					
//			}
			text.setText(buf.toString());
		}
	}
}
