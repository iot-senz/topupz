package com.score.shopz.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//import com.score.payz.pojos.Summary;
//import com.score.payz.pojos.Transaction;

import com.score.shopz.pojos.Bill;

/**
 * Created by root on 11/19/15.
 */
public class SenzorsDbSource {
    private static final String TAG = SenzorsDbSource.class.getName();
    private static Context context;

    public SenzorsDbSource(Context context) {
        Log.d(TAG, "Init: db source");
        this.context = context;
    }

    public void createPay(Bill bill) {
        SQLiteDatabase db = SenzorsDbHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SenzorsDbContract.Pay.COLUMN_NAME_shopNo, bill.getShopNo());
        values.put(SenzorsDbContract.Pay.COLUMN_NAME_shopName, bill.getShopName());
        values.put(SenzorsDbContract.Pay.COLUMN_NAME_invoiceNumber, bill.getInvoiceNumber());
        values.put(SenzorsDbContract.Pay.COLUMN_NAME_payAmount, bill.getPayAmount());
        values.put(SenzorsDbContract.Pay.COLUMN_NAME_payTime, bill.getPayTime());

        long id = db.insert(SenzorsDbContract.Pay.TABLE_NAME, null, values);
        db.close();

    }

    /*public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> sensorList = new ArrayList();

        SQLiteDatabase db = SenzorsDbHelper.getInstance(context).getReadableDatabase();

        // join query to retrieve data
        String query = "SELECT * " +
                "FROM " + SenzorsDbContract.Transaction.TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);
// sensor/user attributes
        int id;
        String clientName;
        String clientNic;
        String clientAccountNo;
        String previousBalance;
        int transactionAmount;
        String transactionTime;
        String transactionType;
        Log.e(TAG, cursor.getCount() + "f");
        // extract attributes
        while (cursor.moveToNext()) {
            HashMap<String, String> senzAttributes = new HashMap<>();

            // get senz attributes

            id = cursor.getInt(cursor.getColumnIndex(SenzorsDbContract.Transaction._ID));

            clientName = cursor.getString(cursor.getColumnIndex(SenzorsDbContract.Transaction.COLUMN_NAME_CLIENTNAME));

            clientAccountNo = cursor.getString(cursor.getColumnIndex(SenzorsDbContract.Transaction.COLUMN_NAME_clientAccountNo));

            previousBalance = cursor.getString(cursor.getColumnIndex(SenzorsDbContract.Transaction.COLUMN_NAME_previousBalance));

            transactionAmount = cursor.getInt(cursor.getColumnIndex(SenzorsDbContract.Transaction.COLUMN_NAME_transactionAmount));

            transactionTime = cursor.getString(cursor.getColumnIndex(SenzorsDbContract.Transaction.COLUMN_NAME_transactionTime));

            transactionType = cursor.getString(cursor.getColumnIndex(SenzorsDbContract.Transaction.COLUMN_NAME_transactionType));

            clientNic = cursor.getString(cursor.getColumnIndex(SenzorsDbContract.Transaction.COLUMN_NAME_clientNIC));
            System.out.println(id + " " + clientName + " " + clientAccountNo + " " + previousBalance + " " + transactionAmount + " " + transactionTime + " " + transactionType);
            Transaction transaction = new Transaction(id, clientName, clientNic, clientAccountNo, previousBalance, transactionAmount, transactionTime, transactionType);
            //senzAttributes.put(_senzName, _senzValue);

*//*

            Log.d(TAG,cursor.getColumnIndex(SenzorsDbContract.Transaction.COLUMN_NAME_ID)+"");
            Log.d(TAG,cursor.getColumnIndex(SenzorsDbContract.Transaction.COLUMN_NAME_previousBalance)+"");
            Log.d(TAG,cursor.getColumnIndex(SenzorsDbContract.Transaction.COLUMN_NAME_transactionTime)+"");
            Log.d(TAG,cursor.getColumnIndex(SenzorsDbContract.Transaction.COLUMN_NAME_transactionType)+"");
            Log.d(TAG,cursor.getColumnIndex(SenzorsDbContract.Transaction.COLUMN_NAME_clientAccountNo)+"");
            Log.d(TAG,cursor.getColumnIndex(SenzorsDbContract.Transaction.COLUMN_NAME_CLIENTNAME)+"");
            Log.d(TAG,cursor.getColumnIndex(SenzorsDbContract.Transaction.COLUMN_NAME_clientNIC)+"");
            Log.d(TAG,cursor.getColumnIndex(SenzorsDbContract.Transaction.COLUMN_NAME_transactionAmount)+"");

*//*

            // fill senz list
            System.out.println("Done in Create object");
            sensorList.add(transaction);
        }

        // clean
        cursor.close();
        db.close();


        return sensorList;

    }*/

    public void clearTable() {
        SQLiteDatabase db = SenzorsDbHelper.getInstance(context).getWritableDatabase();

        // delete senz of given user

        db.close();
    }
/*

    public Summary getSummeryAmmount(){
        SQLiteDatabase db = SenzorsDbHelper.getInstance(context).getWritableDatabase();
        String query = "SELECT COUNT("+SenzorsDbContract.Transaction._ID+") AS trcount, SUM("+SenzorsDbContract.Transaction.COLUMN_NAME_transactionAmount+") AS total" +
                " FROM " +SenzorsDbContract.Transaction.TABLE_NAME;
        Log.e(TAG,query);
        Cursor cursor = db.rawQuery(query, null);
        int tcount;
        int tamount;
        if(cursor.moveToFirst()){
            tcount = cursor.getInt(cursor.getColumnIndex("trcount"));

            tamount = cursor.getInt(cursor.getColumnIndex("total"));
        }
        else{
            tcount = 0;

            tamount = 0;
        }

        Calendar cp = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(cp.getTime());

        Summary tempsum=new Summary("10255",""+tcount,""+tamount,formattedDate);



        return tempsum;
    }
*/

}
