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
		}
	}

	private void getCellInfoProcess(Context arg0, Intent arg1) {
		ArrayList<CellInfo> cellInfos = arg1
				.getParcelableArrayListExtra("cellinfo");
		if (cellInfos != null)
			CellModuleManager.instance().setCellInfos(cellInfos);
	}

}
