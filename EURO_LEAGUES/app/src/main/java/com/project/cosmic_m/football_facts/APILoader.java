package com.project.cosmic_m.football_facts;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cosmic_M on 04.07.2017.
 */

public class APILoader{
    private static final String TAG = "TAG";
    private static final String API_KEY = "d72e5663af1945a098a92010682288d8";

    /*
    * this method prepare url for request to API
     */
    private String fetchJsonString(String link) throws IOException{
        String url = Uri.parse(link)
                .buildUpon()
                .appendQueryParameter("X-Auth-Token", API_KEY)
                .build().toString();
        return new BaseAPI().getURLString(url);
    }

    public Map<String, TeamStanding> fetchItems(String link, Context context, String league) throws IOException, JSONException {
        String respond = fetchJsonString(link);
        Log.i(TAG, "response => " + respond);
        Map<String, String> mStoreImage = SingletonLeague.getSingleton(context)
                .getStoreOfImageResource();
        JSONObject jObj = new JSONObject(respond);
        String leagueCaption = jObj.getString("leagueCaption");
        int index = leagueCaption.lastIndexOf("20");
        String leagueCaptionWithoutYear = leagueCaption.substring(0, index).trim();
        String currentMatchDay = jObj.getString("matchday");
        if (leagueCaptionWithoutYear.equals("Champions League") && Integer.parseInt(currentMatchDay) > 6){
            String repeatRequest = link + "?matchday=6";
            respond = fetchJsonString(repeatRequest);
            jObj = new JSONObject(respond);
            currentMatchDay = "6";
        }
        TeamStanding teamStanding = new TeamStanding();
        teamStanding.setLeagueCaption(leagueCaption);
        TeamStanding.Standing standing;
        Map<String, TeamStanding> map = new HashMap<>();
        if (leagueCaptionWithoutYear.equals("Champions League")) {
            link = "http://api.football-data.org/v1/competitions/" +
                    String.valueOf(league) + "/fixtures";
            Map<String, Event> m = fetchEvents(link);
            List<Event> list = new ArrayList<>(m.values());
            JSONObject jsonObject = jObj.getJSONObject("standings");
            int matchDay = Integer.parseInt(currentMatchDay);
            String[] group = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
            for (int i = 0; i < group.length; i++) {
                JSONArray jsonArray = jsonObject.getJSONArray(group[i]);
                for (int j = 0; j < jsonArray.length(); j++){
                    standing = teamStanding.createNewStandingClass();
                    JSONObject jO = jsonArray.getJSONObject(j);
                    standing.setGroup(group[i]);
                    standing.setRank("1");//temporary
                    standing.setTeam(jO.getString("team"));
                    standing.setPlayedGames(jObj.getString("matchday"));
                    mStoreImage.put("http://api.football-data.org/v1/teams/" +
                            jO.getString("teamId"), jO.getString("crestURI"));
                    standing.setResourceOfPicture(jO.getString("crestURI"));
                    standing.setTeamId("http://api.football-data.org/v1/teams/" +
                            jO.getString("teamId"));
                    standing.setPts(jO.getString("points"));
                    standing.setGoals(jO.getString("goals"));
                    standing.setGoalsAgainst(jO.getString("goalsAgainst"));
                    standing.setGoalsDifference(jO.getString("goalDifference"));

                    int countWins = 0;
                    int countDraws = 0;
                    int countLosses = 0;

                    String teamId = "http://api.football-data.org/v1/teams/" + jO.getString("teamId");
                    List<Event> listOfEvent = filterListOfAllMatchesById(list, teamId);
                    listOfEvent = this.ListEventsBeforeMatchDay(listOfEvent, String.valueOf(matchDay));
                    for (int n = 0; n < listOfEvent.size(); n++) {
                        Event event = listOfEvent.get(n);
                        String homeTeamId = event.getHomeTeamId();
                        if (event.getStatus().equals("FINISHED")) {
                            int goalsHomeTeam = Integer.parseInt(event.getGoalsHomeTeam());
                            int goalsAwayTeam = Integer.parseInt(event.getGoalsAwayTeam());
                            if (goalsHomeTeam > goalsAwayTeam) {
                                if (homeTeamId.equals(teamId)) {
                                    countWins++;
                                } else {
                                    countLosses++;
                                }
                            } else if (goalsHomeTeam < goalsAwayTeam) {
                                if (homeTeamId.equals(teamId)) {
                                    countLosses++;
                                } else {
                                    countWins++;
                                }
                            } else {
                                countDraws++;
                            }
                        }
                    }
                    standing.setWinGames(String.valueOf(countWins));
                    standing.setDrawGames(String.valueOf(countDraws));
                    standing.setLossGames(String.valueOf(countLosses));
                    teamStanding.addStanding(jO.getString("team"), standing);
                }
            }
            teamStanding.setMatchDay(String.valueOf(matchDay));
            map.put(league, teamStanding);
        }
        else {
            JSONArray jArr = jObj.getJSONArray("standing");
            for (int j = 0; j < jArr.length(); j++) {
                standing = teamStanding.createNewStandingClass();
                JSONObject jOb = jArr.getJSONObject(j);
                standing.setRank(String.valueOf(j + 1));
                standing.setTeam(jOb.getString("teamName"));
                standing.setPlayedGames(jOb.getString("playedGames"));
                JSONObject jsonOuter = jOb.getJSONObject("_links");
                JSONObject jsonInner = jsonOuter.getJSONObject("team");
                standing.setTeamId(jsonInner.getString("href"));
                standing.setWinGames(jOb.getString("wins"));
                standing.setDrawGames(jOb.getString("draws"));
                standing.setLossGames(jOb.getString("losses"));
                standing.setPts(jOb.getString("points"));
                standing.setGoals(jOb.getString("goals"));
                standing.setGoalsAgainst(jOb.getString("goalsAgainst"));
                standing.setGoalsDifference(jOb.getString("goalDifference"));
                standing.setResourceOfPicture(jOb.getString("crestURI"));
                mStoreImage.put(jsonInner.getString("href"), jOb.getString("crestURI"));
                teamStanding.addStanding(jOb.getString("teamName"), standing);
            }
            teamStanding.setMatchDay(String.valueOf(jObj.getString("matchday")));
            map.put(league, teamStanding);
        }
        return map;
    }

