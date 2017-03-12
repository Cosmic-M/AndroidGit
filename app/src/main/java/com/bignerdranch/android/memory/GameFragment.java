/*
 *5.03.2017
 */

package com.bignerdranch.android.memory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.TextView;

public class GameFragment extends OriginalFragment {
    private CountDownTimer mCountDownTimer;

    public static Intent newIntent(Context context, int allPics) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(EXTRA_MESSAGE, allPics);
        return intent;
    }

    @Override
    void timerCount(final Activity activity, final TextView timer) {
        mCountDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText("" + (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                activity.finish();
            }
        }.start();
    }

    @Override
    void setVisibilityForScore(TextView score) {
        ///visibility without change;
    }

    @Override
    void finishStage(Activity activity) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            getActivity().finish();
        }
    }
}
