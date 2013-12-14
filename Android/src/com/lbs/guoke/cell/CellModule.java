package com.lbs.guoke.cell;

import java.util.List;

import com.lbs.guoke.controller.CellModuleManager;
import com.lbs.guoke.structure.CellInfo;

import android.content.Context;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;

public class CellModule {

	private boolean mEnabled = false;
	private RadioLayerProvider radioDataProvider;
	private Context mContext;

	public CellModule(Context context) {
		mContext = context;
		radioDataProvider = RadioLayerProvider.getInst(context);
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
			if (rd.cellId > 0 && rd.locationAreaCode > 0) {
				CellModuleManager.instance().getCellInfos().clear();
				CellInfo cellInfo = new CellInfo();
				cellInfo.isCDMA = 0;
				cellInfo.cellid = rd.cellId;
				cellInfo.lac = rd.locationAreaCode;
				cellInfo.mnc = rd.mobileNetworkCode;
				cellInfo.mcc = rd.mobileCountryCode;
				CellModuleManager.instance().getCellInfos().add(cellInfo);
				getNeiborCells(0, rd.locationAreaCode, rd.mobileNetworkCode,
						rd.mobileCountryCode);
				CellModuleManager.instance().UpdateCellData();
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
			if (cdmaData != null) {
				CellModuleManager.instance().getCellInfos().clear();
				CellInfo cellInfo = new CellInfo();
				cellInfo.isCDMA = 1;
				cellInfo.cellid = cdmaData.baseStationId;
				cellInfo.lac = cdmaData.networkId;
				cellInfo.mnc = cdmaData.mobileNetworkCode;
				cellInfo.mcc = cdmaData.mobileCountryCode;
				CellModuleManager.instance().getCellInfos().add(cellInfo);
				getNeiborCells(1, cdmaData.networkId,
						cdmaData.mobileNetworkCode, cdmaData.mobileCountryCode);
				CellModuleManager.instance().UpdateCellData();
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
					CellModuleManager.instance().getCellInfos().add(cellInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}