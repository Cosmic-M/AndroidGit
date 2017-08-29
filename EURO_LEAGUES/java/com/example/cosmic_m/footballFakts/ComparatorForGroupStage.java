package com.example.cosmic_m.footballFakts;

import java.util.Comparator;

/**
 * Created by Cosmic_M on 12.07.2017.
 */

public class ComparatorForGroupStage implements Comparator<TeamStanding.Standing> {
    @Override
    public int compare(TeamStanding.Standing ob1, TeamStanding.Standing ob2) {
        String str1 = ob1.getGroup();
        String str2 = ob2.getGroup();
        if (!str1.equals(str2)) {
            return str1.compareTo(str2);
        } else if (Integer.parseInt(ob1.getPts()) < Integer.parseInt(ob2.getPts())) {
            return 1;
        } else if (Integer.parseInt(ob1.getPts()) > Integer.parseInt(ob2.getPts())) {
            return -1;
        } else if (Integer.parseInt(ob1.getGoalsDifference()) < Integer.parseInt(ob2.getGoalsDifference())) {
            return 1;
        } else if (Integer.parseInt(ob1.getGoalsDifference()) > Integer.parseInt(ob2.getGoalsDifference())) {
            return -1;
        }
        return 0;
    }
}
