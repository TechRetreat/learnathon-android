package techretreat.jgzuke.geocaching.FoundPage;

import android.content.Context;

import java.util.Map;

import techretreat.jgzuke.geocaching.Utilities.DataUtilities;
import techretreat.jgzuke.geocaching.R;

public class FoundDataInteractor {

    // System objects
    private Context context;

    // Callback
    private DataReceiver reciever;
    public interface DataReceiver {
        void getFoundCaches(Map<String, FoundCaches.Cache> caches);
    }

    public FoundDataInteractor(Context context, DataReceiver reciever) {
        this.context = context;
        this.reciever = reciever;

        getFoundCaches();
    }

    public void getFoundCaches() {
        FoundCaches foundCaches = DataUtilities.getResponse(context, FoundCaches.class, R.raw.caches_found);
        reciever.getFoundCaches(foundCaches.caches);
    }
}
