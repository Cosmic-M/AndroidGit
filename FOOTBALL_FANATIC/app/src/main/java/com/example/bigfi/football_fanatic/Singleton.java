package com.example.bigfi.football_fanatic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bigfi.football_fanatic.database.FootballBaseHelper;
import com.example.bigfi.football_fanatic.database.FootballCursorWrapper;
import com.example.bigfi.football_fanatic.database.SchemaDB.LeagueTable;
import com.example.bigfi.football_fanatic.database.SchemaDB.EventTable;
import com.example.bigfi.football_fanatic.database.SchemaDB.TeamStandingTable;
import com.example.bigfi.football_fanatic.pojo_model.Championship;
import com.example.bigfi.football_fanatic.pojo_model.Event;
import com.example.bigfi.football_fanatic.pojo_model.League;
import com.example.bigfi.football_fanatic.pojo_model.Standing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by bigfi on 28.11.2017.
 */

public class Singleton {
    private static final String TAG = "Singleton";
    private static Singleton sSingleton;
    private Context mContext;
    private SQLiteDatabase mDataBase;

    public static Singleton getSingleton(Context context) {
        if (sSingleton == null) {
            sSingleton = new Singleton(context);
        }
        return sSingleton;
    }

    private Singleton(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new FootballBaseHelper(mContext).getWritableDatabase();
    }

    private ContentValues[] getMassContentValuesListOfLeague(List<Championship> championshipList){
        ContentValues[] massValues = new ContentValues[championshipList.size()];
        for (int i = 0; i < championshipList.size(); i++){
            ContentValues values = new ContentValues();
            values.put(LeagueTable.Cols.CAPTION, championshipList.get(i).getCaption());
            values.put(LeagueTable.Cols.LEAGUE, championshipList.get(i).getLeague());
            values.put(LeagueTable.Cols.CAPTION_ID, championshipList.get(i).getId());
            values.put(LeagueTable.Cols.QUANTITY_TEAMS, championshipList.get(i).getNumberOfTeams());
            massValues[i] = values;
        }
        return massValues;
    }

    public void insertListOfChampionship(List<Championship> listOfChampionships){
        ContentValues[] values = getMassContentValuesListOfLeague(listOfChampionships);
        for (int i = 0; i < listOfChampionships.size(); i++){
            Cursor cursor = mDataBase.query(LeagueTable.NAME, null, LeagueTable.Cols.CAPTION + " =? ",
                    new String[]{listOfChampionships.get(i).getCaption()}, null, null, null);
            if (cursor.getCount() == 0){
                Log.i(TAG, "cursor.getCount() = " + cursor.getCount());
                mDataBase.insert(LeagueTable.NAME, null, values[i]);
            }
            cursor.close();
        }
    }

    private FootballCursorWrapper getCursor(String name, String whereClause, String...args){
        Cursor cursor = mDataBase.query(
                name,
                null,
                whereClause,
                args,
                null,
                null,
                null
        );
        return new FootballCursorWrapper(cursor);
    }

    private FootballCursorWrapper getCursor(String name, String[] columns, String whereClause, String...args){
        Cursor cursor = mDataBase.query(
                name,
                columns,
                whereClause,
                args,
                null,
                null,
                null
        );
        return new FootballCursorWrapper(cursor);
    }

    private static ContentValues getContentValuesOfStanding(Standing standing, String leagueCaption, int matchDay) {
        ContentValues values = new ContentValues();
        values.put(TeamStandingTable.Cols.LEAGUE_CAPTION, leagueCaption);
        values.put(TeamStandingTable.Cols.MATCH_DAY, matchDay);
        values.put(TeamStandingTable.Cols.InnerCols.TEAM_NAME, standing.getTeamName());
        values.put(TeamStandingTable.Cols.InnerCols.POSITION, standing.getPosition());
        values.put(TeamStandingTable.Cols.InnerCols.TEAM_ID, standing.getTeamId());
        values.put(TeamStandingTable.Cols.InnerCols.PLAYED_GAMES, standing.getPlayedGames());
        values.put(TeamStandingTable.Cols.InnerCols.WINS, standing.getWins());
        values.put(TeamStandingTable.Cols.InnerCols.DRAWS, standing.getDraws());
        values.put(TeamStandingTable.Cols.InnerCols.LOSSES, standing.getLosses());
        values.put(TeamStandingTable.Cols.InnerCols.POINTS, standing.getPoints());
        values.put(TeamStandingTable.Cols.InnerCols.GOALS, standing.getGoals());
        values.put(TeamStandingTable.Cols.InnerCols.GOALS_AGAINST, standing.getGoalsAgainst());
        values.put(TeamStandingTable.Cols.InnerCols.GOALS_DIFFERENCE, standing.getGoalDifference());
        values.put(TeamStandingTable.Cols.InnerCols.CREST_URI, standing.getCrestURI());
        values.put(TeamStandingTable.Cols.InnerCols.PRE_RESULT, standing.getPreResult());
        values.put(TeamStandingTable.Cols.InnerCols.PRE2_RESULT, standing.getPre2Result());
        values.put(TeamStandingTable.Cols.InnerCols.PRE3_RESULT, standing.getPre3Result());
        values.put(TeamStandingTable.Cols.InnerCols.PRE4_RESULT, standing.getPre4Result());
        values.put(TeamStandingTable.Cols.InnerCols.PRE5_RESULT, standing.getPre5Result());
        values.put(TeamStandingTable.Cols.InnerCols.GROUP, standing.getGroup());
        return values;
    }

