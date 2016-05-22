package com.score.topupz.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO to hold Bill attributes
 *
 * @author eranga bandara(erangaeb@gmail.com)
 */
public class Bill implements Parcelable {
    String billNo;
    String account;
    String amount;

    public Bill(String billNo, String account, String amount) {
        this.billNo = billNo;
        this.account = account;
        this.amount = amount;
    }

    protected Bill(Parcel in) {
        billNo = in.readString();
        account = in.readString();
        amount = in.readString();
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

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

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(billNo);
        dest.writeString(account);
        dest.writeString(amount);
    }
}
