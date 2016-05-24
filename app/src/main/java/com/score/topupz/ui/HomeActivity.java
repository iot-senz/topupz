package com.score.topupz.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.score.senzc.pojos.User;
import com.score.topupz.R;
import com.score.topupz.exceptions.NoUserException;
import com.score.topupz.pojos.Bill;
import com.score.topupz.utils.PreferenceUtils;

/**
 * Created by chathura on 5/13/16.
 */
public class HomeActivity extends Activity implements View.OnClickListener {

    // UI components
    private RelativeLayout relativeLayoutTopUp;
    private RelativeLayout relativeLayoutBill;
    private RelativeLayout relativeLayoutSettings;

    private TextView billText;
    private TextView topUpText;
    private TextView settingsText;

    private TextView billIcon;
    private TextView topUpIcon;
    private TextView settingsIcon;

    // custom type face
    private Typeface typeface;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.shopz_home_layout);

        initUi();
        //initActionBar();
    }

    /**
     * Initialize activity components
     */
    public void initUi() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");

        relativeLayoutTopUp = (RelativeLayout) findViewById(R.id.topup_layout);
        relativeLayoutBill = (RelativeLayout) findViewById(R.id.bill_layout);
        relativeLayoutSettings = (RelativeLayout) findViewById(R.id.settings_layout);

        relativeLayoutTopUp.setOnClickListener(HomeActivity.this);
        relativeLayoutBill.setOnClickListener(HomeActivity.this);
        relativeLayoutSettings.setOnClickListener(HomeActivity.this);

        billIcon = (TextView) findViewById(R.id.bill_icon);
        billText = (TextView) findViewById(R.id.bill_text);
        billIcon.setTypeface(typeface, Typeface.BOLD);
        billText.setTypeface(typeface, Typeface.BOLD);

        topUpIcon = (TextView) findViewById(R.id.topup_icon);
        topUpText = (TextView) findViewById(R.id.topup_text);
        topUpIcon.setTypeface(typeface, Typeface.BOLD);
        topUpText.setTypeface(typeface, Typeface.BOLD);

        settingsIcon = (TextView) findViewById(R.id.settings_icon);
        settingsText = (TextView) findViewById(R.id.settings_text);
        settingsIcon.setTypeface(typeface, Typeface.BOLD);
        settingsText.setTypeface(typeface, Typeface.BOLD);
    }

    /**
     * Initialize action bar
     */
    private void initActionBar() {
        // Set up action bar.
        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        final ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // initialize action bar title with current user
        try {
            User user = PreferenceUtils.getUser(this);
            actionBar.setTitle("TopupZ @" + user.getUsername());
        } catch (NoUserException e) {
            e.printStackTrace();

            // no user
            actionBar.setTitle("TopupZ @");
        }

        // set custom font for
        //  1. action bar title
        //  2. other ui texts
        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView actionBarTitle = (TextView) (findViewById(titleId));
        actionBarTitle.setTextColor(getResources().getColor(R.color.white));
        actionBarTitle.setTypeface(typeface);
    }

    /**
     * Call when click on view
     *
     * @param view
     */
    public void onClick(View view) {
        if (view == relativeLayoutBill) {
            //navigateToBillActivity();
        } else if (view == relativeLayoutTopUp) {
            // display top up activity
            startActivity(new Intent(HomeActivity.this, TopupActivity.class));
            overridePendingTransition(R.anim.right_in, R.anim.stay_in);
        } else if (view == relativeLayoutSettings) {

        }
    }

    /**
     * Navigate to BillActivity
     */
    private void navigateToBillActivity() {
        // create Bill first
        try {
            User user = PreferenceUtils.getUser(this);
            Bill bill = new Bill("0045121", user.getUsername(), "00");

            // display bill activity
            Intent intent = new Intent(HomeActivity.this, BillActivity.class);
            intent.putExtra("EXTRA", bill);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.stay_in);
        } catch (NoUserException e) {
            e.printStackTrace();
        }
    }

}
