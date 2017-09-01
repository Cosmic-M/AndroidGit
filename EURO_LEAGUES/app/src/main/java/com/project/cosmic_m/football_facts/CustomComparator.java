package com.project.cosmic_m.football_facts;

import java.util.Comparator;

/**
 * Created by Cosmic_M on 12.06.2017.
 */

public class CustomComparator implements Comparator<TeamStanding.Standing> {

    @Override
    public int compare(TeamStanding.Standing o1, TeamStanding.Standing o2) {
        if (Integer.parseInt(o1.getRank()) > Integer.parseInt(o2.getRank())) return 1;
        else return -1;
    }
}