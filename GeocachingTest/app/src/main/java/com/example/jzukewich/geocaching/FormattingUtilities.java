package com.example.jzukewich.geocaching;

import android.content.Context;
import android.text.format.DateUtils;

/**
 * Created by jgzuke on 16-03-25.
 */
public class FormattingUtilities {
    public static String getDifficultyString(int difficulty, Context context) {
        return context.getString(R.string.difficulty_out_of_five, difficulty);
    }

    public static String getTimeAgoString(long timestamp, Context context) {
        String timeAgo = DateUtils.getRelativeTimeSpanString(context, timestamp).toString();
        return context.getString(R.string.find_time, timeAgo);
    }
}
