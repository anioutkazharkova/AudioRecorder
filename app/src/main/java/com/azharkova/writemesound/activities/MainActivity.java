package com.azharkova.writemesound.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.azharkova.writemesound.R;
import com.azharkova.writemesound.Utility;
import com.azharkova.writemesound.fragments.AllRecordsFragment;
import com.azharkova.writemesound.fragments.CollectionsFragment;
import com.azharkova.writemesound.fragments.NavigationDrawerFragment;
import com.azharkova.writemesound.fragments.RecordSettingsFragment;

import java.io.File;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private String[] titles;

    int selectedPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titles=getResources().getStringArray(R.array.sections);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        File folder = new File(Utility.ROOT_FOLDER);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        onNavigationDrawerItemSelected(selectedPosition);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragmentss
        selectedPosition=position;
      FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new AllRecordsFragment())
                    .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new CollectionsFragment())
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new RecordSettingsFragment())
                        .commit();
                break;
        }


    }

    public void onSectionAttached(int number) {
        mTitle=titles[number-1];
        setTitle(mTitle);

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data==null)
            return;

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }


    }

}
