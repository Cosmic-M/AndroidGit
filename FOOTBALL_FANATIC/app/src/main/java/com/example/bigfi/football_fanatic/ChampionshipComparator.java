package com.example.bigfi.football_fanatic;

import com.example.bigfi.football_fanatic.pojo_model.Standing;

import java.util.Comparator;

/**
 * Created by bigfi on 17.12.2017.
 */

public class ChampionshipComparator implements Comparator<Standing> {
    @Override
    public int compare(Standing o1, Standing o2) {
        return  o1.getPosition() - o2.getPosition();
    }
}
