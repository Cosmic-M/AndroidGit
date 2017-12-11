package com.example.bigfi.football_fanatic.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.bigfi.football_fanatic.pojo_model.Championship;
import com.example.bigfi.football_fanatic.pojo_model.Event;
import com.example.bigfi.football_fanatic.pojo_model.League;
import com.example.bigfi.football_fanatic.pojo_model.Result;
import com.example.bigfi.football_fanatic.pojo_model.Standing;


/**
 * Created by Cosmic_M on 26.07.2017.
 */

public class FootballCursorWrapper extends CursorWrapper {

    public FootballCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Championship getChampionship(){
        String caption = getString(getColumnIndex(SchemaDB.LeagueTable.Cols.CAPTION));
        String league = getString(getColumnIndex(SchemaDB.LeagueTable.Cols.LEAGUE));
        int id = Integer.parseInt(getString(getColumnIndex(SchemaDB.LeagueTable.Cols.CAPTION_ID)));
        int numberOfTeams = Integer.parseInt(getString(getColumnIndex(SchemaDB.LeagueTable.Cols.QUANTITY_TEAMS)));
        Championship championship = new Championship(caption, league, id, numberOfTeams);
        return championship;
    }

    public Standing inflateTeamStanding(Standing standing) {
        int rank = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.POSITION));
        String team = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.TEAM_NAME));
        int teamId = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.TEAM_ID));
        int playedGames = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.PLAYED_GAMES));
        int winedGames = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.WINS));
        int drawnGames = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.DRAWS));
        int lossesGames = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.LOSSES));
        int points = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.POINTS));
        int goals = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.GOALS));
        int goalsAgainst = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.GOALS_AGAINST));
        int goalsDifference = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.GOALS_DIFFERENCE));
        String crestURI = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.CREST_URI));
        int preResult = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.PRE_RESULT));
        int pre2Result = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.PRE2_RESULT));
        int pre3Result = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.PRE3_RESULT));
        int pre4Result = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.PRE4_RESULT));
        int pre5Result = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.PRE5_RESULT));
        String group = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.InnerCols.GROUP));

        standing.setRank(rank);
        standing.setTeamName(team);
        standing.setTeamId(teamId);
        standing.setPlayedGames(playedGames);
        standing.setWins(winedGames);
        standing.setDraws(drawnGames);
        standing.setLosses(lossesGames);
        standing.setPoints(points);
        standing.setGoals(goals);
        standing.setGoalsAgainst(goalsAgainst);
        standing.setGoalDifference(goalsDifference);
        standing.setCrestURI(crestURI);
        standing.setPreResult(preResult);
        standing.setPre2Result(pre2Result);
        standing.setPre3Result(pre3Result);
        standing.setPre4Result(pre4Result);
        standing.setPre5Result(pre5Result);
        standing.setGroup(group);

        return standing;
    }

    public League getLeague() {
        String leagueCaption = getString(getColumnIndex(SchemaDB.TeamStandingTable.Cols.LEAGUE_CAPTION));
        int matchDay = getInt(getColumnIndex(SchemaDB.TeamStandingTable.Cols.MATCH_DAY));

        League league = new League(leagueCaption, matchDay);

        return league;
    }

    public Event getEvent(){
        int matchId = getInt(getColumnIndex(SchemaDB.EventTable.Cols.MATCH_ID));
        int competitionId = getInt(getColumnIndex(SchemaDB.EventTable.Cols.COMPETITION_ID));
        String date = getString(getColumnIndex(SchemaDB.EventTable.Cols.DATE));
        int matchDay = getInt(getColumnIndex(SchemaDB.EventTable.Cols.MATCH_DAY));
        String homeTeamName = getString(getColumnIndex(SchemaDB.EventTable.Cols.HOME_TEAM_NAME));
        int homeTeamId = getInt(getColumnIndex(SchemaDB.EventTable.Cols.HOME_TEAM_ID));
        String awayTeamName = getString(getColumnIndex(SchemaDB.EventTable.Cols.AWAY_TEAM_NAME));
        int awayTeamId = getInt(getColumnIndex(SchemaDB.EventTable.Cols.AWAY_TEAM_ID));
        String status = getString(getColumnIndex(SchemaDB.EventTable.Cols.STATUS));
        int goalsHomeTeam = getInt(getColumnIndex(SchemaDB.EventTable.Cols.GOALS_HOME_TEAM));
        int goalsAwayTeam = getInt(getColumnIndex(SchemaDB.EventTable.Cols.GOALS_AWAY_TEAM));
        Result result = new Result(goalsHomeTeam, goalsAwayTeam);
        Event event = new Event(matchId, competitionId, date, status, matchDay, homeTeamName, homeTeamId, awayTeamName, awayTeamId, result);
        return event;
    }
}