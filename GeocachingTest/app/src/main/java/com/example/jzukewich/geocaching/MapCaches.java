package com.example.jzukewich.geocaching;

import java.util.Map;

/**
 * Created by jgzuke on 16-03-26.
 */
public class MapCaches {
    public Map<String, Cache> caches;

    public static class Cache {
        public String name;
        public String description;
        public int difficulty;
        public Location location;
    }

    public static class Location {
        public double latitude;
        public double longitude;
    }
}
