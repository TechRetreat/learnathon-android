package com.example.jzukewich.geocaching;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentStatePagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPagerAdapter = new GeocachingPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
    }

    class GeocachingPagerAdapter extends FragmentStatePagerAdapter {

        public static final int FOUND_TAB = 0;
        public static final int MAP_TAB = 1;
        public static final int SETTINGS_TAB = 2;

        public GeocachingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == FOUND_TAB) {
                return new FoundCachesFragment();
            } else if (position == MAP_TAB) {
                return new MapFragment();
            } else {
                return new SettingsFragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == FOUND_TAB) {
                return getString(R.string.found);
            } else if (position == MAP_TAB) {
                return getString(R.string.map);
            } else {
                return getString(R.string.settings);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
