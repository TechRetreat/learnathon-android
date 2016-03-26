package com.example.jzukewich.geocaching;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jgzuke on 16-03-20.
 */
public class DataUtilities {

    public interface FoundCachesReceiver {
        void onResults(FoundCaches results);
    }

    public static void getFoundCaches(Context context, FoundCachesReceiver receiver) {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.caches_found);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            receiver.onResults(gson.fromJson(json, FoundCaches.class));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public interface MapCachesReceiver {
        void onResults(MapCaches results);
    }

    public static void getMapCaches(Context context, MapCachesReceiver receiver) {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.caches_map);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            receiver.onResults(gson.fromJson(json, MapCaches.class));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
