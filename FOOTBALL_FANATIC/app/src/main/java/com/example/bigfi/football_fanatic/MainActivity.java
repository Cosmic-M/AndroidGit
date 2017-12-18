package com.example.bigfi.football_fanatic;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;


public class MainActivity extends AppCompatActivity {
    private FragmentManager mFragmentManager;

    protected Fragment createFragment() {
        return FirstFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    if (mFragmentManager.getBackStackEntryCount() == 0){
                        openQuitDialog();
                    }
                    mFragmentManager.popBackStack();
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void openQuitDialog(){
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(MainActivity.this);
        quitDialog.setTitle(R.string.quit);

        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which){
                finish();
            }
        });

        quitDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which){
            }
        });

        AlertDialog alertDialog = quitDialog.create();
        alertDialog.show();
    }
}
