package techretreat.jgzuke.geocaching.Utilities;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class DataUtilities {

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
