package techretreat.jgzuke.geocaching.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import techretreat.jgzuke.geocaching.FoundPage.FoundFragment;
import techretreat.jgzuke.geocaching.MapPage.MapFragment;
import techretreat.jgzuke.geocaching.SettingsPage.SettingsFragment;

public class GeocachingPagerAdapter extends FragmentStatePagerAdapter {

    // Constants
    public static final int FOUND_TAB = 0;
    public static final int MAP_TAB = 1;
    public static final int SETTINGS_TAB = 2;
    private static final int NUMBER_OF_TABS = 3;

    public GeocachingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FOUND_TAB:
                return FoundFragment.newInstance();
            case MAP_TAB:
                return  MapFragment.newInstance();
            case SETTINGS_TAB:
                return SettingsFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUMBER_OF_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
