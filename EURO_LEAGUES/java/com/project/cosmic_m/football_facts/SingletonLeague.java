package com.project.cosmic_m.football_facts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.project.cosmic_m.football_facts.database.FootballBaseHelper;
import com.project.cosmic_m.football_facts.database.FootballCursorWrapper;
import com.project.cosmic_m.football_facts.database.SchemaDB.EventTable;
import com.project.cosmic_m.football_facts.database.SchemaDB.LeagueTable;
import com.project.cosmic_m.football_facts.database.SchemaDB.TeamStandingTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Cosmic_M on 13.05.2017.
 */

public class SingletonLeague {
    private static final String TAG = "TAG";
    private static SingletonLeague sSingleton;
    private Context mContext;
    private SQLiteDatabase mDataBase;
    //private Map<String, League> mLeagueMap;//key=mLeagueCaption
    //private Map<String, Event> mEventMap;//key=matchId
    private Map<String, String> mStoreOfImageResource; //key=TeamId
    private Map<String, Drawable> mCoupleDrawable;
    private Map<String, Drawable> mEmblemDrawable;//key=TeamId

    public static SingletonLeague getSingleton(Context context) {
        if (sSingleton == null) {
            sSingleton = new SingletonLeague(context);
        }
        return sSingleton;
    }

    private SingletonLeague(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new FootballBaseHelper(mContext).getWritableDatabase();
        //mLeagueMap = new HashMap<>();
        //mTeamsStanding = new HashMap<>();
        //mEventMap = new HashMap<>();
        mStoreOfImageResource = new HashMap<>();
        mCoupleDrawable = new HashMap<>();
        mEmblemDrawable = new HashMap<>();
    }

//    public Map<String, Event> getEventMap(){
//        return mEventMap;
//    }

//    public void setEventMap(Map<String, Event> eventMap){
//        mEventMap = eventMap;
//    }

//    public void setEmblemDrawable(String teamId, Drawable drawable){
//        mEmblemDrawable.put(teamId, drawable);
//    }

//    public void setEmblemDrawable(Map<String, Drawable> map){
//        mEmblemDrawable = map;
//    }

    public Drawable getEmblemDrawable(String teamId){
        return mEmblemDrawable.get(teamId);
    }

    public Map<String, Drawable> getEmblemDrawableMap(){
        return mEmblemDrawable;
    }

    private static ContentValues[] getMassContentValuesListOfLeague(Map<String, League> leagueMap){
        List<League> leagueList = new ArrayList<>(leagueMap.values());
        ContentValues[] massValues = new ContentValues[leagueList.size()];
        for (int i = 0; i < leagueList.size(); i++){
            ContentValues values = new ContentValues();
            values.put(LeagueTable.Cols.CAPTION, leagueList.get(i).getCaption());
            values.put(LeagueTable.Cols.LEAGUE, leagueList.get(i).getLeague());
            values.put(LeagueTable.Cols.CAPTION_ID, leagueList.get(i).getId());
            values.put(LeagueTable.Cols.QUANTITY_TEAMS, leagueList.get(i).getTeamsCount());
            massValues[i] = values;
        }
        return massValues;
    }

