package com.example.will.cachedateproject;

import android.os.Parcel;
import android.os.Parcelable;

public class TotalAssignNumBean implements Parcelable{

    private String name;
    private String value;

    protected TotalAssignNumBean(Parcel in) {
        name = in.readString();
        value = in.readString();
    }

    public static final Creator<TotalAssignNumBean> CREATOR = new Creator<TotalAssignNumBean>() {
        @Override
        public TotalAssignNumBean createFromParcel(Parcel in) {
            return new TotalAssignNumBean(in);
        }

        @Override
        public TotalAssignNumBean[] newArray(int size) {
            return new TotalAssignNumBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(value);
    }
}
