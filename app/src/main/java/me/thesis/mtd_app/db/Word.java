package me.thesis.mtd_app.db;

import android.database.Cursor;
import android.provider.BaseColumns;

public class Word{

    public static final String TABLE_NAME="Word";

    public static final String ID= BaseColumns._ID;
    public static final String WORD="word";
    public static final String DEFN="defn";
    public static final String FAVORITE="favorite";
    public static final String LOOKUP="lookup";

    private int _id;
    private String _word;
    private String _defn;
    private int _favorite;
    private int _lookup;

    public Word() {}

    public Word(String word, String defn, int favorite, int lookup) {
        this._word=word;
        this._defn=defn;
        this._favorite=favorite;
        this._lookup=lookup;
    }

    public Word(Cursor c) {
        this._id=c.getInt(c.getColumnIndex(ID));
        this._word=c.getString(c.getColumnIndex(WORD));
        this._defn=c.getString(c.getColumnIndex(DEFN));
        this._favorite=c.getInt(c.getColumnIndex(FAVORITE));
        this._lookup=c.getInt(c.getColumnIndex(LOOKUP));
    }

    public void setFavorite(int favorite) {
        this._favorite=favorite;
    }

    public void setLookup(int lookup) {
        this._lookup=lookup;
    }

    public String getWord() {
        return _word;
    }

    public String getDefn() {
        return _defn;
    }

    public int isFavorite() {
        return _favorite;
    }

    public int getLookup() {
        return _lookup;
    }

    public int getFavorite() {
        return _favorite;
    }

}
