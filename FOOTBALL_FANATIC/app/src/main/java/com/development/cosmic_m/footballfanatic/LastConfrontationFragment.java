package com.development.cosmic_m.footballfanatic;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Cosmic_M on 29.06.2017.
 */

public class LastConfrontationFragment extends Fragment {
    private SingletonLeague mSingletonLeague;
    private RecyclerView mRecyclerView;
    private String mConfrontationId;
    private List<Event> mItems = new ArrayList<>();

    public static LastConfrontationFragment newInstance(){
        return new LastConfrontationFragment();
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mConfrontationId = bundle.getString("confrontationId");
        }
        mSingletonLeague = SingletonLeague.getSingleton(getActivity());
        if (saveInstanceState == null) {
            new BackgroundTask().execute(mConfrontationId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.container_recycler_view, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_id);
        mRecyclerView.setLayoutManager(new
                LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        setupAdapter();
        return view;
    }

    private void setupAdapter(){
        if (this.isAdded()){
            mRecyclerView.setAdapter(new ListMatchesAdapter(mItems));
        }
    }

    private class ListMatchesHolder extends RecyclerView.ViewHolder {
        private TextView mHomeTeam;
        private ImageView mImageHomeTeam;
        private TextView mScoreOfMatch;
        private ImageView mImageAwayTeam;
        private TextView mAwayTeam;
        private TextView mDateOfMatch;

        ListMatchesHolder(View itemView){
            super(itemView);
            mHomeTeam = (TextView) itemView.findViewById(R.id.home_team);
            mImageHomeTeam = (ImageView) itemView.findViewById(R.id.image_home_team);
            mImageHomeTeam.setBackgroundColor(Color.TRANSPARENT);
            mImageHomeTeam.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mScoreOfMatch = (TextView) itemView.findViewById(R.id.score_of_match);
            mImageAwayTeam = (ImageView) itemView.findViewById(R.id.image_away_team);
            mImageAwayTeam.setBackgroundColor(Color.TRANSPARENT);
            mImageAwayTeam.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mAwayTeam = (TextView) itemView.findViewById(R.id.away_team);
            mDateOfMatch = (TextView) itemView.findViewById(R.id.date_of_match);
        }

        public void bindEvents(Event event){
            mHomeTeam.setText(event.getHomeTeamName());
            mAwayTeam.setText(event.getAwayTeamName());
            mImageHomeTeam.setImageDrawable(mSingletonLeague
                    .getTeamDrawable(mHomeTeam.getText().toString()));
            mScoreOfMatch.setText(event.getGoalsHomeTeam() + " : " + event.getGoalsAwayTeam());
            mImageAwayTeam.setImageDrawable(mSingletonLeague
                    .getTeamDrawable(mAwayTeam.getText().toString()));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            Date result;
            String dateOfMatch = "";
            try {
                result = df.parse(event.getMatchDate());
                SimpleDateFormat sdfForDate = new SimpleDateFormat("dd MMMM, yyyy");
                SimpleDateFormat sdfForTime = new SimpleDateFormat("HH:mm");
                sdfForDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                sdfForTime.setTimeZone(TimeZone.getTimeZone("GMT"));
                dateOfMatch = sdfForDate.format(result);
            }
            catch(java.text.ParseException e){
                e.printStackTrace();
            }
            mDateOfMatch.setText(dateOfMatch);
        }
    }

    private class ListMatchesAdapter extends RecyclerView.Adapter<ListMatchesHolder>{
        private List<Event> mItems;

        public ListMatchesAdapter(List<Event> items){
            mItems = items;
        }

        @Override
        public ListMatchesHolder onCreateViewHolder(ViewGroup viewGroup, int itemView){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.item_historical_confrontation, viewGroup, false);
            return new ListMatchesHolder(view);
        }

        @Override
        public void onBindViewHolder(ListMatchesHolder holder, int position){
            Event event = mItems.get(position);
            holder.bindEvents(event);
        }

        @Override
        public int getItemCount(){
            return mItems.size();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    private class BackgroundTask extends AsyncTask<String, Void, List<Event>> {

        @Override
        protected List<Event> doInBackground(String...params){
            String link = params[0];
            List<Event> listOfConfrontatioin = null;
            try {
                listOfConfrontatioin = new APILoader().fetchItemsHistory(link);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return listOfConfrontatioin;
        }

        @Override
        public void onPostExecute(List<Event> list){
            mItems = list;
            if (mItems == null){
                Toast.makeText(getActivity(), "YOU REACH YOUR REQUEST LIMIT", Toast.LENGTH_SHORT).show();
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            else {
                setupAdapter();
            }
        }
    }
}

