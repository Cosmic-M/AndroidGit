package com.example.bigfi.football_fanatic;

import android.app.Activity;;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bigfi.football_fanatic.pojo_model.Standing;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bigfi on 26.11.2017.
 */

public class LeagueTableAdapter extends Adapter<ViewHolder> {

    private static final String TAG = "LeagueTableAdapter";
    private static String[] group = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
    private List<Standing> mStandings;
    private boolean isChampionsLeague;
    private int mLeagueId;
    private Activity mActivity;

    public LeagueTableAdapter(Activity activity, List<Standing> standings, boolean isChampionsLeague, int leagueId) {
        this.mActivity = activity;
        this.mStandings = standings;
        this.isChampionsLeague = isChampionsLeague;
        this.mLeagueId = leagueId;
    }

    @Override
    public int getItemViewType(int position) {
        if (isChampionsLeague && (position % 5) == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
                viewHolder = new GroupNameHolder(view);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_standing_item, parent, false);
                viewHolder = new TeamStandingHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (this.getItemViewType(position)) {
            case 0:
                GroupNameHolder groupName = (GroupNameHolder) holder;
                int groupItem = position / 5;
                groupName.bindItem(group[groupItem]);
                break;
            case 1:
                TeamStandingHolder teamStandingHolder = (TeamStandingHolder) holder;

                Standing standing = mStandings.get(position);
                if (isChampionsLeague) {
                    standing.setRank(position % 5);
                }
                teamStandingHolder.bindItem(standing);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mStandings.size();
    }

    private class GroupNameHolder extends ViewHolder {
        private TextView mGroup;

        GroupNameHolder(View itemView) {
            super(itemView);
            mGroup = (TextView) itemView.findViewById(R.id.group_id);
        }

        public void bindItem(String group) {
            mGroup.setText("GROUP: " + group);
        }
    }

    private class TeamStandingHolder extends ViewHolder implements View.OnClickListener {
        private TextView mPosition;
        private ImageView mEmblem;
        private TextView mTeamName;
        private TextView mWinGames;
        private TextView mDrawGames;
        private TextView mLossGames;
        private TextView mPlayedGames;
        private TextView mGoalScoredAndMissedGoals;
        private TextView mPoints;
        private ImageView preOneMatchDay;
        private ImageView preTwoMatchDay;
        private ImageView preThreeMatchDay;
        private ImageView preFourMatchDay;
        private ImageView preFiveMatchDay;

        TeamStandingHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTeamName = (TextView) itemView.findViewById(R.id.team_name);
            mEmblem = (ImageView) itemView.findViewById(R.id.image_id);
            mEmblem.setBackgroundColor(Color.TRANSPARENT);
            mEmblem.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mPosition = (TextView) itemView.findViewById(R.id.position);
            mPlayedGames = (TextView) itemView.findViewById(R.id.played_matches);
            mWinGames = (TextView) itemView.findViewById(R.id.win_games);
            mDrawGames = (TextView) itemView.findViewById(R.id.draw_games);
            mLossGames = (TextView) itemView.findViewById(R.id.loss_games);
            mGoalScoredAndMissedGoals = (TextView) itemView
                    .findViewById(R.id.goal_scored_and_missed_goals);
            mPoints = (TextView) itemView.findViewById(R.id.points);
            preOneMatchDay = (ImageView) itemView.findViewById(R.id.matchDay1);
            preTwoMatchDay = (ImageView) itemView.findViewById(R.id.matchDay2);
            preThreeMatchDay = (ImageView) itemView.findViewById(R.id.matchDay3);
            preFourMatchDay = (ImageView) itemView.findViewById(R.id.matchDay4);
            preFiveMatchDay = (ImageView) itemView.findViewById(R.id.matchDay5);
        }

        public void bindItem(Standing item) {
            mTeamName.setText(item.getTeamName());
            mPosition.setText(String.valueOf(item.getPosition()));
            mPlayedGames.setText(String.valueOf(item.getPlayedGames()));
            mWinGames.setText(item.getWins());
            mDrawGames.setText(item.getDraws());
            mLossGames.setText(item.getLosses());

            Log.i(TAG, "CREST_URI => " + item.getCrestURI());
            Log.i(TAG, "RANK => " + item.getPosition());
            Log.i(TAG, "PLAYED_GAMES => " + item.getPlayedGames());
            Log.i(TAG, "POINTS => " + item.getPoints());

            Picasso.with(mActivity)
                    .load(item.getCrestURI())
                    .placeholder(R.raw.paceholder)
                    .error(R.raw.failure)
                    .into(mEmblem);

            mGoalScoredAndMissedGoals.setText(item
                    .getGoals() + "-" + item.getGoalsAgainst());

            mPoints.setText(item.getPoints());

            int[] mass = new int[]{item.getPreResult(), item.getPre2Result(), item.getPre3Result(),
                    item.getPre4Result(), item.getPre5Result()};

            ImageView[] images = {preOneMatchDay, preTwoMatchDay,
                    preThreeMatchDay, preFourMatchDay, preFiveMatchDay};

            for (int i = 0; i < mass.length; i++) {
                switch (mass[i]) {
                    case 1:
                        images[i].setImageResource(R.drawable.gray_circle);
                        break;
                    case -1:
                        images[i].setImageResource(R.drawable.red_circle);
                        break;
                    case 3:
                        images[i].setImageResource(R.drawable.green_circle);
                        break;
                    default:
                        images[i].setVisibility(View.GONE);
                        break;
                }
            }
        }

        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick");
            Fragment fragment = MatchesTeamFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("team_name_id", mTeamName.getText().toString());
            bundle.putInt("league_id", mLeagueId);
            fragment.setArguments(bundle);
            android.app.FragmentManager fragmentManager = mActivity.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,  fragment)
                    .addToBackStack("").commit();
        }
    }
}
