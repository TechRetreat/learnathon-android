package techretreat.jgzuke.geocaching;

import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import techretreat.jgzuke.geocaching.FoundPage.FoundController;
import techretreat.jgzuke.geocaching.MapPage.MapController;
import techretreat.jgzuke.geocaching.MapPage.MapFragment;
import techretreat.jgzuke.geocaching.SettingsPage.SettingsController;

public class MainActivity extends AppCompatActivity {

    public static final int MAPS_PAGE_LOCATION_PERMISSIONS_REQUEST_CODE = 1;
    private static final int NUMBER_OF_TABS = 3;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private GeocachingPagerAdapter pagerAdapter;

    //TODO not this
    private String userId = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.found)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.map)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.settings)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        pagerAdapter = new GeocachingPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new GeocachingTabSelectedListener());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length == 0) {
            return;
        }
        switch (requestCode) {
            case MAPS_PAGE_LOCATION_PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Fragment fragment = pagerAdapter.getActiveFragment(viewPager.getCurrentItem());
                    if (fragment instanceof MapFragment) {
                        ((MapFragment) fragment).updateLocationPermissions();
                    }
                }
                break;

        }
    }

    private class GeocachingTabSelectedListener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    private class GeocachingPagerAdapter extends FragmentStatePagerAdapter {

        private Map<Integer, Fragment> pageReferenceMap;

        public GeocachingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getActiveFragment(int index) {
            return pageReferenceMap.get(index);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new FoundController(userId, MainActivity.this).getFragment();
                    break;
                case 1:
                    fragment = new MapController(userId, MainActivity.this).getFragment();
                    break;
                case 2:
                    fragment = new SettingsController(userId, MainActivity.this).getFragment();
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
}