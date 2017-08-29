package com.example.cosmic_m.footballFakts;

        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.KeyEvent;

public class FragmentActivity extends AppCompatActivity{
    public static final String TAG = "TAG";
    private FragmentManager mFragmentManager;
    private final int TASK_CODE_1 = 1;

    protected Fragment createFragment() {
        return GreetFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mFragmentManager = getSupportFragmentManager();
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fragment_container);
        Log.i(TAG, "protected void onCreate(Bundle savedInstanceState)");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        getSupportFragmentManager().getFragments().get(1)
                .onActivityResult(requestCode, resultCode, data);
    }

    private void openQuitDialog(){
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(FragmentActivity.this);
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
