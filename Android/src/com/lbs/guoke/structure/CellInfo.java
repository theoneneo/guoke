package com.lbs.guoke.structure;

import android.os.Parcel;
import android.os.Parcelable;

public class CellInfo implements Parcelable {
	public String key = null;
	public int isCDMA;
	public int cellid;
	public int lac;
	public int mcc;
	public int mnc;

	public CellInfo() {

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		arg0.writeInt(isCDMA);
		arg0.writeInt(cellid);
		arg0.writeInt(lac);
		arg0.writeInt(mcc);
		arg0.writeInt(mnc);
	}

	public static final Parcelable.Creator<CellInfo> CREATOR = new Creator<CellInfo>() {
		// 实现从source中创建出类的实例的功能
		@Override
		public CellInfo createFromParcel(Parcel source) {
			CellInfo parInfo = new CellInfo();
			parInfo.isCDMA = source.readInt();
			parInfo.cellid = source.readInt();
			parInfo.lac = source.readInt();
			parInfo.mcc = source.readInt();
			parInfo.mnc = source.readInt();
			return parInfo;
		}

		// 创建一个类型为T，长度为size的数组
		@Override
		public CellInfo[] newArray(int size) {
			return new CellInfo[size];
		}
	};
}
