package com.example.bigfi.football_fanatic;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
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

public class RepresentAdapter extends Adapter<ViewHolder> {

    public static final String TAG = "RepresentAdapter";
    private List<Championship> posts;
    private static List<Integer> years = new ArrayList<>();
    private Activity mActivity;

    public RepresentAdapter(Activity activity, List<Championship> posts, List<Integer> years) {
        this.mActivity = activity;
        this.posts = posts;
        this.years = years;
    }

    @Override
    public int getItemViewType(int position) {
        if (years.contains(position)) {
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
                Championship onlyYear = posts.get(position);
                yearOfLeagueHolder.bindItem(onlyYear);
                break;
            case 1:
                LeagueHolder leagueHolder = (LeagueHolder) holder;
                Championship leagueWithYear = posts.get(position);
                leagueHolder.bindItem(leagueWithYear);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
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
            championship = item;
            int index = item.getCaption().indexOf("20");
            //leagueCaption.setText(item.getCaption().substring(0, index).trim().toUpperCase());
            leagueCaption.setText(item.getCaption());
            String league = item.getLeague();
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
            Log.i(RepresentAdapter.TAG, "choice league");
            LeagueStandingFragment leagueStandingFragment = LeagueStandingFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putInt("id", championship.getId());
            leagueStandingFragment.setArguments(bundle);
            FragmentManager fragmentManager = mActivity.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, leagueStandingFragment, "leagueStandingFragmentTAG")
                    .addToBackStack("").commit();

        }
    }

    class YearOfLeagueHolder extends ViewHolder {
        TextView year;

        public YearOfLeagueHolder(View itemView) {
            super(itemView);
            year = (TextView) itemView.findViewById(R.id.year_id);
        }

        public void bindItem(Championship item) {
            year.setText("SEASON " + item.getYear() + "/" + (item.getYear() + 1));
        }
    }
}
