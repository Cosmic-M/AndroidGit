package com.example.cosmic_m.footballFakts;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Cosmic_M on 13.07.2017.
 */

public class GreetFragment extends Fragment {

    private static final String TAG = "TESTING";

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

    public static Fragment newInstance() {
        return new GreetFragment();
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
        mRecycler = (RecyclerView) view
                .findViewById(R.id.recycler_view_id);
        mRecycler.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        setupAdapter();

        return view;
    }

    private void setupAdapter() {
        if (this.isAdded()) {
            mRecycler.setAdapter(new Adapter(mItems));
        }
    }

    private class GreetHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private League mLeague;
        private TextView mCaptionLeague;
        private ImageView mLeagueImage;
        private LinearLayout mLinearLayout;

        public GreetHolder(View itemView) {
            super(itemView);
            mCaptionLeague = (TextView) itemView.findViewById(R.id.season_id);
            mLeagueImage = (ImageView) itemView.findViewById(R.id.image_id);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout_id);
            mLinearLayout.setOnClickListener(this);
        }


        public void bindItem(League item) {
            mLeague = item;
            mCaptionLeague.setText(item.getCaption());
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
            LeagueStandingFragment lsf = new LeagueStandingFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", mLeague.getId());
            bundle.putInt("numberOfTeams", mLeague.getTeamsCount());
            bundle.putString("leagueCaption", mLeague.getCaption());
            lsf.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,  lsf)
                    .addToBackStack("").commit();
        }
    }

        private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

            private List<League> mItems;

            public Adapter(List<League> leagues) {
                mItems = leagues;
            }

            @Override
            public int getItemViewType(int position) {
                if (true) {
                    return 0;
                } else {
                    return 1;
                }
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                RecyclerView.ViewHolder viewHolder = null;
                LayoutInflater inflater;
                View view;

                switch (viewType) {
                    case 0:
                        inflater = LayoutInflater.from(getActivity());
                        view = inflater.inflate(R.layout.season_item, viewGroup, false);
                        viewHolder = new GreetHolder(view);
                        break;
                    case 1:
                        inflater = LayoutInflater.from(getActivity());
                        view = inflater.inflate(R.layout.container_item_recycler_view, viewGroup, false);
                        break;
                }
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                switch (this.getItemViewType(position)) {
                    case 0:
                        GreetHolder greetHolder = (GreetHolder) holder;
                        League league = mItems.get(position);
                        greetHolder.bindItem(league);
                        break;
                    case 1:
                        break;
                }
            }

            @Override
            public int getItemCount() {
                return mItems.size();
            }
        }

            @Override
            public void onDestroy() {
                super.onDestroy();
            }

            @Override
            public void onDestroyView() {
                super.onDestroyView();
            }

            @Override
            public void onDetach(){
                super.onDetach();
                Log.i(TAG, "onDetach()");
            }

            private class A_Task extends AsyncTask<Void, Void, List<League>> {
                private boolean flag;

                @Override
                protected List<League> doInBackground(Void... params) {
                    Map<String, League> map;
                    try {
                    Calendar calendar = Calendar.getInstance();
                    int currentYear = calendar.get(Calendar.YEAR);
                    int previousYear = calendar.get(Calendar.YEAR) - 1;
                    String linkCurYear = "http://api.football-data.org/v1/competitions/" + "?season=" + currentYear;
                    String linkPrevYear = "http://api.football-data.org/v1/competitions/" + "?season=" + previousYear;
                    map = new APILoader().getAllSupportLeagues(linkCurYear, getActivity());
                    map.putAll(new APILoader().getAllSupportLeagues(linkPrevYear, getActivity()));
                    SingletonLeague.getSingleton(getActivity()).insertListOfLeague(map);
                    }
                    catch (IOException e) {
                        Log.i(TAG, "IOException worked :" + e.getMessage());
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
                    return list;
                }

                @Override
                protected void onPostExecute(List<League> ligaList) {
                    if (flag) {
                        Toast.makeText(getActivity(), "CONNECTING TO DATABASE", Toast.LENGTH_SHORT).show();
                    }
                    mItems = ligaList;
                    setupAdapter();
                }
            }
        }
