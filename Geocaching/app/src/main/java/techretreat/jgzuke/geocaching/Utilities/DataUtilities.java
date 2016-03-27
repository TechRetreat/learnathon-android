package techretreat.jgzuke.geocaching.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import techretreat.jgzuke.geocaching.FoundPage.FoundCaches;
import techretreat.jgzuke.geocaching.MapPage.MapCaches;
import techretreat.jgzuke.geocaching.R;

public class DataUtilities {

    private static final boolean USE_TEST_DATA = true;
    private static final String MAP_CACHES = "mapCaches";
    private static final String FOUND_CACHES = "foundCaches";
    private static final Map<String, Integer> PATH_TO_TEST_JSON_ID;

    static {
        Map<String, Integer> aMap = new HashMap<>(2);
        aMap.put(MAP_CACHES, R.raw.caches);
        aMap.put(FOUND_CACHES, R.raw.caches_found);
        PATH_TO_TEST_JSON_ID = Collections.unmodifiableMap(aMap);
    }

    public static void getMapCaches(Context context, Receiver<MapCaches> receiver) {
        getResponse(context, MAP_CACHES, receiver);
    }

    public static void getFoundCaches(Context context, Receiver<FoundCaches> receiver) {
        getResponse(context, FOUND_CACHES, receiver);
    }

    public interface Receiver<T> {
        void onResults(T results);

        void onError();
    }

    public static <T> void getResponse(Context context, String path, Receiver<T> receiver) {
        if (USE_TEST_DATA) {
            getResponseTest(context, path, receiver);
        } else {
            if (isNetworkAvailable(context)) {
                getResponseFromNetwork(context, path, receiver);
            } else {
                getResponseStored(context, path, receiver);
            }
        }
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;
    }

    public static <T> void getResponseTest(Context context, String path, Receiver<T> receiver) {
        try {
            InputStream is = context.getResources().openRawResource(PATH_TO_TEST_JSON_ID.get(path));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            Type type = new TypeToken<T>() {}.getType();
            T result = gson.fromJson(json, type);
            receiver.onResults(result);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        receiver.onError();
    }

    public static <T> void getResponseStored(Context context, String path, Receiver<T> receiver) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(path)) {
            String json = sharedPreferences.getString(path, "");

            Gson gson = new Gson();
            Type type = new TypeToken<T>() {}.getType();
            T result = gson.fromJson(json, type);
            receiver.onResults(result);
        } else {
            receiver.onError();
        }
    }

    public static <T> void getResponseFromNetwork(Context context, String path, Receiver<T> receiver) {
        new NetworkCall<>(context, receiver).execute(path);
    }

    private static class NetworkCall<T> extends AsyncTask<String, Void, T> {

        private Context context;
        private Receiver<T> receiver;

        public NetworkCall(Context context, Receiver<T> receiver) {
            this.context = context;
            this.receiver = receiver;
        }

        @Override
        protected T doInBackground(String... params) {
            String path = params[0]; // URL to call
            InputStream in;

            //TODO mod path to real one

            try {
                URL url = new URL(path);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (Exception e) {
                return null;
            }

            String json = in.toString();
            if (TextUtils.isEmpty(json)) {
                return null;
            }

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPreferences.edit().putString(path, json).apply();

            Gson gson = new Gson();
            Type type = new TypeToken<T>() {}.getType();
            return gson.fromJson(json, type);
        }

        @Override
        protected void onPostExecute(T result) {
            if (result == null) {
                receiver.onError();
            } else {
                receiver.onResults(result);
            }
        }
    }
}
