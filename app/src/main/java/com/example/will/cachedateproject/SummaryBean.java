package com.example.will.cachedateproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SummaryBean implements Parcelable{

    private int total_noassign_num;
    private TotalAssignNumBean total_assign_num;
    private ArrayList<TotalAssignNumBean> this_month_summary;

    protected SummaryBean(Parcel in) {
        total_noassign_num = in.readInt();
        total_assign_num = in.readParcelable(TotalAssignNumBean.class.getClassLoader());
        this_month_summary = in.createTypedArrayList(TotalAssignNumBean.CREATOR);
    }

    public static final Creator<SummaryBean> CREATOR = new Creator<SummaryBean>() {
        @Override
        public SummaryBean createFromParcel(Parcel in) {
            return new SummaryBean(in);
        }

        @Override
        public SummaryBean[] newArray(int size) {
            return new SummaryBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(total_noassign_num);
        parcel.writeParcelable(total_assign_num, i);
        parcel.writeTypedList(this_month_summary);
    }
}
