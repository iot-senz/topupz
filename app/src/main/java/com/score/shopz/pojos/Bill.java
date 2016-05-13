package com.score.shopz.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chathura on 5/11/16.
 */
public class Bill implements Parcelable {

    int id;
    String shopName;
    String shopNo;
    String invoiceNumber;
    double payAmount;
    String payTime;

    public Bill(int id,
                String shopName,
                String shopNo,
                String invoiceNumber,
                double payAmount,
                String payTime) {
        this.id = id;
        this.shopName = shopName;
        this.shopNo = shopNo;
        this.invoiceNumber = invoiceNumber;
        this.payAmount = payAmount;
        this.payTime = payTime;
    }

    protected Bill(Parcel in) {
        id = in.readInt();
        shopName = in.readString();
        shopNo = in.readString();
        invoiceNumber = in.readString();
        payAmount = in.readDouble();
        payTime = in.readString();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shopName);
        dest.writeString(shopNo);
        dest.writeString(invoiceNumber);
        dest.writeDouble(payAmount);
        dest.writeString(payTime);
    }
}
