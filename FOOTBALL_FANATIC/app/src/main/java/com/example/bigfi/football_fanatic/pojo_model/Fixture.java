package com.example.bigfi.football_fanatic.pojo_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bigfi on 12.12.2017.
 */

public class Fixture {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("competitionId")
    @Expose
    public Integer competitionId;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("matchday")
    @Expose
    public Integer matchday;
    @SerializedName("homeTeamName")
    @Expose
    public String homeTeamName;
    @SerializedName("homeTeamId")
    @Expose
    public Integer homeTeamId;
    @SerializedName("awayTeamName")
    @Expose
    public String awayTeamName;
    @SerializedName("awayTeamId")
    @Expose
    public Integer awayTeamId;
    @SerializedName("result")
    @Expose
    public Result result;
}
