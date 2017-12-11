package com.example.bigfi.football_fanatic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bigfi.football_fanatic.pojo_model.Championship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by bigfi on 26.11.2017.
 */

public class RepresentFragment extends Fragment {
    private static final String TAG = "MainActivity";
    private List<Championship> championships;
    private RecyclerView mRecyclerView;
    private static final List<String> leagueShortNames = Arrays
            .asList(new String[]{"BL1", "CL", "DED", "FL1", "PD", "PL", "PPL", "SA"});
    private Observable<List<Championship>> observable;
    private List<Integer> years = new ArrayList<>();

    public static Fragment newInstance(){
        return new RepresentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_represent_recycler_view, viewGroup, false);

        championships = new ArrayList<>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.represent_list_leagues_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        final RepresentAdapter adapter = new RepresentAdapter(getActivity(), championships, years);
        mRecyclerView.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        Observable<List<Championship>>[] observableArray = new Observable[calendar.get(Calendar.YEAR) - 2015 + 1];

        for (int year = 2017, i = 0; year <= calendar.get(Calendar.YEAR); year++, i++){
            observable = App.getAPI()
                    .getData(year)
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<List<Championship>, List<Championship>>() {
                        /*
                        * this this function will obtain renew list of PostModels
                        * which included only leagues from leagueShortNames
                         */
                        @Override
                        public List<Championship> call(List<Championship> postModel) {
                            for (int i = 1; i < postModel.size(); i++) {
                                if (!leagueShortNames.contains(championships.get(i).getLeague())) {
                                    postModel.remove(i--);
                                }
                            }
                            return postModel;
                        }
                    });
            observableArray[i] = observable;
        }

        Observable.merge(observableArray, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Championship>>() {
                    @Override
                    public void onNext(List<Championship> list) {
                        Log.i(TAG, "onNext called");
                        int yearOfObtainedCompetitions = Integer.parseInt(list.get(0).getYear());
                        Championship c = new Championship();
                        c.setYear(String.valueOf(yearOfObtainedCompetitions));
                        list.add(0, c);
                        years.add(championships.size());
                        championships.addAll(list);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "all leagues loaded");
                    }

                    @Override
                        public void onError(Throwable e) {
                            Log.i(TAG, e.getMessage());
                    }
                });
        return view;
    }
}
