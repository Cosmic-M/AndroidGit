package com.example.bigfi.football_fanatic;

        import android.app.Activity;
        import android.graphics.Color;
        import android.graphics.drawable.PictureDrawable;
        import android.net.Uri;
        import android.support.v7.widget.RecyclerView.ViewHolder;
        import android.support.v7.widget.RecyclerView.Adapter;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.bumptech.glide.GenericRequestBuilder;
        import com.bumptech.glide.Glide;
        import com.bumptech.glide.load.engine.DiskCacheStrategy;
        import com.caverock.androidsvg.SVG;
        import com.example.bigfi.football_fanatic.pojo_model.Event;

        import java.io.InputStream;
        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;
        import java.util.TimeZone;

/**
 * Created by bigfi on 12.12.2017.
 */

public class FourthAdapter extends Adapter<ViewHolder> {
    private static final String TAG = "FourthAdapter";
    private Activity mActivity;
    private List<Event> mEvents = new ArrayList<>();
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> mRequestBuilder;

    public void setData(Activity activity, List<Event> events, GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder) {
        Log.d(TAG, "setData() called");
        this.mActivity = activity;
        this.mEvents.clear();
        this.mEvents.addAll(events);
        this.mRequestBuilder = requestBuilder;
        notifyDataSetChanged();
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
            String urlHomeTeam = event.getHomeTeamUrl();
            Uri uri = Uri.parse(urlHomeTeam);
            if (urlHomeTeam.endsWith(".svg")) {
                showIfSVG(uri, mImageHomeTeam);
            }
            else{
                showIfPNG(uri, mImageHomeTeam);
            }
            String urlAwayTeam = event.getAwayTeamUrl();
            uri = Uri.parse(urlAwayTeam);
            if (urlHomeTeam.endsWith(".svg")) {
                showIfSVG(uri, mImageAwayTeam);
            }
            else{
                showIfPNG(uri, mImageAwayTeam);
            }

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

        private void showIfSVG(Uri uri, ImageView view){
            mRequestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .load(uri)
                    .into(view);
        }

        private void showIfPNG(Uri uri, ImageView view){
            Glide
                    .with(mActivity)
                    .load(uri)
                    .into(view);
        }
    }
}
