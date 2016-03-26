package com.example.jzukewich.geocaching;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by jgzuke on 16-03-20.
 */
public class DataUtilities {

    public interface FoundCachesReceiver {
        void onResults(Map<String, FoundCache> results);
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
            Type responseType  = new TypeToken<Map<String, FoundCache>>() {}.getType();
            Map<String, FoundCache> foundCacheMap = gson.fromJson(json, responseType);
            receiver.onResults(foundCacheMap);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public interface MapCachesReceiver {
        void onResults(Map<String, MapCache> results);
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
            Map<String, MapCache> cacheMap = gson.fromJson(json, new TypeToken<Map<String, MapCache>>() {}.getType());
            receiver.onResults(cacheMap);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
