package com.example.bigfi.football_fanatic;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigfi.football_fanatic.pojo_model.Championship;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigfi on 24.11.2017.
 */

public class FirstAdapter extends Adapter<ViewHolder> {

    public static final String TAG = "FirstAdapter";
    private List<Championship> mChampionships = new ArrayList<>();
    private List<Integer> mYears = new ArrayList<>();
    private Activity mActivity;

    public void setData(Activity activity, List<Championship> championships, List<Integer> years){
        mChampionships.clear();
        this.mYears.clear();
        this.mActivity = activity;
        this.mChampionships .addAll(championships);
        this.mYears.addAll(years);
        Log.i(TAG, "mChampionships.size() = " + mChampionships.size());
        Log.i(TAG, "years.size() = " + years.size());
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mYears.contains(position)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder = null;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.year_item, parent, false);
                viewHolder = new YearOfLeagueHolder(view);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.league_item, parent, false);
                viewHolder = new LeagueHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (this.getItemViewType(position)) {
            case 0:
                YearOfLeagueHolder yearOfLeagueHolder = (YearOfLeagueHolder) holder;
                Championship onlyYear = mChampionships.get(position);
                yearOfLeagueHolder.bindItem(onlyYear);
                break;
            case 1:
                LeagueHolder leagueHolder = (LeagueHolder) holder;
                Championship leagueWithYear = mChampionships.get(position);
                leagueHolder.bindItem(leagueWithYear);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mChampionships.size();
    }

    class LeagueHolder extends ViewHolder implements View.OnClickListener {
        Championship championship;
        TextView leagueCaption;
        ImageView leagueImage;
        LinearLayout linearLayout;

        public LeagueHolder(View itemView) {
            super(itemView);
            leagueCaption = (TextView) itemView.findViewById(R.id.leagueCaption);
            leagueImage = (ImageView) itemView.findViewById(R.id.imageOfLeague);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout_id);
            linearLayout.setOnClickListener(this);
        }

        public void bindItem(Championship item) {
            Log.i(TAG, "CAPTION: " + item.getCaption());
            Log.i(TAG, "LEAGUE: " + item.getLeague());
            championship = item;
            int index = championship.getCaption().indexOf("20");
            //leagueCaption.setText(item.getCaption().substring(0, index).trim().toUpperCase());
            leagueCaption.setText(championship.getCaption());
            String league = championship.getLeague();
            switch (league) {
                case "PL":
                    leagueImage.setImageResource(R.drawable.pl);
                    break;
                case "SA":
                    leagueImage.setImageResource(R.drawable.sa);
                    break;
                case "PD":
                    leagueImage.setImageResource(R.drawable.pd);
                    break;
                case "BL1":
                    leagueImage.setImageResource(R.drawable.bl1);
                    break;
                case "CL":
                    leagueImage.setImageResource(R.drawable.cl);
                    break;
                case "DED":
                    leagueImage.setImageResource(R.drawable.ded);
                    break;
                case "FL1":
                    leagueImage.setImageResource(R.drawable.fl1);
                    break;
                case "PPL":
                    leagueImage.setImageResource(R.drawable.ppl);
                    break;
            }
        }

        @Override
        public void onClick(View view) {
            Log.i(FirstAdapter.TAG, "choice league");
            Intent intent = SecondActivity.newInstance(mActivity, championship.getId());
            mActivity.startActivity(intent);
        }
    }

    class YearOfLeagueHolder extends ViewHolder {
        TextView year;

        public YearOfLeagueHolder(View itemView) {
            super(itemView);
            year = (TextView) itemView.findViewById(R.id.year_id);
        }

        public void bindItem(Championship item) {
            Log.i(TAG, "YearOfLeagueHolder -> " + item.getYear());
            year.setText("SEASON " + item.getYear() + "/" + (item.getYear() + 1));
        }
    }
}
