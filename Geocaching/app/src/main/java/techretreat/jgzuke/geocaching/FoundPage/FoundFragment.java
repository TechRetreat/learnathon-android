package techretreat.jgzuke.geocaching.FoundPage;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import techretreat.jgzuke.geocaching.R;
import techretreat.jgzuke.geocaching.UiUtilities;

public class FoundFragment extends Fragment {

    // Data
    private List<Map.Entry<String, FoundCaches.Cache>> foundCaches;

    // View
    private RecyclerView cachesRecycerView;
    private CacheAdapter cachesRecycerViewAdapter;

    // Callback
    private Callback callback;
    public interface Callback {
        void selectCache(String cacheId);
    }

    public static FoundFragment newInstance(Callback callback) {
        Bundle args = new Bundle();
        FoundFragment fragment = new FoundFragment();
        fragment.setArguments(args);
        fragment.setCallBack(callback);
        return fragment;
    }

    public void setCallBack(Callback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_found, container, false);

        cachesRecycerView = (RecyclerView) rootView.findViewById(R.id.found_caches_recycler_view);
        setUpRecyclerView();

        return rootView;
    }

    public void setUpRecyclerView() {
        cachesRecycerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cachesRecycerViewAdapter = new CacheAdapter();
        cachesRecycerView.setAdapter(cachesRecycerViewAdapter);
    }

    public void setFoundCaches(Map<String, FoundCaches.Cache> caches) {
        foundCaches = new ArrayList<>(caches.size());
        foundCaches.addAll(caches.entrySet());
        if(cachesRecycerViewAdapter != null) {
            cachesRecycerViewAdapter.notifyDataSetChanged();
        }
    }

    private class CacheHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView difficultyTextView;
        private final TextView findTimeTextView;

        public CacheHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.cache_name);
            difficultyTextView = (TextView) itemView.findViewById(R.id.cache_difficulty);
            findTimeTextView = (TextView) itemView.findViewById(R.id.cache_find_time);
        }

        public void bindCache(FoundCaches.Cache cache, final String cacheId) {
            nameTextView.setText(cache.name);
            difficultyTextView.setText(UiUtilities.getDifficultyString(cache.difficulty, getContext()));
            findTimeTextView.setText(UiUtilities.getTimeAgoString(cache.found, getContext()));
            findTimeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.selectCache(cacheId);
                }
            });
        }
    }

    private class CacheAdapter extends RecyclerView.Adapter<CacheHolder> {
        @Override
        public CacheHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_found_cache, parent, false);
            return new CacheHolder(view);
        }

        @Override
        public void onBindViewHolder(CacheHolder holder, int pos) {
            FoundCaches.Cache cache = foundCaches.get(pos).getValue();
            String cacheId = foundCaches.get(pos).getKey();
            holder.bindCache(cache, cacheId);
        }

        @Override
        public int getItemCount() {
            if(foundCaches == null) {
                return 0;
            }
            return foundCaches.size();
        }
    }
}
