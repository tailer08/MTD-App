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
    public static final String DISPLAY_HOME="me.thesis.mtd_app.service.DISPLAY_HOME";

    private final IBinder mBinder=new LocalBinder();
    private DBHandler dbHandler=null;

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
        if (intent!=null && intent.getAction().equals(ACTION_INIT_DB)) {
            dbHandler=new DBHandler(this);

            if (dbHandler.getDBCount("words") ==0) {
                initWord();
            }

            if (dbHandler.getDBCount("phonetic") == 0 ) {
//                initPhonetic();
            }

            Intent i=new Intent(DISPLAY_HOME);
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
