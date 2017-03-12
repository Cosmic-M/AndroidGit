/*
 *5.03.2017
 */

package com.bignerdranch.android.memory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


abstract class SingleFragmentActivity extends AppCompatActivity {

    abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_activity);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_container_id);
        if (fragment == null){
            fragment = createFragment();
            fm.beginTransaction().add(R.id.activity_container_id, fragment).commit();
        }
    }
}
