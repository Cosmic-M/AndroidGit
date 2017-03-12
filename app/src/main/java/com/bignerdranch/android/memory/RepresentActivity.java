/*
 *5.03.2017
 */

package com.bignerdranch.android.memory;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;


public class RepresentActivity extends AppCompatActivity {
    private static final String TOTAL_TIME_RESULT = "total_time";
    private static final String SAVED_TEXT = "saved_record";
    private static final String SAVED_TEXT_EXTRA = "saved_extra_record";
    private static final int RESULT_CODE = 1;
    private static final int EXTRA_RESULT_CODE = 2;
    private Button mStartGameButton;
    private Button mStartExtraGameButton;
    private TextView mResultAfterGame;
    private int mStage = 1;
    private final int PICTURES_IN_LINE = 6;
    private SharedPreferences mSharedPreferences;
    private SoundBox mSoundBox;
    private List<Sound> mSoundList = new ArrayList<>();
    private int mTimeResult;

    @Override
    public void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);
        setContentView(R.layout.represent_activity);
        mSoundBox = SoundBox.getSoundBox(this);
        mSoundList = mSoundBox.getSounds();
        mStartGameButton = (Button) findViewById(R.id.start_game_button);
        mStartExtraGameButton = (Button) findViewById(R.id.start_extra_game_button);
        mStartExtraGameButton.setTextColor(Color.argb(95, 0, 0, 0));
        mResultAfterGame = (TextView) findViewById(R.id.result_after_game_textView);
        mSharedPreferences = getPreferences(MODE_PRIVATE);

        renewRecords();

        mStartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSoundBox.play(mSoundList.get(3));
                HoldPictures.setNewListPictures(RepresentActivity.this, PICTURES_IN_LINE * 2);
                Intent intent = GameFragment.newIntent(RepresentActivity.this, PICTURES_IN_LINE * 2);
                startActivityForResult(intent, RESULT_CODE);
            }
        });

        mStartExtraGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSoundBox.play(mSoundList.get(3));
                HoldPictures.setNewListPictures(RepresentActivity.this, PICTURES_IN_LINE * 7);
                Intent intent =
                            ExtraGameFragment.newIntent(RepresentActivity.this, PICTURES_IN_LINE * 7);
                startActivityForResult(intent, EXTRA_RESULT_CODE);
            }
        });
    }//public void onCreate(Bundle savedInstanceSate)

    public void renewRecords(){
        loadRecord();
        loadExtraRecord();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    if (mStage < 6) {
                        ++mStage;
                        HoldPictures.setNewListPictures(this, PICTURES_IN_LINE * (mStage + 1));
                        Intent intent = GameFragment.
                                    newIntent(RepresentActivity.this, PICTURES_IN_LINE * (mStage + 1));
                        startActivityForResult(intent, RESULT_CODE);
                    } else {
                        saveRecord();
                        mResultAfterGame.setText("Game Over, result: " + Results.getPoints());
                        renewRecords();
                    }
                } else {
                    if (Results.getPoints() != 0) {
                        mResultAfterGame.setText("Your result: " + Results.getPoints());
                    }
                    else {
                        mResultAfterGame.setText("");
                    }
                    mStage = 1;
                    saveRecord();
                    Results.resetPoints();
                    HoldPictures.setNewListPictures(this, PICTURES_IN_LINE * (mStage + 1));
                    renewRecords();
                }
                break;
            case EXTRA_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    mTimeResult = data.getIntExtra(TOTAL_TIME_RESULT, 0);
                    saveExtraRecord();
                    Formatter formatter = new Formatter();
                    String result = String.valueOf(formatter.format("%d:%02d",
                            mTimeResult /60, mTimeResult % 60));
                    mResultAfterGame.setText("Your result: " + result);
                    Results.resetPoints();
                    renewRecords();
                }
                else{
                    mResultAfterGame.setText("");
                }
                break;
        }
    }//public void onActivityResult(int requestCode, int resultCode, Intent data)

    public void saveRecord() {
        int currentRecord = mSharedPreferences.getInt(SAVED_TEXT, 0);
        if (currentRecord == 0 || currentRecord < Results.getPoints()) {
            singleSaveRecords(SAVED_TEXT, Results.getPoints());
        }
    }

        public void saveExtraRecord() {
            int currentExtraRecord = mSharedPreferences.getInt(SAVED_TEXT_EXTRA, 0);
            if (currentExtraRecord == 0 || currentExtraRecord > mTimeResult) {
                singleSaveRecords(SAVED_TEXT_EXTRA, mTimeResult);
            }
        }

    void singleSaveRecords(final String mode, int data){
        Editor editor = mSharedPreferences.edit();
        editor.putInt(mode, data);
        editor.commit();
    }

    public void loadRecord() {
        int currentRecord = mSharedPreferences.getInt(SAVED_TEXT, 0);
        if (currentRecord == 0)
            return;
            if (currentRecord > 350){
                mStartExtraGameButton.setEnabled(true);
                mStartExtraGameButton.setTextColor(Color.argb(255, 0, 0, 0));
                mStartExtraGameButton.setText(R.string.start_extra_game);
            }
            mStartGameButton.setText(R.string.start_game);
            SpannableString spannableString = singleLoadRecords(String.valueOf(currentRecord));
            mStartGameButton.append(spannableString);
    }

    public void loadExtraRecord() {
        int currentExtraRecord = mSharedPreferences.getInt(SAVED_TEXT_EXTRA, 0);
        if (currentExtraRecord == 0) {
            return;
        }
        else{
            mStartExtraGameButton.setText(R.string.start_extra_game);
            Formatter formatter = new Formatter();
            String currentRecordAsTimeInMinAndSeconds = String.valueOf(formatter.format("%d:%02d",
                    currentExtraRecord /60, currentExtraRecord % 60));
            SpannableString spannableString = singleLoadRecords(currentRecordAsTimeInMinAndSeconds);
            mStartExtraGameButton.append(spannableString);
        }
    }

    SpannableString singleLoadRecords(String result){
        String smallText = "\nRecord - " + result;
        int lengthOfRecordString = smallText.length();
        SpannableString spannableString = new SpannableString(smallText);
        spannableString.setSpan(new RelativeSizeSpan(0.4f), 0, lengthOfRecordString,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
    }
}
