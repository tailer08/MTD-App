package me.thesis.mtd_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import me.thesis.mtd_app.R;
import me.thesis.mtd_app.db.DBHandler;
import me.thesis.mtd_app.db.Word;

public class WordView extends AppCompatActivity {

    TextView word;
    TextView defn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        final ImageButton close=(ImageButton)findViewById(R.id.word_close);
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final EditText et=(EditText)findViewById(R.id.main_textbox);
                et.setText("");
            }
        });

        word = (TextView)findViewById(R.id.dictionary_word);
        defn = (TextView)findViewById(R.id.dictionary_meaning);
    }

    private void setText(String str) {
        String wrd;
        String dfn;

//        DBHandler wordDB = new DBHandler(this);

        if (str.equalsIgnoreCase("aso")) {
//            get word here
            wrd="Aso";
            dfn="n.\nayam";
        }
        else if (str.equalsIgnoreCase("ginhihigugma")) {
//          get word here
            wrd="Ginhihigugma";
            dfn="v.\nminamahal";
        }
        else if (str.equalsIgnoreCase("misay")) {
//            get word here
            wrd="Misay";
            dfn="n.\npusa";
        }
        else if (str.equalsIgnoreCase("ngayon")) {
//            get word here
            wrd="Ngayon";
            dfn="adv.\nyana";
        }
        else if (str.equalsIgnoreCase("lidong")) {
//            get word here
            wrd="Lidong";
            dfn="bilog";
        }
//        set textview here
        else {
            wrd="Word does not exist";
            dfn="";
        }
        word.setText(wrd);
        defn.setText(dfn);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("mtd","back pressed");
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mtd","herezy");

        Intent intent=getIntent();
        String str=(String)intent.getStringExtra("selectedText");
        setText(str);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_ENTER) {
            setText(((EditText)findViewById(R.id.word_textbox)).getText().toString());
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}