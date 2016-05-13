package com.score.shopz.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.score.shopz.R;

/**
 * Created by chathura on 5/13/16.
 */
public class ShopzHomeActivity extends Activity implements View.OnClickListener {

    // activity components
    RelativeLayout billLayout;
    RelativeLayout topupLayout;
    RelativeLayout settingsLayout;
    //RelativeLayout logout;
    //TextView logoutText;
    TextView billText;
    TextView billIcon;
    TextView topupText;
    TextView topupIcon;
    TextView settingsText;
    TextView settingsIcon;
    //TextView mbankIcon;

    // custom type face
    private Typeface typeface;


    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopz_home_layout);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");

        init();
        initActionBar();
    }

    private void initActionBar() {
        // Set up action bar.
        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Shopz");
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xff384e77));

        // set custom font for
        //  1. action bar title
        //  2. other ui texts
        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView actionBarTitle = (TextView) (findViewById(titleId));
        actionBarTitle.setTextColor(getResources().getColor(R.color.white));
        actionBarTitle.setTypeface(typeface);
    }


    /**
     * Initialize activity components
     */
    public void init() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");

        billLayout = (RelativeLayout) findViewById(R.id.bill_layout);
        topupLayout = (RelativeLayout) findViewById(R.id.topup_layout);
        settingsLayout = (RelativeLayout) findViewById(R.id.settings_layout);
//        logout = (RelativeLayout) findViewById(R.id.mobile_bank_layout_logout);

        // set custom font
//        logoutText = (TextView) findViewById(R.id.mobile_bank_layout_logout_text);
//        logoutText.setTypeface(typeface, Typeface.BOLD);

        billIcon = (TextView) findViewById(R.id.bill_icon);
        billText = (TextView) findViewById(R.id.bill_text);
        billIcon.setTypeface(typeface, Typeface.BOLD);
        billText.setTypeface(typeface, Typeface.BOLD);

        topupIcon = (TextView) findViewById(R.id.topup_icon);
        topupText = (TextView) findViewById(R.id.topup_text);
        topupIcon.setTypeface(typeface, Typeface.BOLD);
        topupText.setTypeface(typeface, Typeface.BOLD);

        settingsIcon = (TextView) findViewById(R.id.settings_icon);
        settingsText = (TextView) findViewById(R.id.settings_text);
        settingsIcon.setTypeface(typeface, Typeface.BOLD);
        settingsText.setTypeface(typeface, Typeface.BOLD);

        //mbankIcon = (TextView) findViewById(R.id.mbank_icon);
        //mbankIcon.setTypeface(typeface, Typeface.BOLD);

        billLayout.setOnClickListener(ShopzHomeActivity.this);
        topupLayout.setOnClickListener(ShopzHomeActivity.this);
        settingsLayout.setOnClickListener(ShopzHomeActivity.this);
//        logout.setOnClickListener(ShopzHomeActivity.this);
    }

    /**
     * Call when click on view
     *
     * @param view
     */
    public void onClick(View view) {
        if (view == billLayout) {
            // display bill activity
            startActivity(new Intent(ShopzHomeActivity.this, BillActivity.class));
            //ShopzHomeActivity.this.finish();
        } else if (view == topupLayout) {
            // display bill list activity
            startActivity(new Intent(ShopzHomeActivity.this, TopupActivity.class));
            //ShopzHomeActivity.this.finish();
        } else if (view == settingsLayout) {
            // display settings activity
            startActivity(new Intent(ShopzHomeActivity.this, BillActivity.class));  //TODO replace Bill Activity with Settings Activity
            //ShopzHomeActivity.this.finish();
        }
//        else if (view == logout) {
//            displayInformationMessageDialog("Are you sure, you want to logout? ");
//        }
    }

    /**
     * Display message dialog when user going to logout
     *
     * @param message
     */
    public void displayInformationMessageDialog(String message) {
        final Dialog dialog = new Dialog(ShopzHomeActivity.this);

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
                // back to login activity
                //startActivity(new Intent(MobileBankActivity.this, LoginActivity.class));
                ShopzHomeActivity.this.finish();
                dialog.cancel();
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
