package com.score.payz.ui;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.score.payz.R;
import com.score.payz.pojos.History;

import java.util.ArrayList;

/**
 * Activity class for sharing
 * Implement sharing related functions
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class HistoryFragment extends android.support.v4.app.Fragment {

    private static final String TAG = TopUpFragment.class.getName();

    // custom font
    private Typeface typeface;

    private ListView historyListView;
    private HistoryListAdapter historyListAdapter;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.history_list_layout, container, false);

        return root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initUi();
        initFriendListView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * Initialize UI components
     */
    private void initUi() {
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/vegur_2.otf");

        // Set up action bar.
        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("History");

        actionBar.setBackgroundDrawable(new ColorDrawable(0xffd26c6c));

        // set custom font for
        //  1. action bar title
        //  2. other ui texts
        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView actionBarTitle = (TextView) (getActivity().findViewById(titleId));
        actionBarTitle.setTextColor(getResources().getColor(R.color.white));
        actionBarTitle.setTypeface(typeface);
    }

    /**
     * Initialize friend list view
     */
    private void initFriendListView() {
        historyListView = (ListView) getActivity().findViewById(R.id.list_view);

        // pop up temporary list
        ArrayList<History> historyList = new ArrayList<>();
        historyList.add(new History("Lunch @cafe 1", "10.4$"));
        historyList.add(new History("Breakfast @Mac", "15$"));
        historyList.add(new History("Starbug", "5.50$"));
        historyList.add(new History("Dinner @cafe2", "15.10$"));
        historyList.add(new History("Coffee", "2.50$"));

        historyListAdapter = new HistoryListAdapter(this, historyList);
        historyListView.setAdapter(historyListAdapter);
    }

}
