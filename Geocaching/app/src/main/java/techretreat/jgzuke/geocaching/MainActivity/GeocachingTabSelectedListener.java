package techretreat.jgzuke.geocaching.MainActivity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

public class GeocachingTabSelectedListener implements TabLayout.OnTabSelectedListener {

    ViewPager viewPager;

    public GeocachingTabSelectedListener(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

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