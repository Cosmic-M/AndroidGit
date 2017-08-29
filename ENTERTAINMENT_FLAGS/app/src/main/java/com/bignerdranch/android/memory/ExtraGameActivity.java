/*
 *5.03.2017
 */

package com.bignerdranch.android.memory;

import android.support.v4.app.Fragment;

public class ExtraGameActivity extends SingleFragmentActivity {
    @Override
    Fragment createFragment() {
        return new ExtraGameFragment();
    }
}
