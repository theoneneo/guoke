package com.lbs.guoke.cell;

import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

/** Simple container for radio data */
public final class RadioData {
    public int cellId = -1;
    public int locationAreaCode = -1;
    public SignalStrength signalStrength = null;
    public int mobileCountryCode = -1;
    public int mobileNetworkCode = -1;
    public int homeMobileCountryCode = -1;
    public int homeMobileNetworkCode = -1;
    public int radioType = RadioLayerProvider.RADIO_TYPE_UNKNOWN;
    public String carrierName;

    /**
     * Constructs radioData object from the given telephony data.
     * 
     * @param telephonyManager
     *            contains the TelephonyManager instance.
     * @param cellLocation
     *            contains information about the current GSM cell.
     * @param signalStrength
     *            is the strength of the network signal.
     * @param serviceState
     *            contains information about the network service.
     * @return a new RadioData object populated with the currently available
     *         network information or null if there isn't enough information.
     */
    public static RadioData getInstance(TelephonyManager telephonyManager,
	    CellLocation cellLocation, SignalStrength signalStrength,
	    ServiceState serviceState) {

	if (!(cellLocation instanceof GsmCellLocation)) {
	    // This also covers the case when cellLocation is null.
	    // When that happens, we do not bother creating a
	    // RadioData instance.
	    return null;
	}

	RadioData radioData = new RadioData();
	GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;

	// Extract the cell id, LAC, and signal strength.
	radioData.cellId = gsmCellLocation.getCid();
	radioData.locationAreaCode = gsmCellLocation.getLac();
	radioData.signalStrength = signalStrength;
	// Extract the home MCC and home MNC.
	String operator = telephonyManager.getNetworkOperator();
	radioData.setMobileCodes(operator, true);

	if (serviceState != null) {
	    // Extract the carrier name.
	    radioData.carrierName = serviceState.getOperatorAlphaLong();

	    // Extract the MCC and MNC.
	    operator = serviceState.getOperatorNumeric();
	    radioData.setMobileCodes(operator, false);
	}

	// Finally get the radio type.
	int type = telephonyManager.getNetworkType();
	if (type == TelephonyManager.NETWORK_TYPE_UMTS) {
	    radioData.radioType = RadioLayerProvider.RADIO_TYPE_WCDMA;
	} else if (type == TelephonyManager.NETWORK_TYPE_GPRS
		|| type == TelephonyManager.NETWORK_TYPE_EDGE) {
	    radioData.radioType = RadioLayerProvider.RADIO_TYPE_GSM;
	}
	return radioData;
    }

    private RadioData() {
    }

    /**
     * Parses a string containing a mobile country code and a mobile network
     * code and sets the corresponding member variables.
     * 
     * @param codes
     *            is the string to parse.
     * @param homeValues
     *            flags whether the codes are for the home operator.
     */
    private void setMobileCodes(String codes, boolean homeValues) {
	if (codes != null) {
	    try {
		// The operator numeric format is 3 digit country code plus 2 or
		// 3 digit network code.
		int mcc = Integer.parseInt(codes.substring(0, 3));
		int mnc = Integer.parseInt(codes.substring(3));
		if (homeValues) {
		    homeMobileCountryCode = mcc;
		    homeMobileNetworkCode = mnc;

		    mobileCountryCode = mcc;
		    mobileNetworkCode = mnc;
		} else {
		    mobileCountryCode = mcc;
		    mobileNetworkCode = mnc;
		}
	    } catch (IndexOutOfBoundsException ex) {
		mobileCountryCode = 460;
		mobileNetworkCode = 0;
	    } catch (NumberFormatException ex) {
		mobileCountryCode = 460;
		mobileNetworkCode = 0;
	    }
	}
    }
};