package techretreat.jgzuke.geocaching;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class DataUtilities {

    public static <T> T getResponse(Context context, Class<T> type, int jsonResId) {
        //TODO: take in api call instead, run async
        try {
            InputStream is = context.getResources().openRawResource(jsonResId);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            Gson gson = new Gson();

            return gson.fromJson(json, type);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
