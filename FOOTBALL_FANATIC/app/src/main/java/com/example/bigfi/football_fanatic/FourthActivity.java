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


import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bigfi on 06.12.2017.
 */

public class FourthActivity extends AppCompatActivity {
    private static final String TAG = "FourthActivity";
    private static final String MATCH_ID = "matchId";
    private static final String HOME_URL = "homeTeamUrl";
    private static final String AWAY_URL = "awayTeamUrl";
    private static final String HOME_TEAM_NAME = "homeTeamName";
    private static final String AWAY_TEAM_NAME = "awayTeamName";

    private RecyclerView mRecyclerView;
    private FourthAdapter mAdapter;
    private int mId;
    private Map<String, String> urlMap;
    private List<Event> mEvents;
    private JsonUtils mJsonUtils;
    GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> mRequestBuilder;

    Observable<Response<ResponseBody>> observable;

    public static Intent newInstance(Activity activity, int _id, String homeUrl, String awayUrl, String homeName, String awayName){
        Intent intent = new Intent(activity, FourthActivity.class);
        intent.putExtra(MATCH_ID, _id);
        intent.putExtra(HOME_URL, homeUrl);
        intent.putExtra(AWAY_URL, awayUrl);
        intent.putExtra(HOME_TEAM_NAME, homeName);
        intent.putExtra(AWAY_TEAM_NAME, awayName);
        return intent;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.container_rv);
        Log.i(TAG, "FourthActivity start creating");
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_id);
        mRecyclerView.setLayoutManager(new
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new FourthAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mId = getIntent().getIntExtra(MATCH_ID, 0);
        Log.i(TAG, "mId = " + mId);
        String homeTeamName = getIntent().getStringExtra(HOME_TEAM_NAME);
        String awayTeamName = getIntent().getStringExtra(AWAY_TEAM_NAME);
        String awayUrl = getIntent().getStringExtra(AWAY_URL);
        String homeUrl = getIntent().getStringExtra(HOME_URL);
        urlMap = new HashMap<>();
        urlMap.put(homeTeamName, homeUrl);
        urlMap.put(awayTeamName, awayUrl);

        mEvents = new ArrayList<>();
        mJsonUtils = new JsonUtils();

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

        observable = App.getAPI().getEventsForCoupleTeams(mId);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "JSON, onCompleted");
                        mAdapter.setData(FourthActivity.this, mEvents, mRequestBuilder);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "SOMETHING WRONG WITH JSON: " + e.getMessage().toString());
                    }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Log.i(TAG, "response is successfully");
                            String answer = "";
                            try {
                                answer = response.body().string();
                                mEvents.clear();
                                Log.i(TAG, "ANSWER -> " + answer);
                                mEvents = mJsonUtils.parseJsonToEventsForCoupleTeams(answer);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException exc) {
                                exc.printStackTrace();
                            }
                        }
                        for (Event event : mEvents) {
                            event.setHomeTeamUrl(urlMap.get(event.getHomeTeamName()));
                            event.setAwayTeamUrl(urlMap.get(event.getAwayTeamName()));
                        }
                    }
                });
    }
}
