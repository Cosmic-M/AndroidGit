package com.project.cosmic_m.football_facts;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cosmic_M on 13.07.2017.
 */

public class RepresentAllLeaguesFragment extends Fragment {

    private static final int PL = R.drawable.england_flag;
    private static final int SA = R.drawable.italy_flag;
    private static final int PD = R.drawable.spain_flag;
    private static final int BL1 = R.drawable.germany_flag;
    private static final int CL = R.drawable.europe_flag;
    private static final int DED = R.drawable.holland_flag;
    private static final int FL1 = R.drawable.france_flag;
    private static final int PPL = R.drawable.portugal_flag;

    private RecyclerView mRecycler;
    private List<League> mItems =  new ArrayList<>();
    private ProgressBar mProgressBar;
    private boolean flag_progress_bar;

    public static Fragment newInstance() {
        return new RepresentAllLeaguesFragment();
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
        A_Task aTask= new A_Task();
        aTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.container_recycler_view, container, false);
        mRecycler = (RecyclerView) view.findViewById(R.id.recycler_view_id);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mRecycler.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        if (flag_progress_bar == false){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        setupAdapter();
        return view;
    }

    private void setupAdapter() {
        if (this.isAdded()) {
            mRecycler.setAdapter(new Adapter(mItems));
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private class LeagueHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mYearOfLeague;
        private League mLeague;
        private TextView mCaptionLeague;
        private ImageView mLeagueImage;
        private LinearLayout mLinearLayout;

        public LeagueHolder(View itemView) {
            super(itemView);
            mYearOfLeague = (TextView) itemView.findViewById(R.id.yearOfSeason);
            mCaptionLeague = (TextView) itemView.findViewById(R.id.season_id);
            mLeagueImage = (ImageView) itemView.findViewById(R.id.image_id);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout_id);
            mLinearLayout.setOnClickListener(this);
        }

        public void bindItem(League item){
            mLeague = item;
            int index = item.getCaption().indexOf("20");
            String captionYear = item.getCaption().substring(index);
            if (item.getHeader()) {
                mYearOfLeague.setVisibility(View.VISIBLE);
                mYearOfLeague.setText("SEASON " + captionYear);
            }
            else{
                mYearOfLeague.setVisibility(View.GONE);
            }
            mCaptionLeague.setText(item.getCaption().substring(0, index).trim().toUpperCase());
            String league = item.getLeague();
            switch (league) {
                case "PL":
                    mLeagueImage.setImageResource(PL);
                    break;
                case "SA":
                    mLeagueImage.setImageResource(SA);
                    break;
                case "PD":
                    mLeagueImage.setImageResource(PD);
                    break;
                case "BL1":
                    mLeagueImage.setImageResource(BL1);
                    break;
                case "CL":
                    mLeagueImage.setImageResource(CL);
                    break;
                case "DED":
                    mLeagueImage.setImageResource(DED);
                    break;
                case "FL1":
                    mLeagueImage.setImageResource(FL1);
                    break;
                case "PPL":
                    mLeagueImage.setImageResource(PPL);
                    break;
            }
        }

        @Override
        public void onClick(View view) {
            LeagueStandingFragment leagueStandingFragment = LeagueStandingFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putInt("id", mLeague.getId());
            bundle.putString("leagueCaption", mLeague.getCaption());
            leagueStandingFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,  leagueStandingFragment, "leagueStandingFragmentTAG")
                    .addToBackStack("").commit();
        }
    }

        private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

            private List<League> mItems;

            public Adapter(List<League> leagues) {
                mItems = leagues;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View view = inflater.inflate(R.layout.season_item, viewGroup, false);
                RecyclerView.ViewHolder viewHolder = new LeagueHolder(view);
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                LeagueHolder leagueHolder = (LeagueHolder) holder;
                League leagueWithYear = mItems.get(position);
                leagueHolder.bindItem(leagueWithYear);
            }

            @Override
            public int getItemCount() {
                return mItems.size();
            }
        }

    @Override
    public void onStop(){
        super.onStop();
        flag_progress_bar = true;
        mProgressBar.setVisibility(View.GONE);
    }

            private class A_Task extends AsyncTask<Void, Void, List<League>> {
                private boolean flag;
                private String previousCaption = "";
                private String currentCaption = "";

                @Override
                protected List<League> doInBackground(Void... params) {
                    Map<String, League> map = new HashMap<>();
                    try {
                    Calendar calendar = Calendar.getInstance();
                        for (int i = 2015; i <= calendar.get(Calendar.YEAR); i++){
                            String link = "http://api.football-data.org/v1/competitions/" + "?season=" + i;
                            map.putAll(new APILoader().getAllSupportLeagues(link));
                        }
                    SingletonLeague.getSingleton(getActivity()).insertListOfLeague(map);
                    }
                    catch (IOException e) {
                        map = SingletonLeague.getSingleton(getActivity()).getLeagueMap();
                        flag = true;
                    }
                    List<League> list = new ArrayList<>(map.values());
                    Collections.sort(list, new Comparator<League>(){
                        @Override
                        public int compare(League o1, League o2) {
                            int league_1 = o1.getId();
                            int league_2 = o2.getId();
                            if (league_1 > league_2) return -1;
                            else return 1;
                        }
                    });

                    for (int i = 0; i < list.size(); i++){
                        int startIndex = list.get(i).getCaption().indexOf("20");
                        int endIndex = list.get(i).getCaption().lastIndexOf("/");
                        if (endIndex == -1){
                            currentCaption = list.get(i).getCaption().substring(startIndex).trim();
                        }
                        else{
                            currentCaption = list.get(i).getCaption()
                                    .substring(startIndex, endIndex).trim();
                        }
                        if (i == 0){
                            list.get(i).setHeader(true);
                            previousCaption = currentCaption;
                        }
                        else if (Integer.parseInt(currentCaption) < Integer.parseInt(previousCaption)){
                            list.get(i).setHeader(true);
                            previousCaption = currentCaption;
                        }
                        else{
                            previousCaption = currentCaption;
                        }
                    }
                    return list;
                }

                @Override
                protected void onPostExecute(List<League> ligaList) {
                    mProgressBar.setVisibility(View.GONE);
                    if (flag) {
                        if (ligaList.size() == 0){
                            Toast.makeText(getActivity(), "WITHOUT NETWORK / DATA IS ABSENT", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "CONNECTING TO DATABASE", Toast.LENGTH_SHORT).show();
                        }
                    }
                    mItems = ligaList;
                    setupAdapter();
                }
            }
        }
