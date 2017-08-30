package com.project.cosmic_m.football_facts;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Cosmic_M on 12.07.2017.
 * this comparator sort teams in group by:
 * 1. pts
 * 2. pts by personal сonfrontations
 * 3. difference of goals in personal сonfrontations
 * 4. count of away goals in personal сonfrontations
 * 5. difference of goals in all confrontations
 */

public class ComparatorForGroupStage implements Comparator<TeamStanding.Standing> {
    private Activity mActivity;

    private int ptsTeam1 = 0;
    private int ptsTeam2 = 0;

    private int team1_goalsCount = 0;
    private int team2_goalsCount = 0;

    private int team1_goalsCountAway = 0;
    private int team2_goalsCountAway = 0;

    ComparatorForGroupStage(Activity activity){
        mActivity = activity;
    }

    @Override
    public int compare(TeamStanding.Standing ob1, TeamStanding.Standing ob2) {
        try {
            String str1 = ob1.getGroup();
            String str2 = ob2.getGroup();

            if (!str1.equals(str2)) {
                return str1.compareTo(str2);
            }
            if (Integer.parseInt(ob1.getPts()) < Integer.parseInt(ob2.getPts())) {
                return 1;
            }
            if (Integer.parseInt(ob1.getPts()) > Integer.parseInt(ob2.getPts())) {
                return -1;
            }
            String teamIdOb1 = ob1.getTeamId();
            String teamIdOb2 = ob2.getTeamId();
            Collection<Event> e = SingletonLeague.getSingleton(mActivity).getEventMap(null, null).values();
            List<Event> list = new ArrayList<>(e);
            for (Event event : list) {
                if (event.getStatus().equals("FINISHED")) {
                    if (event.getHomeTeamId().equals(teamIdOb1) && event.getAwayTeamId().equals(teamIdOb2)) {
                        if (Integer.parseInt(event.getGoalsHomeTeam()) > Integer.parseInt(event.getGoalsAwayTeam())) {
                            ptsTeam1 += 3;
                        } else if (Integer.parseInt(event.getGoalsHomeTeam()) < Integer.parseInt(event.getGoalsAwayTeam())) {
                            ptsTeam2 += 3;
                        } else {
                            ptsTeam1 += 1;
                            ptsTeam2 += 1;
                        }
                        team1_goalsCount += Integer.parseInt(event.getGoalsHomeTeam());
                        team2_goalsCount += Integer.parseInt(event.getGoalsAwayTeam());
                        team2_goalsCountAway = Integer.parseInt(event.getGoalsAwayTeam());
                    }
                    if (event.getHomeTeamId().equals(teamIdOb2) && event.getAwayTeamId().equals(teamIdOb1)) {
                        if (Integer.parseInt(event.getGoalsHomeTeam()) > Integer.parseInt(event.getGoalsAwayTeam())) {
                            ptsTeam2 += 3;
                        } else if (Integer.parseInt(event.getGoalsHomeTeam()) < Integer.parseInt(event.getGoalsAwayTeam())) {
                            ptsTeam1 += 3;
                        } else {
                            ptsTeam1 += 1;
                            ptsTeam2 += 1;
                        }
                        team1_goalsCount += Integer.parseInt(event.getGoalsAwayTeam());
                        team2_goalsCount += Integer.parseInt(event.getGoalsHomeTeam());
                        team1_goalsCountAway = Integer.parseInt(event.getGoalsAwayTeam());
                    }
                }
            }
            if (ptsTeam1 > ptsTeam2) {
                return -1;
            }
            if (ptsTeam1 < ptsTeam2) {
                return 1;
            }

            if ((team1_goalsCount - 1) > team2_goalsCount) {
                return -1;
            }
            if ((team1_goalsCount - 1) < team2_goalsCount) {
                return 1;
            }
            if (team1_goalsCountAway > team2_goalsCountAway) {
                return -1;
            }
            if (Integer.parseInt(ob1.getGoalsDifference()) < Integer.parseInt(ob2.getGoalsDifference())) {
                return 1;
            } else if (Integer.parseInt(ob1.getGoalsDifference()) > Integer.parseInt(ob2.getGoalsDifference())) {
                return -1;
            }
            return 0;
        }
        finally {
            team1_goalsCount = 0;
            team2_goalsCount = 0;
            team1_goalsCountAway = 0;
            team2_goalsCountAway = 0;
            ptsTeam1 = 0;
            ptsTeam2 = 0;
        }
    }
}
