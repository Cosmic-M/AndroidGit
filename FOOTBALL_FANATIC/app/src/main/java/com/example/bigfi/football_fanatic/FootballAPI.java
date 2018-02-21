package com.example.bigfi.football_fanatic;

import com.example.bigfi.football_fanatic.pojo_model.Championship;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by bigfi on 24.11.2017.
 */

public interface FootballAPI {
    //request using for first fragment
    @GET("/v1/competitions")
    Observable<List<Championship>> getData(@Query("season") int season);

    //request using for second fragment
    @GET("/v1/competitions/{leagueId}/leagueTable")
    Observable<Response<ResponseBody>> getLeague(@Path("leagueId") int league);

    //request using for second fragment
    @GET("/v1/competitions/{leagueId}/leagueTable?matchday=6")
    Observable<Response<ResponseBody>> getFinalGroupStage(@Path("leagueId") int league);

    //request using for second fragment
    @GET("/v1/competitions/{leagueId}/fixtures")
    Observable<Response<ResponseBody>> getEvents(@Path("leagueId") int league);

    //request using for fourth fragment
    @GET("/v1/fixtures/{matchId}")
    Observable<Response<ResponseBody>> getEventsForCoupleTeams(@Path("matchId") int matchId);
}