package com.example.bigfi.football_fanatic;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.example.bigfi.football_fanatic.pojo_model.Event;
import com.example.bigfi.football_fanatic.pojo_model.League;
import com.example.bigfi.football_fanatic.pojo_model.Standing;
import com.example.bigfi.football_fanatic.supportsGlide.SvgDecoder;
import com.example.bigfi.football_fanatic.supportsGlide.SvgDrawableTranscoder;
import com.example.bigfi.football_fanatic.supportsGlide.SvgSoftwareLayerSetter;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Created by bigfi on 26.11.2017.
 */

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";
    private static final String CHAMPIONSHIP_ID = "championshipId";
    private static final String CAPTION = "caption";
    private static String[] group = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
    private ConnectableObservable<List<Standing>> observableStandings;
    private ConnectableObservable<List<Standing>> observableFinalGroupStage;
    private Observable<Response<ResponseBody>> observableEvents;
    private int mLeagueId;
    private String mCaption;
    private RecyclerView mRecyclerView;
    private SecondAdapter mAdapter;
    private boolean isChampionsLeague;
    private List<Event> mEvents;
    private League mLeague;
    private JsonUtils mJasonUtils;
    GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> mRequestBuilder;

    public static Intent newInstance(Activity activity, int championshipId, String caption){
        Intent intent = new Intent(activity, SecondActivity.class);
        intent.putExtra(CHAMPIONSHIP_ID, championshipId);
        intent.putExtra(CAPTION, caption);
        return intent;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_table);

        mLeagueId = getIntent().getIntExtra(CHAMPIONSHIP_ID, 0);
        mCaption = getIntent().getStringExtra(CAPTION);

        int index = mCaption.indexOf("20");
        isChampionsLeague = mCaption.substring(0, index).trim().equals("Champions League");

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_league_table_recycler_view_id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SecondActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new SecondAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mJasonUtils = new JsonUtils();
        mEvents = new ArrayList<>();

        mRequestBuilder = Glide.with(getApplicationContext())
                .using(Glide.buildStreamModelLoader(Uri.class, getApplicationContext()), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .placeholder(R.drawable.placeholder_icon)
                .error(R.drawable.failure)
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());

        final Func1<Response<ResponseBody>, List<Standing>> assignLastResult  = new Func1<Response<ResponseBody>, List<Standing>>() {

            @Override
            public List<Standing> call(Response<ResponseBody> response) {

                League league = null;
                String answer = "";
                try {
                    answer = response.body().string();
                    Log.i(TAG, "ANSWER AS STANDINGS => " + answer);
                    league = mJasonUtils.parseJsonToLeague(answer, mEvents);

                }
                catch (IOException e) {
                    Log.i(TAG, "IO_EXCEPTION: " + e.getMessage());
                }
                catch (JSONException exc){
                    Log.i(TAG, "JSON_EXCEPTION: " + exc.getMessage());
                }

                mLeague = league;

                List<Standing> standings = league.getStanding();
                if (isChampionsLeague) {
                    standings = setPosition(standings);
                }
                return fetchFiveLastResults(standings, mEvents, league.getMatchday());
            }
        };

        if (isChampionsLeague){
            observableFinalGroupStage = App.getAPI().getFinalGroupStage(mLeagueId)
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(Schedulers.computation())
                    .map(assignLastResult)
                    .observeOn(AndroidSchedulers.mainThread())
                    .replay();
            observableFinalGroupStage.connect();
        }

        else {
            observableStandings = App.getAPI().getLeague(mLeagueId)
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(Schedulers.computation())
                    .map(assignLastResult)
                    .observeOn(AndroidSchedulers.mainThread())
                    .replay();
            observableStandings.connect();
        }

        observableEvents = App.getAPI().getEvents(mLeagueId);

        final Observer<List<Standing>> observer = new Observer<List<Standing>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
                //Singleton.getSingleton(SecondActivity.this).updateAndInsertTeamStanding(mLeague);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "ERROR IN OBSERVABLE_STANDINGS:" + e.getMessage());
            }

            @Override
            public void onNext(List<Standing> standings) {
                Log.i(TAG, "standings obtained!");
                mLeague.getStanding().clear();
                mLeague.setStanding(standings);
                Log.i(TAG, "STANDINGS.size() = " + standings.size());
                /*
                collection of groups have need to add to standings's list
                 */
                if (isChampionsLeague) {
                    List<Standing> groups = new ArrayList<>();
                    for (int i = 0; i < group.length; i++) {
                        Standing standing = new Standing();
                        standing.setGroup(group[i]);
                        standing.setPoints(19);
                        groups.add(standing);
                    }
                    standings.addAll(groups);
                    Collections.sort(standings, new GroupStageComparator(SecondActivity.this));

                    for (int i = 0; i < standings.size(); i++) {
                        if ((i % 5) != 0) {
                            standings.get(i).setPosition((i % 5));
                        }
                    }
                }
                else{
                    Collections.sort(standings, new ChampionshipComparator());
                }

                Log.i(TAG, "result.size() = " + standings.size());
                mAdapter.setData(SecondActivity.this, standings, isChampionsLeague, mLeagueId, mRequestBuilder);
                Singleton.getSingleton(SecondActivity.this).updateAndInsertTeamStanding(mLeague);
            }
        };

        observableEvents
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");
                        if (isChampionsLeague){
                            observableFinalGroupStage.subscribe(observer);
                        }
                        else{
                            observableStandings.subscribe(observer);
                        }
                    }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        Log.i(TAG, "onNext started...");
                        String answer = "";
                        try {
                            answer = response.body().string();
                            Log.i(TAG, "ANSWER = " + answer);
                            mEvents = mJasonUtils.parseJsonToEvents(answer);

                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        catch (JSONException exc){
                            exc.printStackTrace();
                        }
                        Log.i(TAG, "eventList.size() = " + mEvents.size());
                        Singleton.getSingleton(SecondActivity.this).updateAndInsertEvents(mEvents);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "ERROR IN OBSERVABLE_EVENTS:" + e.getMessage());
                    }
                });
    }

    private List<Standing> setPosition(List<Standing> standings){
        Collections.sort(standings, new GroupStageComparator(SecondActivity.this));
        int rank = 1;
        for (Standing standing : standings){
            standing.setPosition((rank % 4 == 0) ? rank = 1 : rank++);
        }
        return standings;
    }

    private List<Standing> fetchFiveLastResults(List<Standing> standings, List<Event> events, int currentMatchDay) {
        Event event;
        int dayOfMatch;
        Standing homeStanding;
        Standing awayStanding;
        int[] res;
        List<Standing> standingList = new ArrayList<>();

        Map<String, Standing> mapStandings = new HashMap<>();
        for (Standing standing : standings) {
            mapStandings.put(standing.getTeamName(), standing);
        }
        for (int i = 0; i < events.size(); i++) {
            event = events.get(i);
            dayOfMatch = event.getMatchday();
            if (event.getStatus().equals("FINISHED") && (dayOfMatch >= currentMatchDay - 5)
                    && (dayOfMatch < currentMatchDay)) {
                homeStanding = mapStandings.get(event.getHomeTeamName());
                awayStanding = mapStandings.get(event.getAwayTeamName());
                switch (currentMatchDay - dayOfMatch) {
                    case 1: {
                        res = assignPreviousResultForHomeTeam(event);
                        homeStanding.setPreResult(res[0]);
                        awayStanding.setPreResult(res[1]);
                        break;
                    }
                    case 2: {
                        res = assignPreviousResultForHomeTeam(event);
                        homeStanding.setPre2Result(res[0]);
                        awayStanding.setPre2Result(res[1]);
                        break;
                    }
                    case 3: {
                        res = assignPreviousResultForHomeTeam(event);
                        homeStanding.setPre3Result(res[0]);
                        awayStanding.setPre3Result(res[1]);
                        break;
                    }
                    case 4: {
                        res = assignPreviousResultForHomeTeam(event);
                        homeStanding.setPre4Result(res[0]);
                        awayStanding.setPre4Result(res[1]);
                        break;
                    }
                    case 5: {
                        res = assignPreviousResultForHomeTeam(event);
                        homeStanding.setPre5Result(res[0]);
                        awayStanding.setPre5Result(res[1]);
                        break;
                    }
                }
            }
        }
        standingList.addAll(mapStandings.values());
        return standingList;
    }

    private int[] assignPreviousResultForHomeTeam(Event event) {
        int[] results = new int[2];
        if (event.getResult().getGoalsHomeTeam() > event.getResult().getGoalsAwayTeam()) {
            results[0] = 3;
            results[1] = -1;
            return results;
        } else if (event.getResult().getGoalsHomeTeam() < event.getResult().getGoalsAwayTeam()) {
            results[0] = -1;
            results[1] = 3;
            return results;
        } else {
            results[0] = 1;
            results[1] = 1;
            return results;
        }
    }
}

