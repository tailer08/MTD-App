package me.thesis.mtd_app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

import me.thesis.mtd_app.db.DBHandler;
import me.thesis.mtd_app.db.Word;
import me.thesis.mtd_app.service.MTDService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    TextToSpeech.OnInitListener {

    private MTDService mService;
    private boolean isBound=false;
    private TextToSpeech tts;

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

        tts=new TextToSpeech(this,this);
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
            WordFragment wordFragment=new WordFragment();
            b.putString("state","Search");
            wordFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame,wordFragment).commit();
        } else if (id == R.id.nav_favorite) {
            WordFragment wordFragment=new WordFragment();
            b.putString("state","Favorite");
            wordFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame,wordFragment).commit();
        } else if (id == R.id.nav_wartag) {
            ListFragment listFragment=new ListFragment();
            b.putString("language","Waray");
            listFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame,listFragment).commit();
        } else if (id == R.id.nav_tagwar) {
            ListFragment listFragment=new ListFragment();
            b.putString("language","Tagalog");
            listFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.content_frame,listFragment).commit();
        } else if (id == R.id.nav_about) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame,new AboutFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void speak(String word) {
        Log.d("mtd-app","beb be alayb");
        tts.speak(word,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            Locale US = tts.getLanguage();
            int result = tts.setLanguage(US);
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.d("mtd", "Language not supported");
            }else{}
        }
    }

    public void editFavorite(Word word, int i) {
        DBHandler dbTemp=mService.getDBHandler();
        dbTemp.updateFavorite(word,i);
    }
}