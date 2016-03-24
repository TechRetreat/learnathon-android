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

        DataUtilities.getResponseTest(getContext(), new DataUtilities.Receiver() {
            @Override
            public void onResults(FoundCaches results) {
                // Do something with the results
            }
        });
        ArrayList<String> itemNames = new ArrayList<>();
        itemNames.add("Cache 1");
        itemNames.add("Cache 2");
        itemNames.add("Cache 3");
        cachesRecycerViewAdapter = new CacheAdapter(itemNames);
        cachesRecycerView.setAdapter(cachesRecycerViewAdapter);

        return rootView;
    }

    private class CacheHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;

        public CacheHolder(TextView itemView) {
            super(itemView);
            nameTextView = itemView;
        }

        public void bindCache(String name) {
            nameTextView.setText(name);
        }
    }

    private class CacheAdapter extends RecyclerView.Adapter<CacheHolder> {

        private List<String> items;

        public CacheAdapter(List<String> items) {
            this.items = items;
        }

        @Override
        public CacheHolder onCreateViewHolder(ViewGroup parent, int pos) {
            TextView view = new TextView(getContext());
            return new CacheHolder(view);
        }

        @Override
        public void onBindViewHolder(CacheHolder holder, int pos) {
            String item = items.get(pos);
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
