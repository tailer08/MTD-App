package me.thesis.mtd_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import me.thesis.mtd_app.db.DBHandler;

public class MainActivity extends AppCompatActivity {

    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHandler db = new DBHandler(this);

        addWord(db, "Aso" , "n.\n ayam", 1, 1);

        Log.i("mtd", "Displaying DB Contents");
        Cursor data = db.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            listData.add(data.getString(1));
        }

        for(String s : listData){
            Log.i("mtd", s);
        }
//        Log.i("mtd","wetwew");
//        Log.i("mtd","inserting values to database");
//        db.addWord("Gugma" , "love", 0, 0);
//        Log.i("mtd","reading db");

        et=(EditText)findViewById(R.id.main_textbox);

        final ImageButton recent=(ImageButton)findViewById(R.id.main_recent);
        recent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,Recent.class);
                startActivity(i);
            }
        });

        final ImageButton fave=(ImageButton)findViewById(R.id.main_fave);
        fave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,Favorite.class);
                startActivity(i);
            }
        });

        final ImageButton close=(ImageButton)findViewById(R.id.main_close);
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                et.setText("");
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mtd","naresume ko na po ang main");
    }

    public void addWord(DBHandler db, String word, String defn, int fav, int look){
        boolean insertWord = db.addWord(word , defn , fav , look);
        if(insertWord){
            Log.i("mtd", "Successfully added the word " + word);
        }else{
            Log.i("mtd", "UNSUCCESSFUL");
        }
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_ENTER) {
            Intent i=new Intent(MainActivity.this,WordView.class);
            i.putExtra("selectedText",et.getText().toString());
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
//            finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("mtd","restart main");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("mtd","sinira ko na po and main");
    }
}