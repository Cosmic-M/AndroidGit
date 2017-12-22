package com.example.bigfi.football_fanatic.pojo_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bigfi on 27.11.2017.
 */

public class Event {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("competitionId")
    @Expose
    private int competitionId;
    private int matchId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("matchday")
    @Expose
    private Integer matchday;
    @SerializedName("homeTeamName")
    @Expose
    private String homeTeamName;
    @SerializedName("homeTeamId")
    @Expose
    private Integer homeTeamId;
    private String homeTeamUrl;
    @SerializedName("awayTeamName")
    @Expose
    private String awayTeamName;
    @SerializedName("awayTeamId")
    @Expose
    private Integer awayTeamId;
    private String awayTeamUrl;
    @SerializedName("result")
    @Expose
    private Result result;

    public Event(){}

    public Event(int id, int competitionId, String date, String status, int matchday, String homeTeamName,
                  int homeTeamId, String homeTeamUrl, String awayTeamName, int awayTeamId, String awayTeamUrl, Result result){
        this.id = id;
        this.competitionId = competitionId;
        this.date = date;
        this.status = status;
        this.matchday = matchday;
        this.homeTeamName = homeTeamName;
        this.homeTeamId = homeTeamId;
        this.homeTeamUrl = homeTeamUrl;
        this.awayTeamName = awayTeamName;
        this.awayTeamId = awayTeamId;
        this.awayTeamUrl = awayTeamUrl;
        this.result = result;
    }

    public Event(int competitionId, int matchId, int homeTeamId,  int awayTeamId, String date, String status, int matchday,
                 String homeTeamName, String awayTeamName, Result result){
        this.competitionId = competitionId;
        this.matchId = matchId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.date = date;
        this.status = status;
        this.matchday = matchday;
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.result = result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(int competitionId) {
        this.competitionId = competitionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMatchday() {
        return matchday;
    }

    public void setMatchday(Integer matchday) {
        this.matchday = matchday;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public Integer getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(Integer homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getHomeTeamUrl() {
        return homeTeamUrl;
    }

    public void setHomeTeamUrl(String homeTeamUrl) {
        this.homeTeamUrl = homeTeamUrl;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public Integer getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(int awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public String getAwayTeamUrl() {
        return awayTeamUrl;
    }

    public void setAwayTeamUrl(String awayTeamUrl) {
        this.awayTeamUrl = awayTeamUrl;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}