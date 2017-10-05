package me.thesis.mtd_app.db;

import android.database.Cursor;
import android.provider.BaseColumns;

public class Word{

    public static final String TABLE_NAME="words";

    public static final String ID= BaseColumns._ID;
    public static final String WORD="word";
    public static final String DEFN="defn";
    public static final String FAVORITE="favorite";
    public static final String LANGUAGE="language";
    public static final String LOOKUP="lookup";
//    public static final String PHONETIC="phonetic";

    private int _id;
    private String _word;
    private String _defn;
    private int _favorite;
    private String _language;
    private int _lookup;
//    private String _phonetic;

    public Word() {}

    public Word(String word, String defn, int favorite, String language, int lookup/*, String phonetic*/) {
        this._word=word;
        this._defn=defn;
        this._favorite=favorite;
        this._language=language;
        this._lookup=lookup;
//        this._phonetic=phonetic;
    }

    public Word(Cursor c) {
        this._id=c.getInt(c.getColumnIndex(ID));
        this._word=c.getString(c.getColumnIndex(WORD));
        this._defn=c.getString(c.getColumnIndex(DEFN));
        this._favorite=c.getInt(c.getColumnIndex(FAVORITE));
        this._language=c.getString(c.getColumnIndex(LANGUAGE));
        this._lookup=c.getInt(c.getColumnIndex(LOOKUP));
//        this._phonetic=c.getString(c.getColumnIndex(PHONETIC));
    }

    public int getID() {
        return _id;
    }

    public String getWord() {
        return _word;
    }

    public String getDefn() {
        return _defn;
    }

    public int getFavorite() {
        return _favorite;
    }

    public int getLookup() { return _lookup; }
//    public String getPhonetic() { return _phonetic; }
}
