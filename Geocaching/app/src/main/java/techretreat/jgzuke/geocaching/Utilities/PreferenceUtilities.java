package techretreat.jgzuke.geocaching.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

public class PreferenceUtilities {

    // Constants
    private static final String COMPASS_ENABLED = "map_settings_compass_enabled";
    private static final String ZOOM_BUTTONS_ENABLED = "map_settings_compass_enabled";
    private static final String LOCATION_ENABLED = "map_settings_compass_enabled";
    private static final String TOOLBAR_ENABLED = "map_settings_compass_enabled";

    public static boolean getCompassEnabled(Context context) {
        return getPref(context).getBoolean(COMPASS_ENABLED, true);
    }

    public static boolean getZoomButtonsEnabled(Context context) {
        return getPref(context).getBoolean(ZOOM_BUTTONS_ENABLED, true);
    }

    public static boolean getLocationEnabled(Context context) {
        return getPref(context).getBoolean(LOCATION_ENABLED, true);
    }

    public static boolean getToolbarEnabled(Context context) {
        return getPref(context).getBoolean(TOOLBAR_ENABLED, true);
    }

    public static SharedPreferences getPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
