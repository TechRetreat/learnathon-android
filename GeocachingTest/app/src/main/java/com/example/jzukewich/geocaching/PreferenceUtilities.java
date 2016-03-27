package com.example.jzukewich.geocaching;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

/**
 * Created by jgzuke on 16-03-26.
 */
public class PreferenceUtilities {
    private static final String ZOOM_ENABLED = "map_settings_zoom_buttons_enabled";
    private static final String TOOLBAR_ENABLED = "map_settings_toolbar_enabled";

    public static boolean getZoomEnabled(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(ZOOM_ENABLED, true);
    }

    public static boolean getToolbarEnabled(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(TOOLBAR_ENABLED, true);
    }
}
