package com.project.cosmic_m.football_facts;

/**
 * Created by Cosmic_M on 15.07.2017.
 */

public class League {
    private boolean header;//this field determine header - year of season by building list of leagues season
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

    public void setHeader(boolean header){
        this.header = header;
    }

    public boolean getHeader(){
        return header;
    }

    public String getCaption() {
        return mCaption;
    }

    public String getLeague() {
        return mLeague;
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
}
