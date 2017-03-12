/*
 *5.03.2017
 */

package com.bignerdranch.android.memory;

public class Results {
    private static int sPoints;
    private static int sExtraTime;

    public static int getPoints() {
        return sPoints;
    }

    public static void addPoints(int points) {
        sPoints += points;
    }

    public static void resetPoints(){
        sPoints = 0;
    }

    public static int getExtraTime() {
        return sExtraTime;
    }

    public static void setExtraTime(int extraTime) {
        sExtraTime = extraTime;
    }
}
