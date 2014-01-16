package com.lbs.guoke.controller;

import java.util.ArrayList;

import com.lbs.guoke.tempDialogActivity;
import com.lbs.guoke.structure.CellInfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CellInfoBroadCast extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		if ("com.lbs.guoke.cellinfo".equals(arg1.getAction())) {
			getCellInfoProcess(arg0, arg1);
			startTemp(arg0);
		}
	}

	private void getCellInfoProcess(Context arg0, Intent arg1) {
		ArrayList<CellInfo> cellInfos = arg1
				.getParcelableArrayListExtra("cellinfo");
		if (cellInfos != null)
			CellModuleManager.instance().setCellInfos(cellInfos);
	}
	
	private void startTemp(Context arg0){
		Intent i = new Intent(arg0, tempDialogActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		arg0.startActivity(i);
	}

}
