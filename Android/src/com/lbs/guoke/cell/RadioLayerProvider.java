package com.lbs.guoke.cell;

import java.util.HashSet;
import java.util.Iterator;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

public class RadioLayerProvider extends PhoneStateListener {

    /** Network types */
    public static final int RADIO_TYPE_UNKNOWN = 0;
    public static final int RADIO_TYPE_GSM = 1;
    public static final int RADIO_TYPE_WCDMA = 2;

    /** Event Signal */
    public static final int EVENT_SIGNAL_CELL_LOCATION = 0x01;
    public static final int EVENT_SIGNAL_STRENGTH = 0x02;
    public static final int EVENT_SIGNAL_SEVICE_STATE = 0x04;

    private int signal = 0;
    private HashSet rilNotifiers = null;

    /** The last known cellLocation */
    private CellLocation cellLocation = null;

    /** The last known signal strength */
    private SignalStrength signalStrength = null;

    /** The last known serviceState */
    private ServiceState serviceState = null;

    /**
     * Our TelephonyManager instance.
     */
    private TelephonyManager telephonyManager;

    private static RadioLayerProvider inst = null;

    /**
     * Public constructor. Uses the activity to get the Context object.
     */
    private RadioLayerProvider(Context context) {
	super();
	telephonyManager = (TelephonyManager) context
		.getSystemService(Context.TELEPHONY_SERVICE);
	if (telephonyManager == null) {
	    throw new NullPointerException(
		    "RadioLayerProvider: telephonyManager is null.");
	}

	rilNotifiers = new HashSet();
    }

    public static RadioLayerProvider getInst(Context context) {
	if (inst == null) {
	    inst = new RadioLayerProvider(context);
	}

	return inst;
    }

    public void open() {
	// Register for cell id, signal strength and service state changed
	// notifications.
	telephonyManager.listen(this, PhoneStateListener.LISTEN_CELL_LOCATION
		| PhoneStateListener.LISTEN_SIGNAL_STRENGTH
		| PhoneStateListener.LISTEN_SERVICE_STATE);
    }

    /**
     * Should be called when the provider is no longer needed.
     */
    public void close() {
	telephonyManager.listen(this, PhoneStateListener.LISTEN_NONE);
    }

    public RadioData getRadioData() {
	cellLocation = telephonyManager.getCellLocation();
	RadioData radioData = RadioData.getInstance(telephonyManager,
		cellLocation, signalStrength, serviceState);
	return radioData;
    }

    @Override
    public void onServiceStateChanged(ServiceState state) {
	serviceState = state;
	signal = EVENT_SIGNAL_SEVICE_STATE;
	notifyListeners();
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength ss) {
	signalStrength = ss;
	signal = EVENT_SIGNAL_STRENGTH;
	notifyListeners();
    }

    @Override
    public void onCellLocationChanged(CellLocation location) {
	cellLocation = location;
	signal = EVENT_SIGNAL_CELL_LOCATION;
	notifyListeners();
    }

    private void notifyRadioChangeListeners(RadioData rd, int signal) {
	try {
	    Iterator iterator = rilNotifiers.iterator();
	    do {
		if (!iterator.hasNext())
		    return;
		RadioLayerNotify rilNotifier = (RadioLayerNotify) iterator
			.next();
		if (rilNotifier != null)
		    rilNotifier.onStateChange(rd, signal);
	    } while (true);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void notifyRadioChangeListeners(CdmaData cd, int signal) {

	try {
	    Iterator iterator = rilNotifiers.iterator();
	    do {
		if (!iterator.hasNext())
		    return;
		RadioLayerNotify rilNotifier = (RadioLayerNotify) iterator
			.next();
		if (rilNotifier != null)
		    rilNotifier.onStateChange(cd, signal);
	    } while (true);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void notifyListeners() {
	int phoneType = telephonyManager.getPhoneType();
	switch (phoneType) {
	// 非电�?3G手机获取 基站信息

	case TelephonyManager.PHONE_TYPE_GSM:
	    RadioData radioData = RadioData.getInstance(telephonyManager,
		    cellLocation, signalStrength, serviceState);
	    if (radioData != null) {
		notifyRadioChangeListeners(radioData, signal);
	    }
	    break;

	// 电信 3G手机直接获取坐标点信息，value*0.25/3600 进行转换�?

	case TelephonyManager.PHONE_TYPE_CDMA:
	    CdmaData cdmaData = CdmaData.getInstanceCdma(telephonyManager,
		    cellLocation, signalStrength, serviceState);
	    if (cdmaData != null) {
		notifyRadioChangeListeners(cdmaData, signal);
	    }

	    break;
	case TelephonyManager.PHONE_TYPE_NONE:
	    break;
	default:
	    RadioData htcRadioData = RadioData.getInstance(telephonyManager,
		    cellLocation, signalStrength, serviceState);
	    if (htcRadioData != null) {
		notifyRadioChangeListeners(htcRadioData, signal);
	    }
	    break;
	}

	/*
	 * RadioData radioData = RadioData.getInstance(telephonyManager,
	 * cellLocation, signalStrength, serviceState); if (radioData != null) {
	 * if(rilNotifier != null) rilNotifier.onStateChange(radioData, signal);
	 * }
	 */
    }

    public void registerRilNotifier(RadioLayerNotify rilnotifier) {
	if (rilNotifiers != null) {
	    if (rilNotifiers.size() == 0) {
		open();
	    }
	    rilNotifiers.add(rilnotifier);
	}
    }

    public void unregisterRilNotifier(RadioLayerNotify rilnotifier) {
	if (rilNotifiers != null) {
	    rilNotifiers.remove(rilnotifier);
	    if (rilNotifiers.size() == 0) {
		close();
	    }
	}
    }
}
