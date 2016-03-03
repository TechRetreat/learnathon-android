package techretreat.jgzuke.geocaching.SettingsPage;

import android.content.Context;
import android.support.v4.app.Fragment;

public class SettingsController implements SettingsDataInteractor.DataReceiver {

    private SettingsFragment settingsFragment;
    private SettingsDataInteractor settingsDataInteractor;

    public SettingsController(Context context) {
        settingsFragment = SettingsFragment.newInstance();
        settingsDataInteractor = new SettingsDataInteractor(context, this);
    }

    public Fragment getFragment() {
        return settingsFragment;
    }
}
