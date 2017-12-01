package me.thesis.mtd_app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "DBHandler";
    private static final int DATABASE_VERSION   = 5;
    private static final String DATABASE_NAME   = "mtd";
    private static final String TABLE_USERWORDS = "userwords";
    private static final String TABLE_WORDS     = "words";
    private static final String KEY_WORD        = "word";
    private static final String KEY_DEFN        = "defn";
    private static final String KEY_FAVORITE    = "favorite";
    private static final String KEY_LANGUAGE    = "language";
    private static final String KEY_LOOKUP      = "lookup";
    private static final String TABLE_USERS     = "users";
    private static final String KEY_USERNAME    = "username";
    private static final String KEY_PASSWORD    = "password";
//    private static final String KEY_PHONETIC      = "phonetic";

    private static final String CREATE_WORD_TABLE=
            "CREATE TABLE "+ TABLE_WORDS + " ( " +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    KEY_WORD    + " TEXT NOT NULL," +
                    KEY_DEFN    + " TEXT NOT NULL," +
                    KEY_LANGUAGE+ " TEXT NOT NULL," +
                    KEY_FAVORITE+ " INTEGER DEFAULT 0,"+
                    KEY_LOOKUP  + " INTEGER DEFAULT 0)";

    private static final String CREATE_USER_TABLE=
            "CREATE TABLE "+ TABLE_USERS + " ( " +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    KEY_USERNAME    + " TEXT NOT NULL," +
                    KEY_PASSWORD    + " TEXT NOT NULL)";

    private static final String CREATE_USERWORD_TABLE=
            "CREATE TABLE "+ TABLE_USERWORDS + " ( " +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    KEY_WORD    + " TEXT NOT NULL," +
                    KEY_DEFN    + " TEXT NOT NULL," +
                    KEY_LANGUAGE+ " TEXT NOT NULL," +
                    KEY_FAVORITE+ " INTEGER DEFAULT 0,"+
                    KEY_LOOKUP  + " INTEGER DEFAULT 0)";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.d("mtd-app","at create db");
        db.execSQL(CREATE_WORD_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_USERWORD_TABLE);
        Log.d("mtd-app","********************suceess at create db");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
       Log.d("mtd-app","here at upgrade");
        db.execSQL("DROP TABLE " + TABLE_WORDS);
        db.execSQL("DROP TABLE " + TABLE_USERS);
        db.execSQL("DROP TABLE " + TABLE_USERWORDS);
        onCreate(db);
    }

    public boolean addWord(String word, String def, int fav, String lang, int lookup) {
        Cursor data = getData(word);
        if(data.getCount() == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_WORD, word);
            values.put(KEY_DEFN, def);
            values.put(KEY_FAVORITE, fav);
            values.put(KEY_LANGUAGE, lang);
            values.put(KEY_LOOKUP, lookup);

            Log.i("mtd-app", "add data: ADDING "+word+" : "+def+" : "+fav+" : "+lang+" to "+TABLE_WORDS);
            long result = db.insert(TABLE_WORDS, null, values);

            db.close();
            if(result == -1) {
                return false;
            }else {
                return true;
            }
        } else{
            Log.i("mtd-app", "already inside database");
            return false;
        }
    }

    public boolean addUserWord(String word, String def, int fav, String lang, int lookup) {
        Cursor data = getData(word);
        if(data.getCount() == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_WORD, word);
            values.put(KEY_DEFN, def);
            values.put(KEY_FAVORITE, fav);
            values.put(KEY_LANGUAGE, lang);
            values.put(KEY_LOOKUP, lookup);

            Log.i("mtd-app", "add data: ADDING "+word+" : "+def+" : "+fav+" : "+lang+" to "+TABLE_USERWORDS);
            long result = db.insert(TABLE_USERWORDS, null, values);

            db.close();
            if(result == -1) {
                return false;
            }else {
                return true;
            }
        } else{
            Log.i("mtd-app", "already inside database");
            return false;
        }
    }

    public boolean addUser(String username, String password) {
        Cursor data = getUsersData(username);
        if(data.getCount() == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_USERNAME, username);
            values.put(KEY_PASSWORD, password);

            Log.i("mtd-app", "add data: ADDING username: " + username + " password: " + password);
            long result = db.insert(TABLE_USERS, null, values);

            db.close();
            if(result == -1) {
                return false;
            }else {
                return true;
            }
        } else{
            Log.i("mtd-app", "already inside database");
            return false;
        }
    }

    public Cursor getData(String word){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_WORDS + " WHERE word = '" + word + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getUserWordsData(String word){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERWORDS + " WHERE word = '" + word + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getUsersData(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE username = '" + username + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public ArrayList<String> searchWords(String condition) {
        String query="SELECT * FROM "+TABLE_WORDS+" WHERE "+condition+" ASC";
        SQLiteDatabase db=getWritableDatabase();
        ArrayList<String> list=new ArrayList<String>();

        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();

        while(!c.isAfterLast()) {
            Word w=new Word(c);
            list.add(w.getWord());
            c.moveToNext();
        }
        db.close();
        return list;
    }

    public void updateFavorite(Word word, int i) {
        Log.d("mtd-app","updating word="+i);
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Word.FAVORITE,i);
        db.update(Word.TABLE_NAME,values,Word.ID+"=?",
                new String[]{Integer.toString(word.getID())});
        Log.d("mtd-app","updated bish");
    }

    public void updateLookup(Word word, int i) {
        Log.d("mtd-app","updating lookup");
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Word.LOOKUP,i);
        db.update(Word.TABLE_NAME,values,Word.ID+"=?",
                new String[]{Integer.toString(word.getID())});
        Log.d("mtd-app","updated lookup");
    }

    public Cursor getFavoriteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_WORDS + " ORDER BY favorite DESC LIMIT 10";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getRecentData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_WORDS + " ORDER BY lookup DESC LIMIT 10";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}