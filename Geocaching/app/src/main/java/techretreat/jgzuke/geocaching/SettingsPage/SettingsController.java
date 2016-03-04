package techretreat.jgzuke.geocaching.SettingsPage;

import android.content.Context;
import android.support.v4.app.Fragment;

public class SettingsController {

    private SettingsFragment settingsFragment;

    public SettingsController(Context context) {
        settingsFragment = SettingsFragment.newInstance();
    }

    public Fragment getFragment() {
        return settingsFragment;
    }
}
