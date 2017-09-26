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

    private final IBinder mBinder=new LocalBinder();
    private DBHandler dbHandler=null;


    private void initDatabase() {
        BufferedReader reader;

        try {
            final InputStream file=getAssets().open("dictionary.txt");
            reader=new BufferedReader(new InputStreamReader(file));
            String line;
            while((line=reader.readLine())!=null) {
                String[] tokens=line.split(" , ");
                addWord(tokens[0], tokens[1],
                        Integer.parseInt(tokens[2]), tokens[3]);
            }
        } catch(IOException e) {
            Log.d("mtd-app","cannot open file");
        }
    }

    private void addWord(String word, String defn, int fav, String lang){
        boolean insertWord = dbHandler.addWord(word,defn,fav,lang);
        if(insertWord){
            Log.i("mtd-app", "Successfully added the word " + word);
        }else{
            Log.i("mtd-app", "UNSUCCESSFUL "+word);
        }
    }

    public MTDService() {
        super("MTDService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && intent.getAction().equals(ACTION_INIT_DB)) {
            dbHandler=new DBHandler(this);
            initDatabase();
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