    private List<Event> ListEventsBeforeMatchDay(List<Event> list, String matchDay){
        List<Event> listEventsBeforeMatchDay = new ArrayList<>();
        for (Event event : list){
            if (Integer.parseInt(event.getMatchDay()) <= Integer.parseInt(matchDay)){
                listEventsBeforeMatchDay.add(event);
            }
        }
        return listEventsBeforeMatchDay;
    }

    /*
    *this method return list of leagues
     */
    public Map<String, League> getAllSupportLeagues(String link) throws IOException{
        Map<String, League> allLeagues = new HashMap<>();
        String response = fetchJsonString(link);
        Log.i(TAG, "response league = " + response);
        try{
            JSONArray jArray = new JSONArray(response);
            for (int i = 0; i < jArray.length(); i++){
                JSONObject jO = jArray.getJSONObject(i);
                String liga = jO.getString("league");
                switch (liga){
                    case "BL1":
                        break;
                    case "PL":
                        break;
                    case "DED":
                        break;
                    case "FL1":
                        break;
                    case "SA":
                        break;
                    case "PPL":
                        break;
                    case "CL":
                        break;
                    case "PD":
                        break;
                    default:
                        continue;
                }
                String caption = jO.getString("caption");
                int id = Integer.parseInt(jO.getString("id"));
                int numberOfTeams = Integer.parseInt(jO.getString("numberOfTeams"));
                if (id == 464) continue;
                League league = new League(caption, liga, id, numberOfTeams);
                allLeagues.put(caption, league);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allLeagues;
    }


    /*
    * this method to parse JSON String, fetch and add Event to Map
     */
    public Map<String, Event> fetchEvents(String link) throws IOException, JSONException {
        Map<String, Event> mapOfEvents = new HashMap<>();
        String response = fetchJsonString(link);
        Log.i(TAG, "response = " + response);
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jA = jsonObject.getJSONArray("fixtures");
        for (int i = 0; i < jA.length(); i++) {
            JSONObject jObject = jA.getJSONObject(i);
            Event event = new Event();
            JSONObject json = jObject.getJSONObject("_links");
            JSONObject js = json.getJSONObject("self");
            event.setMatchId(js.getString("href"));
            String competitionId = json.getJSONObject("competition").getString("href");
            int index = competitionId.lastIndexOf("/");
            event.setCompetitionId(competitionId.substring(index + 1));
            JSONObject jss = json.getJSONObject("homeTeam");
            JSONObject jsss = json.getJSONObject("awayTeam");
            event.setMatchDate(jObject.getString("date"));
            event.setMatchDay(jObject.getString("matchday"));
            event.setHomeTeamName(jObject.getString("homeTeamName"));
            event.setHomeTeamId(jss.getString("href"));
            event.setAwayTeamName(jObject.getString("awayTeamName"));
            event.setAwayTeamId(jsss.getString("href"));
            event.setStatus(jObject.getString("status"));
            JSONObject jOb = jObject.getJSONObject("result");
            event.setGoalsHomeTeam(jOb.getString("goalsHomeTeam"));
            event.setGoalsAwayTeam(jOb.getString("goalsAwayTeam"));
            mapOfEvents.put(js.getString("href"), event);
        }
        return mapOfEvents;
    }

    /*
    * this method fetch last five results for all teams in specific league
     */
    public TeamStanding fetchFiveLastResults(TeamStanding ts, List<Event> list, int matchDay){
        TeamStanding teamStanding = ts;
        for (int i = 0; i < list.size(); i++) {
            Event event = list.get(i);
            if ((Integer.parseInt(event.getMatchDay()) > matchDay - 5)
                    && (Integer.parseInt(event.getMatchDay()) <= matchDay) && event.getStatus().equals("FINISHED")){
                if (Integer.parseInt(event.getGoalsHomeTeam()) > Integer.parseInt(event.getGoalsAwayTeam())){
                    TeamStanding.Standing standing = teamStanding.getStandingByTeam(event.getHomeTeamName());
                    int[] massHomeTeam = standing.getMassOfLastResults();
                    massHomeTeam[4 - (matchDay - Integer.parseInt(event.getMatchDay()))] = 3;
                    standing = teamStanding.getStandingByTeam(event.getAwayTeamName());
                    int[] massAwayTeam = standing.getMassOfLastResults();
                    massAwayTeam[4 - (matchDay - Integer.parseInt(event.getMatchDay()))] = -1;
                }
                else if (Integer.parseInt(event.getGoalsHomeTeam()) < Integer.parseInt(event.getGoalsAwayTeam())){
                    TeamStanding.Standing standing = teamStanding.getStandingByTeam(event.getHomeTeamName());
                    int[] massHomeTeam = standing.getMassOfLastResults();
                    massHomeTeam[4 - (matchDay - Integer.parseInt(event.getMatchDay()))] = -1;
                    standing = teamStanding.getStandingByTeam(event.getAwayTeamName());
                    int[] massAwayTeam = standing.getMassOfLastResults();
                    massAwayTeam[4 - (matchDay - Integer.parseInt(event.getMatchDay()))] = 3;
                }
                else{
                    TeamStanding.Standing standing = teamStanding.getStandingByTeam(event.getHomeTeamName());
                    int[] massHomeTeam = standing.getMassOfLastResults();
                    massHomeTeam[4 - (matchDay - Integer.parseInt(event.getMatchDay()))] = 1;
                    standing = teamStanding.getStandingByTeam(event.getAwayTeamName());
                    int[] massAwayTeam = standing.getMassOfLastResults();
                    massAwayTeam[4 - (matchDay - Integer.parseInt(event.getMatchDay()))] = 1;
                }
            }
        }
        return teamStanding;
    }

    public List<Event> filterListOfAllMatchesById(List<Event> list, String teamId){
        List<Event> filteredList = new ArrayList<>();
        for (Event event : list){
            if (event.getAwayTeamId().equals(teamId) || event.getHomeTeamId().equals(teamId)){
                filteredList.add(event);
            }
        }
        return filteredList;
    }

    public List<Event> fetchItemsHistory(String link) throws IOException, JSONException{
        List<Event> list = new ArrayList<>();
        String respond = fetchJsonString(link);
        JSONObject jsonObject = new JSONObject(respond);
        JSONObject jA = jsonObject.getJSONObject("head2head");
        JSONArray jsonArray = jA.getJSONArray("fixtures");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jObject = jsonArray.getJSONObject(i);
            Event event = new Event();
            JSONObject json = jObject.getJSONObject("_links");
            JSONObject js = json.getJSONObject("self");
            event.setMatchId(js.getString("href"));
            JSONObject jss = json.getJSONObject("homeTeam");
            JSONObject jsss = json.getJSONObject("awayTeam");
            event.setMatchDate(jObject.getString("date"));
            event.setMatchDay(jObject.getString("matchday"));
            event.setHomeTeamName(jObject.getString("homeTeamName"));
            event.setHomeTeamId(jss.getString("href"));
            event.setAwayTeamName(jObject.getString("awayTeamName"));
            event.setAwayTeamId(jsss.getString("href"));
            event.setStatus(jObject.getString("status"));
            JSONObject jOb = jObject.getJSONObject("result");
            event.setGoalsHomeTeam(jOb.getString("goalsHomeTeam"));
            event.setGoalsAwayTeam(jOb.getString("goalsAwayTeam"));
            list.add(event);
        }
        return list;
    }
}
