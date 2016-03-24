package com.example.jzukewich.geocaching;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jgzuke on 16-03-20.
 */
public class DataUtilities {

    public interface Receiver {
        void onResults(FoundCaches results);
    }

    public static void getResponseTest(Context context, Receiver receiver) {
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
        /*try {
            InputStream is = context.getResources().openRawResource(PATH_TO_TEST_JSON_ID.get(path));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            receiver.onResults(gson.fromJson(json, type));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        receiver.onError();*/
    }
}
