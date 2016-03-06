package techretreat.jgzuke.geocaching.SettingsPage;

import android.content.Context;

public class SettingsController implements SettingsFragment.Callback {

    private Context context;
    private SettingsFragment mapFragment;

    public SettingsController(Context context, SettingsFragment fragment) {
        this.context = context;
        this.mapFragment = fragment;
    }

    @Override
    public void onDumpCache() {

    }

    @Override
    public void onClearCache() {

    }

    @Override
    public void onClearFoundCaches() {

    }
}
