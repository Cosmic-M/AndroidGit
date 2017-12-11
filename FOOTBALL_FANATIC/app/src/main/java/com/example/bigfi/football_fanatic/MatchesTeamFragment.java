package com.example.bigfi.football_fanatic;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bigfi.football_fanatic.database.SchemaDB;
import com.example.bigfi.football_fanatic.pojo_model.Event;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by bigfi on 06.12.2017.
 */

public class MatchesTeamFragment extends Fragment {
    private static final String TAG = "MatchesTeamFragment";
    private RecyclerView mRecyclerView;
    private List<Event> mEvents;
    private String mTeamName;
    private int mLeagueId;

    private Observable<List<Event>> observable;
    private Observable.OnSubscribe<List<Event>> onSubscribe;
    private Observer<List<Event>> observer;

    public static Fragment newInstance(){
        return new MatchesTeamFragment();
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mTeamName = bundle.getString("team_name_id");
            mLeagueId = bundle.getInt("league_id");
        }

        final String clause = "(" + SchemaDB.EventTable.Cols.HOME_TEAM_NAME + " =? OR "
                + SchemaDB.EventTable.Cols.AWAY_TEAM_NAME + " =?) AND "
                + SchemaDB.EventTable.Cols.COMPETITION_ID + " =?";

        final String args = mTeamName;

        Func1<List<Event>, List<Event>> eventComparator = new Func1<List<Event>, List<Event>>() {
            @Override
            public List<Event> call(List<Event> events) {
                Collections.sort(events, new Comparator<Event>() {
                    @Override
                    public int compare(Event e1, Event e2) {
                        String dateForEvent1 = e1.getDate();
                        String dateForEvent2 = e2.getDate();
                        return dateForEvent1.compareTo(dateForEvent2);
                    }
                });
                return events;
            }
        };

        onSubscribe = new Observable.OnSubscribe<List<Event>>(){
            @Override
            public void call(Subscriber<? super List<Event>> subscriber){
                subscriber.onNext(Singleton.getSingleton(getActivity())
                        .getEventList(clause, new String[]{args, args, String.valueOf(mLeagueId)}));
                subscriber.onCompleted();
            }
        };

        observable.create(onSubscribe)
                .subscribeOn(Schedulers.io())
                .map(eventComparator);

        observer = new Observer<List<Event>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onComplete called");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onNext(List<Event> events) {
                mEvents = events;
                setupAdapter();
            }
        };

        observable.subscribe(observer);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
        View view = inflater.inflate(R.layout.fragment_represent_recycler_view, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.represent_list_leagues_recycle_view);
        mRecyclerView.setLayoutManager(new
                LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        setupAdapter();

        return view;
    }

    private void setupAdapter(){
        if (this.isAdded()){
            mRecyclerView.setAdapter(new ResultMatchesAdapter(getActivity(), mEvents));
        }
    }
}
