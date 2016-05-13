package com.score.shopz.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.score.shopz.R;

/**
 * Created by chathura on 5/13/16.
 */
public class TopupActivity extends Activity {

    private static final String TAG = TopupActivity.class.getName();

    // custom type face
    private Typeface typeface;

    /*
    * {@inheritDoc}
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topup_layout);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");

        initUi();
        initActionBar();
//        initNfc();
//        initDrawer();
//        initDrawerUser();
//        initDrawerList();
//        loadPayz();
    }

    private void initUi(){
        typeface = Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
    }

    private void initActionBar() {
        // Set up action bar.
        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Topup");
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
