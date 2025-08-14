package com.myanime.common.utils;

public class RandomUtils {
    public static double randomRate() {
        return Math.round((5 + (Math.random() * (10 - 5))) * 10) / 10.0; // [5.0, 10.0)
    }

    public static long randomViews() {
        return Math.round(Math.random() * 10000);
    }
}
