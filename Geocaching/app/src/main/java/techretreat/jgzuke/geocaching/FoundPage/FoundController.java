package techretreat.jgzuke.geocaching.FoundPage;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.Map;

public class FoundController implements FoundDataInteractor.DataReceiver, FoundFragment.Callback {

    private FoundFragment foundFragment;
    private FoundDataInteractor foundDataInteractor;

    private Callback callback;
    public interface Callback {
        public void viewFoundCacheOnMap(String cacheId);
    }

    public FoundController(Context context, Callback callback) {
        foundFragment = FoundFragment.newInstance(this);
        foundDataInteractor = new FoundDataInteractor(context, this);
        this.callback = callback;
    }

    public Fragment getFragment() {
        return foundFragment;
    }

    // FoundDataInteractor.DataReceiver
    @Override
    public void getFoundCaches(Map<String, FoundCaches.Cache> caches) {
        foundFragment.setFoundCaches(caches);
    }

    // FoundFragment.Callback
    @Override
    public void selectCache(String cacheId) {
        callback.viewFoundCacheOnMap(cacheId);
    }
}
