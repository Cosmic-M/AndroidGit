package com.example.cosmic_m.footballFakts;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosmic_m.footballFakts.database.SchemaDB;
import com.example.cosmic_m.footballFakts.database.SchemaDB.TeamStandingTable;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class LeagueStandingFragment extends Fragment {
    private static final String TAG = "TAG";
    private TeamStanding mTeamStanding;
    private Map<String, Event> mEventMap;
    private RecyclerView mPhotoRecyclerView;
    private List<TeamStanding.Standing> mItems = new ArrayList<>();
    private int mIdLeague;
    private int mNumberOfTeams;
    private String mLeagueCaption;
    private String mLeagueCaptionWithoutYear;
    private ProgressBar mProgressBar;
    private View mView;
    public final static String TASK = "task";
    private final int TASK_CODE_1 = 1;
    private final int TASK_CODE_2 = 2;//renew TeamStanding in database
    private final int TASK_CODE_3 = 3;//renew Events in database
    public final static int STATUS_WITHOUT_NETWORK = 200;
    public final static int STATUS_FINISH = 100;
    private PendingIntent mPendingIntent;
    public final static String LINK = "link";
    public final static String LEAGUE_CAPTION = "leagueCaption";
    public final static String LEAGUE_ID = "leagueId";
    public final static String PARAM_PEN_INTENT = "pendingIntent";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mIdLeague = bundle.getInt("id");
            mNumberOfTeams = bundle.getInt("numberOfTeams");
            mLeagueCaption = bundle.getString("leagueCaption");
        }
        int index = mLeagueCaption.lastIndexOf("20");
        mLeagueCaptionWithoutYear = mLeagueCaption.substring(0, index).trim();
        String link = "http://api.football-data.org/v1/competitions/"
                        + String.valueOf(mIdLeague)+ "/leagueTable/";
        mPendingIntent = getActivity().createPendingResult(TASK_CODE_1, new Intent(), 0);
        Intent intent = TaskInService.newInstance(getContext());
        intent.putExtra(LINK, link);
        intent.putExtra(LEAGUE_CAPTION, mLeagueCaption);
        Log.i(TAG, "leagueCaption in LeagueStandingFragment = " + mLeagueCaption);
        intent.putExtra(LEAGUE_ID, mIdLeague);
        intent.putExtra(PARAM_PEN_INTENT, mPendingIntent);
        getActivity().startService(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        mProgressBar.setVisibility(View.GONE);
        if (resultCode == STATUS_FINISH) {
            switch (requestCode) {
                case TASK_CODE_1:
                    TeamStanding ts = SingletonLeague.getSingleton(getActivity())
                            .getTeamsStandingMap(TeamStandingTable.Cols.LEAGUE_CAPTION + " =? ",
                                    new String[]{mLeagueCaption}).get(mLeagueCaption);
                    sharedMethodForRenewAdapter(ts);
                    break;
            }
        }
        if (resultCode == STATUS_WITHOUT_NETWORK) {
            Log.i(TAG, "WITHOUT_NETWORK");
            Toast.makeText(getActivity(), "WITHOUT_NETWORK", Toast.LENGTH_SHORT).show();
            TeamStanding ts = SingletonLeague.getSingleton(getActivity())
                        .getTeamsStandingMap(TeamStandingTable.Cols.LEAGUE_CAPTION + " =? ",
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
            comparator = new ComparatorForGroupStage();
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
        mPhotoRecyclerView = (RecyclerView) view
                .findViewById(R.id.fragment_standing_teams_id);
        mPhotoRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mView = view;
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        setupAdapter();

        return view;
    }

    private void setupAdapter() {
        if (this.isAdded()) {
            mPhotoRecyclerView.setAdapter(new TeamAdapter(mItems));
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
                        images[i].setVisibility(View.INVISIBLE);
                        break;
                }
            }
        }

        @Override
        public void onClick(View view){

            ListAllMatchesOfTeamFragment lamf = new ListAllMatchesOfTeamFragment();
            Bundle bundle = new Bundle();
            bundle.putString("team_id", mTeamId);
            bundle.putString("league_id", String.valueOf(mIdLeague));
            bundle.putString("leagueCaption", mLeagueCaption);
            lamf.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,  lamf)
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

    public class FetchItemsTask extends AsyncTask<Void,Integer,TeamStanding> {
        Map<String, Event> eventMap;
        TeamStanding ts;
        FetchItemsTask mFetchItemsTask = this;
        private int count;

        @Override
        protected void onPreExecute(){
            mProgressBar = (ProgressBar)  mView.findViewById(R.id.progressBar);
            mProgressBar.setProgress(0);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setMax(mNumberOfTeams);
        }

        @Override
        protected void onProgressUpdate(Integer...params){
            count++;
            Log.i(TAG, "recalling " + count);
            mProgressBar.setProgress(count);
        }

        @Override
        protected TeamStanding doInBackground(Void... params) {
            String link;
            link = "http://api.football-data.org/v1/competitions/"
                        + String.valueOf(mIdLeague)+ "/leagueTable/";
            try {
                Map<String, TeamStanding> map = new APILoader().fetchItems(link, getActivity(), String.valueOf(mIdLeague));
                ts = map.get(String.valueOf(mIdLeague));
                TeamStanding.Standing[] standings = new TeamStanding.Standing[ts.getStanding().values().size()];
                int i = 0;
                for (TeamStanding.Standing standing : ts.getStanding().values()){
                    standings[i++] = standing;
                }
                Map<String, Drawable> result = SingletonLeague.getSingleton(getActivity()).getEmblemDrawableMap();
                Future<Map<String, Drawable>> f1 = null;
                Future<Map<String, Drawable>> f2 = null;
                Future<Map<String, Drawable>> f3 = null;
                Future<Map<String, Drawable>> f4 = null;
                Future<Map<String, Drawable>> f5 = null;
                Future<Map<String, Drawable>> f6 = null;
                Future<Map<String, Drawable>> f7 = null;
                Future<Map<String, Drawable>> f8 = null;
                ExecutorService es = Executors.newFixedThreadPool(8);
                if (standings.length <= 20) {
                    f1 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[0], standings[1], standings[2]}));
                    f2 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[3], standings[4], standings[5]}));
                    f3 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[6], standings[7], standings[8]}));
                    f4 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[9], standings[10], standings[11]}));
                    f5 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[12], standings[13]}));
                    f6 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[14], standings[15]}));
                    f7 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[16], standings[17]}));
                    if (standings.length == 20) {
                        f8 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[18], standings[19]}));
                    }
                }
                    else{
                        f1 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[0], standings[1], standings[2], standings[3]}));
                        f2 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[4], standings[5], standings[6], standings[7]}));
                        f3 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[8], standings[9], standings[10], standings[11]}));
                        f4 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[12], standings[13], standings[14], standings[15]}));
                        f5 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[16], standings[17], standings[18], standings[19]}));
                        f6 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[20], standings[21], standings[22], standings[23]}));
                        f7 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[24], standings[25], standings[26], standings[27]}));
                        f8 = es.submit(new EmblemClubFetcher(new TeamStanding.Standing[]{standings[28], standings[29], standings[30], standings[31]}));
                    }

                try {
                    result.putAll(f1.get());
                    result.putAll(f2.get());
                    result.putAll(f3.get());
                    result.putAll(f4.get());
                    result.putAll(f5.get());
                    result.putAll(f6.get());
                    result.putAll(f7.get());
                    result.putAll(f8.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            catch (IOException e) {
                Map<String, TeamStanding> map = SingletonLeague
                        .getSingleton(getActivity()).getTeamsStandingMap(TeamStandingTable.Cols.LEAGUE_CAPTION + " =? ", new String[]{mLeagueCaption});
                ts = map.get(mLeagueCaption);
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String matchDay = ts.getMatchDay();
            link = "http://api.football-data.org/v1/competitions/" +
                    String.valueOf(mIdLeague) + "/fixtures";

            APILoader apiLoader = new APILoader();
            List<Event> listOfEvent = new ArrayList<>();
            try {
                long start = System.currentTimeMillis();
                eventMap = apiLoader.fetchEvents(link);
                SingletonLeague.getSingleton(getActivity()).setEventMap(eventMap);
                listOfEvent = new ArrayList<>(eventMap.values());
                long end = System.currentTimeMillis();
                Log.i(TAG, "result time 1 = " + (end - start));
            } catch (IOException e) {
                eventMap = SingletonLeague.getSingleton(getActivity()).getEventMap(null, null);
                listOfEvent = new ArrayList<>(eventMap.values());
                for (int i = 0; i < listOfEvent.size(); i++){
                    if (Integer.parseInt(listOfEvent.get(i).getCompetitionId()) != mIdLeague){
                        listOfEvent.remove(i--);
                    }
                }
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            long start = System.currentTimeMillis();
            ts = apiLoader.fetchFiveLastResults(ts, listOfEvent, getActivity(), mLeagueCaption, Integer.parseInt(matchDay));
            long end = System.currentTimeMillis();
            Log.i(TAG, "result time 2 = " + (end - start));
            return  ts;
        }

        @Override
        protected void onPostExecute(TeamStanding teamStanding) {
            mProgressBar.setVisibility(View.GONE);
            mEventMap = eventMap;
            mTeamStanding = ts;
            Collection<TeamStanding.Standing> collection = teamStanding.getStanding().values();
            Comparator comparator;
            Set<TeamStanding.Standing> set;
            if (mLeagueCaptionWithoutYear.equals("Champions League")){
                String[] group = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
                comparator = new ComparatorForGroupStage();
                set = new TreeSet<>(comparator);
                for (String someGroup : group){
                    TeamStanding.Standing standing = new TeamStanding().createNewStandingClass();
                    standing.setGroup(someGroup);
                    standing.setPts("19");//comparator set this item in head of group
                    standing.setGoalsDifference("0");//not important because... watch previous string
                    set.add(standing);
                }
            }
            else {
                comparator = new CustomComparator();
                set = new TreeSet<>(comparator);
            }
            set.addAll(collection);
            mItems = new ArrayList<>(set);
            setupAdapter();
//            new InsertAndUpdateTeamStandingInDataBase().execute(mTeamStanding);
//            new InsertAndUpdateEventsInDataBase().execute(mEventMap);
        }
    }

    private class InsertAndUpdateTeamStandingInDataBase extends AsyncTask<TeamStanding,Void,Void> {

        @Override
        protected Void doInBackground(TeamStanding...params){
            SingletonLeague.getSingleton(getActivity()).updateAndInsertTeamStanding(params[0]);
            return null;
        }
    }

    private class InsertAndUpdateEventsInDataBase extends AsyncTask<Map<String, Event>,Void,Void> {

        @Override
        protected Void doInBackground(Map<String, Event>...params){
            SingletonLeague.getSingleton(getActivity()).updateAndInsertEvents(params[0]);
            return null;
        }
    }
}
