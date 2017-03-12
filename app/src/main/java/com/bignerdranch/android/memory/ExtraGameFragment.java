/*
 *5.03.2017
 */

package com.bignerdranch.android.memory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.Formatter;

public class ExtraGameFragment extends OriginalFragment{
    private static final String TOTAL_TIME_RESULT = "total_time";
    private CountDownTimer mCountDownTimer;

    public static Intent newIntent(Context context, int allPics){
        Intent intent = new Intent(context, ExtraGameActivity.class);
        intent.putExtra(EXTRA_MESSAGE, allPics);
        return intent;
    }

    @Override
    void timerCount(final Activity activity, final TextView timer) {
        mCountDownTimer = new CountDownTimer(1500000, 1000){
            @Override
            public void onTick(long millisUntilFinished){
                int xXx = (int) Math.abs(1500 - millisUntilFinished / 1000);
                Results.setExtraTime(xXx);
                Formatter formatter = new Formatter();
                String result = String.valueOf(formatter.format("%d:%02d", xXx /60, xXx % 60));
                timer.setText(result);
            }
            @Override
            public void onFinish(){
                activity.finish();
            }
        }.start();
    }

    @Override
    void setVisibilityForScore(TextView score){
        score.setVisibility(View.INVISIBLE);
    }

    @Override
    void finishStage(Activity activity){
        int totalTime = Results.getExtraTime();
        Intent intent = new Intent();
        intent.putExtra(TOTAL_TIME_RESULT, totalTime);
        activity.setResult(Activity.RESULT_OK, intent);
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        activity.finish();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        getActivity().finish();
    }
}