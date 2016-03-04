package techretreat.jgzuke.geocaching.FoundPage;

import android.content.Context;

import java.util.Map;

import techretreat.jgzuke.geocaching.Utilities.DataUtilities;
import techretreat.jgzuke.geocaching.R;

public class FoundDataInteractor implements DataUtilities.Receiver<FoundCaches> {

    // System objects
    private Context context;

    // Callback
    private DataReceiver reciever;

    @Override
    public void getResults(FoundCaches results) {

    }

    public interface DataReceiver {
        void getFoundCaches(Map<String, FoundCaches.Cache> caches);
    }

    public FoundDataInteractor(Context context, DataReceiver reciever) {
        this.context = context;
        this.reciever = reciever;

        DataUtilities.getResponse(context, FoundCaches.class, R.raw.caches_found, this);
    }
}
