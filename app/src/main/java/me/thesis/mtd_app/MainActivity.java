package me.thesis.mtd_app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import me.thesis.mtd_app.service.MTDService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MTDService mService;
    private boolean isBound=false;

    private ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            if (service.toString().equals("MTD")) {
                mService=((MTDService.LocalBinder)service).getService();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) { mService=null;}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("mtd-app","created app");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent=new Intent(this,MTDService.class);
        intent.setAction(MTDService.ACTION_INIT_DB);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isBound) {
            bindService(new Intent(MainActivity.this, MTDService.class),
                    mConnection,
                    Context.BIND_AUTO_CREATE);
            isBound=true;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle b=new Bundle();

        if (id == R.id.nav_search) {
            WordListFragment wordListFragment =new WordListFragment();
            b.putString("state","Search");
            wordListFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, wordListFragment).commit();
        } else if (id == R.id.nav_favorite) {
            WordListFragment wordListFragment =new WordListFragment();
            b.putString("state","Favorite");
            wordListFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, wordListFragment).commit();
        } else if (id == R.id.nav_wartag) {
            LetterListFragment letterListFragment =new LetterListFragment();
            b.putString("language","Waray");
            letterListFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, letterListFragment).commit();
        } else if (id == R.id.nav_tagwar) {
            LetterListFragment letterListFragment =new LetterListFragment();
            b.putString("language","Tagalog");
            letterListFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, letterListFragment).commit();
        } else if (id == R.id.nav_about) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame,new AboutFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        if (isBound) {
            isBound=false;
            unbindService(mConnection);
        }

        super.onDestroy();
        Log.d("mtd-app","here bruh");
    }
}