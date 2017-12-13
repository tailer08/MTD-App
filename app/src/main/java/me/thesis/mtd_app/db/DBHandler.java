package me.thesis.mtd_app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "DBHandler";
    private static final int DATABASE_VERSION   = 7;
    private static final String DATABASE_NAME   = "mtd";
    private static final String TABLE_WORDS     = "words";
    private static final String TABLE_USERS     = "users";
    private static final String TABLE_PHONETIC  = "phonetic";
    private static final String KEY_USERNAME    = "username";
    private static final String KEY_PASSWORD    = "password";

    private static final String CREATE_WORD_TABLE=
            "CREATE TABLE "+ TABLE_WORDS + " ( " +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    Word.WORD    + " TEXT NOT NULL," +
                    Word.DEFN    + " TEXT NOT NULL," +
                    Word.LANGUAGE+ " TEXT NOT NULL," +
                    Word.FAVORITE+ " INTEGER DEFAULT 0,"+
                    Word.LOOKUP  + " INTEGER DEFAULT 0,"+
                    Word.USERWORD+ " INTEGER DEFAULT 0)";

    private static final String CREATE_USER_TABLE=
            "CREATE TABLE "+ TABLE_USERS + " ( " +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    KEY_USERNAME    + " TEXT NOT NULL," +
                    KEY_PASSWORD    + " TEXT NOT NULL)";

    private static final String CREATE_PHONETIC_TABLE=
            "CREATE TABLE "+ TABLE_PHONETIC + " ( " +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    Phonetic.WORD   + " TEXT NOT NULL," +
                    Phonetic.PHONETIC + " TEXT NOT NULL)";

    private int isAvailable=0;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.d("mtd-app","at create db");
        db.execSQL(CREATE_WORD_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PHONETIC_TABLE);
        Log.d("mtd-app","********************suceess at create db");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
       Log.d("mtd-app","here at upgrade");
        db.execSQL("DROP TABLE " + TABLE_WORDS);
        db.execSQL("DROP TABLE " + TABLE_USERS);
        db.execSQL("DROP TABLE " + TABLE_PHONETIC);
        onCreate(db);
    }

    public boolean addWord(String word, String def, String lang, int userWord) {
        Cursor data = getData(word);
        data.moveToFirst();
        if (data.getCount() == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(Word.WORD, word);
            values.put(Word.DEFN, def);
            values.put(Word.FAVORITE, 0);
            values.put(Word.LANGUAGE, lang);
            values.put(Word.LOOKUP, 0);
            values.put(Word.USERWORD, userWord);

           Log.i("mtd-app", "add data: ADDING "+word+" : "+def+" : "+lang+" to "+TABLE_WORDS);
            long result = db.insert(TABLE_WORDS, null, values);

            data.close();
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

            data.close();
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

    public boolean addPhonetic(String word, String phonetic) {
        Cursor data = getData(word);
        if(data.getCount() == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(Phonetic.WORD, word);
            values.put(Phonetic.PHONETIC, phonetic);

            Log.i("mtd-app", "add data: ADDING "+word+" : "+phonetic+" to "+TABLE_PHONETIC);
            long result = db.insert(TABLE_PHONETIC, null, values);

            data.close();
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

//  Get word data from default_words_db
    public Cursor getData(String word){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_WORDS + " WHERE word = '" + word + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public String getPhonetic(String word) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PHONETIC + " WHERE word = '" + word + "'";
        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        Log.d("mtd-app","look for phonetic="+word);
        return (new Phonetic(data)).getPhonetic();
    }

//  Get user data from user_db
    public Cursor getUsersData(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE username = '" + username + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

//  Get default words
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

    public long getDBCount(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, table);
        db.close();
        return cnt;
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
}