    public void insertListOfLeague(Map<String, League> leagueMap){
        ContentValues[] values = getMassContentValuesListOfLeague(leagueMap);
        Set<String> leagues = leagueMap.keySet();
        String[] allLeagues = new String[leagues.size()];
        int index = 0;
        for (String liga : leagues){
            allLeagues[index++] = liga;
        }
        for (int i = 0; i < values.length; i++){
            Cursor cursor = mDataBase.query(LeagueTable.NAME, null, LeagueTable.Cols.CAPTION + " =? ",
                    new String[]{leagueMap.get(allLeagues[i]).getCaption()}, null, null, null);
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

    private static ContentValues[] getMassContentValuesOfTeamStanding(TeamStanding teamStanding){
        ContentValues[] massValues = new ContentValues[teamStanding.getStanding().size()];
        Set<String> teams = teamStanding.getStanding().keySet();
        String[] massTeams = new String[teamStanding.getStanding().size()];
        int j = 0;
        for (String team : teams){
            massTeams[j++] = team;
        }
        for (int i = 0; i < massValues.length; i++) {
            ContentValues values = new ContentValues();
            values.put(TeamStandingTable.Cols.LEAGUE_CAPTION, teamStanding.getLeagueCaption());
            values.put(TeamStandingTable.Cols.MATCH_DAY, teamStanding.getMatchDay());
            Log.i(TAG, teamStanding.getLeagueCaption());
            values.put(TeamStandingTable.Cols.InnerCols.TEAM, teamStanding.getStandingByTeam(massTeams[i]).getTeam());
            values.put(TeamStandingTable.Cols.InnerCols.RANK, teamStanding.getStandingByTeam(massTeams[i]).getRank());
            values.put(TeamStandingTable.Cols.InnerCols.TEAM_ID, teamStanding.getStandingByTeam(massTeams[i]).getTeamId());
            values.put(TeamStandingTable.Cols.InnerCols.PLAYED_GAMES, teamStanding.getStandingByTeam(massTeams[i]).getPlayedGames());
            values.put(TeamStandingTable.Cols.InnerCols.WINED_GAMES, teamStanding.getStandingByTeam(massTeams[i]).getWinGames());
            values.put(TeamStandingTable.Cols.InnerCols.DRAWN_GAMES, teamStanding.getStandingByTeam(massTeams[i]).getDrawGames());
            values.put(TeamStandingTable.Cols.InnerCols.LOSSES_GAMES, teamStanding.getStandingByTeam(massTeams[i]).getLossGames());
            values.put(TeamStandingTable.Cols.InnerCols.POINTS, teamStanding.getStandingByTeam(massTeams[i]).getPts());
            values.put(TeamStandingTable.Cols.InnerCols.GOALS, teamStanding.getStandingByTeam(massTeams[i]).getGoals());
            values.put(TeamStandingTable.Cols.InnerCols.GOALS_AGAINST, teamStanding.getStandingByTeam(massTeams[i]).getGoalsAgainst());
            values.put(TeamStandingTable.Cols.InnerCols.GOALS_DIFFERENCE, teamStanding.getStandingByTeam(massTeams[i]).getGoalsDifference());
            values.put(TeamStandingTable.Cols.InnerCols.RESOURCE_IMAGE, teamStanding.getStandingByTeam(massTeams[i]).getResourceOfPicture());
            values.put(TeamStandingTable.Cols.InnerCols.FIRST_LAST_RESULT, teamStanding.getStandingByTeam(massTeams[i]).getMassOfLastResults()[0]);
            values.put(TeamStandingTable.Cols.InnerCols.SECOND_LAST_RESULT, teamStanding.getStandingByTeam(massTeams[i]).getMassOfLastResults()[1]);
            values.put(TeamStandingTable.Cols.InnerCols.THIRD_LAST_RESULT, teamStanding.getStandingByTeam(massTeams[i]).getMassOfLastResults()[2]);
            values.put(TeamStandingTable.Cols.InnerCols.FOURTH_LAST_RESULT, teamStanding.getStandingByTeam(massTeams[i]).getMassOfLastResults()[3]);
            values.put(TeamStandingTable.Cols.InnerCols.FIFTH_LAST_RESULT, teamStanding.getStandingByTeam(massTeams[i]).getMassOfLastResults()[4]);
            values.put(TeamStandingTable.Cols.InnerCols.GROUP, teamStanding.getStandingByTeam(massTeams[i]).getGroup());
            massValues[i] = values;
        }
        return massValues;
    }

    public void updateAndInsertTeamStanding(TeamStanding ts){
        long countUpdateRow;
        long _ID;
        ContentValues[] massValues = getMassContentValuesOfTeamStanding(ts);
        String leagueCaption = ts.getLeagueCaption();
        String[] massTeams = new String[ts.getStanding().size()];
        Set<String> teams = ts.getStanding().keySet();
        int j = 0;
        for (String team : teams){
            massTeams[j++] = team;
        }
        for (int i = 0; i < massValues.length; i++){
            countUpdateRow = mDataBase.update(TeamStandingTable.NAME, massValues[i],
                            TeamStandingTable.Cols.LEAGUE_CAPTION + " =? and " +
                            TeamStandingTable.Cols.InnerCols.TEAM + " =? ",
                    new String[]{leagueCaption, massTeams[i]});
            Log.i(TAG, "update " + countUpdateRow + " row: TEAM_ID = " + massTeams[i].toString());
            if (countUpdateRow == 0){
                _ID = mDataBase.insert(TeamStandingTable.NAME, null, massValues[i]);
                Log.i(TAG, "size = " + massValues[i].size());
                Log.i(TAG, "insert Row: ID = " + _ID + " : TEAM_ID = " + massTeams[i].toString());
            }
            else if (countUpdateRow > 1){
                Log.i(TAG, "ERROR: countUpdateRow = " + countUpdateRow);
            }
        }
    }

    private static ContentValues getContentValuesOfEvent(Event event){
        ContentValues values = new ContentValues();
        values.put(EventTable.Cols.MATCH_ID, event.getMatchId());
        values.put(EventTable.Cols.COMPETITION_ID, event.getCompetitionId());
        values.put(EventTable.Cols.DATE, event.getMatchDate());
        values.put(EventTable.Cols.MATCH_DAY, event.getMatchDay());
        values.put(EventTable.Cols.HOME_TEAM_NAME, event.getHomeTeamName());
        values.put(EventTable.Cols.HOME_TEAM_ID, event.getHomeTeamId());
        values.put(EventTable.Cols.AWAY_TEAM_NAME, event.getAwayTeamName());
        values.put(EventTable.Cols.AWAY_TEAM_ID, event.getAwayTeamId());
        values.put(EventTable.Cols.STATUS, event.getStatus());
        values.put(EventTable.Cols.GOALS_HOME_TEAM, event.getGoalsHomeTeam());
        values.put(EventTable.Cols.GOALS_AWAY_TEAM, event.getGoalsAwayTeam());
        return values;
    }

    public void updateAndInsertEvents(Map<String, Event> eventsMap) {
        Log.i(TAG, "table " + EventTable.NAME + " START UPDATING...");
        String[] allKeys = new String[eventsMap.size()];
        int index = 0;
        for (String key : eventsMap.keySet()) {
            allKeys[index++] = key;
        }
        String competitionId = eventsMap.get(allKeys[0]).getCompetitionId();
        Cursor cursor = mDataBase.query(EventTable.NAME, null, EventTable.Cols.COMPETITION_ID + " =? ", new String[]{competitionId}, null, null, null);
        try {
            if (cursor.getCount() == 0) {
                long start = System.currentTimeMillis();
                for (int i = 0; i < allKeys.length; i++) {
                    mDataBase.insert(EventTable.NAME, null, getContentValuesOfEvent(eventsMap.get(allKeys[i])));
                    Log.i(TAG, "insert element in " + EventTable.NAME);
                }
                long end = System.currentTimeMillis();
                Log.i(TAG, "insert " + allKeys.length + " rows for " + (end - start) + " ms");
            }
            else {
                cursor.moveToFirst();
                int i = 0;
                while (!cursor.isAfterLast()) {
                    int row = mDataBase.update(EventTable.NAME, getContentValuesOfEvent(eventsMap.get(allKeys[i])),
                            EventTable.Cols.MATCH_ID + " =? ", new String[]{allKeys[i]});
                    Log.i(TAG, "update " + row + " row successfully");
                    i++;
                    cursor.moveToNext();
                }
                Log.i(TAG, "update " + allKeys.length + " rows");
            }
        }
        finally {
            cursor.close();
        }
    }

//    private static ContentValues getContentValuesOfLeague(League league){
//        ContentValues values = new ContentValues();
//        values.put(LeagueTable.Cols.CAPTION, league.getCaption());
//        values.put(LeagueTable.Cols.LEAGUE, league.getLeague());
//        values.put(LeagueTable.Cols.CAPTION_ID, league.getId());
//        values.put(LeagueTable.Cols.QUANTITY_TEAMS, league.getTeamsCount());
//        return values;
//    }

    public Map<String, League> getLeagueMap(){
        Map<String, League> map = new HashMap<>();
        FootballCursorWrapper footballCursorWrapper = getCursor(LeagueTable.NAME, null, null);
        try{
            footballCursorWrapper.moveToFirst();
            while (!footballCursorWrapper.isAfterLast()){
                League league = footballCursorWrapper.getLeague();
                String key = league.getCaption();
                map.put(key, league);
                footballCursorWrapper.moveToNext();
            }
        }
        finally {
            footballCursorWrapper.close();
        }
        return map;
    }

    public Map<String, TeamStanding> getTeamsStandingMap(String clause, String[] arg) {
        boolean flag = true;
        TeamStanding teamStanding = null;
        Map<String, TeamStanding> map = new HashMap<>();
        FootballCursorWrapper footballCursorWrapper = getCursor(TeamStandingTable.NAME, clause, arg);
        try {
            footballCursorWrapper.moveToFirst();
            while (!footballCursorWrapper.isAfterLast()){
                if (flag){
                    teamStanding = footballCursorWrapper.getTeamStanding();
                    flag = false;
                }
                else {
                    teamStanding = footballCursorWrapper.inflateTeamStanding(teamStanding);
                    footballCursorWrapper.moveToNext();
                }
            }
        }
        finally {
            footballCursorWrapper.close();
        }
        if (teamStanding != null) {
            map.put(teamStanding.getLeagueCaption(), teamStanding);
        }
        return map;
    }

//    public Map<String, TeamStanding> getTeamsStanding(){
//        return mTeamsStanding;
//    }
//
//    public void setTeamStanding(String leagueCaption, TeamStanding ts){
//        mTeamsStanding.put(leagueCaption, ts);
//    }

    public Map<String, Event> getEventMap(String clause, String[] args){
        Map<String, Event> map = new HashMap<>();
        FootballCursorWrapper footballCursorWrapper = getCursor(EventTable.NAME, clause, args);
        try {
            footballCursorWrapper.moveToFirst();
            while (!footballCursorWrapper.isAfterLast()){
                Event event = footballCursorWrapper.getEvent();
                String key = event.getMatchId();
                map.put(key, event);
                footballCursorWrapper.moveToNext();
            }
        }
        finally {
            footballCursorWrapper.close();
        }
        return map;
    }

    public Map<String, String> getStoreOfImageResource(){
        return mStoreOfImageResource;
    }

    public Drawable getTeamDrawable(String team) {
        return mCoupleDrawable.get(team);
    }

    public void setTeamDrawable(String team, Drawable drawable) {
        mCoupleDrawable.put(team, drawable);
    }
}
