package com.score.topupz.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO class to hold topup details
 *
 * @author eranga bandara(erangaeb@gmail.com)
 */
public class TopUp implements Parcelable {
    String account;
    String amount;
    String time;

    public TopUp(String account, String amount, String time) {
        this.account = account;
        this.amount = amount;
        this.time = time;
    }

    protected TopUp(Parcel in) {
        account = in.readString();
        amount = in.readString();
        time = in.readString();
    }

    public static final Creator<TopUp> CREATOR = new Creator<TopUp>() {
        @Override
        public TopUp createFromParcel(Parcel in) {
            return new TopUp(in);
        }

        @Override
        public TopUp[] newArray(int size) {
            return new TopUp[size];
        }
    };

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(account);
        dest.writeString(amount);
        dest.writeString(time);
    }
}
