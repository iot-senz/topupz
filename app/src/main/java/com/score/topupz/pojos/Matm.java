package com.score.topupz.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by eranga on 5/21/16.
 */
public class Matm implements Parcelable {
    String tId;
    String key;

    public Matm(String tId, String key) {
        this.tId = tId;
        this.key = key;
    }

    protected Matm(Parcel in) {
        tId = in.readString();
        key = in.readString();
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static final Creator<Matm> CREATOR = new Creator<Matm>() {
        @Override
        public Matm createFromParcel(Parcel in) {
            return new Matm(in);
        }

        @Override
        public Matm[] newArray(int size) {
            return new Matm[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tId);
        dest.writeString(key);
    }
}
