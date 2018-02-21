package com.example.bigfi.football_fanatic;

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

    public List<Event> parseJsonToEventsForCoupleTeams(String jsonString) throws JSONException {
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

    public List<Event> parseJsonToEvents(String jsonString) throws JSONException {
        List<Event> events = new ArrayList<>(380);
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject json = jsonObject.getJSONObject("_links");
        String link = json.getString("competition");
        int slash = link.lastIndexOf("/");
        int hook = link.lastIndexOf("\"");
        int competitionId = Integer.parseInt(link.substring(slash + 1, hook)); // COMPETITION_ID
        JSONArray jArray = jsonObject.getJSONArray("fixtures");
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jO = jArray.getJSONObject(i);
            json = jO.getJSONObject("_links");
            JSONObject _json = json.getJSONObject("self");;
            link = _json.getString("href");
            slash = link.lastIndexOf("/");
            int end = link.length();
            int matchId = Integer.parseInt(link.substring(slash + 1, end)); // MATCH_ID
            link = json.getString("homeTeam");
            slash = link.lastIndexOf("/");
            hook = link.lastIndexOf("\"");
            int homeTeamId = Integer.parseInt(link.substring(slash + 1, hook)); // HOME_TEAM_ID
            link = json.getString("awayTeam");
            slash = link.lastIndexOf("/");
            hook = link.lastIndexOf("\"");
            int awayTeamId = Integer.parseInt(link.substring(slash + 1, hook)); // AWAY_TEAM_ID


            String data = jO.getString("date"); // DATA
            String status = jO.getString("status"); // STATUS
            int matchday = jO.getInt("matchday"); // MATCHDAY
            String homeTeamName = jO.getString("homeTeamName"); // HOME_TEAM_NAME
            String awayTeamName = jO.getString("awayTeamName"); // AWAY_TEAM_NAME
            JSONObject jObInner = jO.getJSONObject("result");
            int goalsHomeTeam;
            int goalsAwayTeam;
            try {
                goalsHomeTeam = jObInner.getInt("goalsHomeTeam"); // GOALS_HOME_TEAM
                goalsAwayTeam = jObInner.getInt("goalsAwayTeam"); // GOALS_AWAY_TEAM
            }
            catch (Exception exc){
                goalsHomeTeam = -1;
                goalsAwayTeam = -1;
            }

            Result result = new Result(); // RESULT
            result.setGoalsHomeTeam(goalsHomeTeam);
            result.setGoalsAwayTeam(goalsAwayTeam);

            Event event = new Event(competitionId, matchId, homeTeamId, awayTeamId, data, status, matchday, homeTeamName, awayTeamName, result);
            events.add(event);
        }
        Log.i(TAG, "events.size() => " + events.size() + " before return");
        return events;
    }

    public League parseJsonToLeague(String jsonString, List<Event> events) throws JSONException{
        Log.i(TAG, "jsonString => " + jsonString);
        League league = new League();
        List<Standing> standings = new ArrayList<>();
        JSONObject jObject = new JSONObject(jsonString);
        String leagueCaption = jObject.getString("leagueCaption");
        int matchday = jObject.getInt("matchday");
        league.setLeagueCaption(leagueCaption);
        league.setMatchday(matchday);

        if(isChampionsLeague(leagueCaption)){
            Log.i(TAG, "isChampionsLeague - TRUE");
            String[] group = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
            JSONObject jsonObject = jObject.getJSONObject("standings");
            for (int i = 0; i < group.length; i++) {
                JSONArray jsonArray = jsonObject.getJSONArray(group[i]);
                for (int j = 0; j < jsonArray.length(); j++){
                    Log.i(TAG, "count = " + j);
                    Standing standing = new Standing();

                    JSONObject jO = jsonArray.getJSONObject(j);

                    int teamId = jO.getInt("teamId"); // TEAM_ID
                    Log.i(TAG, "teamId = " + teamId);
                    standing.setGroup(group[i]);
                    standing.setTeamName(jO.getString("team"));
                    standing.setCrestURI(jO.getString("crestURI"));
                    Log.i(TAG, "CREST_URI = " + jO.getString("crestURI"));
                    standing.setTeamId(teamId);
                    standing.setPoints(jO.getInt("points"));
                    standing.setGoals(jO.getInt("goals"));
                    standing.setGoalsAgainst(jO.getInt("goalsAgainst"));
                    standing.setGoalDifference(jO.getInt("goalDifference"));
                    Log.i(TAG, "goalDifference = " + jO.getInt("goalDifference"));

                    int wins = 0;
                    int draws = 0;
                    int losses = 0;

                    for (Event event : events){
                        if (event.getMatchday() > 6) continue;
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
                JSONObject json1 = jO.getJSONObject("_links");
                JSONObject json2 = json1.getJSONObject("team");
                String str = json2.getString("href");
                int index = str.lastIndexOf("/");
                int teamId = Integer.parseInt(str.substring(index + 1, str.length()));;
                standing.setTeamId(teamId);
                standing.setPosition(jO.getInt("position"));
                standing.setTeamName(jO.getString("teamName"));
                standing.setPlayedGames(jO.getInt("playedGames"));
                standing.setCrestURI(jO.getString("crestURI"));
                standing.setPoints(jO.getInt("points"));
                standing.setGoals(jO.getInt("goals"));
                standing.setGoalsAgainst(jO.getInt("goalsAgainst"));
                standing.setGoalDifference(jO.getInt("goalDifference"));
                standing.setWins(jO.getInt("wins"));
                standing.setDraws(jO.getInt("draws"));
                standing.setLosses(jO.getInt("losses"));
                standings.add(standing);
            }
        }
        league.setStanding(standings);
        Log.i(TAG, "SIZE OF STANDINGS = " + league.getStanding().size());
        Log.i(TAG, "LEAGUE CAPTION = " + league.getLeagueCaption());
        Log.i(TAG, "MATCHDAY = " + league.getMatchday());
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
