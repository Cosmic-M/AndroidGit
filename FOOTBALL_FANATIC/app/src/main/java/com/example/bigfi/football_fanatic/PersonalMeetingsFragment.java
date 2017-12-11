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
import com.example.bigfi.football_fanatic.pojo_model.Fixture;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bigfi on 06.12.2017.
 */

public class PersonalMeetingsFragment extends Fragment {
    private static final String TAG = "PersonalMeetingsFragment";

    private Singleton mSingleton;
    private RecyclerView mRecyclerView;
    private int mId;
    private List<Event> mEvents = new ArrayList<>();

    Observable<List<Fixture>> observable;

    public static Fragment newInstance() {
        return new PersonalMeetingsFragment();
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mId = bundle.getInt("eventId");
        }

        observable = App.getAPI().getEventsForCoupleTeams(mId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observer<List<Fixture>> observer = new Observer<List<Fixture>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Fixture> fixtures) {

            }
        };

        mSingleton = Singleton.getSingleton(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_rv, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_id);
        mRecyclerView.setLayoutManager(new
                LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        setupAdapter();

        return view;
    }

    private void setupAdapter(){
        if (this.isAdded()){
            mRecyclerView.setAdapter(new LastMatchesAdapter(getActivity(), mEvents));
        }
    }
}
