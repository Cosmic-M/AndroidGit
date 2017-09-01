package com.project.cosmic_m.football_facts;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.cosmic_m.football_facts.database.SchemaDB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class LeagueStandingFragment extends Fragment {
    private boolean flag;
    private RecyclerView mRecyclerView;
    private List<TeamStanding.Standing> mItems = new ArrayList<>();
    private int mIdLeague;
    private String mLeagueCaption;
    private String mLeagueCaptionWithoutYear;
    private ProgressBar mProgressBar;
    private final int TASK_CODE = 1;
    private final static String FLAG_PROGRESS_BAR = "flag";
    public final static int STATUS_WITHOUT_NETWORK = 200;
    public final static int STATUS_FINISH = 100;
    public final static int STATUS_FINISH_WITHOUT_DATA = 300;
    private PendingIntent mPendingIntent;
    public final static String LINK = "link";
    public final static String LEAGUE_CAPTION = "leagueCaption";
    public final static String LEAGUE_ID = "leagueId";
    public final static String PARAM_PEN_INTENT = "pendingIntent";

    public static LeagueStandingFragment newInstance(){
        return new LeagueStandingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mIdLeague = bundle.getInt("id");
            mLeagueCaption = bundle.getString("leagueCaption");
        }
        int index = mLeagueCaption.lastIndexOf("20");
        mLeagueCaptionWithoutYear = mLeagueCaption.substring(0, index).trim();
        String link = "http://api.football-data.org/v1/competitions/"
                        + String.valueOf(mIdLeague)+ "/leagueTable/";
        mPendingIntent = getActivity().createPendingResult(TASK_CODE, new Intent(), 0);
        Intent intent = TaskService.newInstance(getContext());
        intent.putExtra(LINK, link);
        intent.putExtra(LEAGUE_CAPTION, mLeagueCaption);
        intent.putExtra(LEAGUE_ID, mIdLeague);
        intent.putExtra(PARAM_PEN_INTENT, mPendingIntent);
        getActivity().startService(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        flag = true;
        mProgressBar.setVisibility(View.GONE);
        if (resultCode == STATUS_FINISH_WITHOUT_DATA) {
            Toast.makeText(getActivity(), "DATA IS ABSENT", Toast.LENGTH_SHORT).show();
        }
        if (resultCode == STATUS_FINISH) {
            switch (requestCode) {
                case TASK_CODE:
                    TeamStanding ts = SingletonLeague.getSingleton(getActivity())
                            .getTeamsStandingMap(SchemaDB.TeamStandingTable.Cols.LEAGUE_CAPTION + " =? ",
                                    new String[]{mLeagueCaption}).get(mLeagueCaption);
                    sharedMethodForRenewAdapter(ts);
                    break;
            }
        }
        if (resultCode == STATUS_WITHOUT_NETWORK) {
            Toast.makeText(getActivity(), "WITHOUT_NETWORK", Toast.LENGTH_SHORT).show();
            TeamStanding ts = SingletonLeague.getSingleton(getActivity())
                        .getTeamsStandingMap(SchemaDB.TeamStandingTable.Cols.LEAGUE_CAPTION + " =? ",
                                new String[]{mLeagueCaption}).get(mLeagueCaption);
            if (ts != null) {
                sharedMethodForRenewAdapter(ts);
            }
            else{
                Toast.makeText(getActivity(), "DATA IS ABSENT", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sharedMethodForRenewAdapter(TeamStanding ts){
        Collection<TeamStanding.Standing> collection = ts.getStanding().values();
        Comparator comparator;
        Set<TeamStanding.Standing> set;
        if (mLeagueCaptionWithoutYear.equals("Champions League")) {
            String[] group = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
            comparator = new ComparatorForGroupStage(getActivity());
            set = new TreeSet<>(comparator);
            for (String someGroup : group) {
                TeamStanding.Standing standing = new TeamStanding().createNewStandingClass();
                standing.setGroup(someGroup);
                standing.setPts("19");//comparator set this item in head of group
                standing.setGoalsDifference("0");//not important because... watch previous string
                set.add(standing);
            }
        } else {
            comparator = new CustomComparator();
            set = new TreeSet<>(comparator);
        }
        set.addAll(collection);
        mItems = new ArrayList<>(set);
        setupAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_standing_teams, container, false);
        mRecyclerView = (RecyclerView) view
                .findViewById(R.id.fragment_standing_teams_id);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        setupAdapter();
        if (savedInstanceState != null){
            flag = savedInstanceState.getBoolean(FLAG_PROGRESS_BAR);
        }
        if(flag == false && mItems.size()==0){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onStop(){
        super.onStop();
        flag = false;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(FLAG_PROGRESS_BAR, flag);
    }

    private void setupAdapter() {
        if (this.isAdded()) {
            mRecyclerView.setAdapter(new TeamAdapter(mItems));
        }
    }

    private class SeparatorHolder extends RecyclerView.ViewHolder{
        private TextView mGroup;

        public SeparatorHolder(View itemView){
            super(itemView);
            mGroup = (TextView) itemView.findViewById(R.id.group_id);
        }

        public void bindItem(TeamStanding.Standing item){
            mGroup.setText("GROUP: " + item.getGroup());
        }
    }

    private class StandingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mPosition;
        private ImageView mEmblem;
        private TextView mTeam;
        private String mTeamId;
        private TextView mWinGames;
        private TextView mDrawGames;
        private TextView mLossGames;
        private TextView mPlayedGames;
        private TextView mGoalScoredAndMissedGoals;
        private TextView mPts;
        private ImageView preOneMatchDay;
        private ImageView preTwoMatchDay;
        private ImageView preThreeMatchDay;
        private ImageView preFourMatchDay;
        private ImageView preFiveMatchDay;

        public StandingHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTeam = (TextView) itemView.findViewById(R.id.team_name);
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
            mPts = (TextView) itemView.findViewById(R.id.points);
            preOneMatchDay = (ImageView)itemView.findViewById(R.id.matchDay1);
            preTwoMatchDay = (ImageView)itemView.findViewById(R.id.matchDay2);
            preThreeMatchDay = (ImageView)itemView.findViewById(R.id.matchDay3);
            preFourMatchDay = (ImageView)itemView.findViewById(R.id.matchDay4);
            preFiveMatchDay = (ImageView)itemView.findViewById(R.id.matchDay5);
        }

        public void bindItem(TeamStanding.Standing item) {
            mTeamId = item.getTeamId();
            mTeam.setText(item.getTeam());
            mPosition.setText(item.getRank());
            mPlayedGames.setText(item.getPlayedGames());
            mWinGames.setText(item.getWinGames());
            mDrawGames.setText(item.getDrawGames());
            mLossGames.setText(item.getLossGames());
            Drawable drawable = SingletonLeague.getSingleton(getActivity()).getEmblemDrawable(item.getTeamId());
            mEmblem.setImageDrawable(drawable);
            mGoalScoredAndMissedGoals.setText(item
                    .getGoals() + "-" + item.getGoalsAgainst());
            mPts.setText(item.getPts());
            int[] mass = item.getMassOfLastResults();
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
        public void onClick(View view){
            ListAllMatchesOfTeamFragment listAllMatchesOfTeamFragment = ListAllMatchesOfTeamFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("team_id", mTeamId);
            bundle.putString("league_id", String.valueOf(mIdLeague));
            bundle.putString("leagueCaption", mLeagueCaption);
            listAllMatchesOfTeamFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,  listAllMatchesOfTeamFragment)
                    .addToBackStack("").commit();
        }
    }

    private class TeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<TeamStanding.Standing> mStandings;

        public TeamAdapter(List<TeamStanding.Standing> standings) {
            mStandings = standings;
        }

        @Override
        public int getItemViewType(int position){
            if ((position % 5) == 0 && mLeagueCaptionWithoutYear.equals("Champions League")){
                return 0;
            }
            else{
                return 1;
            }
        }

        @Override
        public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater;
            View view;

            switch (viewType) {
                case 0:
                    inflater = LayoutInflater.from(getActivity());
                    view = inflater.inflate(R.layout.separator_group_item, viewGroup, false);
                    viewHolder =  new SeparatorHolder(view);
                    break;
                case 1:
                    inflater = LayoutInflater.from(getActivity());
                    view = inflater.inflate(R.layout.container_item_recycler_view, viewGroup, false);
                    viewHolder =  new StandingHolder(view);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (this.getItemViewType(position)) {
                case 0:
                    SeparatorHolder separatorHolder = (SeparatorHolder) holder;
                    TeamStanding.Standing tS = mStandings.get(position);
                    separatorHolder.bindItem(tS);
                    break;
                case 1:
                    StandingHolder standingHolder = (StandingHolder) holder;
                    TeamStanding.Standing teamStanding = mStandings.get(position);
                    if (mLeagueCaptionWithoutYear.equals("Champions League")) {
                        teamStanding.setRank(String.valueOf(position % 5));
                    }
                    standingHolder.bindItem(teamStanding);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mStandings.size();
        }
    }
}
