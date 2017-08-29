package com.project.cosmic_m.football_facts;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class TeamStanding implements Serializable{

    private String mLeagueCaption;
    private String mMatchDay;
    private Map<String, Standing> mStanding = new TreeMap<>();

    public Standing getStandingByTeam(String team){
        return mStanding.get(team);
    }

    public class Standing {
        private String mRank;
        private String mTeam;
        private String mTeamId;
        private String mPlayedGames;
        private String mWinGames;
        private String mDrawGames;
        private String mLossGames;
        private String mPts;
        private String mGoals;
        private String mGoalsAgainst;
        private String mGoalsDifference;
        private String mResourceOfPicture;
        private int[] fifeLastResults = new int[5];
        private String mGroup;

        public int[] getMassOfLastResults(){
            return fifeLastResults;
        }

        public String getResourceOfPicture() {
            return mResourceOfPicture;
        }

        public void setResourceOfPicture(String resourceOfPicture) {
            mResourceOfPicture = resourceOfPicture;
        }

        public String getRank() {
            return mRank;
        }

        public void setRank(String rank) {
            mRank = rank;
        }

        public String getTeam() {
            return mTeam;
        }

        public void setTeam(String team) {
            mTeam = team;
        }

        public String getTeamId() {
            return mTeamId;
        }

        public void setTeamId(String teamId) {
            mTeamId = teamId;
        }

        public String getPlayedGames() {
            return mPlayedGames;
        }

        public void setPlayedGames(String playedGames) {
            mPlayedGames = playedGames;
        }

        public String getWinGames() {
            return mWinGames;
        }

        public void setWinGames(String winGames) {
            mWinGames = winGames;
        }

        public String getDrawGames() {
            return mDrawGames;
        }

        public void setDrawGames(String drawGames) {
            mDrawGames = drawGames;
        }

        public String getLossGames() {
            return mLossGames;
        }

        public void setLossGames(String lossGames) {
            mLossGames = lossGames;
        }

        public String getPts() {
            return mPts;
        }

        public void setPts(String pts) {
            mPts = pts;
        }

        public String getGoals() {
            return mGoals;
        }

        public void setGoals(String goals) {
            mGoals = goals;
        }

        public String getGoalsAgainst() {
            return mGoalsAgainst;
        }

        public void setGoalsAgainst(String goalsAgainst) {
            mGoalsAgainst = goalsAgainst;
        }

        public String getGoalsDifference() {
            return mGoalsDifference;
        }

        public void setGoalsDifference(String goalsDifference) {
            mGoalsDifference = goalsDifference;
        }

        public String getGroup() {
            return mGroup;
        }

        public void setGroup(String group) {
            mGroup = group;
        }
    }//public class Standing

    public Standing createNewStandingClass(){
        return new Standing();
    }

    public String getLeagueCaption() {
        return mLeagueCaption;
    }

    public void setLeagueCaption(String leagueCaption) {
        mLeagueCaption = leagueCaption;
    }

    public String getMatchDay() {
        return mMatchDay;
    }

    public void setMatchDay(String matchDay) {
        mMatchDay = matchDay;
    }

    public Map<String, Standing> getStanding() {
        return mStanding;
    }

    public void addStanding(String team, Standing standing) {
        mStanding.put(team, standing);
    }
}