package com.example.bigfi.football_fanatic;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bigfi.football_fanatic.pojo_model.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by bigfi on 06.12.2017.
 */

public class ResultMatchesAdapter extends Adapter<ViewHolder> {

    private List<Event> mItems;
    private Activity mActivity;

    public ResultMatchesAdapter(Activity activity, List<Event> items) {
        this.mActivity = activity;
        mItems = items;
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup viewGroup, int itemView) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.result_match_item, viewGroup, false);
        return new EventHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = mItems.get(position);
        ((EventHolder) holder).bindEvents(event);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class EventHolder extends ViewHolder implements View.OnClickListener {
        private TextView mLeagueCaption;
        private TextView mHomeTeamName;
        private ImageView mHomeTeamImage;
        private TextView mScore;
        private ImageView mAwayTeamImage;
        private TextView mAwayTeamName;
        private TextView mDate;
        private Integer mId;

        EventHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mLeagueCaption = (TextView) itemView.findViewById(R.id.league_caption);
            mHomeTeamName = (TextView) itemView.findViewById(R.id.home_team_name);
            mHomeTeamImage = (ImageView) itemView.findViewById(R.id.home_team_image);
            mHomeTeamImage.setBackgroundColor(Color.TRANSPARENT);
            mHomeTeamImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mScore = (TextView) itemView.findViewById(R.id.score);
            mAwayTeamImage = (ImageView) itemView.findViewById(R.id.away_team_image);
            mAwayTeamImage.setBackgroundColor(Color.TRANSPARENT);
            mAwayTeamImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mAwayTeamName = (TextView) itemView.findViewById(R.id.away_team_name);
            mDate = (TextView) itemView.findViewById(R.id.date);
        }

        public void bindEvents(Event event) {
            mLeagueCaption.setText("");
            mHomeTeamName.setText(event.getHomeTeamName());
            mAwayTeamName.setText(event.getAwayTeamName());

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            Date result;
            String dateOfMatch = "";
            String timeOfMatch = "";
            try {
                result = df.parse(event.getDate());
                SimpleDateFormat sdfForDate = new SimpleDateFormat("EEEE, dd MMMM");
                SimpleDateFormat sdfForTime = new SimpleDateFormat("HH:mm");
                sdfForDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                sdfForTime.setTimeZone(TimeZone.getTimeZone("GMT"));
                dateOfMatch = sdfForDate.format(result);
                timeOfMatch = sdfForTime.format(result);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            if (event.getResult().getGoalsHomeTeam().equals("null")
                    || event.getResult().getGoalsAwayTeam().equals("null")) {
                mScore.setText(timeOfMatch);
            } else {
                mScore.setText(event.getResult().getGoalsHomeTeam() + " : " + event.getResult().getGoalsAwayTeam());
            }
            mDate.setText(dateOfMatch);
            mId = event.getId();
        }

        private boolean isNetworkAvailableAndConnected() {
            ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(CONNECTIVITY_SERVICE);
            boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
            boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
            return isNetworkConnected;
        }

        @Override
        public void onClick(View view) {
            if (!isNetworkAvailableAndConnected()) {
                Toast.makeText(mActivity, "WITHOUT CONNECTION TO NETWORK", Toast.LENGTH_SHORT).show();
                return;
            }
            Fragment fragment = new PersonalMeetingsFragment().newInstance();
            Bundle bundle = new Bundle();
            bundle.putInt("eventId", mId);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = mActivity.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                    .addToBackStack("").commit();
        }
    }
}