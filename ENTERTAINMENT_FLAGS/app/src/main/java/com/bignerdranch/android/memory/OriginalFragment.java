/*
 *5.03.2017
 */

package com.bignerdranch.android.memory;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public abstract class OriginalFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "quantity_of_pictures";
    private TextView mTimer;
    private ImageView mSoundSwitcher;
    private TextView mScore;
    private Activity mActivity;
    private RecyclerView mRecyclerView;
    private List<Picture> mPictures;
    private boolean registerWhichStartingAsyncTask;
    private Picture previousPicture;
    private int notMoreTwoDifferentPicture;
    private MemoryAdapter mAdapter;
    private int quantityOfPictures;
    private int limitLevelPoint;
    private SoundBox mSoundBox;
    private List<Sound> mSoundList = new ArrayList<>();

    abstract void timerCount(Activity activity, TextView timer);
    abstract void setVisibilityForScore(TextView score);
    abstract void finishStage(Activity activity);

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        quantityOfPictures = getActivity().getIntent().getIntExtra(EXTRA_MESSAGE, 42);
        limitLevelPoint = Results.getPoints() + quantityOfPictures / 2 * 10;
        mSoundBox = SoundBox.getSoundBox(getContext());
        mSoundList = mSoundBox.getSounds();
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mActivity = getActivity();
        View view = inflater.inflate(R.layout.container_for_recycler_view, container, false);
        mTimer = (TextView) view.findViewById(R.id.timer);
        mScore = (TextView) view.findViewById(R.id.score);
        mTimer.setTypeface(Typeface.SERIF);
        mScore.setTypeface(Typeface.SERIF);
        mScore.setText(String.valueOf(Results.getPoints()));

        mSoundSwitcher = (ImageView) view.findViewById(R.id.sound_on_or_off_id);
        ColorFilter filter = new LightingColorFilter(R.color.score_and_timer, Color.CYAN);
        mSoundSwitcher.setColorFilter(filter);

        if (mSoundBox.getSoundPower() > 0){
            mSoundSwitcher.setImageResource(R.drawable.ic_action_sound_on);
        }
        else{
            mSoundSwitcher.setImageResource(R.drawable.ic_action_sound_off);
        }

        mSoundSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSoundBox.getSoundPower() > 0) {
                    mSoundSwitcher.setImageResource(R.drawable.ic_action_sound_off);
                    mSoundBox.switchOffSound();
                }
                else{
                    mSoundSwitcher.setImageResource(R.drawable.ic_action_sound_on);
                    mSoundBox.switchOnSound();
                }
            }
        });

        timerCount(mActivity, mTimer);
        setVisibilityForScore(mScore);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_rv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 6));
        mPictures = HoldPictures.getHoldPictures(getContext(), quantityOfPictures).getListPictures();
        setupAdapter();
        mAdapter = new MemoryAdapter(mPictures);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private void setupAdapter() {
        if (isAdded()) mRecyclerView.setAdapter(mAdapter);
    }

    private class MemoryPictureHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        private ImageView mImageView;
        private Picture mPicture;

        MemoryPictureHolder(View itemView){
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view_id);
            mImageView.setBackgroundColor(Color.TRANSPARENT);
            mImageView.setOnTouchListener(this);
        }

        public void bindDataWithView(Picture picture){
            mPicture = picture;
            if (mPicture.isDecided()) {
                mImageView.setVisibility(View.INVISIBLE);
                return;
            }
            if(!mPicture.isOpen()) {
                mImageView.setImageResource(R.drawable.sign_question);
            }
            else {
                mImageView.setImageResource(mPicture.getImageId());
                mImageView.setEnabled(false);
            }
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (notMoreTwoDifferentPicture < 2) {
                    if (!registerWhichStartingAsyncTask) {
                        mSoundBox.play(mSoundList.get(0));
                        mPicture.setOpen(true);
                        view.setEnabled(false);
                        registerWhichStartingAsyncTask = true;
                        notMoreTwoDifferentPicture++;
                        mImageView.setImageResource(mPicture.getImageId());
                        previousPicture = mPicture;
                    } else {
                        mPicture.setOpen(true);
                        view.setEnabled(false);
                        registerWhichStartingAsyncTask = false;
                        notMoreTwoDifferentPicture++;
                        mImageView.setImageResource(mPicture.getImageId());
                        if (mPicture.getImageId() == previousPicture.getImageId()) {
                            Toast.makeText(mActivity, mPicture.getCountry(), Toast.LENGTH_SHORT).show();
                            mSoundBox.play(mSoundList.get(1));
                            Results.addPoints(10);
                            int score = Results.getPoints();
                            mScore.setText(String.valueOf(score));
                        } else {
                            mSoundBox.play(mSoundList.get(0));
                        }
                        new IfTwoPictureBecameVisible().execute(previousPicture, mPicture);
                    }
                }
            }
            return false;
        }//public void onClick(View view)
    }

    private class MemoryAdapter extends RecyclerView.Adapter<MemoryPictureHolder>{
        private List<Picture> mPictures;

        public MemoryAdapter(List<Picture> pictures){
            mPictures = pictures;
        }

        @Override
        public MemoryPictureHolder onCreateViewHolder(ViewGroup container, int itemView){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.item_for_recycler_view, container, false);
            return new MemoryPictureHolder(view);
        }

        @Override
        public void onBindViewHolder(MemoryPictureHolder memoryPictureHolder, int position){
            Picture picture = mPictures.get(position);
            memoryPictureHolder.bindDataWithView(picture);
        }

        @Override
        public int getItemCount(){
            return mPictures.size();
        }
    }//private class MemoryAdapter extends RecyclerView.Adapter<MemoryPictureHolder>

    private class IfTwoPictureBecameVisible extends AsyncTask<Picture, Void, List<Picture>>{

        @Override
        protected List<Picture> doInBackground(Picture...items){
            Picture pic_one = items[0];
            Picture pic_two = items[1];
            if (pic_one.getImageId() == pic_two.getImageId()) {
                notMoreTwoDifferentPicture = 0;
            }
            try {
                Thread.sleep(900);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pic_one.setOpen(false);
            pic_two.setOpen(false);
            if (pic_one.getImageId() == pic_two.getImageId()){
                pic_one.setDecided(true);
                pic_two.setDecided(true);
            }
            if (pic_one.getImageId() != pic_two.getImageId()) {
                notMoreTwoDifferentPicture = 0;
            }
            return HoldPictures.getHoldPictures(getContext(), quantityOfPictures).getListPictures();
        }//protected List<Picture> doInBackground(Picture...items)


        @Override
        protected void onPostExecute(List<Picture> pictures){
            mPictures = pictures;
            if (limitLevelPoint == Results.getPoints()){
                mSoundBox.play(mSoundList.get(2));
                finishStage(mActivity);
            }
            setupAdapter();
        }
    }
}
