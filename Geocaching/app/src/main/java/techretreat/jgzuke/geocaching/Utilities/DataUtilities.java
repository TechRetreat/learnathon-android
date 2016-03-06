package techretreat.jgzuke.geocaching.Utilities;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import techretreat.jgzuke.geocaching.FoundPage.FoundCaches;
import techretreat.jgzuke.geocaching.MapPage.MapCaches;
import techretreat.jgzuke.geocaching.R;

public class DataUtilities {

    public static void getMapCaches(Context context, Receiver<MapCaches> receiver) {
        getResponse(context, MapCaches.class, R.raw.caches, receiver);
    }

    public static void getFoundCaches(Context context, Receiver<FoundCaches> receiver) {
        getResponse(context, FoundCaches.class, R.raw.caches_found, receiver);
    }

    public interface Receiver<T> {
        void getResults(T results);
    }

    public static <T> void getResponse(Context context, Class<T> type, int jsonResId, Receiver<T> receiver) {
        // TODO: call api instead, run async
        try {
            InputStream is = context.getResources().openRawResource(jsonResId);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            Gson gson = new Gson();

            receiver.getResults(gson.fromJson(json, type));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        receiver.getResults(null);
    }
}
