package com.example.bigfi.football_fanatic.pojo_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bigfi on 27.11.2017.
 */

public class Result {
    @SerializedName("goalsHomeTeam")
    @Expose
    private Integer goalsHomeTeam;
    @SerializedName("goalsAwayTeam")
    @Expose
    private Integer goalsAwayTeam;

    public Result(){}

    public Result(int goalsHomeTeam, int goalsAwayTeam){
        this.goalsHomeTeam = goalsHomeTeam;
        this.goalsAwayTeam = goalsAwayTeam;
    }

    public Integer getGoalsHomeTeam() {
        return goalsHomeTeam;
    }

    public void setGoalsHomeTeam(Integer goalsHomeTeam) {
        this.goalsHomeTeam = goalsHomeTeam;
    }

    public Integer getGoalsAwayTeam() {
        return goalsAwayTeam;
    }

    public void setGoalsAwayTeam(Integer goalsAwayTeam) {
        this.goalsAwayTeam = goalsAwayTeam;
    }
}
