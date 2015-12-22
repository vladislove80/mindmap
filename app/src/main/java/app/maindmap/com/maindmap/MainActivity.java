package app.maindmap.com.maindmap;

import android.app.ActionBar;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import app.maindmap.com.maindmap.view.FirstFragment;

public class MainActivity extends FragmentActivity {

    private FirstFragment firstFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        /*ActionBar actionBar = getActionBar();
        //actionBar.setDisplayShowHomeEnabled(false); //не показываем иконку приложения
        //actionBar.setDisplayShowTitleEnabled(false); // и заголовок тоже прячем
        //actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_actionbar);*/

        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();
        firstFragment = new FirstFragment();

        transaction = manager.beginTransaction();
        transaction.add(R.id.conteiner, firstFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
