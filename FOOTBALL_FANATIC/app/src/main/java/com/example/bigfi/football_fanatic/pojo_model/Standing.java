package com.example.bigfi.football_fanatic.pojo_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bigfi on 26.11.2017.
 */

public class Standing {

    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName(value = "teamName", alternate = "team")
    @Expose
    private String teamName;
    @SerializedName("playedGames")
    @Expose
    private Integer playedGames;
    @SerializedName("wins")
    @Expose
    private int wins; // only countryChamp
    @SerializedName("draws")
    @Expose
    private int draws; // only countryChamp
    @SerializedName("losses")
    @Expose
    private int losses; // only countryChamp
    @SerializedName("crestURI")
    @Expose
    private String crestURI;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("goals")
    @Expose
    private Integer goals;
    @SerializedName("goalsAgainst")
    @Expose
    private Integer goalsAgainst;
    @SerializedName("goalDifference")
    @Expose
    private Integer goalDifference;

    private int preResult;
    private int pre2Result;
    private int pre3Result;
    private int pre4Result;
    private int pre5Result;

    @SerializedName("group")
    @Expose
    private String group; // only ChampionsLeague
    @SerializedName("teamId")
    @Expose
    private Integer teamId; // only ChampionsLeague


    public Integer getPosition() {
        return position;
    }

    public void setPosition(int position){
        this.points = position;
    }

    public void setRank(Integer rank) {
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

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getPlayedGames() {
        return playedGames;
    }

    public void setPlayedGames(Integer playedGames) {
        this.playedGames = playedGames;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int winGames) {
        this.wins = wins;
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
        this.losses = losses;
    }

    public String getCrestURI() {
        return crestURI;
    }

    public void setCrestURI(String crestURI) {
        this.crestURI = crestURI;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public Integer getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(Integer goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public Integer getGoalDifference() {
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
