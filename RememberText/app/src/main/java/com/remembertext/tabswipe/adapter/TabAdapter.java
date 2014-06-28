package com.remembertext.tabswipe.adapter;

import com.remembertext.tabswipe.adapter.SettingsFragment;
import com.remembertext.tabswipe.adapter.LeaderboardFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by connie on 6/28/14.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Settings fragment activity
                return new SettingsFragment();
            case 1:
                // Leaderboard fragment activity
                return new LeaderboardFragment();
            default:
                // Default to Settings fragment activity
                return new SettingsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}