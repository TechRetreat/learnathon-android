package com.example.jzukewich.geocaching;

import java.util.Map;

/**
 * Created by jgzuke on 16-03-20.
 */
public class FoundCaches {
    public Map<String, Cache> caches;

    public static class Cache {
        public String name;
        public int difficulty;
        public long found;
    }
}
