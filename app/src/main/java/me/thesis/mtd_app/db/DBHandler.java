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
    private static final int DATABASE_VERSION   = 4;
    private static final String DATABASE_NAME   = "mtd";
    private static final String TABLE_WORDS     = "words";
    private static final String KEY_WORD        = "word";
    private static final String KEY_DEFN        = "defn";
    private static final String KEY_FAVORITE    = "favorite";
    private static final String KEY_LANGUAGE      = "language";
//    private static final String KEY_PHONETIC      = "phonetic";

    private static final String CREATE_WORD_TABLE=
            "CREATE TABLE "+ TABLE_WORDS + " ( " +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    KEY_WORD    + " TEXT NOT NULL," +
                    KEY_DEFN    + " TEXT NOT NULL," +
                    KEY_LANGUAGE+ " TEXT NOT NULL," +
                    KEY_FAVORITE+ " INTEGER DEFAULT 0)";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.d("mtd-app","at create db");
        db.execSQL(CREATE_WORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
       Log.d("mtd-app","here at upgrade");
        db.execSQL("DROP IF TABLE EXISTS " + Word.TABLE_NAME);
        onCreate(db);
    }

    public boolean addWord(String word, String def, int fav, String lang) {
        Cursor data = getData(word);
        if(data.getCount() == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_WORD, word);
            values.put(KEY_DEFN, def);
            values.put(KEY_FAVORITE, fav);
            values.put(KEY_LANGUAGE, lang);

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

//    public Cursor getData(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_WORDS;
//        Cursor data = db.rawQuery(query, null);
//        return data;
//    }
//

     private Cursor getData(String word){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_WORDS + " WHERE word = '" + word + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public ArrayList<Word> searchWords(String condition) {
        Log.d("mtd-app","condition="+condition);
        String query="SELECT * FROM "+TABLE_WORDS+" WHERE "+condition+" ORDER BY "+Word.WORD+" ASC";
        SQLiteDatabase db=getWritableDatabase();
        ArrayList<Word> list=new ArrayList<Word>();

        Log.d("mtd-app","querrry="+query);
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();

        while(!c.isAfterLast()) {
            list.add(new Word(c));
            c.moveToNext();
        }
        db.close();
        return list;
    }

    public void updateFavorite(Word word,int i) {
        Log.d("mtd-app","updating word");
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Word.FAVORITE,i);
        db.update(Word.TABLE_NAME,values,Word.ID+"=?",
                new String[]{String.valueOf(1)});
        Log.d("mtd-app","updated bish");
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