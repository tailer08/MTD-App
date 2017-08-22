package me.thesis.mtd_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import me.thesis.mtd_app.R;
import me.thesis.mtd_app.db.DBHandler;
import me.thesis.mtd_app.db.Word;

public class WordView extends AppCompatActivity {

    TextView word;
    TextView defn;

    Word w;
    DBHandler dbHandler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        dbHandler=new DBHandler(this);

        final ImageButton close=(ImageButton)findViewById(R.id.word_close);
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final EditText et=(EditText)findViewById(R.id.main_textbox);
                et.setText("");
            }
        });

        final ImageButton fave=(ImageButton)findViewById(R.id.word_fave);
        fave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                w.setFavorite(1);
            }
        });

        word=(TextView)findViewById(R.id.dictionary_word);
        defn=(TextView)findViewById(R.id.dictionary_meaning);
    }

    @Override
    public void onBackPressed() {
        dbHandler.updateWord(w);
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent=getIntent();
        String str=intent.getStringExtra("selectedText");

        w=dbHandler.getWord(str);
        w.setLookup(w.getLookup()+1);

        word.setText(w.getWord());
        defn.setText(w.getDefn());
    }
}