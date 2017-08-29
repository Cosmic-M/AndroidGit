package com.example.cosmic_m.footballFakts;

import java.util.Comparator;

/**
 * Created by Cosmic_M on 06.05.2017.
 */

public class ComparatorTeams implements Comparator<TeamStanding.Standing> {

    @Override
    public int compare(TeamStanding.Standing a, TeamStanding.Standing b){
        Integer one, two;
        one = Integer.parseInt(a.getRank());
        two = Integer.parseInt(b.getRank());
        return one.compareTo(two);
    }
}
