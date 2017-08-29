package com.example.cosmic_m.footballFakts.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.example.cosmic_m.footballFakts.Event;
import com.example.cosmic_m.footballFakts.League;
import com.example.cosmic_m.footballFakts.TeamStanding;
import com.example.cosmic_m.footballFakts.database.SchemaDB.LeagueTable;
import com.example.cosmic_m.footballFakts.database.SchemaDB.TeamStandingTable;

/**
 * Created by Cosmic_M on 26.07.2017.
 */

public class FootballCursorWrapper extends CursorWrapper {
    public static final String TAG = "TESTING";
    public FootballCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public League getLeague(){
        String caption = getString(getColumnIndex(LeagueTable.Cols.CAPTION));
        String shortCaption = getString(getColumnIndex(LeagueTable.Cols.LEAGUE));
        int _id = Integer.parseInt(getString(getColumnIndex(LeagueTable.Cols.CAPTION_ID)));
        int quantityOfTeams = Integer.parseInt(getString(getColumnIndex(LeagueTable.Cols.QUANTITY_TEAMS)));
        Log.i(TAG, "getLeague : " + caption + " " + shortCaption + " " + _id + " " + quantityOfTeams);
        League league = new League(caption, shortCaption, _id, quantityOfTeams);
        return league;
    }

    public TeamStanding inflateTeamStanding(TeamStanding ts) {
        String teamName = getString(getColumnIndex(TeamStandingTable.Cols.InnerCols.TEAM));
        String rank = getString(getColumnIndex(TeamStandingTable.Cols.InnerCols.RANK));
        String teamId = getString(getColumnIndex(TeamStandingTable.Cols.InnerCols.TEAM_ID));
        String playedGames = getString(getColumnIndex(TeamStandingTable.Cols.InnerCols.PLAYED_GAMES));
        String winedGames = getString(getColumnIndex(TeamStandingTable.Cols.InnerCols.WINED_GAMES));
        String drawnGames = getString(getColumnIndex(TeamStandingTable.Cols.InnerCols.DRAWN_GAMES));
        String lossesGames = getString(getColumnIndex(TeamStandingTable.Cols.InnerCols.LOSSES_GAMES));
        String pts = getString(getColumnIndex(TeamStandingTable.Cols.InnerCols.POINTS));
        String goals = getString(getColumnIndex(TeamStandingTable.Cols.InnerCols.GOALS));
        String goalsAgainst = getString(getColumnIndex(TeamStandingTable.Cols.InnerCols.GOALS_AGAINST));
        String goalsDifference = getString(getColumnIndex(TeamStandingTable.Cols.InnerCols.GOALS_DIFFERENCE));
        int firstLastRes = getInt(getColumnIndex(TeamStandingTable.Cols.InnerCols.FIRST_LAST_RESULT));
        int secondLastRes = getInt(getColumnIndex(TeamStandingTable.Cols.InnerCols.SECOND_LAST_RESULT));
        int thirdLastRes = getInt(getColumnIndex(TeamStandingTable.Cols.InnerCols.THIRD_LAST_RESULT));
        int fourthLastRes = getInt(getColumnIndex(TeamStandingTable.Cols.InnerCols.FOURTH_LAST_RESULT));
        int fifthLastRes = getInt(getColumnIndex(TeamStandingTable.Cols.InnerCols.FIFTH_LAST_RESULT));
        String group = getString(getColumnIndex(TeamStandingTable.Cols.InnerCols.GROUP));

        TeamStanding.Standing standing = ts.createNewStandingClass();
        standing.setTeam(teamName);
        standing.setRank(rank);
        standing.setTeamId(teamId);
        standing.setPlayedGames(playedGames);
        standing.setWinGames(winedGames);
        standing.setDrawGames(drawnGames);
        standing.setLossGames(lossesGames);
        standing.setPts(pts);
        standing.setGoals(goals);
        standing.setGoalsAgainst(goalsAgainst);
        standing.setGoalsDifference(goalsDifference);
        int[] massLastRes = standing.getMassOfLastResults();
        massLastRes[0] = firstLastRes;
        massLastRes[1] = secondLastRes;
        massLastRes[2] = thirdLastRes;
        massLastRes[3] = fourthLastRes;
        massLastRes[4] = fifthLastRes;
        standing.setGroup(group);
        Log.i(TAG, "Standing create and attach to TeamStanding successfully");
        ts.addStanding(teamName, standing);
        return ts;
    }

    public TeamStanding getTeamStanding() {
        String leagueCaption = getString(getColumnIndex(TeamStandingTable.Cols.LEAGUE_CAPTION));
        String matchDay = getString(getColumnIndex(TeamStandingTable.Cols.MATCH_DAY));

        TeamStanding ts = new TeamStanding();
        ts.setLeagueCaption(leagueCaption);
        ts.setMatchDay(matchDay);
        Log.i(TAG, "teamStanding create and returned successfully");
        return ts;
    }

    public Event getEvent(){
        String matchId = getString(getColumnIndex(SchemaDB.EventTable.Cols.MATCH_ID));
        String competitionId = getString(getColumnIndex(SchemaDB.EventTable.Cols.COMPETITION_ID));
        String date = getString(getColumnIndex(SchemaDB.EventTable.Cols.DATE));
        String matchDay = getString(getColumnIndex(SchemaDB.EventTable.Cols.MATCH_DAY));
        String homeTeamName = getString(getColumnIndex(SchemaDB.EventTable.Cols.HOME_TEAM_NAME));
        String homeTeamId = getString(getColumnIndex(SchemaDB.EventTable.Cols.HOME_TEAM_ID));
        String awayTeamName = getString(getColumnIndex(SchemaDB.EventTable.Cols.AWAY_TEAM_NAME));
        String awayTeamId = getString(getColumnIndex(SchemaDB.EventTable.Cols.AWAY_TEAM_ID));
        String status = getString(getColumnIndex(SchemaDB.EventTable.Cols.STATUS));
        String goalsHomeTeam = getString(getColumnIndex(SchemaDB.EventTable.Cols.GOALS_HOME_TEAM));
        String goalsAwayTeam = getString(getColumnIndex(SchemaDB.EventTable.Cols.GOALS_AWAY_TEAM));
        Event event = new Event();
        event.setMatchId(matchId);
        event.setCompetitionId(competitionId);
        event.setMatchDate(date);
        event.setMatchDay(matchDay);
        event.setHomeTeamName(homeTeamName);
        event.setHomeTeamId(homeTeamId);
        event.setAwayTeamName(awayTeamName);
        event.setAwayTeamId(awayTeamId);
        event.setStatus(status);
        event.setGoalsHomeTeam(goalsHomeTeam);
        event.setGoalsAwayTeam(goalsAwayTeam);
        Log.i(TAG, "success worked method getEvent()!");
        return event;
    }
}
