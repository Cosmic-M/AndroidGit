package com.example.bigfi.football_fanatic;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bigfi.football_fanatic.pojo_model.Event;
import com.example.bigfi.football_fanatic.pojo_model.League;
import com.example.bigfi.football_fanatic.pojo_model.Standing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Created by bigfi on 26.11.2017.
 */

public class LeagueStandingFragment extends Fragment {

    private static final String TAG = "LeagueStandingFragment";
    private ConnectableObservable<List<Standing>> observableStandings;
    private Observable<List<Event>> observableEvents;
    private int mLeagueId;
    private RecyclerView mRecyclerView;
    private LeagueTableAdapter mAdapter;
    private boolean isChampionsLeague;
    private List<Event> mEvents;
    private League mLeague;

    public static LeagueStandingFragment newInstance() {
        return new LeagueStandingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate called");
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mLeagueId = bundle.getInt("id");
        }

        Log.i(TAG, "mLeagueId = " + mLeagueId);

        final Func1<League, List<Standing>> assignLastResult  = new Func1<League, List<Standing>>() {
            @Override
            public List<Standing> call(League league) {
                mLeague = league;
                int index = league.getLeagueCaption().indexOf("20");
                isChampionsLeague = league.getLeagueCaption().substring(0, index).trim().equals("Champions League");
                List<Standing> standings = league.getStanding();
                if (isChampionsLeague) {
                    standings = setLastResults(standings);
                }
                return fetchFiveLastResults(standings, mEvents, league.getMatchday());
            }
        };

        observableStandings = App.getAPI().getLeague(mLeagueId)
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.computation())
                .map(assignLastResult)
                .observeOn(AndroidSchedulers.mainThread())
                .replay();

        observableStandings.connect();

        observableEvents = App.getAPI().getEvents(mLeagueId);

        final Observer<List<Standing>> observer = new Observer<List<Standing>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
                Singleton.getSingleton(getActivity()).updateAndInsertTeamStanding(mLeague);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onNext(List<Standing> standings) {
                mLeague.getStanding().clear();
                mLeague.setStanding(standings);
                mAdapter = new LeagueTableAdapter(getActivity(), standings, isChampionsLeague, mLeagueId);
                mRecyclerView.setAdapter(mAdapter);
            }
        };

        observableEvents
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Event>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");
                        observableStandings.subscribe(observer);
                    }

                    @Override
                    public void onNext(List<Event> eventList) {
                        mEvents = eventList;
                        Singleton.getSingleton(getActivity()).updateAndInsertEvents(eventList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, e.getMessage());
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_league_table, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_league_table_recycler_view_id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    private List<Standing> setLastResults(List<Standing> standings){
        Collections.sort(standings, new GroupStageComparator(getActivity()));
        int rank = 1;
        for (Standing standing : standings){
            standing.setPosition((rank % 4 == 0) ? rank = 1 : rank++);
        }
        return standings;
    }

    private List<Standing> fetchFiveLastResults(List<Standing> standings, List<Event> events, int matchDay) {
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
            if (event.getStatus().equals("FINISHED") && (dayOfMatch > matchDay - 5)
                    && (dayOfMatch <= matchDay)) {
                homeStanding = mapStandings.get(event.getHomeTeamName());
                awayStanding = mapStandings.get(event.getAwayTeamName());
                switch (matchDay - dayOfMatch) {
                    case 0: {
                        res = assignPreviousResultForHomeTeam(event);
                        homeStanding.setPreResult(res[0]);
                        awayStanding.setPreResult(res[1]);
                        break;
                    }
                    case 1: {
                        res = assignPreviousResultForHomeTeam(event);
                        homeStanding.setPreResult(res[0]);
                        awayStanding.setPreResult(res[1]);
                        break;
                    }
                    case 2: {
                        res = assignPreviousResultForHomeTeam(event);
                        homeStanding.setPreResult(res[0]);
                        awayStanding.setPreResult(res[1]);
                        break;
                    }
                    case 3: {
                        res = assignPreviousResultForHomeTeam(event);
                        homeStanding.setPreResult(res[0]);
                        awayStanding.setPreResult(res[1]);
                        break;
                    }
                    case 4: {
                        res = assignPreviousResultForHomeTeam(event);
                        homeStanding.setPreResult(res[0]);
                        awayStanding.setPreResult(res[1]);
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

