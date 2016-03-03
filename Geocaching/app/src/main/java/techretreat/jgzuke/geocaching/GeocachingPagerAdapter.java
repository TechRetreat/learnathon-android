package techretreat.jgzuke.geocaching;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import techretreat.jgzuke.geocaching.FoundPage.FoundController;
import techretreat.jgzuke.geocaching.MapPage.MapController;
import techretreat.jgzuke.geocaching.SettingsPage.SettingsController;

public class GeocachingPagerAdapter extends FragmentStatePagerAdapter implements FoundController.Callback {

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
            case 0:
                fragment = new FoundController(context, this).getFragment();
                break;
            case 1:
                fragment = new MapController(context).getFragment();
                break;
            case 2:
                fragment = new SettingsController(context).getFragment();
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

    @Override
    public void viewFoundCacheOnMap(String cacheId) {

    }
}
