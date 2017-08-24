package me.thesis.mtd_app.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

import me.thesis.mtd_app.R;
import me.thesis.mtd_app.db.DBHandler;
import me.thesis.mtd_app.db.Word;

public class MainActivity extends AppCompatActivity {

    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("mtd","yohoooooooo");
        DBHandler db = new DBHandler(this);
        Log.i("mtd","wetwew");
        Log.i("mtd","inserting values to database");
        db.addWord("Gugma" , "love", 0, 0);
        Log.i("mtd","reading db");

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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_ENTER) {
            Intent i=new Intent(MainActivity.this,WordView.class);
            i.putExtra("selectedText",et.getText().toString());
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
            finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}