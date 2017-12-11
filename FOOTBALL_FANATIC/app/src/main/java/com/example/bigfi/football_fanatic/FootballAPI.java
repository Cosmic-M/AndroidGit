package com.example.bigfi.football_fanatic;

import com.example.bigfi.football_fanatic.pojo_model.Championship;
import com.example.bigfi.football_fanatic.pojo_model.Event;
import com.example.bigfi.football_fanatic.pojo_model.Fixture;
import com.example.bigfi.football_fanatic.pojo_model.League;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by bigfi on 24.11.2017.
 */

public interface FootballAPI {
    @GET("/v1/competitions")
    Observable<List<Championship>> getData(@Query("season") int season);

    @GET("/v1/competitions/{leagueId}/leagueTable")
    Observable<League> getLeague(@Query("leagueId") int league);

    @GET("/v1/competitions/{leagueId}/fixtures")
    Observable<List<Event>> getEvents(@Query("leagueId") int league);

    @GET("/v1/fixtures/{eventId}")
    Observable<List<Fixture>> getEventsForCoupleTeams(@Query("eventId") int eventId);
}