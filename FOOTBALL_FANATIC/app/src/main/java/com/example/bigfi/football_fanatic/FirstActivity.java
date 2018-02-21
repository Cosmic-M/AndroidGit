package com.example.bigfi.football_fanatic;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;

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

public class FirstActivity extends AppCompatActivity {
    private static final String TAG = "FirstActivity";
    private List<Championship> mChampionships;
    private RecyclerView mRecyclerView;
    private static final List<String> leagueShortNames = Arrays
            .asList(new String[]{"BL1", "CL", "DED", "FL1", "PD", "PL", "PPL", "SA"});
    private Observable<List<Championship>> observable;
    private List<Integer> years;
    private FirstAdapter mAdapter;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.represent_recycler_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.represent_list_leagues_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new FirstAdapter();
        mRecyclerView.setAdapter(mAdapter);

        Calendar calendar = Calendar.getInstance();

        mChampionships = new ArrayList<>();
        years = new ArrayList<>();

        //Observable<List<Championship>>[] observableArray = new Observable[calendar.get(Calendar.YEAR) - 2015 + 1];
        Observable<List<Championship>>[] observableArray = new Observable[1];

        for (int year = 2017, i = 0; year < calendar.get(Calendar.YEAR); year++, i++){ // or (int year = 2017, i = 0; year <= calendar.get(Calendar.YEAR); year++, i++){
            observable = App.getAPI()
                    .getData(year)
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<List<Championship>, List<Championship>>() {
                        /*
                        * this function will obtain renew list of Championships
                        * which included only leagues from leagueShortNames
                         */
                        @Override
                        public List<Championship> call(List<Championship> championships) {
                            for (int i = 0; i < championships.size(); i++) {
                                if (!leagueShortNames.contains(championships.get(i).getLeague())) {
                                    championships.remove(i--);
                                }
                            }
                            return championships;
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
                        Singleton.getSingleton(FirstActivity.this).insertListOfChampionship(list);
                        Log.i(TAG, "onNext called");
                        for (Championship c : list) {
                            Log.i(TAG, "list of championships: " + c.getCaption() + " " + c.getLeague());
                        }
                        int yearOfObtainedCompetitions = Integer.parseInt(list.get(0).getYear());
                        Championship c = new Championship();
                        c.setYear(String.valueOf(yearOfObtainedCompetitions));
                        list.add(0, c);
                        years.add(mChampionships.size());
                        mChampionships.addAll(list);
                        Log.i(TAG, "mChampionShips.size() = " + mChampionships.size());
                        Log.i(TAG, "years.size() = " + years.size());
                    }

                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "all leagues loaded");
                        mAdapter.setData(FirstActivity.this, mChampionships, years);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onERROR: " + e.getMessage());
                        mChampionships = Singleton.getSingleton(FirstActivity.this).getChampionships();
                        Log.i(TAG, "mChampionships.size() = " +  mChampionships.size());
                        mAdapter.setData(FirstActivity.this, mChampionships, years);
                    }
                });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    openQuitDialog();
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void openQuitDialog(){
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(FirstActivity.this);
        quitDialog.setTitle(R.string.quit);

        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which){
                finish();
            }
        });

        quitDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which){
            }
        });

        AlertDialog alertDialog = quitDialog.create();
        alertDialog.show();
    }
}
