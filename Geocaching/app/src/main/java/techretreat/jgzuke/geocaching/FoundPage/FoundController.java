package techretreat.jgzuke.geocaching.FoundPage;

import android.content.Context;

import techretreat.jgzuke.geocaching.Utilities.DataUtilities;
import techretreat.jgzuke.geocaching.Utilities.DataUtilities.Receiver;

public class FoundController implements FoundFragment.Callback {

    private FoundFragment foundFragment;

    private GoToMapCallback goToMapCallback;

    public interface GoToMapCallback {
        void onGoToMap();
    }

    public FoundController(Context context, FoundFragment fragment, GoToMapCallback callback) {
        foundFragment = fragment;
        goToMapCallback = callback;

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
    public void onGoToMap() {
        if (goToMapCallback != null) {
            goToMapCallback.onGoToMap();
        }
    }

    @Override
    public void onSelectCache(String cacheId) {

    }
}
