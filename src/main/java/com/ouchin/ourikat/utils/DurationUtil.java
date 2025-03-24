package com.ouchin.ourikat.utils;

import java.time.Duration;

public class DurationUtil {

    public static String formatDuration(Duration duration) {
        if (duration == null) {
            return "0H0M";
        }

        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();

        return String.format("%dH%dM", hours, minutes);
    }
}