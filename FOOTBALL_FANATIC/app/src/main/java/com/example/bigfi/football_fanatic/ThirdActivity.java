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
import com.example.bigfi.football_fanatic.supportsGlide.SvgDecoder;
import com.example.bigfi.football_fanatic.supportsGlide.SvgDrawableTranscoder;
import com.example.bigfi.football_fanatic.supportsGlide.SvgSoftwareLayerSetter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by bigfi on 06.12.2017.
 */

public class ThirdActivity extends AppCompatActivity {
    private static final String TAG = "ThirdActivity";
    private static final String CHAMPIONSHIP_ID = "championshipId";
    private static final String TEAM_ID = "teamId";

    private RecyclerView mRecyclerView;
    private List<Event> mEvents;
    private int mTeamId;
    private int mLeagueId;
    private ThirdAdapter mAdapter;
    private Observable.OnSubscribe<List<Event>> onSubscribe;
    private Observer<List<Event>> observer;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> mRequestBuilder;

    public static Intent newInstance(Activity activity, int leagueId, int teamId){
        Intent intent = new Intent(activity, ThirdActivity.class);
        intent.putExtra(CHAMPIONSHIP_ID, leagueId);
        intent.putExtra(TEAM_ID, teamId);
        return intent;
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.events_list_recycler_view);

        mLeagueId = getIntent().getIntExtra(CHAMPIONSHIP_ID, 0);
        mTeamId = getIntent().getIntExtra(TEAM_ID, 0);
        Log.i(TAG, "mLeagueId = " + mLeagueId);
        Log.i(TAG, "mTeamName = " + mTeamId);
        mEvents = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.represent_list_recycle_view_id);
        mRecyclerView.setLayoutManager(new
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ThirdAdapter();
        mRecyclerView.setAdapter(mAdapter);

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
                subscriber.onNext(Singleton.getSingleton(ThirdActivity.this)
                        .getEventsByCompetitionAndTeam(mLeagueId, mTeamId));
                subscriber.onCompleted();
            }
        };

        Observable<List<Event>> mObservable = Observable.create(onSubscribe)
                .subscribeOn(Schedulers.io())
                .map(eventComparator)
                .observeOn(AndroidSchedulers.mainThread());

        observer = new Observer<List<Event>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onComplete called");
                mAdapter.setData(ThirdActivity.this, mEvents, mRequestBuilder);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "ERROR: " + e.getMessage());
            }

            @Override
            public void onNext(List<Event> events) {
                mEvents.clear();
                mEvents.addAll(events);
                Log.i(TAG, "events.size() = " + events.size());
            }
        };

        mObservable.subscribe(observer);

    }
}
