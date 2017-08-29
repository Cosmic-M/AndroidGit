package com.example.cosmic_m.footballFakts;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Cosmic_M on 07.05.2017.
 */

public class Event {
    private String mMatchId;
    private String mCompetitionId;
    private String mMatchDate;
    private String mMatchDay;
    private String mHomeTeamName;
    private String mHomeTeamId;
    private String mAwayTeamName;
    private String mAwayTeamId;
    private String mStatus;
    private String mGoalsHomeTeam;
    private String mGoalsAwayTeam;
    private String mResourceOfPictureForHomeTeam;
    private String mResourceOfPictureForAwayTeam;

    public String getMatchId() {
        return mMatchId;
    }

    public void setMatchId(String match_id) {
        mMatchId = match_id;
    }

    public String getCompetitionId() {
        return mCompetitionId;
    }

    public void setCompetitionId(String competitionId) {// maybe should delete..
        mCompetitionId = competitionId;
    }

    public String getMatchDate() {
        return mMatchDate;
    }

    public void setMatchDate(String match_date) {
        mMatchDate = match_date;
    }

    public String getMatchDay() {
        return mMatchDay;
    }

    public void setMatchDay(String matchDay) {
        mMatchDay = matchDay;
    }

    public String getHomeTeamName() {
        return mHomeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        mHomeTeamName = homeTeamName;
    }

    public String getHomeTeamId() {
        return mHomeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        mHomeTeamId = homeTeamId;
    }

    public String getAwayTeamName() {
        return mAwayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        mAwayTeamName = awayTeamName;
    }

    public String getAwayTeamId() {
        return mAwayTeamId;
    }

    public void setAwayTeamId(String awayTeamId) {
        mAwayTeamId = awayTeamId;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getGoalsHomeTeam() {
        return mGoalsHomeTeam;
    }

    public void setGoalsHomeTeam(String goalsHomeTeam) {
        mGoalsHomeTeam = goalsHomeTeam;
    }

    public String getGoalsAwayTeam() {
        return mGoalsAwayTeam;
    }

    public void setGoalsAwayTeam(String goalsAwayTeam) {
        mGoalsAwayTeam = goalsAwayTeam;
    }

    public String getResourceOfPictureForHomeTeam() {// maybe should delete..
        return mResourceOfPictureForHomeTeam;
    }

    public String getResourceOfPictureForAwayTeam() {// maybe should delete..
        return mResourceOfPictureForAwayTeam;
    }

    public void setResourceOfPictureForHomeTeam(String resourceOfPicture) {// maybe should delete..
        mResourceOfPictureForHomeTeam = resourceOfPicture;
    }

    public void setResourceOfPictureForAwayTeam(String resourceOfPicture) {// maybe should delete..
        mResourceOfPictureForAwayTeam = resourceOfPicture;
    }
}
