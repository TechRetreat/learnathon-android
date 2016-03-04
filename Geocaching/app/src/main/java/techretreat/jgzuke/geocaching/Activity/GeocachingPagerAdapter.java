package techretreat.jgzuke.geocaching.Activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import techretreat.jgzuke.geocaching.FoundPage.FoundFragment;
import techretreat.jgzuke.geocaching.MapPage.MapFragment;
import techretreat.jgzuke.geocaching.SettingsPage.SettingsFragment;

public class GeocachingPagerAdapter extends FragmentStatePagerAdapter {

    // Constants
    public static final int FOUND_TAB = 0;
    public static final int MAP_TAB = 1;
    public static final int SETTINGS_TAB = 2;
    private static final int NUMBER_OF_TABS = 3;

    private Map<Integer, Fragment> pageReferenceMap = new HashMap<>(3);
    private Context context;

    public GeocachingPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public Fragment getActiveFragment(int index) {
        return pageReferenceMap.get(index);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case FOUND_TAB:
                fragment = FoundFragment.newInstance();
                break;
            case MAP_TAB:
                fragment = MapFragment.newInstance();
                break;
            case SETTINGS_TAB:
                fragment = SettingsFragment.newInstance();
                break;
        }
        pageReferenceMap.put(position, fragment);
        return fragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        pageReferenceMap.put(position, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return NUMBER_OF_TABS;
    }
}
