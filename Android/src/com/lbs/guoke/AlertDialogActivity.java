package com.lbs.guoke;

import com.lbs.guoke.controller.CellModuleManager;
import com.lbs.guoke.controller.RemindModuleManager;
import com.lbs.guoke.structure.CellInfo;
import com.neo.tools.SystemTools;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AlertDialogActivity extends BaseActivity {
	private TextView text;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_alert);
		SystemTools.instance().wakeLockStart();
		initUI();
	}
	
	public void onDestroy(){
		super.onDestroy();
		SystemTools.instance().wakeLockStop();
	}
	
	private void initUI(){
		text = (TextView)findViewById(R.id.event_desc);
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < RemindModuleManager.instance().getMatchRemindInfos().size(); i++){
			buf.append(RemindModuleManager.instance().getMatchRemindInfos().get(i).remindTitle+"=====");
			for(int m = 0; m < CellModuleManager.instance().getDBCellInfos().size(); m++){
				CellInfo cellInfo = CellModuleManager.instance().getDBCellInfos().get(m);
				if(cellInfo.key.equals(RemindModuleManager.instance()
					.getMatchRemindInfos().get(i).key)){
					buf.append(cellInfo.cellid);
					buf.append(";");
				}
					
			}
			buf.append("=====");
			for(int m = 0; m < CellModuleManager.instance().getCellInfos().size(); m++){
				CellInfo cellInfo = CellModuleManager.instance().getCellInfos().get(m);
				buf.append(cellInfo.cellid);
				buf.append(";");	
			}			
			text.setText(buf.toString());
		}
	}
}
