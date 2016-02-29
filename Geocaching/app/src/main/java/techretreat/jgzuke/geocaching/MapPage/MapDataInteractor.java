package techretreat.jgzuke.geocaching.MapPage;


import android.content.Context;

import java.util.Map;

import techretreat.jgzuke.geocaching.DataUtilities;
import techretreat.jgzuke.geocaching.R;

public class MapDataInteractor {

    public interface DataReceiver {
        void getCaches(Map<String, MapCaches.Cache> cacheIdToCache);
    }

    private String userId;
    private Context context;
    private DataReceiver reciever;

    public MapDataInteractor(String userId, Context context, DataReceiver reciever) {
        this.userId = userId;
        this.context = context;
        this.reciever = reciever;
    }

    public void getCaches() {
        MapCaches response = new DataUtilities<MapCaches>().getResponse(context, MapCaches.class, R.raw.caches);
        reciever.getCaches(response.caches);
    }
}