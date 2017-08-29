package com.project.cosmic_m.football_facts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.cosmic_m.football_facts.database.SchemaDB.EventTable;
import com.project.cosmic_m.football_facts.database.SchemaDB.LeagueTable;
import com.project.cosmic_m.football_facts.database.SchemaDB.TeamStandingTable;

/**
 * Created by Cosmic_M on 23.07.2017.
 */

public class FootballBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String NAME = "footballDase";

    public FootballBaseHelper(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LeagueTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                LeagueTable.Cols.CAPTION + ", " +
                LeagueTable.Cols.LEAGUE + ", " +
                LeagueTable.Cols.CAPTION_ID + ", " +
                LeagueTable.Cols.QUANTITY_TEAMS +
                ")"
        );
        db.execSQL("create table " + TeamStandingTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                TeamStandingTable.Cols.LEAGUE_CAPTION + ", " +
                TeamStandingTable.Cols.MATCH_DAY + ", " +
                TeamStandingTable.Cols.InnerCols.TEAM + ", " +
                TeamStandingTable.Cols.InnerCols.RANK + ", " +
                TeamStandingTable.Cols.InnerCols.TEAM_ID + ", " +
                TeamStandingTable.Cols.InnerCols.PLAYED_GAMES + ", " +
                TeamStandingTable.Cols.InnerCols.WINED_GAMES + ", " +
                TeamStandingTable.Cols.InnerCols.DRAWN_GAMES + ", " +
                TeamStandingTable.Cols.InnerCols.LOSSES_GAMES + ", " +
                TeamStandingTable.Cols.InnerCols.POINTS + ", " +
                TeamStandingTable.Cols.InnerCols.GOALS + ", " +
                TeamStandingTable.Cols.InnerCols.GOALS_AGAINST + ", " +
                TeamStandingTable.Cols.InnerCols.GOALS_DIFFERENCE + ", " +
                TeamStandingTable.Cols.InnerCols.RESOURCE_IMAGE + ", " +
                TeamStandingTable.Cols.InnerCols.FIRST_LAST_RESULT + ", " +
                TeamStandingTable.Cols.InnerCols.SECOND_LAST_RESULT + ", " +
                TeamStandingTable.Cols.InnerCols.THIRD_LAST_RESULT + ", " +
                TeamStandingTable.Cols.InnerCols.FOURTH_LAST_RESULT + ", " +
                TeamStandingTable.Cols.InnerCols.FIFTH_LAST_RESULT + ", " +
                TeamStandingTable.Cols.InnerCols.GROUP +
                ")"
        );
        db.execSQL("create table " + EventTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                EventTable.Cols.MATCH_ID + ", " +
                EventTable.Cols.COMPETITION_ID+ ", " +
                EventTable.Cols.DATE + ", " +
                EventTable.Cols.MATCH_DAY + ", " +
                EventTable.Cols.HOME_TEAM_NAME + ", " +
                EventTable.Cols.HOME_TEAM_ID + ", " +
                EventTable.Cols.AWAY_TEAM_NAME + ", " +
                EventTable.Cols.AWAY_TEAM_ID + ", " +
                EventTable.Cols.STATUS + ", " +
                EventTable.Cols.GOALS_HOME_TEAM + ", " +
                EventTable.Cols.GOALS_AWAY_TEAM +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
