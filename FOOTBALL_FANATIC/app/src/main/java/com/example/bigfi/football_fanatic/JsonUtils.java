package com.example.bigfi.football_fanatic;

import android.app.Activity;
import android.util.Log;

import com.example.bigfi.football_fanatic.pojo_model.Event;
import com.example.bigfi.football_fanatic.pojo_model.League;
import com.example.bigfi.football_fanatic.pojo_model.Result;
import com.example.bigfi.football_fanatic.pojo_model.Standing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigfi on 17.12.2017.
 */

public class JsonUtils {
    private static final String TAG = "JsonUtils";

    public List<Event> parseJsonToEvents(String jsonString) throws JSONException {
        List<Event> events = new ArrayList<>();

        JSONObject jObject = new JSONObject(jsonString);
        JSONObject jObjectInner = jObject.getJSONObject("head2head");
        JSONArray jArray = jObjectInner.getJSONArray("fixtures");
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jO = jArray.getJSONObject(i);
            Event event = new Event();
            event.setDate(jO.getString("date"));
            event.setHomeTeamName(jO.getString("homeTeamName"));
            event.setAwayTeamName(jO.getString("awayTeamName"));
            JSONObject jObInner = jO.getJSONObject("result");
            Result result = new Result();
            result.setGoalsHomeTeam(jObInner.getInt("goalsHomeTeam"));
            result.setGoalsAwayTeam(jObInner.getInt("goalsAwayTeam"));
            event.setResult(result);
            events.add(event);
        }
        return events;
    }

    public League parseJsonToLeague(String jsonString, List<Event> events) throws JSONException{
        League league = new League();
        List<Standing> standings = new ArrayList<>();
        JSONObject jObject = new JSONObject(jsonString);
        String leagueCaption = jObject.getString("leagueCaption");
        int matchday = jObject.getInt("matchday");
        league.setLeagueCaption(leagueCaption);
        league.setMatchday(matchday);

        if(isChampionsLeague(leagueCaption)){
            String[] group = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
            JSONObject jsonObject = jObject.getJSONObject("standings");
            for (int i = 0; i < group.length; i++) {
                JSONArray jsonArray = jsonObject.getJSONArray(group[i]);
                for (int j = 0; j < jsonArray.length(); j++){
                    Standing standing = new Standing();

                    JSONObject jO = jsonArray.getJSONObject(j);

                    int teamId = jO.getInt("teamId");
                    standing.setGroup(group[i]);
                    standing.setTeamName(jO.getString("team"));
                    standing.setCrestURI(jO.getString("crestURI"));
                    standing.setTeamId(teamId);
                    standing.setPoints(jO.getInt("points"));
                    standing.setGoals(jO.getInt("goals"));
                    standing.setGoalsAgainst(jO.getInt("goalsAgainst"));
                    standing.setGoalDifference(jO.getInt("goalDifference"));

                    int wins = 0;
                    int draws = 0;
                    int losses = 0;

                    for (Event event : events){
                        if (event.getHomeTeamId() == teamId){
                            if(event.getResult().getGoalsHomeTeam() > event.getResult().getGoalsAwayTeam()){
                                wins++;
                            }
                            else if (event.getResult().getGoalsHomeTeam() < event.getResult().getGoalsAwayTeam()){
                                losses++;
                            }
                            else{
                                draws++;
                            }
                        }
                        else if (event.getAwayTeamId() == teamId){
                            if(event.getResult().getGoalsHomeTeam() < event.getResult().getGoalsAwayTeam()){
                                wins++;
                            }
                            else if (event.getResult().getGoalsHomeTeam() > event.getResult().getGoalsAwayTeam()){
                                losses++;
                            }
                            else{
                                draws++;
                            }
                        }
                    }

                    standing.setWins(wins);
                    standing.setDraws(draws);
                    standing.setLosses(losses);

                    standings.add(standing);
                }
            }
        }
        else{
            JSONArray jArr = jObject.getJSONArray("standing");
            for (int j = 0; j < jArr.length(); j++) {
                Standing standing = new Standing();
                JSONObject jO = jArr.getJSONObject(j);
                standing.setPosition(jO.getInt("position"));
                standing.setTeamName(jO.getString("teamName"));
                standing.setPlayedGames(jO.getInt("playedGames"));
                standing.setCrestURI(jO.getString("crestURI"));
                standing.setPoints(jO.getInt("points"));
                standing.setGoals(jO.getInt("goals"));
                standing.setGoalsAgainst(jO.getInt("goalsAgainst"));
                standing.setGoalDifference(jO.getInt("goalDifference"));
                Log.i(TAG, "wins -> " + jO.getInt("wins"));
                standing.setWins(jO.getInt("wins"));
                standing.setDraws(jO.getInt("draws"));
                standing.setLosses(jO.getInt("losses"));
                standings.add(standing);
            }
        }
        league.setStanding(standings);
        return league;
    }


    private boolean isChampionsLeague(String caption) {

        int index = caption.indexOf("20");
        String captionWithoutYears = caption.substring(0, index).trim();

        if (captionWithoutYears.equals("Champions League")) {
            return true;
        } else {
            return false;
        }
    }

}
