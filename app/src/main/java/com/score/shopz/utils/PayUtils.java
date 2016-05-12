package com.score.shopz.utils;

import com.score.shopz.exceptions.EmptyFieldsException;
import com.score.shopz.exceptions.InvalidAccountException;
import com.score.shopz.exceptions.InvalidBalanceAmountException;
import com.score.shopz.pojos.Summary;
import com.score.shopz.pojos.Pay;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Utility class of transaction activity
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class PayUtils {

    /**
     * Validate transaction form fields
     *
     * @param accountNo
     * @param amount
     */
    public static void validateFields(String accountNo, String amount) throws EmptyFieldsException, NumberFormatException {
        // check empty of fields
        if (accountNo.equals("") || amount.equals("")) {
            throw new EmptyFieldsException();
        }

        // validate amount
        try {
            Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    /**
     * Crete new transaction
     *
     * @param shopNo      user's shop no
     * @param invoiceNumber current receipt no equals to invoice number
     * @param payAmount        transaction amount
     * @return transaction
     * @throws InvalidAccountException
     */
    public static Pay createPay(String shopName, String shopNo, String invoiceNumber, double payAmount) throws InvalidAccountException, InvalidBalanceAmountException {
        Pay pay = new Pay(3,shopName,shopNo,invoiceNumber,payAmount,getCurrentTime().toString());

        return pay;
    }

    /**
     * Calculate current balance
     *
     * @param previousBalance
     * @param transactionAmount
     * @return currentBalance
     */
    private static String getBalanceAmount(String previousBalance, String transactionAmount) throws InvalidBalanceAmountException {
        // calculate and format balance into #.## format
        // cna raise number format exception when parsing client balance
        try {
            double balance = (Double.parseDouble(previousBalance)) + (Double.parseDouble(transactionAmount));
            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            return decimalFormat.format(balance);
        } catch (NumberFormatException e) {
            throw new InvalidBalanceAmountException();
        }
    }

    /**
     * Get current date and time as transaction time
     * format - yyyy/MM/dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTime() {
        //date format
        String DATE_FORMAT_NOW = "yyyy/MM/dd HH:mm:ss";

        // generate time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_NOW);

        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * Generate receipt id according to receipt no and branch id
     *
     * @param branchId  users branch id
     * @param receiptNo receipt no
     * @return receiptId
     */
    public static String getReceiptId(String branchId, int receiptNo) {
        String receiptId;

        if (branchId.length() == 1) {
            receiptId = "0" + branchId + receiptNo;
        } else {
            receiptId = branchId + receiptNo;
        }

        return receiptId;
    }

    /**
     * Get summary as a list of attributes
     *
     * @param payList
     */
    public static Summary getSummary(ArrayList<Pay> payList) {
        int transactionCount = payList.size();

        // get formatted total transaction amount
        String totalPayAmount = "0.00";
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        try {
            totalPayAmount = decimalFormat.format(getTotalPayAmount(payList));
        } catch (NumberFormatException e) {
            System.out.println(e);
        }

        String currentTime = getCurrentTime();

        // new summary
        return new Summary("Branch", Integer.toString(transactionCount), totalPayAmount, currentTime);
    }

    /**
     * Get to total transaction amount
     *
     * @param payList
     * @return deposit count
     */
    public static double getTotalPayAmount(ArrayList<Pay> payList) throws NumberFormatException {
        double total = 0;

        for (int i = 0; i < payList.size(); i++) {
            total = total + payList.get(i).getPayAmount();
        }

        return total;
    }


}