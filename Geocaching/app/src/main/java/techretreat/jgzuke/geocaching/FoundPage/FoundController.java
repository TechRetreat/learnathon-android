package techretreat.jgzuke.geocaching.FoundPage;

import android.content.Context;

import techretreat.jgzuke.geocaching.Utilities.DataUtilities;
import techretreat.jgzuke.geocaching.Utilities.DataUtilities.Receiver;

public class FoundController implements FoundFragment.Callback {

    private FoundFragment foundFragment;

    private ViewCacheOnMapCallback viewCacheOnMapCallback;
    public interface ViewCacheOnMapCallback {
        void viewFoundCacheOnMap(String cacheId);
    }

    public FoundController(Context context, FoundFragment fragment, ViewCacheOnMapCallback callback) {
        foundFragment = fragment;
        viewCacheOnMapCallback = callback;

        DataUtilities.getFoundCaches(context, new Receiver<FoundCaches>() {
            @Override
            public void onResults(FoundCaches results) {
                foundFragment.setFoundCaches(results.caches);
            }

            @Override
            public void onError() {

            }
        });
    }

    // FoundFragment.Callback
    @Override
    public void selectCache(String cacheId) {
        if(viewCacheOnMapCallback == null) {
            return;
        }
        viewCacheOnMapCallback.viewFoundCacheOnMap(cacheId);
    }
}
