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
import java.util.List;


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
        long countUpdateRow;
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
            Log.i(TAG, "update " + countUpdateRow + " row: TEAM_ID = " + standing.getTeamId());
            if (countUpdateRow == 0){
                _ID = mDataBase.insert(TeamStandingTable.NAME, null, contentValues);
                Log.i(TAG, "insert Row: ID = " + _ID + " : TEAM_ID = " +  standing.getTeamId());
            }
            else if (countUpdateRow > 1){
                Log.i(TAG, "ERROR: countUpdateRow = " + countUpdateRow);
            }
        }
    }

    private ContentValues getContentValuesOfEvent(Event event){
        ContentValues values = new ContentValues();
        values.put(EventTable.Cols.MATCH_ID, event.getId());
        values.put(EventTable.Cols.COMPETITION_ID, event.getCompetitionId());
        values.put(EventTable.Cols.DATE, event.getDate());
        values.put(EventTable.Cols.MATCH_DAY, event.getMatchday());
        values.put(EventTable.Cols.HOME_TEAM_NAME, event.getHomeTeamName());
        values.put(EventTable.Cols.HOME_TEAM_ID, event.getHomeTeamId());
        values.put(EventTable.Cols.AWAY_TEAM_NAME, event.getAwayTeamName());
        values.put(EventTable.Cols.AWAY_TEAM_ID, event.getAwayTeamId());
        values.put(EventTable.Cols.STATUS, event.getStatus());
        values.put(EventTable.Cols.GOALS_HOME_TEAM, event.getResult().getGoalsHomeTeam());
        values.put(EventTable.Cols.GOALS_AWAY_TEAM, event.getResult().getGoalsAwayTeam());
        return values;
    }

    public void updateAndInsertEvents(List<Event> eventsList) {
        Log.i(TAG, "table " + EventTable.NAME + " START UPDATING...");
        String competitionId = String.valueOf(eventsList.get(0).getCompetitionId());
        Cursor cursor = mDataBase.query(EventTable.NAME, null, EventTable.Cols.COMPETITION_ID + " =? ", new String[]{competitionId}, null, null, null);
        try {
            if (cursor.getCount() == 0) {
                long start = System.currentTimeMillis();
                for (int i = 0; i < eventsList.size(); i++) {
                    mDataBase.insert(EventTable.NAME, null, getContentValuesOfEvent(eventsList.get(i)));
                    Log.i(TAG, "insert element in " + EventTable.NAME);
                }
                long end = System.currentTimeMillis();
                Log.i(TAG, "insert " + eventsList.size() + " rows for " + (end - start) + " ms");
            }
            else {
                cursor.moveToFirst();
                int i = 0;
                while (!cursor.isAfterLast()) {
                    int row = mDataBase.update(EventTable.NAME, getContentValuesOfEvent(eventsList.get(i)),
                            EventTable.Cols.MATCH_ID + " =? ", new String[]{String.valueOf(eventsList.get(i).getId())});
                    Log.i(TAG, "update " + row + " row successfully");
                    i++;
                    cursor.moveToNext();
                }
                Log.i(TAG, "update " + eventsList.size() + " rows");
            }
        }
        finally {
            cursor.close();
        }
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
}