    public void updateAndInsertTeamStanding(League league){
        int countUpdateRow;
        long _ID;
        String leagueCaption = league.getLeagueCaption();
        int matchDay = league.getMatchday();
        List<Standing> standingsList = league.getStanding();

        for (Standing standing : standingsList){
            ContentValues contentValues = getContentValuesOfStanding(standing, leagueCaption, matchDay);
            countUpdateRow = mDataBase.update(TeamStandingTable.NAME, contentValues,
                    TeamStandingTable.Cols.LEAGUE_CAPTION + " =? and " +
                            TeamStandingTable.Cols.InnerCols.TEAM_NAME + " =? ",
                    new String[]{leagueCaption, standing.getTeamName()});
            Log.i(TAG, "update " + countUpdateRow + " row: TEAM_ID = " + standing.getTeamId() + " TEAM_NAME = " + standing.getTeamName());
            if (countUpdateRow == 0){
                _ID = mDataBase.insert(TeamStandingTable.NAME, null, contentValues);
                Log.i(TAG, "insert Row: ID = " + _ID + " : TEAM_ID = " +  standing.getTeamId() + " TEAM_NAME = " + standing.getTeamName());
            }
            else if (countUpdateRow > 1){
                Log.i(TAG, "ERROR: countUpdateRow = " + countUpdateRow);
            }
        }
    }

    private ContentValues getContentValuesOfEvent(Event event){
        ContentValues values = new ContentValues();
        values.put(EventTable.Cols.COMPETITION_ID, event.getCompetitionId());
        values.put(EventTable.Cols.MATCH_ID, event.getId());
        values.put(EventTable.Cols.HOME_TEAM_ID, event.getHomeTeamId());
        values.put(EventTable.Cols.AWAY_TEAM_ID, event.getAwayTeamId());
        values.put(EventTable.Cols.DATE, event.getDate());
        values.put(EventTable.Cols.STATUS, event.getStatus());
        values.put(EventTable.Cols.MATCH_DAY, event.getMatchday());
        values.put(EventTable.Cols.HOME_TEAM_NAME, event.getHomeTeamName());
        values.put(EventTable.Cols.AWAY_TEAM_NAME, event.getAwayTeamName());
        values.put(EventTable.Cols.GOALS_HOME_TEAM, event.getResult().getGoalsHomeTeam());
        values.put(EventTable.Cols.GOALS_AWAY_TEAM, event.getResult().getGoalsAwayTeam());
        return values;
    }

    public void updateAndInsertEvents(List<Event> events) {
        Log.i(TAG, "table " + EventTable.NAME + " START INSERT_OR_UPDATE...");
        int competitionId = events.get(0).getCompetitionId();
        Log.i(TAG, "COMPETITION_ID before request to DB = " + competitionId);

        Cursor cursor = mDataBase.query(EventTable.NAME, null, EventTable.Cols.COMPETITION_ID + " = ? ", new String[]{Integer.toString(competitionId)}, null, null, null);

        Log.i(TAG, "CURSOR.getCOUNT() = " + cursor.getCount());

        try {
            if (cursor.getCount() == 0) {
                long start = System.currentTimeMillis();
                for (int i = 0; i < events.size(); i++) {
                    long insertRorCount = mDataBase.insert(EventTable.NAME, null, getContentValuesOfEvent(events.get(i)));
                    Log.i(TAG, "insert " + insertRorCount + " element in " + EventTable.NAME);
                }
                long end = System.currentTimeMillis();
                Log.i(TAG, "insert " + events.size() + " rows for " + (end - start) + " ms");
            }
            else {
                cursor.moveToFirst();
                int i = 0;
                long start = System.currentTimeMillis();
                while (!cursor.isAfterLast()) {
                    int row = mDataBase.update(EventTable.NAME, getContentValuesOfEvent(events.get(i)),
                            EventTable.Cols.MATCH_ID + " =? ", new String[]{Integer.toString(events.get(i).getMatchId())});
                    Log.i(TAG, "update " + row + " row successfully");
                    i++;
                    cursor.moveToNext();
                }
                long end = System.currentTimeMillis();
                Log.i(TAG, "update " + events.size() + " rows for " + (end - start) + " ms");
            }
        }
        finally {
            cursor.close();
        }
    }

