package com.example.bigfi.football_fanatic;

import android.app.Activity;

import com.example.bigfi.football_fanatic.pojo_model.Event;
import com.example.bigfi.football_fanatic.pojo_model.Standing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by bigfi on 01.12.2017.
 */

public class GroupStageComparator implements Comparator<Standing> {

    /**
     * Created by Cosmic_M on 12.07.2017.
     * this comparator sort teams in group by:
     * 1. pts
     * 2. pts by personal сonfrontations
     * 3. difference of goals in personal сonfrontations
     * 4. count of away goals in personal сonfrontations
     * 5. difference of goals in all confrontations
     */
    private Activity mActivity;

    private int ptsTeam1 = 0;
    private int ptsTeam2 = 0;

    private int team1_goalsCount = 0;
    private int team2_goalsCount = 0;

    private int team1_goalsCountAway = 0;
    private int team2_goalsCountAway = 0;

    GroupStageComparator(Activity activity) {
        mActivity = activity;
    }

    @Override
    public int compare(Standing ob1, Standing ob2) {
        try {
            String str1 = ob1.getGroup();
            String str2 = ob2.getGroup();

            if (!str1.equals(str2)) {
                return str1.compareTo(str2);
            }
            if (ob1.getPoints() < ob2.getPoints()) {
                return 1;
            }
            if (ob1.getPoints() > ob2.getPoints()) {
                return -1;
            }
            int teamId_Ob1 = ob1.getTeamId();
            int teamId_Ob2 = ob2.getTeamId();
            List<Event> e = Singleton.getSingleton(mActivity).getEventList(null, null);
            List<Event> list = new ArrayList<>(e);
            for (Event event : list) {
                if (event.getStatus().equals("FINISHED")) {
                    if (event.getHomeTeamId().equals(teamId_Ob1) && event.getAwayTeamId().equals(teamId_Ob2)) {
                        if (event.getResult().getGoalsHomeTeam() > event.getResult().getGoalsAwayTeam()) {
                            ptsTeam1 += 3;
                        } else if (event.getResult().getGoalsHomeTeam() < event.getResult().getGoalsAwayTeam()) {
                            ptsTeam2 += 3;
                        } else {
                            ptsTeam1 += 1;
                            ptsTeam2 += 1;
                        }
                        team1_goalsCount += event.getResult().getGoalsHomeTeam();
                        team2_goalsCount += event.getResult().getGoalsAwayTeam();
                        team2_goalsCountAway = event.getResult().getGoalsAwayTeam();
                    }
                    if (event.getHomeTeamId().equals(teamId_Ob2) && event.getAwayTeamId().equals(teamId_Ob1)) {
                        if (event.getResult().getGoalsHomeTeam() > event.getResult().getGoalsAwayTeam()) {
                            ptsTeam2 += 3;
                        } else if (event.getResult().getGoalsHomeTeam() < event.getResult().getGoalsAwayTeam()) {
                            ptsTeam1 += 3;
                        } else {
                            ptsTeam1 += 1;
                            ptsTeam2 += 1;
                        }
                        team1_goalsCount += event.getResult().getGoalsAwayTeam();
                        team2_goalsCount += event.getResult().getGoalsHomeTeam();
                        team1_goalsCountAway = event.getResult().getGoalsAwayTeam();
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
            if (team1_goalsCountAway > team2_goalsCountAway) { // rule of away goal
                return -1;
            }
            if (ob1.getGoalDifference() < ob2.getGoalDifference()) {
                return 1;
            } else if (ob1.getGoalDifference() > ob2.getGoalDifference()) {
                return -1;
            }
            return 0;
        } finally {
            team1_goalsCount = 0;
            team2_goalsCount = 0;
            team1_goalsCountAway = 0;
            team2_goalsCountAway = 0;
            ptsTeam1 = 0;
            ptsTeam2 = 0;
        }
    }
}