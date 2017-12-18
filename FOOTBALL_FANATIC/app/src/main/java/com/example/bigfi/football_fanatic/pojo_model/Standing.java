package com.example.bigfi.football_fanatic.pojo_model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bigfi on 26.11.2017.
 */

public class Standing {

    private int position;
    private String teamName;
    private int playedGames;
    private int wins;
    private int draws;
    private int losses;
    private String crestURI;
    private int points;
    private int goals;
    private int goalsAgainst;
    private int goalDifference;

    private int preResult;
    private int pre2Result;
    private int pre3Result;
    private int pre4Result;
    private int pre5Result;

    private String group; // only ChampionsLeague
    private int teamId; // only ChampionsLeague

    public int getPosition(){
        return position;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String team) {
        this.teamName = team;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getPlayedGames() {
        return playedGames;
    }

    public void setPlayedGames(int playedGames) {
        this.playedGames = playedGames;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int winGames) {
        this.wins = winGames;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int lossGames) {
        this.losses = lossGames;
    }

    public String getCrestURI() {
        return crestURI;
    }

    public void setCrestURI(String crestURI) {
        this.crestURI = crestURI;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(Integer goalDifference) {
        this.goalDifference = goalDifference;
    }

    public int getPreResult() {
        return preResult;
    }

    public void setPreResult(int preResult) {
        this.preResult = preResult;
    }

    public int getPre2Result() {
        return pre2Result;
    }

    public void setPre2Result(int pre2Result) {
        this.pre2Result = pre2Result;
    }

    public int getPre3Result() {
        return pre3Result;
    }

    public void setPre3Result(int pre3Result) {
        this.pre3Result = pre3Result;
    }

    public int getPre4Result() {
        return pre4Result;
    }

    public void setPre4Result(int pre4Result) {
        this.pre4Result = pre4Result;
    }

    public int getPre5Result() {
        return pre5Result;
    }

    public void setPre5Result(int pre5Result) {
        this.pre5Result = pre5Result;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

}
