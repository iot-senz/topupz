package com.score.shopz.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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

import com.score.senz.ISenzService;
import com.score.senzc.enums.SenzTypeEnum;
import com.score.senzc.pojos.Senz;
import com.score.senzc.pojos.User;
import com.score.shopz.R;
import com.score.shopz.pojos.TopUp;
import com.score.shopz.utils.ActivityUtils;
import com.score.shopz.utils.NetworkUtil;

import java.util.HashMap;

public class PayActivity extends Activity implements View.OnClickListener {

    private static final String TAG = PayActivity.class.getName();

    // custom type face
    private Typeface typeface;

    // UI components
    private TextView textViewAppIcon;
    private TextView clickToPay;
    private TextView payAmountText;
    private RelativeLayout accept;

    // use to track registration timeout
    private SenzCountDownTimer senzCountDownTimer;
    private boolean isResponseReceived;

    // service interface
    private ISenzService senzService;
    private boolean isServiceBound;

    // activity deal with TopUp
    private TopUp topUp;

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

    // senz message receiver
    private BroadcastReceiver senzMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Got message from Senz service");
            handleSenzMessage(intent);
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.pay_layout);

        initUi();
        initActionBar();
        initPay();

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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(senzServiceConnection);
        unregisterReceiver(senzMessageReceiver);
    }

    private void initUi() {
        typeface = Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");

        textViewAppIcon = (TextView) findViewById(R.id.app_icon);
        clickToPay = (TextView) findViewById(R.id.click_to_pay);
        payAmountText = (TextView) findViewById(R.id.pay_amount_text);

        textViewAppIcon.setTypeface(typeface, Typeface.BOLD);
        clickToPay.setTypeface(typeface, Typeface.BOLD);
        payAmountText.setTypeface(typeface, Typeface.BOLD);

        accept = (RelativeLayout) findViewById(R.id.pay_amount_relative_layout);
        accept.setOnClickListener(PayActivity.this);
    }

    private void initActionBar() {
        // Set up action bar.
        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Top Up");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // set custom font for
        //  1. action bar title
        //  2. other ui texts
        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView actionBarTitle = (TextView) (findViewById(titleId));
        actionBarTitle.setTextColor(getResources().getColor(R.color.white));
        actionBarTitle.setTypeface(typeface);
        actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }

    private void initPay() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            topUp = bundle.getParcelable("EXTRA");

            if (topUp != null) {
                Log.i(TAG, "Top up account :" + topUp.getAccount());
                Log.i(TAG, "Top up amount :" + topUp.getAmount());
                Log.i(TAG, "Top up time :" + topUp.getTime());

                payAmountText.setText("$" + topUp.getAmount());
            }
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View view) {
        if (view == accept) {
            onClickPut();
        }
    }

    private void onClickPut() {
        ActivityUtils.hideSoftKeyboard(this);

        if (NetworkUtil.isAvailableNetwork(this)) {
            displayInformationMessageDialog("Are you sure you want to top up" + " #Amount " + topUp.getAmount() + "for #Account " + topUp.getAccount());
        } else {
            displayMessageDialog("#ERROR", "No network connection");
        }
    }

    private void doPut(Senz senz) {
        try {
            senzService.send(senz);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Senz createPutSenz() {
        HashMap<String, String> senzAttributes = new HashMap<>();
        senzAttributes.put("amnt", topUp.getAmount());
        senzAttributes.put("acc", topUp.getAccount());
        senzAttributes.put("time", ((Long) (System.currentTimeMillis() / 1000)).toString());

        // new senz
        String id = "_ID";
        String signature = "_SIGNATURE";
        SenzTypeEnum senzType = SenzTypeEnum.PUT;
        User receiver = new User("", "payzbank");

        return new Senz(id, signature, senzType, null, receiver, senzAttributes);
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


    /**
     * Handle broadcast message receives
     * Need to handle registration success failure here
     *
     * @param intent intent
     */
    private void handleSenzMessage(Intent intent) {
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

                    // exit from activity
                    this.finish();
                    this.overridePendingTransition(R.anim.stay_in, R.anim.bottom_out);
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
     * @param message message to display
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
                senzCountDownTimer = new SenzCountDownTimer(16000, 5000, createPutSenz());
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

}
