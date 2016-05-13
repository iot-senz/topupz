package com.score.shopz.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.score.shopz.R;
import com.score.shopz.db.SenzorsDbSource;
import com.score.shopz.exceptions.InvalidAccountException;
import com.score.shopz.exceptions.InvalidInputFieldsException;
//import com.score.shopz.pojos.;
import com.score.shopz.utils.ActivityUtils;
import com.score.shopz.utils.NetworkUtil;
//import com.score.shopz.utils.PayUtils;
import com.score.senz.ISenzService;
import com.score.senzc.enums.SenzTypeEnum;
import com.score.senzc.pojos.Senz;
import com.score.senzc.pojos.User;

import java.util.HashMap;

//import com.wasn.pojos.BalanceQuery;
//import com.wasn.utils.TransactionUtils;


public class PayActivity extends Activity implements View.OnClickListener{

    private static final String TAG = PayActivity.class.getName();

    // deals with NFC
    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;
    private IntentFilter[] nfcIntentFilters;
    private String[][] nfcTechLists;

    // custom type face
    private Typeface typeface;

    // UI components
    private TextView clickToPay;
    private TextView payAmountText;
    private TextView acceptText;
    private TextView rejectText;

    // header
    private RelativeLayout cancel;
    private RelativeLayout accept;
    private TextView headerText;

    // use to track registration timeout
    private SenzCountDownTimer senzCountDownTimer;
    private boolean isResponseReceived;

    // service interface
    private ISenzService senzService;
    private boolean isServiceBound;

    // service connection
    private ServiceConnection senzServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d("TAG", "Connected with senz service");
            isServiceBound = true;
            senzService = ISenzService.Stub.asInterface(service);
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d("TAG", "Disconnected from senz service");

            senzService = null;
            isServiceBound = false;
        }
    };

    // current pay
    //private Pay pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_layout);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");

        //initNfc();
        initUi();
        initActionBar();
        initPaymentDetails();

        // service
        senzService = null;
        isServiceBound = false;

        // register broadcast receiver
        registerReceiver(senzMessageReceiver, new IntentFilter("com.score.shopz.DATA_SENZ"));

        // bind with senz service
        // bind to service from here as well
        if (!isServiceBound) {
            Intent intent = new Intent();
            intent.setClassName("com.score.shopz", "com.score.shopz.services.RemoteSenzService");
            bindService(intent, senzServiceConnection, Context.BIND_AUTO_CREATE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(senzServiceConnection);
        unregisterReceiver(senzMessageReceiver);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();

//        // enable foreground dispatch
//        if (nfcAdapter != null) {
//            nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, nfcIntentFilters, nfcTechLists);
//        }

//        Intent intent = getIntent();
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
//            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//
//            NdefMessage message = (NdefMessage) rawMessages[0];
//            String amount = new String(message.getRecords()[0].getPayload());
//            Log.d(TAG, amount);
//
//            payAmountText.setText("$" + amount);
//        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause() {
        super.onPause();

        // disable foreground dispatch
//        if (nfcAdapter != null)
//            nfcAdapter.disableForegroundDispatch(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNewIntent(Intent intent) {
//        String action = intent.getAction();
//        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//
//        String s = action + "\n\n" + tag.toString();
//        Log.d(TAG, "tag... " + s);
//
//        // parse through all NDEF messages and their records and pick text type only
//        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//        if (data != null) {
//            NdefMessage message = (NdefMessage) data[0];
//            String amount = new String(message.getRecords()[0].getPayload());
//            Log.d(TAG, amount);
//
//            payAmountText.setText("$" + amount);
//        }
    }

    /**
     * Initialize NFC components
     */
    private void initNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "[ERROR] No NFC supported", Toast.LENGTH_LONG).show();
        } else {
            // create an intent with tag data and deliver to this activity
            nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

            // set an intent filter for all MIME data
            IntentFilter nfcDiscoveredIntentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            try {
                nfcDiscoveredIntentFilter.addDataType("text/plain");
                nfcIntentFilters = new IntentFilter[]{nfcDiscoveredIntentFilter};
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }

            // tech list
            nfcTechLists = new String[][]{new String[]{NfcF.class.getName()}};
        }
    }

    private void initUi() {
        typeface = Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");

        clickToPay = (TextView) findViewById(R.id.click_to_pay);
        payAmountText = (TextView) findViewById(R.id.pay_amount_text);
        //acceptText = (TextView) findViewById(R.id.accept_text);
        rejectText = (TextView) findViewById(R.id.reject_text);

        cancel = (RelativeLayout) findViewById(R.id.sign_in_button_panel);
        accept = (RelativeLayout) findViewById(R.id.pay_amount_relative_layout);

        cancel.setOnClickListener(PayActivity.this);
        accept.setOnClickListener(PayActivity.this);

        clickToPay.setTypeface(typeface, Typeface.NORMAL);
        payAmountText.setTypeface(typeface, Typeface.BOLD);
        //acceptText.setTypeface(typeface, Typeface.BOLD);
        rejectText.setTypeface(typeface, Typeface.BOLD);


    }

    private void initActionBar() {
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

    private void initPaymentDetails() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String amount = bundle.getString("EXTRA");
            payAmountText.setText(amount + "$");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.stay_in, R.anim.bottom_out);
    }

