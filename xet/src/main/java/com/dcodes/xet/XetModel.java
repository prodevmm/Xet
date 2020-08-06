package com.dcodes.xet;

import android.os.Parcel;
import android.os.Parcelable;

public class XetModel implements Parcelable {
    private String sdUrl, hdUrl;

    public XetModel(String sdUrl, String hdUrl) {
        this.sdUrl = sdUrl;
        this.hdUrl = hdUrl;
    }

    private XetModel(Parcel in) {
        sdUrl = in.readString();
        hdUrl = in.readString();
    }

    public static final Creator<XetModel> CREATOR = new Creator<XetModel>() {
        @Override
        public XetModel createFromParcel(Parcel in) {
            return new XetModel(in);
        }

        @Override
        public XetModel[] newArray(int size) {
            return new XetModel[size];
        }
    };

    public String getSdUrl() {
        return sdUrl;
    }

    public String getHdUrl() {
        return hdUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sdUrl);
        dest.writeString(hdUrl);
    }
}
