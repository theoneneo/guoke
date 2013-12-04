package com.lbs.guoke.cell;

import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;

public final class CdmaData {

    public int baseStationLatitude = 0;
    public int baseStationLongitude = 0;
    public int baseStationId = -1;
    public int networkId = -1;
    public int systemId = -1;
    public int mobileCountryCode = -1;
    public int mobileNetworkCode = -1;
    public int homeMobileCountryCode = -1;
    public int homeMobileNetworkCode = -1;
    public SignalStrength strength = null;

    public static CdmaData getInstanceCdma(TelephonyManager telephonyManager,
	    CellLocation cellLocation, SignalStrength signalStrength,
	    ServiceState serviceState) {

	if (!(cellLocation instanceof CdmaCellLocation)) {

	    return null;
	}
	CdmaCellLocation cdmacellLocation = (CdmaCellLocation) cellLocation;

	CdmaData cdmaData = new CdmaData();
	int BaseStationLatitude = cdmacellLocation.getBaseStationLatitude();
	int BaseStationLongitude = cdmacellLocation.getBaseStationLongitude();

	cdmaData.baseStationLatitude = BaseStationLatitude;
	cdmaData.baseStationLongitude = BaseStationLongitude;

	// Extract the home MCC and home MNC.
	String operator = telephonyManager.getNetworkOperator();
	cdmaData.setMobileCodes(operator, true);

	if (serviceState != null) {
	    // Extract the MCC and MNC.
	    operator = serviceState.getOperatorNumeric();
	    cdmaData.setMobileCodes(operator, false);
	}

	cdmaData.systemId = cdmacellLocation.getSystemId();
	cdmaData.networkId = cdmacellLocation.getNetworkId();
	cdmaData.baseStationId = cdmacellLocation.getBaseStationId();
	cdmaData.strength = signalStrength;

	return cdmaData;
    }

    private CdmaData() {
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
		mobileNetworkCode = 1;

	    } catch (NumberFormatException ex) {

		mobileCountryCode = 460;
		mobileNetworkCode = 1;
	    }
	}
    }
}