//-------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View view) {
        if (view == cancel) {
            // cancel to main activity
            PayActivity.this.finish();
        } else if (view == accept) {
            onClickPut();
        }
    }


    private void onClickPut() {
        ActivityUtils.hideSoftKeyboard(this);

        try {
            String account = "ttl";//accountEditText.getText().toString().trim();
            double amount = Double.parseDouble(payAmountText.getText().toString().trim());
            ActivityUtils.isValidPayFields(account, amount);

            // initialize transaction
            // TODO //pay = new Pay(1, "shop abc", account, "inv0001", amount, PayUtils.getCurrentTime().toString());

            // TODO //new SenzorsDbSource(PayActivity.this).createPay(pay);
            //navigateTransactionDetails(transaction);
            if (NetworkUtil.isAvailableNetwork(this)) {
                displayInformationMessageDialog("Are you sure you want to do the transaction " + /* #Account " + /*pay.getClientAccountNo() +*/ " #Amount " + 100);//pay.getPayAmount());
            } else {
                displayMessageDialog("#ERROR", "No network connection");
            }
        } catch (InvalidInputFieldsException | NumberFormatException e) {
            e.printStackTrace();

            displayMessageDialog("#ERROR", "Invalid account no/amount");
        } catch (InvalidAccountException e) {
            e.printStackTrace();

            displayMessageDialog("#ERROR", "Account no should be 12 character length");
        }
    }

    private Senz getPutSenz() {
        HashMap<String, String> senzAttributes = new HashMap<>();
        senzAttributes.put("amnt", "100");//payAmountText.getText().toString().trim());
        senzAttributes.put("acc", "shop01" /*amountEditText.getText().toString().trim()*/);
        senzAttributes.put("time", ((Long) (System.currentTimeMillis() / 1000)).toString());

        // new senz
        String id = "_ID";
        String signature = "_SIGNATURE";
        SenzTypeEnum senzType = SenzTypeEnum.PUT;
        User receiver = new User("", "payzbank");

        return new Senz(id, signature, senzType, null, receiver, senzAttributes);
    }

    private void doPut(Senz senz) {
        try {
            senzService.send(senz);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Keep track with share response timeout
     */
    private class SenzCountDownTimer extends CountDownTimer {

        // timer deals with only one senz
        private Senz senz;

        public SenzCountDownTimer(long millisInFuture, long countDownInterval, final Senz senz) {
            super(millisInFuture, countDownInterval);

            this.senz = senz;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // if response not received yet, resend share
            if (!isResponseReceived) {
                doPut(senz);
                Log.d(TAG, "Response not received yet");
            }
        }

        @Override
        public void onFinish() {
            ActivityUtils.hideSoftKeyboard(PayActivity.this);
            ActivityUtils.cancelProgressDialog();

            // display message dialog that we couldn't reach the user
            if (!isResponseReceived) {
                String message = "Seems we couldn't complete the payment at this moment";
                displayMessageDialog("#PUT Fail", message);
            }
        }
    }

    private BroadcastReceiver senzMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Got message from Senz service");
            handleMessage(intent);
        }
    };


    /**
     * Handle broadcast message receives
     * Need to handle registration success failure here
     *
     * @param intent intent
     */
    private void handleMessage(Intent intent) {
        String action = intent.getAction();

        if (action.equals("com.score.shopz.DATA_SENZ")) {
            Senz senz = intent.getExtras().getParcelable("SENZ");

            if (senz.getAttributes().containsKey("msg")) {
                // msg response received
                ActivityUtils.cancelProgressDialog();
                isResponseReceived = true;
                senzCountDownTimer.cancel();

                String msg = senz.getAttributes().get("msg");
                if (msg != null && msg.equalsIgnoreCase("PUTDONE")) {
                    Toast.makeText(this, "Payment successful", Toast.LENGTH_LONG).show();

                    // save transaction in db
//                    if (pay != null)
//                        new SenzorsDbSource(PayActivity.this).createPay(pay);

                    // navigate
                    //navigatePayDetails(pay);
                } else {
                    String informationMessage = "Failed to complete the payment";
                    displayMessageDialog("PUT fail", informationMessage);
                }
            }
        }
    }

    /**
     * Display message dialog
     *
     * @param messageHeader message header
     * @param message       message to be display
     */
    public void displayMessageDialog(String messageHeader, String message) {
        final Dialog dialog = new Dialog(this);

        //set layout for dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.information_message_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set dialog texts
        TextView messageHeaderTextView = (TextView) dialog.findViewById(R.id.information_message_dialog_layout_message_header_text);
        TextView messageTextView = (TextView) dialog.findViewById(R.id.information_message_dialog_layout_message_text);
        messageHeaderTextView.setText(messageHeader);
        messageTextView.setText(message);

        // set custom font
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        messageHeaderTextView.setTypeface(face);
        messageHeaderTextView.setTypeface(null, Typeface.BOLD);
        messageTextView.setTypeface(face);

        //set ok button
        Button okButton = (Button) dialog.findViewById(R.id.information_message_dialog_layout_ok_button);
        okButton.setTypeface(face);
        okButton.setTypeface(null, Typeface.BOLD);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    /**
     * Display message dialog when user going to logout
     *
     * @param message
     */
    public void displayInformationMessageDialog(String message) {
        final Dialog dialog = new Dialog(this);

        //set layout for dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.share_confirm_message_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set dialog texts
        TextView messageHeaderTextView = (TextView) dialog.findViewById(R.id.information_message_dialog_layout_message_header_text);
        TextView messageTextView = (TextView) dialog.findViewById(R.id.information_message_dialog_layout_message_text);
        messageTextView.setText(message);

        // set custom font
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        messageHeaderTextView.setTypeface(face);
        messageHeaderTextView.setTypeface(null, Typeface.BOLD);
        messageTextView.setTypeface(face);

        //set ok button
        Button okButton = (Button) dialog.findViewById(R.id.information_message_dialog_layout_ok_button);
        okButton.setTypeface(face);
        okButton.setTypeface(null, Typeface.BOLD);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // do transaction
                dialog.cancel();

                ActivityUtils.showProgressDialog(PayActivity.this, "Please wait...");

                // start new timer
                isResponseReceived = false;
                senzCountDownTimer = new SenzCountDownTimer(16000, 5000, getPutSenz());
                senzCountDownTimer.start();
            }
        });

        // cancel button
        Button cancelButton = (Button) dialog.findViewById(R.id.information_message_dialog_layout_cancel_button);
        cancelButton.setTypeface(face);
        cancelButton.setTypeface(null, Typeface.BOLD);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

//    private void navigatePayDetails(Pay pay) {
//        // navigate to transaction details
//        Intent intent = new Intent(PayActivity.this, PayDetailsActivity.class);
//        intent.putExtra("pay", pay);
//        intent.putExtra("ACTIVITY_NAME", PayActivity.class.getName());
//        startActivity(intent);
//
//        PayActivity.this.finish();
//    }

}
