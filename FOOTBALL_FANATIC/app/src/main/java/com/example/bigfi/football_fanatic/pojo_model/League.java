package com.example.bigfi.football_fanatic.pojo_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigfi on 26.11.2017.
 */

public class League {

    @SerializedName("leagueCaption")
    @Expose
    private String leagueCaption;
    @SerializedName("matchday")
    @Expose
    private Integer matchday;
    @SerializedName(value = "standing", alternate = "standings")
    @Expose
    private List<Standing> standing = new ArrayList<>();

    public League(){
    }

    public League(String leagueCaption, int matchday){
        this.leagueCaption = leagueCaption;
        this.matchday = matchday;
    }

    public String getLeagueCaption() {
        return leagueCaption;
    }

    public void setLeagueCaption(String leagueCaption) {
        this.leagueCaption = leagueCaption;
    }

    public Integer getMatchday() {
        return matchday;
    }

    public void setMatchday(Integer matchday) {
        this.matchday = matchday;
    }

    public List<Standing> getStanding() {
        return standing;
    }

    public void setStanding(List<Standing> standing) {
        this.standing = standing;
    }

}
