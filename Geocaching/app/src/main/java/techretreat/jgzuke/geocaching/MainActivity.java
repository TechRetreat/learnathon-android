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

import java.util.HashMap;
import java.util.Map;

import techretreat.jgzuke.geocaching.FoundPage.FoundCaches;
import techretreat.jgzuke.geocaching.FoundPage.FoundController;
import techretreat.jgzuke.geocaching.MapPage.MapController;
import techretreat.jgzuke.geocaching.MapPage.MapFragment;
import techretreat.jgzuke.geocaching.SettingsPage.SettingsController;

public class MainActivity extends AppCompatActivity implements FoundController.Callback {

    public static final int MAPS_PAGE_LOCATION_PERMISSIONS_REQUEST_CODE = 1;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private GeocachingPagerAdapter pagerAdapter;

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

        pagerAdapter = new GeocachingPagerAdapter(getSupportFragmentManager(), this, this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new GeocachingTabSelectedListener(viewPager));
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
                    Fragment fragment = pagerAdapter.getActiveFragment(GeocachingPagerAdapter.MAP_TAB);
                    ((MapFragment) fragment).updateLocationPermissions();
                }
                break;
        }
    }

    @Override
    public void viewFoundCacheOnMap(String cacheId) {
        viewPager.setCurrentItem(GeocachingPagerAdapter.MAP_TAB, true);
        Fragment fragment = pagerAdapter.getActiveFragment(GeocachingPagerAdapter.MAP_TAB);
        ((MapFragment) fragment).selectCache(cacheId);
    }
}