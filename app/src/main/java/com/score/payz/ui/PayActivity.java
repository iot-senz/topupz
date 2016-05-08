package com.score.payz.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.score.payz.R;

public class PayActivity extends Activity {

    private static final String TAG = PayActivity.class.getName();

    // deals with NFC
    private NfcAdapter nfcAdapter;

    // custom type face
    private Typeface typeface;

    // UI components
    private TextView clickToPay;
    private TextView payAmountText;
    private TextView acceptText;
    private TextView rejectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_layout);

        initNfc();
        initUi();
    }

    /**
     * {@inheritDoc}
     */
    protected void onResume() {
        super.onResume();

        // enable foreground dispatch
//        if (nfcAdapter != null) {
//            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
//        }

        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0];
            String amount = new String(message.getRecords()[0].getPayload());
            Log.d(TAG, amount);

            payAmountText.setText("$" + amount);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void onPause() {
        super.onPause();

        // disable foreground dispatch
//        if (nfcAdapter != null)
//            nfcAdapter.disableForegroundDispatch(this);
    }

    /**
     * Initialize NFC components
     */
    private void initNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "[ERROR] No NFC supported", Toast.LENGTH_LONG).show();
        } else {

        }
    }

    private void initUi() {
        typeface = Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");

        clickToPay = (TextView) findViewById(R.id.click_to_pay);
        payAmountText = (TextView) findViewById(R.id.pay_amount_text);
        acceptText = (TextView) findViewById(R.id.accept_text);
        rejectText = (TextView) findViewById(R.id.reject_text);

        clickToPay.setTypeface(typeface, Typeface.NORMAL);
        payAmountText.setTypeface(typeface, Typeface.NORMAL);
        acceptText.setTypeface(typeface, Typeface.BOLD);
        rejectText.setTypeface(typeface, Typeface.BOLD);

        // Set up action bar.
        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Pay");
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xff384e77));

        // set custom font for
        //  1. action bar title
        //  2. other ui texts
        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView actionBarTitle = (TextView) (findViewById(titleId));
        actionBarTitle.setTextColor(getResources().getColor(R.color.white));
        actionBarTitle.setTypeface(typeface);
    }

}
