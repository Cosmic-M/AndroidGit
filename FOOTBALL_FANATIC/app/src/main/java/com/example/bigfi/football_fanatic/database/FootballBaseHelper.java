package com.example.bigfi.football_fanatic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bigfi.football_fanatic.database.SchemaDB.*;

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
                TeamStandingTable.Cols.InnerCols.TEAM_NAME + ", " +
                TeamStandingTable.Cols.InnerCols.POSITION + ", " +
                TeamStandingTable.Cols.InnerCols.TEAM_ID + ", " +
                TeamStandingTable.Cols.InnerCols.PLAYED_GAMES + ", " +
                TeamStandingTable.Cols.InnerCols.WINS + ", " +
                TeamStandingTable.Cols.InnerCols.DRAWS + ", " +
                TeamStandingTable.Cols.InnerCols.LOSSES + ", " +
                TeamStandingTable.Cols.InnerCols.POINTS + ", " +
                TeamStandingTable.Cols.InnerCols.GOALS + ", " +
                TeamStandingTable.Cols.InnerCols.GOALS_AGAINST + ", " +
                TeamStandingTable.Cols.InnerCols.GOALS_DIFFERENCE + ", " +
                TeamStandingTable.Cols.InnerCols.CREST_URI + ", " +
                TeamStandingTable.Cols.InnerCols.PRE_RESULT + ", " +
                TeamStandingTable.Cols.InnerCols.PRE2_RESULT + ", " +
                TeamStandingTable.Cols.InnerCols.PRE3_RESULT + ", " +
                TeamStandingTable.Cols.InnerCols.PRE4_RESULT + ", " +
                TeamStandingTable.Cols.InnerCols.PRE5_RESULT + ", " +
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

