package com.example.cosmic_m.footballFakts;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import com.example.cosmic_m.footballFakts.database.SchemaDB;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Cosmic_M on 13.08.2017.
 */

public class TaskInService extends Service {
    private static final String TAG = "TAG";
    private ExecutorService es;
    private int leagueId;
    private String link;
    private String leagueCaption;
    private PendingIntent mPendingIntent;
    private TeamStanding ts;

    public static Intent newInstance(Context context){
        return new Intent(context, TaskInService.class);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        es = Executors.newFixedThreadPool(1);
        Log.i(TAG, "onCreate worked");
    }


    private boolean isNetworkAvailableAndConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId){
        Log.i(TAG, "onStartCommand worked");

        leagueId = intent.getIntExtra(LeagueStandingFragment.LEAGUE_ID, 0);
        link = intent.getStringExtra(LeagueStandingFragment.LINK);
        leagueCaption = intent.getStringExtra(LeagueStandingFragment.LEAGUE_CAPTION);
        mPendingIntent = intent.getParcelableExtra(LeagueStandingFragment.PARAM_PEN_INTENT);

        if (!isNetworkAvailableAndConnected()){
//            Map<String, Event> eventMap;
//            Map<String, TeamStanding> map;
//            Log.i(TAG, "leagueCaption = " + leagueCaption);
//            Log.i(TAG, "leagueId = " + leagueId);
//            map = SingletonLeague.getSingleton(getApplicationContext())
//                    .getTeamsStandingMap(SchemaDB.TeamStandingTable.Cols.LEAGUE_CAPTION + " =? ", new String[]{leagueCaption});
//            ts = map.get(leagueCaption);
////            eventMap = SingletonLeague.getSingleton(getApplicationContext()).getEventMap(null, null);
//            ArrayList<Event> listOfEvent = new ArrayList<>(eventMap.values());
//            for (int i = 0; i < listOfEvent.size(); i++){
//                if (Integer.parseInt(listOfEvent.get(i).getCompetitionId()) != leagueId){
//                    listOfEvent.remove(i--);
//                }
//            }
//            String matchDay = ts.getMatchDay();
//            ts = new APILoader().fetchFiveLastResults(ts, listOfEvent, getApplicationContext(), String.valueOf(leagueCaption), Integer.parseInt(matchDay));
//            SingletonLeague.getSingleton(getApplicationContext()).setTeamStanding(String.valueOf(leagueId), ts);
            mPendingIntent = intent.getParcelableExtra(LeagueStandingFragment.PARAM_PEN_INTENT);
            try {
                mPendingIntent.send(LeagueStandingFragment.STATUS_WITHOUT_NETWORK);
            } catch (PendingIntent.CanceledException e) {
                Log.i(TAG, e.getMessage());
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
            try {
                map = new APILoader().fetchItems(link, getApplicationContext(), String.valueOf(leagueId));
                ts = map.get(String.valueOf(leagueId));
                TeamStanding.Standing[] standings = new TeamStanding.Standing[ts.getStanding().values().size()];
                int i = 0;
                for (TeamStanding.Standing standing : ts.getStanding().values()){
                    standings[i++] = standing;
                }
                Map<String, Drawable> result = SingletonLeague.getSingleton(getApplicationContext())
                        .getEmblemDrawableMap();
                Future<Map<String, Drawable>> f1 = null;
                Future<Map<String, Drawable>> f2 = null;
                Future<Map<String, Drawable>> f3 = null;
                Future<Map<String, Drawable>> f4 = null;
                Future<Map<String, Drawable>> f5 = null;
                Future<Map<String, Drawable>> f6 = null;
                Future<Map<String, Drawable>> f7 = null;
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
                }
                try {
                    long start = System.currentTimeMillis();
                    result.putAll(f1.get());
                    result.putAll(f2.get());
                    result.putAll(f3.get());
                    result.putAll(f4.get());
                    result.putAll(f5.get());
                    result.putAll(f6.get());
                    result.putAll(f7.get());
                    long end = System.currentTimeMillis();
                    Log.i(TAG, "for " + quantityOfTeams + " commands, time is " + (end - start) + " ms");
                } catch (InterruptedException e) {
                    Log.i(TAG, "InterruptedException: " + e.getMessage());
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    Log.i(TAG, "ExecutionException: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            catch (IOException e) {
                Log.i(TAG, "IOException: " + e.getMessage());
                    map = SingletonLeague.getSingleton(getApplicationContext())
                            .getTeamsStandingMap(SchemaDB.TeamStandingTable.Cols.LEAGUE_CAPTION + " =? ", new String[]{leagueCaption});
                    ts = map.get(leagueCaption);
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            String matchDay = ts.getMatchDay();
            link = "http://api.football-data.org/v1/competitions/" +
                    String.valueOf(leagueId) + "/fixtures";

            APILoader apiLoader = new APILoader();
            List<Event> listOfEvent = new ArrayList<>();
            try {
                eventMap = apiLoader.fetchEvents(link);
                //SingletonLeague.getSingleton(getApplicationContext()).setEventMap(eventMap);
                listOfEvent = new ArrayList<>(eventMap.values());
            } catch (IOException e) {
                eventMap = SingletonLeague.getSingleton(getApplicationContext()).getEventMap(null, null);
                listOfEvent = new ArrayList<>(eventMap.values());
                for (int i = 0; i < listOfEvent.size(); i++){
                    if (Integer.parseInt(listOfEvent.get(i).getCompetitionId()) != leagueId){
                        listOfEvent.remove(i--);
                    }
                }
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ts = apiLoader.fetchFiveLastResults(ts, listOfEvent, getApplicationContext(), leagueCaption, Integer.parseInt(matchDay));
            //SingletonLeague.getSingleton(getApplicationContext()).setTeamStanding(leagueCaption, ts);
            long start = System.currentTimeMillis();
            SingletonLeague.getSingleton(getApplicationContext()).updateAndInsertTeamStanding(ts);
            long end = System.currentTimeMillis();
            Log.i(TAG, "updateAndInsertTeamStanding = " + (end - start) + " ms");
            start = System.currentTimeMillis();
            SingletonLeague.getSingleton(getApplicationContext()).updateAndInsertEvents(eventMap);
            end = System.currentTimeMillis();
            Log.i(TAG, "updateAndInsertEvents = " + (end - start) + " ms");
            try {
                pi.send(LeagueStandingFragment.STATUS_FINISH);
            } catch (PendingIntent.CanceledException e) {
                Log.i(TAG, e.getMessage());
                e.printStackTrace();
            }
            stop();
        }

        void stop() {
            Log.i(TAG, "MyRun#" + startId + " end, stopSelfResult("
                    + startId + ") = " + stopSelfResult(startId));
        }
    }

    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "onDestroy worked");
    }

    public IBinder onBind(Intent intent){
        Log.i(TAG, "onBind worked");
        return null;
    }
}
