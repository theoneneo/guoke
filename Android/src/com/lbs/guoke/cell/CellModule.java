package com.lbs.guoke.cell;

import java.util.List;

import com.lbs.guoke.GuoKeApp;

import android.content.Context;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;

public class CellModule {

    private boolean mEnabled = false;
    private RadioLayerProvider radioDataProvider;
    private Context mContext;
    
    public CellModule(Context context) {
	synchronized (this) {
	    mContext = context;
	    radioDataProvider = RadioLayerProvider.getInst(context);
	}
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
		GuoKeApp.getApplication().getCellInfos().clear();		
		CellInfo cellInfo = new CellInfo();
		cellInfo.isCDMA = false;
		cellInfo.cellid = rd.cellId;
		cellInfo.lac = rd.locationAreaCode;
		cellInfo.mnc = rd.mobileNetworkCode;
		cellInfo.mcc = rd.mobileCountryCode;
		GuoKeApp.getApplication().getCellInfos().add(cellInfo);
		getNeiborCells(false, rd.locationAreaCode, rd.mobileNetworkCode, rd.mobileCountryCode);
		GuoKeApp.getApplication().updateCell();
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
		GuoKeApp.getApplication().getCellInfos().clear();		
		CellInfo cellInfo = new CellInfo();
		cellInfo.isCDMA = true;
		cellInfo.cellid = cdmaData.baseStationId;
		cellInfo.lac = cdmaData.networkId;
		cellInfo.mnc = cdmaData.mobileNetworkCode;
		cellInfo.mcc = cdmaData.mobileCountryCode;
		GuoKeApp.getApplication().getCellInfos().add(cellInfo);
		getNeiborCells(true, cdmaData.networkId, cdmaData.mobileNetworkCode, cdmaData.mobileCountryCode);
		GuoKeApp.getApplication().updateCell();
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

    private void getNeiborCells(boolean isCDMA, int lac, int mnc, int mcc) {
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
		    GuoKeApp.getApplication().getCellInfos().add(cellInfo);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    public class CellInfo {
	    public boolean isCDMA = false;
	    public int cellid;
	    public int lac;
	    public int mcc;
	    public int mnc;
    }
}