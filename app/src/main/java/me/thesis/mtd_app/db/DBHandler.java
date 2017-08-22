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

    private static final String TAG="MTD DB";

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="mtd";

    private static final String DATABASE_CREATE_WORD=
            "CREATE TABLE "+ Word.TABLE_NAME + " ("+
                    BaseColumns._ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    Word.WORD + " TEXT NOT NULL," +
                    Word.DEFN + " TEXT NOT NULL," +
                    Word.FAVORITE + " INTEGER DEFAULT 0 , "+
                    Word.LOOKUP + "INTEGER DEFAULT 0);";
    private static final String DATABASE_INSERT=
            "INSERT INTO " +Word.TABLE_NAME+" ("+
                    Word.WORD +","+Word.DEFN +") VALUES ";

    private Context mContext=null;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_WORD);
        db.execSQL(DATABASE_INSERT+"('Aso','n.\nayam')");
        db.execSQL(DATABASE_INSERT+"('Ginhihigugma','v.\nminamahal')");
        db.execSQL(DATABASE_INSERT+"('Misay','n.\npusa'");
        db.execSQL(DATABASE_INSERT+"('Ngayon','adv.\nyana'");
        db.execSQL(DATABASE_INSERT+"('Lidong','n.\nbilog'");

        Log.i("mtd","created db");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Word.TABLE_NAME);
        onCreate(db);
    }

    public void addWord(Word word) {
        ContentValues v=new ContentValues();

        v.put(Word.WORD, word.getWord());
        v.put(Word.DEFN, word.getDefn());
        v.put(Word.FAVORITE, word.isFavorite());
        v.put(Word.LOOKUP, word.getLookup());
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
}