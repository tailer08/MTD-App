package me.thesis.mtd_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                final EditText et=(EditText)findViewById(R.id.main_textbox);
                et.setText("Search word . . .");
            }
        });
    }
}
