package com.example.bigfi.football_fanatic;


import android.app.Fragment;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ThirdFragment extends Fragment {
    private static final String TAG = "MatchesTeamFragment";
    private RecyclerView mRecyclerView;
    private List<Event> mEvents;
    private String mTeamName;
    private int mLeagueId;
    private ThirdAdapter mAdapter;
    private Observable.OnSubscribe<List<Event>> onSubscribe;
    private Observer<List<Event>> observer;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> mRequestBuilder;

    public static Fragment newInstance(){
        return new ThirdFragment();
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mTeamName = bundle.getString("team_name");
            mLeagueId = bundle.getInt("league_id");
        }

        mRequestBuilder = Glide.with(getActivity().getApplicationContext())
                .using(Glide.buildStreamModelLoader(Uri.class, getActivity().getApplicationContext()), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .placeholder(R.drawable.p_holder)
                .error(R.drawable.error)
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
                subscriber.onNext(Singleton.getSingleton(getActivity())
                        .getEventsByCompetitionAndTeam(mLeagueId, mTeamName));
                subscriber.onCompleted();
            }
        };

        Observable<List<Event>> mObservable = Observable.create(onSubscribe)
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
                mAdapter.setData(getActivity(), events, mRequestBuilder);
            }
        };

        mObservable.subscribe(observer);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
        View view = inflater.inflate(R.layout.fragment_represent_recycler_view, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.represent_list_leagues_recycle_view);
        mRecyclerView.setLayoutManager(new
                LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new ThirdAdapter();
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
