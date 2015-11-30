package org.yccheok.recyclerviewtutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content, mainFragment).commitAllowingStateLoss();
        } else {
            this.mainFragment = (MainFragment)this.getSupportFragmentManager().findFragmentById(R.id.content);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // Forget all Android guidelines as in http://developer.android.com/training/implementing-navigation/ancestral.html
                // They tend to destroy and create new JStockFragmentActivity, which
                // is not something I want.
                onBackPressed();
                break;

            case R.id.menu_notify:
                this.mainFragment.sortAndNotifyDataSetChanged();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
