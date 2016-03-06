package techretreat.jgzuke.geocaching.Activity;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import techretreat.jgzuke.geocaching.FoundPage.FoundController;
import techretreat.jgzuke.geocaching.MapPage.MapFragment;
import techretreat.jgzuke.geocaching.R;

public class GeocachingActivity extends AppCompatActivity implements FoundController.GoToMapCallback {

    public static final int MAPS_PAGE_LOCATION_PERMISSIONS_REQUEST_CODE = 1;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private GeocachingPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new GeocachingPagerAdapter(getSupportFragmentManager(), this);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.found)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.map)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.settings)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setOnTabSelectedListener(new GeocachingTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
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
    public void onGoToMap() {
        viewPager.setCurrentItem(GeocachingPagerAdapter.MAP_TAB, true);
    }
}