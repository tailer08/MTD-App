package me.thesis.mtd_app.db;

import android.database.Cursor;
import android.provider.BaseColumns;

public class Phonetic {

    public static final String TABLE_NAME="phonetic";

    public static final String ID= BaseColumns._ID;
    public static final String WORD="word";
    public static final String PHONETIC="phonetic";

    private int _id;
    private String _word;
    private String _phonetic;

    public Phonetic(Cursor c) {
        this._id=c.getInt(c.getColumnIndex(ID));
        this._word=c.getString(c.getColumnIndex(WORD));
        this._phonetic=c.getString(c.getColumnIndex(PHONETIC));
    }

    public String getPhonetic() {
        return _phonetic;
    }

    public String getWord() {
        return _word;
    }
}