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

public class FourthFragment extends Fragment {
    private static final String TAG = "FourthFragment";

    private RecyclerView mRecyclerView;
    private FourthAdapter mAdapter;
    private int mId;
    private Map<String, String> urlMap;
    private List<Event> mEvents;
    private JsonUtils mJsonUtils;
    GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> mRequestBuilder;

    Observable<Response<ResponseBody>> observable;

    public static Fragment newInstance() {
        return new FourthFragment();
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mId = bundle.getInt("eventId");
            urlMap = new HashMap<>();
            urlMap.put(bundle.getString("homeTeamName"), bundle.getString("homeTeamUrl"));
            urlMap.put(bundle.getString("awayTeamName"), bundle.getString("awayTeamUrl"));
        }

        mEvents = new ArrayList<>();
        mJsonUtils = new JsonUtils();

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

        observable = App.getAPI().getEventsForCoupleTeams(mId);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "JSON, onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "SOMETHING WRONG WITH JSON: " + e.getMessage().toString());
                    }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            String answer = "";
                            try {
                                answer = response.body().string();
                                mEvents.clear();
                                mEvents = mJsonUtils.parseJsonToEvents(answer);
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
                        mAdapter.setData(getActivity(), mEvents, mRequestBuilder);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_rv, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_id);
        mRecyclerView.setLayoutManager(new
                LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        mAdapter = new FourthAdapter();
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
