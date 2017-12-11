package com.example.bigfi.football_fanatic;

        import android.app.Activity;
        import android.graphics.Color;
        import android.support.v7.widget.RecyclerView.ViewHolder;
        import android.support.v7.widget.RecyclerView.Adapter;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.example.bigfi.football_fanatic.pojo_model.Event;

        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.List;
        import java.util.TimeZone;

/**
 * Created by bigfi on 12.12.2017.
 */

public class LastMatchesAdapter extends Adapter<ViewHolder> {
    private Activity mActivity;
    private List<Event> mEvents;

    public LastMatchesAdapter(Activity activity, List<Event> events) {
        mActivity = activity;
        mEvents = events;
    }

    @Override
    public ResultHolder onCreateViewHolder(ViewGroup viewGroup, int itemView) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_last_confrontation, viewGroup, false);
        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = mEvents.get(position);
        ((ResultHolder) holder).bindEvents(event);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    private class ResultHolder extends ViewHolder {
        private TextView mHomeTeam;
        private ImageView mImageHomeTeam;
        private TextView mScoreOfMatch;
        private ImageView mImageAwayTeam;
        private TextView mAwayTeam;
        private TextView mDateOfMatch;

        ResultHolder(View itemView) {
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

        public void bindEvents(Event event) {
            mHomeTeam.setText(event.getHomeTeamName());
            mAwayTeam.setText(event.getAwayTeamName());

            mScoreOfMatch.setText(event.getResult().getGoalsHomeTeam() + " : " + event.getResult().getGoalsAwayTeam());

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            Date result;
            String dateOfMatch = "";
            try {
                result = df.parse(event.getDate());
                SimpleDateFormat sdfForDate = new SimpleDateFormat("dd MMMM, yyyy");
                SimpleDateFormat sdfForTime = new SimpleDateFormat("HH:mm");
                sdfForDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                sdfForTime.setTimeZone(TimeZone.getTimeZone("GMT"));
                dateOfMatch = sdfForDate.format(result);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            mDateOfMatch.setText(dateOfMatch);
        }
    }
}
