package techretreat.jgzuke.geocaching.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import techretreat.jgzuke.geocaching.R;

public class GeocachingActivity extends AppCompatActivity {

    public static final int MAPS_PAGE_LOCATION_PERMISSIONS_REQUEST_CODE = 1;

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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}