    public List<Championship> getChampionships(){
        List<Championship> championships = new ArrayList<>();
        FootballCursorWrapper footballCursorWrapper = getCursor(LeagueTable.NAME, null, null);
        try{
            footballCursorWrapper.moveToFirst();
            while (!footballCursorWrapper.isAfterLast()){
                Championship  championship = footballCursorWrapper.getChampionship();
                championships.add(championship);
                footballCursorWrapper.moveToNext();
            }
        }
        finally {
            footballCursorWrapper.close();
        }
        return championships;
    }

    public League getTeamsStandingMap(String clause, String[] arg) {
        boolean flag = true;
        League league = null;
        List<Standing> standingList = new ArrayList<>();
        FootballCursorWrapper footballCursorWrapper = getCursor(TeamStandingTable.NAME, clause, arg);
        try {
            footballCursorWrapper.moveToFirst();
            while (!footballCursorWrapper.isAfterLast()){
                if (flag){
                    league = footballCursorWrapper.getLeague();
                    flag = false;
                }
                else {
                    Standing standing = new Standing();
                    standingList.add(footballCursorWrapper.inflateTeamStanding(standing));
                    footballCursorWrapper.moveToNext();
                }
            }
        }
        finally {
            footballCursorWrapper.close();
        }
        if (league != null) {
            league.setStanding(standingList);
        }
        return league;
    }

    public List<Event> getEventList(String clause, String[] args){
        List<Event> eventList = new ArrayList<>();
        FootballCursorWrapper footballCursorWrapper = getCursor(EventTable.NAME, clause, args);
        try {
            footballCursorWrapper.moveToFirst();
            while (!footballCursorWrapper.isAfterLast()){
                Event event = footballCursorWrapper.getEvent();
                eventList.add(event);
                footballCursorWrapper.moveToNext();
            }
        }
        finally {
            footballCursorWrapper.close();
        }
        return eventList;
    }

    public List<Event> getEventsByCompetitionAndTeam(int competitionId, int teamId){
        Log.i(TAG, "getEventsByCompetitionAndTeam started");
        List<Event> events = new ArrayList<>();

        String crossTables = "eventTable AS events INNER JOIN teamStandingTable AS standings_home "
                + "INNER JOIN teamStandingTable AS standings_away "
                + "ON events.homeTeamId = standings_home.teamId "
                + "AND events.awayTeamId = standings_away.teamId";
        String columns[] = { "events.*, standings_home.resourceOfPicture, standings_away.resourceOfPicture" };
        String selection = "(events.competitionId = ? AND standings_home.teamId = ?) OR "
                + "(events.competitionId = ? AND standings_away.teamId = ?)";
        String selectionArgs[] = {Integer.toString(competitionId), Integer.toString(teamId), Integer.toString(competitionId), Integer.toString(teamId)};

        FootballCursorWrapper footballCursorWrapper = getCursor(crossTables, columns, selection, selectionArgs);
        Log.i(TAG, "footballCursorWrapper.getCount() = " + footballCursorWrapper.getCount());
        Log.i(TAG, "footballCursorWrapper.getColumnCount() = " + footballCursorWrapper.getColumnCount());
        //ERROR: Couldn't read row 0, col -1 from CursorWindow.  Make sure the Cursor is initialized correctly before accessing data from it.
        Log.i(TAG, "before try...");
        try {
            footballCursorWrapper.moveToFirst();
            while (!footballCursorWrapper.isAfterLast()){
                Event event = footballCursorWrapper.getEvent();
                Log.i(TAG, "obtained event!");
                events.add(event);
                footballCursorWrapper.moveToNext();
            }
        }
        finally {
            footballCursorWrapper.close();
        }
        Log.i(TAG, "events.size() = " + events.size());
        return events;
    }
}
