package com.project.cosmic_m.football_facts.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.project.cosmic_m.football_facts.Event;
import com.project.cosmic_m.football_facts.League;
import com.project.cosmic_m.football_facts.TeamStanding;


/**
 * Created by Cosmic_M on 26.07.2017.
 */

public class FootballCursorWrapper extends CursorWrapper {
    public FootballCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public League getLeague(){
        String caption = getString(getColumnIndex(SchemaDB.LeagueTable.Cols.CAPTION));
        String shortCaption = getString(getColumnIndex(SchemaDB.LeagueTable.Cols.LEAGUE));
        int _id = Integer.parseInt(getString(getColumnIndex(SchemaDB.LeagueTable.Cols.CAPTION_ID)));
        int quantityOfTeams = Integer.parseInt(getString(getColumnIndex(SchemaDB.LeagueTable.Cols.QUANTITY_TEAMS)));
        League league = new League(caption, shortCaption, _id, quantityOfTeams);
        return league;
    }

    public TeamStanding inflateTeamStanding(TeamStanding ts) {
        String teamName = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.TEAM));
        String rank = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.RANK));
        String teamId = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.TEAM_ID));
        String playedGames = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.PLAYED_GAMES));
        String winedGames = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.WINED_GAMES));
        String drawnGames = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.DRAWN_GAMES));
        String lossesGames = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.LOSSES_GAMES));
        String pts = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.POINTS));
        String goals = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.GOALS));
        String goalsAgainst = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.GOALS_AGAINST));
        String goalsDifference = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.GOALS_DIFFERENCE));
        int firstLastRes = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.FIRST_LAST_RESULT));
        int secondLastRes = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.SECOND_LAST_RESULT));
        int thirdLastRes = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.THIRD_LAST_RESULT));
        int fourthLastRes = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.FOURTH_LAST_RESULT));
        int fifthLastRes = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.FIFTH_LAST_RESULT));
        String group = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.GROUP));

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
        ts.addStanding(teamName, standing);
        return ts;
    }

    public TeamStanding getTeamStanding() {
        String leagueCaption = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.LEAGUE_CAPTION));
        String matchDay = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.MATCH_DAY));

        TeamStanding ts = new TeamStanding();
        ts.setLeagueCaption(leagueCaption);
        ts.setMatchDay(matchDay);
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
        return event;
    }
}
