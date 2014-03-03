package com.lbs.guoke.cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.lbs.guoke.controller.LoKaService;
import com.lbs.guoke.structure.CellInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CellModule {
	private static Timer timer = null;
	private boolean mEnabled = false;
	private RadioLayerProvider radioDataProvider;
	private long lastAutoCellTime = 0;
	private Context mContext;
	private static final int AUTO_CELL_CHANGE_INTERVAL = 8000;
	private static ArrayList<CellInfo> mCellInfos = new ArrayList<CellInfo>();

	public CellModule(LoKaService service) {
		mContext = service.getBaseContext();
		radioDataProvider = RadioLayerProvider.getInst(mContext);
	}

	private RadioLayerNotify radioDataNotify = new RadioLayerNotify() {
		public void onStateChange(RadioData rd, int signal) {
			onRadioDataChange(rd, signal);
		}

		public void onStateChange(CdmaData cdmaData, int signal) {
			onCdmaDataChange(cdmaData, signal);
		}
	};

	private void onRadioDataChange(RadioData rd, int signal) {
		switch (signal) {
		case RadioLayerProvider.EVENT_SIGNAL_CELL_LOCATION: {
			if (lastAutoCellTime != 0) {
				long cur_time = System.currentTimeMillis();
				if (rd.locationAreaCode != 0 && rd.cellId != 0) {
					if (cur_time - lastAutoCellTime <= AUTO_CELL_CHANGE_INTERVAL) {
						return;
					}
				}
			}
			
			lastAutoCellTime = System.currentTimeMillis();
			
			if (rd.cellId > 0 && rd.locationAreaCode > 0) {
				mCellInfos.clear();
				CellInfo cellInfo = new CellInfo();
				cellInfo.isCDMA = 0;
				cellInfo.cellid = rd.cellId;
				cellInfo.lac = rd.locationAreaCode;
				cellInfo.mnc = rd.mobileNetworkCode;
				cellInfo.mcc = rd.mobileCountryCode;
				mCellInfos.add(cellInfo);
				getNeiborCells(0, rd.locationAreaCode, rd.mobileNetworkCode,
						rd.mobileCountryCode);
				waitCellInfoBroadCast();
			} else {
			}
		}
			break;
		case RadioLayerProvider.EVENT_SIGNAL_SEVICE_STATE:
			break;
		case RadioLayerProvider.EVENT_SIGNAL_STRENGTH:
			break;
		}
	}

	private void onCdmaDataChange(CdmaData cdmaData, int signal) {
		switch (signal) {
		case RadioLayerProvider.EVENT_SIGNAL_CELL_LOCATION:
			if (lastAutoCellTime != 0) {
				long cur_time = System.currentTimeMillis();
				if (cdmaData.networkId != 0 && cdmaData.baseStationId != 0) {
					if (cur_time - lastAutoCellTime <= AUTO_CELL_CHANGE_INTERVAL) {
						return;
					}
				}
			}
			
			lastAutoCellTime = System.currentTimeMillis();
			
			if (cdmaData != null) {
				mCellInfos.clear();
				CellInfo cellInfo = new CellInfo();
				cellInfo.isCDMA = 1;
				cellInfo.cellid = cdmaData.baseStationId;
				cellInfo.lac = cdmaData.networkId;
				cellInfo.mnc = cdmaData.mobileNetworkCode;
				cellInfo.mcc = cdmaData.mobileCountryCode;
				mCellInfos.add(cellInfo);
				getNeiborCells(1, cdmaData.networkId,
						cdmaData.mobileNetworkCode, cdmaData.mobileCountryCode);
				waitCellInfoBroadCast();
			} else {
			}
			break;
		case RadioLayerProvider.EVENT_SIGNAL_STRENGTH:
			break;
		}
	}

	/**
	 * Enables this provider. When enabled, calls to getStatus() must be
	 * handled. Hardware may be started up when the provider is enabled.
	 */
	public synchronized void enable() {
		if (mEnabled)
			return;
		mEnabled = true;
		radioDataProvider.registerRilNotifier(radioDataNotify);
	}

	/**
	 * Disables this provider. When disabled, calls to getStatus() need not be
	 * handled. Hardware may be shut down while the provider is disabled.
	 */
	public synchronized void disable() {
		if (!mEnabled)
			return;
		mEnabled = false;
		radioDataProvider.unregisterRilNotifier(radioDataNotify);
	}

	public boolean isEnabled() {
		return mEnabled;
	}

	private void getNeiborCells(int isCDMA, int lac, int mnc, int mcc) {
		TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			List<NeighboringCellInfo> list = tm.getNeighboringCellInfo();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					NeighboringCellInfo ci = list.get(i);
					CellInfo cellInfo = new CellInfo();
					cellInfo.isCDMA = isCDMA;
					cellInfo.cellid = ci.getCid();
					cellInfo.lac = lac;
					cellInfo.mnc = mnc;
					cellInfo.mcc = mcc;
					mCellInfos.add(cellInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void waitCellInfoBroadCast(){
		if(timer != null){
			timer.cancel();
			timer = null;
		}
		timer = new Timer();
        timer.schedule(new RemindTask(), 100);
	}
	
	private void sendCellInfoBroadCast(){	
        Intent intent = new Intent("com.lbs.guoke.cellinfo");
        Bundle bundle = new Bundle();  
        bundle.putParcelableArrayList("cellinfo", mCellInfos);
        intent.putExtras(bundle);  
        mContext.sendBroadcast(intent);
	}
	
	public void stopCellBroadCastSchedule(){
		if(timer != null){
			timer.cancel();
			timer = null;
		}
	}
	
	class RemindTask extends TimerTask {
        public void run() {
        	sendCellInfoBroadCast();
        }
    }
}