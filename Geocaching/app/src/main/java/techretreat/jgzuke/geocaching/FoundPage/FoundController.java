package techretreat.jgzuke.geocaching.FoundPage;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.Map;

import techretreat.jgzuke.geocaching.R;
import techretreat.jgzuke.geocaching.Utilities.DataUtilities;
import techretreat.jgzuke.geocaching.Utilities.DataUtilities.Receiver;

public class FoundController implements FoundFragment.Callback {

    private FoundFragment foundFragment;

    private Callback callback;
    public interface Callback {
        public void viewFoundCacheOnMap(String cacheId);
    }

    public FoundController(Context context, Callback callback) {
        foundFragment = FoundFragment.newInstance(this);
        this.callback = callback;

        DataUtilities.getResponse(context, FoundCaches.class, R.raw.caches_found, new Receiver<FoundCaches>() {
            @Override
            public void getResults(FoundCaches results) {
                foundFragment.setFoundCaches(results.caches);
            }
        });
    }

    public Fragment getFragment() {
        return foundFragment;
    }

    // FoundFragment.Callback
    @Override
    public void selectCache(String cacheId) {
        callback.viewFoundCacheOnMap(cacheId);
    }
}
