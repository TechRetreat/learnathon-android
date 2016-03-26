package com.example.jzukewich.geocaching;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by jzukewich on 3/16/16.
 */
public class FoundCachesFragment extends Fragment {

    private CacheAdapter cachesRecycerViewAdapter;
    private RecyclerView cachesRecycerView;

    private TextView helloWorldTextView;
    private Button clickMeButton;
    private int buttonClicks = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_found_caches, container, false);

        helloWorldTextView = (TextView) rootView.findViewById(R.id.my_text_view);
        clickMeButton = (Button) rootView.findViewById(R.id.my_button);

        clickMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Geocaching", "button Clicked");
                buttonClicks++;
                String clicks = getResources().getString(R.string.button_clicks, buttonClicks);
                helloWorldTextView.setText(clicks);
            }
        });


        cachesRecycerView = (RecyclerView) rootView.findViewById(R.id.found_caches_recycler_view);
        cachesRecycerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DataUtilities.getFoundCaches(getContext(), new DataUtilities.FoundCachesReceiver() {
            @Override
            public void onResults(Map<String, FoundCache> results) {
                List<FoundCache> caches = new ArrayList(results.values());
                cachesRecycerViewAdapter = new CacheAdapter(caches);
                cachesRecycerView.setAdapter(cachesRecycerViewAdapter);
            }
        });

        return rootView;
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

        public void bindCache(FoundCache cache) {
            nameTextView.setText(cache.name);
            difficultyTextView.setText(FormattingUtilities.getDifficultyString(cache.difficulty, getContext()));
            findTimeTextView.setText(FormattingUtilities.getTimeAgoString(cache.found, getContext()));
        }
    }

    private class CacheAdapter extends RecyclerView.Adapter<CacheHolder> {

        private List<FoundCache> items;

        public CacheAdapter(List<FoundCache> items) {
            this.items = items;
        }

        @Override
        public CacheHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_found_cache, parent, false);
            return new CacheHolder(view);
        }

        @Override
        public void onBindViewHolder(CacheHolder holder, int pos) {
            FoundCache item = items.get(pos);
            holder.bindCache(item);
        }

        @Override
        public int getItemCount() {
            if (items == null) {
                return 0;
            }
            return items.size();
        }
    }
}
