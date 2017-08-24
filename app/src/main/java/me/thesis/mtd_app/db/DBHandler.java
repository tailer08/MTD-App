package me.thesis.mtd_app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION   = 1;
    private static final String DATABASE_NAME   = "mtd";
    private static final String TABLE_WORDS     = "words";
    private static final String KEY_ID          = "id";
    private static final String KEY_WORD        = "word";
    private static final String KEY_DEFN        = "defn";
    private static final String KEY_FAVORITE    = "favorite";
    private static final String KEY_LOOKUP      = "lookup";

//    private static final String TAG="MTD DB";
    private static final String CREATE_WORD_TABLE=
            "create table "+ TABLE_WORDS + " ("+
                    KEY_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    KEY_WORD    + " TEXT NOT NULL," +
                    KEY_DEFN    + " TEXT NOT NULL," +
                    KEY_FAVORITE+ " INTEGER DEFAULT 0," +
                    KEY_LOOKUP  + " INTEGER DEFAULT 0)";
//    private static final String DATABASE_INSERT=
//            "INSERT INTO " +Word.TABLE_NAME+" ("+
//                    Word.WORD +","+Word.DEFN +") VALUES ";
//    private Context mContext=null;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_WORD_TABLE);
//        db.execSQL(DATABASE_INSERT+"('Aso','n.\nayam')");
//        db.execSQL(DATABASE_INSERT+"('Ginhihigugma','v.\nminamahal')");
//        db.execSQL(DATABASE_INSERT+"('Misay','n.\npusa'");
//        db.execSQL(DATABASE_INSERT+"('Ngayon','adv.\nyana'");
//        db.execSQL(DATABASE_INSERT+"('Lidong','n.\nbilog'");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Word.TABLE_NAME);
        onCreate(db);
    }

    public void addWord(String word, String def, int fav, int look) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v=new ContentValues();

        v.put("word", word);
        v.put("defn", def);
        v.put("favorite", fav);
        v.put("loopup", look);

        db.insert(TABLE_WORDS, null, v);
        db.close(); // Closing database connection
    }

    public List<Word> getAllWords(){
        List<Word> wordList = new ArrayList<Word>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_WORDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Word word = new Word( cursor.getString(0) , cursor.getString(1),
                        Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(0)));
// Adding contact to list
                wordList.add(word);
            } while (cursor.moveToNext());
        }

// return contact list
        return wordList;
    }
    public Word getWord(String word) {
        String query="SELECT * FROM "+ Word.TABLE_NAME +
                " WHERE "+ Word.WORD + "=\"" + word + "\"";

        SQLiteDatabase db=getWritableDatabase();
        Word w=null;

        Cursor cursor=db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            w=new Word(cursor);
            cursor.close();
        }
        db.close();
        return w;
    }

    public ArrayList<String> getRecent() {
        String query="SELECT * FROM "+Word.TABLE_NAME+
                " ORDER BY "+Word.LOOKUP+" DESC LIMIT 15";

        SQLiteDatabase db=getWritableDatabase();
        ArrayList<String> list=new ArrayList<String>();

        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();

        while(!c.isAfterLast()) {
            list.add(c.getString(c.getColumnIndex(Word.WORD)));
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }

    public ArrayList<Word> getFavorite() {
        String query="SELECT * FROM "+Word.TABLE_NAME+
                " WHERE "+Word.FAVORITE+"=1";

        SQLiteDatabase db=getWritableDatabase();
        ArrayList<Word> list=new ArrayList<Word>();

        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();

        while(!c.isAfterLast()) {
            list.add(new Word(c));
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }

    public void updateWord(Word word) {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues v=new ContentValues();
        v.put(Word.WORD,word.getWord());
        v.put(Word.FAVORITE,word.isFavorite());
        v.put(Word.LOOKUP,word.getLookup());

        db.update(Word.TABLE_NAME,v,Word.ID+"=?",
                new String[]{String.valueOf(1)});
    }

    public int getWordsCount() {
        String countQuery = "select * from " + TABLE_WORDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}