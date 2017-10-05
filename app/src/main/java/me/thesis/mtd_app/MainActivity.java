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
import android.view.View;

import me.thesis.mtd_app.service.MTDService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean isBound=false;

    private ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {}

        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("mtd-app","created app");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().replace(R.id.content_frame,
                new HomeFragment()).commit();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView=navigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.content_frame,
                        new HomeFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
            }});

        Intent intent=new Intent(this,MTDService.class);
        intent.setAction(MTDService.ACTION_INIT_DB);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isBound) {
            bindService(new Intent(this, MTDService.class),
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
            Log.d("mtd-app","on back pressed @ mainactivity");
            if (getFragmentManager().getBackStackEntryCount()>0) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
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
            SearchFragment searchFragment=new SearchFragment();
            b.putString("state","Search");
            searchFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame,
                    searchFragment).addToBackStack(null).commit();
        } else if (id == R.id.nav_recent) {
            SearchFragment searchFragment=new SearchFragment();
            b.putString("state","Recent");
            searchFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame,
                    searchFragment).addToBackStack(null).commit();
        } else if (id == R.id.nav_favorite) {
            SearchFragment searchFragment=new SearchFragment();
            b.putString("state","Favorite");
            searchFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame,
                    searchFragment).addToBackStack(null).commit();
        } else if (id == R.id.nav_wartag) {
            LetterListFragment letterListFragment =new LetterListFragment();
            b.putString("language","Waray");
            letterListFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame,
                    letterListFragment).addToBackStack(null).commit();
        } else if (id == R.id.nav_tagwar) {
            LetterListFragment letterListFragment =new LetterListFragment();
            b.putString("language","Tagalog");
            letterListFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame,
                    letterListFragment).addToBackStack(null).commit();
        } else if (id == R.id.nav_about) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame,
                    new AboutFragment()).addToBackStack(null).commit();
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
    }
}