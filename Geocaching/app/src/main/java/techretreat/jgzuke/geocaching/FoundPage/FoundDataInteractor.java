package techretreat.jgzuke.geocaching.FoundPage;

import android.content.Context;

import techretreat.jgzuke.geocaching.DataUtilities;
import techretreat.jgzuke.geocaching.R;

public class FoundDataInteractor {

    public interface DataReceiver {
        void getFoundCaches(FoundCaches.Cache[] caches);
    }

    private String userId;
    private Context context;
    private DataReceiver reciever;

    public FoundDataInteractor(String userId, Context context, DataReceiver reciever) {
        this.userId = userId;
        this.context = context;
        this.reciever = reciever;
    }

    public void getFoundCaches() {
        FoundCaches response = new DataUtilities<FoundCaches>().getResponse(context, FoundCaches.class, R.raw.caches_found);
        reciever.getFoundCaches(response.caches);
    }
}
