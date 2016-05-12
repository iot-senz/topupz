package com.score.shopz.db;

import android.provider.BaseColumns;

/**
 * Created by root on 11/19/15.
 */
public class SenzorsDbContract {

    public static abstract class Pay implements BaseColumns {
        public static final String TABLE_NAME = "pay_table";
        public static final String COLUMN_NAME_shopName = "shop_name";
        public static final String COLUMN_NAME_shopNo = "shop_no";
        public static final String COLUMN_NAME_invoiceNumber = "invoice_number";
        public static final String COLUMN_NAME_payAmount = "pay_amount";
        public static final String COLUMN_NAME_payTime = "pay_time";


    }
    public static abstract class MetaData implements BaseColumns {
        public static final String TABLE_NAME = "metadata";
        public static final String COLUMN_NAME_DATA = "data_name";
        public static final String COLUMN_NAME_VALUE = "value";
    }
}