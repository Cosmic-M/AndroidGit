package com.development.cosmic_m.footballfanatic;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.development.cosmic_m.footballfanatic.database.SchemaDB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Cosmic_M on 07.05.2017.
 */

public class ListAllMatchesOfTeamFragment extends Fragment {
    private String mTeamId;
    private String mLeagueCaption;
    private String mIdLeague;
    private RecyclerView mRecyclerView;
    private List<Event> mItems = new ArrayList<>();

    public static ListAllMatchesOfTeamFragment newInstance(){
        return new ListAllMatchesOfTeamFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTeamId = bundle.getString("team_id");
            mIdLeague = bundle.getString("league_id");
            mLeagueCaption = bundle.getString("leagueCaption");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_for_recycler_view, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_id);
        mRecyclerView.setLayoutManager(new
                LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        String clause = "(" + SchemaDB.EventTable.Cols.HOME_TEAM_ID + " =? OR "
                + SchemaDB.EventTable.Cols.AWAY_TEAM_ID + " =?) AND "
                + SchemaDB.EventTable.Cols.COMPETITION_ID + " =?";
        String args = mTeamId;

        Map<String, Event> mapOfOneTeam = SingletonLeague.getSingleton(getActivity())
                .getEventMap(clause, new String[]{args, args, mIdLeague});
        mItems = new ArrayList<>(mapOfOneTeam.values());
        Collections.sort(mItems, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                String dateForEvent1 = e1.getMatchDate();
                String dateForEvent2 = e2.getMatchDate();
                return dateForEvent1.compareTo(dateForEvent2);
            }
        });
        setupAdapter();
        return view;
    }

    private void setupAdapter(){
        if (this.isAdded()){
            mRecyclerView.setAdapter(new Adapter(mItems));
        }
    }

    private class MatchHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mLeagueTitle;
        private TextView mHomeTeam;
        private ImageView mImageHomeTeam;
        private TextView mScoreOfMatch;
        private ImageView mImageAwayTeam;
        private TextView mAwayTeam;
        private TextView mDateOfMatch;
        private String mMatch_id;

        MatchHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mLeagueTitle = (TextView) itemView.findViewById(R.id.league_title);
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
        }//ListMatchesHolder(View itemView)

        public void bindEvents(Event event){
            mLeagueTitle.setText(mLeagueCaption);
            mHomeTeam.setText(event.getHomeTeamName());
            mAwayTeam.setText(event.getAwayTeamName());
            Drawable homeDrawable = SingletonLeague.getSingleton(getActivity())
                    .getEmblemDrawable(event.getHomeTeamId());
            Drawable awayDrawable = SingletonLeague.getSingleton(getActivity())
                    .getEmblemDrawable(event.getAwayTeamId());
            mImageHomeTeam.setImageDrawable(homeDrawable);
            mImageAwayTeam.setImageDrawable(awayDrawable);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            Date result;
            String dateOfMatch = "";
            String timeOfMatch = "";
            try {
                result = df.parse(event.getMatchDate());
                SimpleDateFormat sdfForDate = new SimpleDateFormat("EEEE, dd MMMM");
                SimpleDateFormat sdfForTime = new SimpleDateFormat("HH:mm");
                sdfForDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                sdfForTime.setTimeZone(TimeZone.getTimeZone("GMT"));
                dateOfMatch = sdfForDate.format(result);
                timeOfMatch = sdfForTime.format(result);
            }
            catch(java.text.ParseException e){
                e.printStackTrace();
            }
            if (event.getGoalsHomeTeam().equals("null")){
                mScoreOfMatch.setText(timeOfMatch);
            }
            else {
                mScoreOfMatch.setText(event.getGoalsHomeTeam() + " : " + event.getGoalsAwayTeam());
            }
            mDateOfMatch.setText(dateOfMatch);
            mMatch_id = event.getMatchId();
        }//public void bindEvents(Events events)

        private boolean isNetworkAvailableAndConnected(){
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
            boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
            boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
            return isNetworkConnected;
        }

        @Override
        public void onClick(View view){
            if (!isNetworkAvailableAndConnected()){
                Toast.makeText(getActivity(), "WITHOUT CONNECTION TO NETWORK", Toast.LENGTH_SHORT).show();
                return;
            }
            LastConfrontationFragment lastConfrontationsFragment = LastConfrontationFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("confrontationId", mMatch_id);
            String homeTeamName = mHomeTeam.getText().toString();
            String awayTeamName = mAwayTeam.getText().toString();
            SingletonLeague singletonLeague = SingletonLeague.getSingleton(getActivity());
            singletonLeague.setTeamDrawable(homeTeamName, mImageHomeTeam.getDrawable());
            singletonLeague.setTeamDrawable(awayTeamName, mImageAwayTeam.getDrawable());
            lastConfrontationsFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,  lastConfrontationsFragment)
                    .addToBackStack("").commit();
        }
    }

    private class Adapter extends RecyclerView.Adapter<MatchHolder>{
        private List<Event> mItems;

        public Adapter(List<Event> items){
            mItems = items;
        }

        @Override
        public MatchHolder onCreateViewHolder(ViewGroup viewGroup, int itemView){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.item_for_list_of_matches, viewGroup, false);
            return new MatchHolder(view);
        }

        @Override
        public void onBindViewHolder(MatchHolder holder, int position){
            Event event = mItems.get(position);
            holder.bindEvents(event);
        }

        @Override
        public int getItemCount(){
            return mItems.size();
        }
    }
}

