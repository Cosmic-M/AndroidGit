package com.development.cosmic_m.footballfanatic;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.development.cosmic_m.footballfanatic.database.SchemaDB;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Cosmic_M on 13.08.2017.
 */

public class TaskService extends Service {
    private ExecutorService es;
    private int leagueId;
    private String link;
    private String leagueCaption;
    private PendingIntent mPendingIntent;
    private TeamStanding ts;

    public static Intent newInstance(Context context){
        return new Intent(context, TaskService.class);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        es = Executors.newFixedThreadPool(1);
    }

    private boolean isNetworkAvailableAndConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId){
        leagueId = intent.getIntExtra(LeagueStandingFragment.LEAGUE_ID, 0);
        link = intent.getStringExtra(LeagueStandingFragment.LINK);
        leagueCaption = intent.getStringExtra(LeagueStandingFragment.LEAGUE_CAPTION);
        mPendingIntent = intent.getParcelableExtra(LeagueStandingFragment.PARAM_PEN_INTENT);

        if (!isNetworkAvailableAndConnected()){
            mPendingIntent = intent.getParcelableExtra(LeagueStandingFragment.PARAM_PEN_INTENT);
            try {
                mPendingIntent.send(LeagueStandingFragment.STATUS_WITHOUT_NETWORK);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
            stopSelf();
            return super.onStartCommand(intent, flag, startId);
        }
        Task task = new Task(link, leagueCaption, leagueId, startId, mPendingIntent);
        es.execute(task);
        return super.onStartCommand(intent, flag, startId);
    }

    private class Task implements Runnable {
        private String link;
        private String leagueCaption;
        private int leagueId;
        private int startId;
        private PendingIntent pi;

        Task(String link, String leagueCaption, int leagueId, int id, PendingIntent pi){
            this.link = link;
            this.leagueCaption = leagueCaption;
            this.leagueId = leagueId;
            startId = id;
            this.pi = pi;
        }

        @Override
        public void run(){
            Map<String, Event> eventMap = null;
            Map<String, TeamStanding> map;
            TeamStanding.Standing[] standings = null;
            int index = 0;
            int downloadedEmblem = 0;
            boolean flag = true;
            try {
                map = new APILoader().fetchItems(link, getApplicationContext(), String.valueOf(leagueId));
                ts = map.get(String.valueOf(leagueId));
                Map<String, Drawable> mapDrawable = SingletonLeague
                        .getSingleton(getApplicationContext()).getEmblemDrawableMap();
                standings = new TeamStanding
                        .Standing[ts.getStanding().values().size()];
                for (TeamStanding.Standing standing : ts.getStanding().values()){// if last API request was successfully - standings wil be inflate the data
                    if(mapDrawable.containsKey(standing.getTeamId())) {
                        downloadedEmblem++;
                    }
                    standings[index++] = standing;
                }
            }
            catch(Exception exc){
                flag = false;
                exc.printStackTrace();
            }
            if (index == 0){
                Set<TeamStanding.Standing> set;
                try {
                    ts = SingletonLeague
                            .getSingleton(getApplicationContext())
                            .getTeamsStandingMap(SchemaDB.TeamStandingTable.Cols
                                    .LEAGUE_CAPTION + " =? ", new String[]{leagueCaption})
                            .get(leagueCaption);
                    set = new HashSet<>(ts.getStanding().values());
                }
                catch (NullPointerException e){// if in database hasn't needed data
                    try {
                        pi.send(LeagueStandingFragment.STATUS_FINISH_WITHOUT_DATA);
                    } catch (PendingIntent.CanceledException exception) {
                        exception.printStackTrace();
                    }
                    stop();
                    return;
                }
                Map<String, Drawable> mapDrawable = SingletonLeague
                        .getSingleton(getApplicationContext()).getEmblemDrawableMap();
                standings = new TeamStanding
                        .Standing[set.size()];
                for (TeamStanding.Standing standing : set){
                    if(mapDrawable.containsKey(standing.getTeamId())) {
                        downloadedEmblem++;
                    }
                    standings[index++] = standing;
                }
            }
            if (downloadedEmblem != index){//if clause is false - it's means we already downloaded all needed emblems
                Map<String, Drawable> result = SingletonLeague.getSingleton(getApplicationContext())
                        .getEmblemDrawableMap();
                Future<Map<String, Drawable>> f1;
                Future<Map<String, Drawable>> f2;
                Future<Map<String, Drawable>> f3;
                Future<Map<String, Drawable>> f4;
                Future<Map<String, Drawable>> f5;
                Future<Map<String, Drawable>> f6;
                Future<Map<String, Drawable>> f7;
                ExecutorService es = Executors.newFixedThreadPool(7);
                int quantityOfTeams = standings.length;
                switch (quantityOfTeams){
                    case 18:
                        f1 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[0], standings[1], standings[2]}));
                        f2 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[3], standings[4], standings[5]}));
                        f3 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[6], standings[7], standings[8]}));
                        f4 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[9], standings[10], standings[11]}));
                        f5 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[12], standings[13]}));
                        f6 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[14], standings[15]}));
                        f7 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[16], standings[17]}));
                        break;
                    case 20:
                        f1 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[0], standings[1], standings[2]}));
                        f2 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[3], standings[4], standings[5]}));
                        f3 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[6], standings[7], standings[8]}));
                        f4 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[9], standings[10], standings[11]}));
                        f5 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[12], standings[13], standings[14]}));
                        f6 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[15], standings[16], standings[17]}));
                        f7 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[18], standings[19]}));
                        break;
                    case 32:
                        f1 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[0], standings[1], standings[2], standings[3], standings[4]}));
                        f2 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[5], standings[6], standings[7], standings[8], standings[9]}));
                        f3 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[10], standings[11], standings[12], standings[13], standings[14]}));
                        f4 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[15], standings[16], standings[17], standings[18], standings[19]}));
                        f5 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[20], standings[21], standings[22], standings[23]}));
                        f6 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[24], standings[25], standings[26], standings[27]}));
                        f7 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[28], standings[29], standings[30], standings[31]}));
                        break;
                    default:
                        f1 = es.submit(new EmblemClubFetcher(standings));
                        f2 = es.submit(new EmblemClubFetcher(null));
                        f3 = es.submit(new EmblemClubFetcher(null));
                        f4 = es.submit(new EmblemClubFetcher(null));
                        f5 = es.submit(new EmblemClubFetcher(null));
                        f6 = es.submit(new EmblemClubFetcher(null));
                        f7 = es.submit(new EmblemClubFetcher(null));
                        break;
                }
                try {
                    result.putAll(f1.get());
                    result.putAll(f2.get());
                    result.putAll(f3.get());
                    result.putAll(f4.get());
                    result.putAll(f5.get());
                    result.putAll(f6.get());
                    result.putAll(f7.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            String matchDay;
            if (ts != null){
                matchDay = ts.getMatchDay();
            }
            else{
                try {
                    pi.send(LeagueStandingFragment.STATUS_FINISH_WITHOUT_DATA);
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
                stop();
                return;
            }
            link = "http://api.football-data.org/v1/competitions/" +
                    String.valueOf(leagueId) + "/fixtures";

            APILoader apiLoader = new APILoader();
            List<Event> listOfEvent = new ArrayList<>();
            try {
                eventMap = apiLoader.fetchEvents(link);
                listOfEvent = new ArrayList<>(eventMap.values());
            } catch (IOException e) {
                eventMap = SingletonLeague.getSingleton(getApplicationContext()).getEventMap(null, null);
                listOfEvent = new ArrayList<>(eventMap.values());
                for (int j = 0; j < listOfEvent.size(); j++){
                    if (Integer.parseInt(listOfEvent.get(j).getCompetitionId()) != leagueId){
                        listOfEvent.remove(j--);
                    }
                }
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ts = apiLoader.fetchFiveLastResults(ts, listOfEvent, Integer.parseInt(matchDay));
            if (flag) {
                SingletonLeague.getSingleton(getApplicationContext()).updateAndInsertTeamStanding(ts);
                SingletonLeague.getSingleton(getApplicationContext()).updateAndInsertEvents(eventMap);
            }

            try {
                pi.send(LeagueStandingFragment.STATUS_FINISH);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
            stop();
        }

        void stop() {
            stopSelfResult(startId);
        }
    }

    public IBinder onBind(Intent intent){
        return null;
    }
}

