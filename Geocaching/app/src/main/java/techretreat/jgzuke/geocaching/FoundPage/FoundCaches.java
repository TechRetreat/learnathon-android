package techretreat.jgzuke.geocaching.FoundPage;

import java.util.Map;

public class FoundCaches {
    public Map<String, Cache> caches;

    public static class Cache {
        public String name;
        public int difficulty;
        public long found;
    }
}
