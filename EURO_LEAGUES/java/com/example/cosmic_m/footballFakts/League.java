package com.example.cosmic_m.footballFakts;

/**
 * Created by Cosmic_M on 15.07.2017.
 */

public class League {
    private String mCaption;//key
    private String mLeague;
    private int mId;
    private int mTeamCount;

    public League(String caption, String league, int id, int teamCount){
        mCaption = caption;
        mLeague = league;
        mId = id;
        mTeamCount = teamCount;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getLeague() {
        return mLeague;
    }

    public void setLeague(String league) {
        mLeague = league;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getTeamsCount() {
        return mTeamCount;
    }

    public void setTeamsCount(int teamsCount) {
        mTeamCount = teamsCount;
    }
}
