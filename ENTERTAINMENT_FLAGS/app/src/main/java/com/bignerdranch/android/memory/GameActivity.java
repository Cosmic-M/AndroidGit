/*
 *5.03.2017
 */

package com.bignerdranch.android.memory;

import android.support.v4.app.Fragment;

public class GameActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new GameFragment();
    }
}
