package techretreat.jgzuke.geocaching.SettingsPage;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import techretreat.jgzuke.geocaching.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    // Callback
    private Callback callback;

    public interface Callback {
        void onDumpCache();

        void onClearCache();

        void onClearFoundCaches();
    }

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = new SettingsController(getContext(), this);

        addPreferencesFromResource(R.xml.preferences);

        Preference dumpCacheButton = findPreference("dump_cache");
        dumpCacheButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //code for what you want it to do
                return true;
            }
        });

        Preference clearCacheButton = findPreference("clear_cache");
        clearCacheButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //code for what you want it to do
                return true;
            }
        });
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
    }
}
