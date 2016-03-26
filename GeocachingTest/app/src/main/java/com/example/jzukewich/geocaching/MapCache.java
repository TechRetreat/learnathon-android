package com.example.jzukewich.geocaching;

/**
 * Created by jgzuke on 16-03-26.
 */
public class MapCache {
    public String name;
    public String description;
    public int difficulty;
    public Location location;

    public static class Location {
        public double latitude;
        public double longitude;
    }
}
