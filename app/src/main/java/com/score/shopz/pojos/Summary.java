package com.score.shopz.pojos;

/**
 * POJO class to hold attributes of transaction summary
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class Summary {
    private String shopNo;
    private String invoiceNo;
    private String totalPayAmount;
    private String time;

    public Summary(String shopNo, String invoiceNo, String totalPayAmount, String time) {
        this.shopNo = shopNo;
        this.invoiceNo = invoiceNo;
        this.totalPayAmount = totalPayAmount;
        this.time = time;
    }

    public String getBranchId() {
        return shopNo;
    }

    public void setBranchId(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getTransactionCount() {
        return invoiceNo;
    }

    public void setTransactionCount(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalTransactionAmount() {
        return totalPayAmount;
    }

    public void setTotalTransactionAmount(String totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }
}
