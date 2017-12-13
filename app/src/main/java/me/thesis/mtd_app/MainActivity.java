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
    private MTDService mService=null;
    private String s;
    private boolean isLoggedIn;
    private int tick = 0;
    private int tick2 = 0;
    private LoginFragment loginFragment = new LoginFragment();
    private UserWordsFragment userWordsFragment;

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

        this.userWordsFragment = new UserWordsFragment();

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
            WordListFragment wordListFragment=new WordListFragment();
            b.putString("state","Search");
            wordListFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame,
                    wordListFragment).commit();
            isLoggedIn = this.loginFragment.isLoggedIn();
        } else if (id == R.id.nav_recent) {
            WordListFragment wordListFragment=new WordListFragment();
            b.putString("state","Recent");
            wordListFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame,
                    wordListFragment).commit();
            isLoggedIn = this.loginFragment.isLoggedIn();
        } else if (id == R.id.nav_favorite) {
            WordListFragment wordListFragment=new  WordListFragment();
            b.putString("state","Favorite");
            wordListFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame,
                    wordListFragment).commit();
            isLoggedIn = this.loginFragment.isLoggedIn();
        } else if (id == R.id.nav_tagwar) {
            LetterListFragment letterListFragment =new LetterListFragment();
            b.putString("language","Tagalog");
            letterListFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame,
                    letterListFragment).commit();
            isLoggedIn = this.loginFragment.isLoggedIn();
        } else if (id == R.id.nav_userwords) {
            isLoggedIn = this.loginFragment.isLoggedIn();
            Bundle bd = new Bundle();
            if (isLoggedIn) {
                bd.putString("status", "logged in");
            } else {
                bd.putString("status", "logged out");
            }
            if(tick == 0){
                this.userWordsFragment.setArguments(bd);
                tick = 1;
            }else{
                this.userWordsFragment.setLoggedIn(isLoggedIn);
            }
            getFragmentManager().beginTransaction().replace(R.id.content_frame,
                    this.userWordsFragment).commit();
        }else if (id == R.id.nav_about) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame,
                    new AboutFragment()).commit();
        } else if (id == R.id.nav_admin) {
            Bundle bl = new Bundle();
            if(this.loginFragment.isLoggedIn()){
                bl.putString("status","logged in");
            }else{
                bl.putString("status","logged out");
            }
            if(tick2 == 0){
                this.loginFragment.setArguments(bl);
            }else{
                tick2 = 1;
            }
            getFragmentManager().beginTransaction().replace(R.id.content_frame,
                    this.loginFragment).commit();
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