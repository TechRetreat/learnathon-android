package techretreat.jgzuke.geocaching.Utilities;

import android.content.Context;
import android.text.format.DateUtils;

import techretreat.jgzuke.geocaching.R;

public class UiUtilities {

    public static String getDifficultyString(int difficulty, Context context) {
        return context.getString(R.string.difficulty_out_of_five, difficulty);
    }

    public static String getTimeAgoString(long timestamp, Context context) {
        String timeAgo = DateUtils.getRelativeTimeSpanString(context, timestamp).toString();
        return context.getString(R.string.found_time_ago, timeAgo);
    }
}
