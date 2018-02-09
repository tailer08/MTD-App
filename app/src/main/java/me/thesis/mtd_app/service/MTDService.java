package me.thesis.mtd_app.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import me.thesis.mtd_app.db.DBHandler;

public class MTDService extends IntentService {

    public static final String ACTION_INIT_DB="me.thesis.mtd_app.service.action.INIT_DB";
    public static final String ACTION_LOG_IN="me.thesis.mtd_app.service.action.LOG_IN";
    public static final String ACTION_EDIT_WORD="me.thesis.mtd_app.service.action.EDIT_WORD";
    public static final String ACTION_DELETE_WORD="me.thesis.mtd_app.service.action.DELETE_WORD";

    public static final String DISPLAY_HOME="me.thesis.mtd_app.service.DISPLAY_HOME";
    public static final String CHECK_ADMIN="me.thesis.mtd_app.service.CHECK_ADMIN";

    public static final String EXTRA_STATE="me.thesis.mtd_app.service.extra.STATE";

    private final IBinder mBinder=new LocalBinder();
    private DBHandler dbHandler=null;

    private boolean isLoggedIn=false;

    private void initWord() {
        try {
            final InputStream file=getAssets().open("dictionary.txt");
            BufferedReader  reader=new BufferedReader(new InputStreamReader(file));
            String line;
            while((line=reader.readLine())!=null) {
                String[] tokens=line.split(" , ");
                addWord(tokens[0],
                        tokens[1],
                        tokens[2]);
            }
        } catch(IOException e) {
            Log.d("mtd-app","cannot open dictionary file");
        }
    }

    private void initPhonetic() {
        try {
            final InputStream file=getAssets().open("waray words.txt");
            BufferedReader reader=new BufferedReader(new InputStreamReader(file));
            String line;
            while ((line=reader.readLine())!=null) {
                String[] tokens=line.split(" , ");
                addPhonetic(tokens[0],tokens[1]);
            }
        } catch (IOException e) {
            Log.d("mtd-app","cannot open phonetic file");
        }
    }

    private void addWord(String word, String defn, String gif){
        dbHandler.addWord(word,defn,"Tagalog",0, gif);
    }

    private void addPhonetic(String word, String phonetic) {
        dbHandler.addPhonetic(word,phonetic);
    }

    public MTDService() {
        super("MTDService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent==null) {
            return;
        }

        if (intent.getAction().equals(ACTION_INIT_DB)) {
            dbHandler=new DBHandler(this);

            if (dbHandler.getDBCount("words") ==0) {
                initWord();
            }

            if (dbHandler.getDBCount("phonetic") == 0 ) {
//                initPhonetic();
            }

            Intent i=new Intent(DISPLAY_HOME);
            sendBroadcast(i);
        } else if (intent.getAction().equals(ACTION_LOG_IN)) {
            isLoggedIn=!isLoggedIn;
            Log.d("mtd-app","Logged in in service");
        } else if (intent.getAction().equals(ACTION_EDIT_WORD) ||
            intent.getAction().equals(ACTION_DELETE_WORD)) {
            Intent i=new Intent(CHECK_ADMIN);

            if (isLoggedIn) {
                i.putExtra("status","ok_"+intent.getStringExtra(EXTRA_STATE));
            } else {
                i.putExtra("status","no");
            }
            sendBroadcast(i);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public MTDService getService() {
            return MTDService.this;
        }

        public String toString() {
            return "MTD";
        }
    }

    public DBHandler getDBHandler() {return dbHandler;}
}
