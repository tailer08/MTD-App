package me.thesis.mtd_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

import me.thesis.mtd_app.db.DBHandler;

public class WordView extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {

    TextView word;
    TextView defn;
    TextToSpeech tts;
    Button buttonSpeak;
    String currentWord;

    private void setText(String str) {
        String wrd = "";
        String dfn = "";

        DBHandler wordDB = new DBHandler(this);

        if (str.equalsIgnoreCase("aso")) {
//            get word here
            Cursor data = wordDB.getData("Aso");
            while(data.moveToNext()){
                wrd = (data.getString(1));
                dfn = (data.getString(2));
            }
        }
        else if (str.equalsIgnoreCase("ginhihigugma")) {
//          get word here
            Cursor data = wordDB.getData("Ginhihigugma");
            while(data.moveToNext()){
                wrd = (data.getString(1));
                dfn = (data.getString(2));
            }

        }
        else if (str.equalsIgnoreCase("misay")) {
//            get word here
            Cursor data = wordDB.getData("Misay");
            while(data.moveToNext()){
                wrd = (data.getString(1));
                dfn = (data.getString(2));
            }
        }
        else if (str.equalsIgnoreCase("ngayon")) {
//            get word here
            Cursor data = wordDB.getData("Ngayon");
            while(data.moveToNext()){
                wrd = (data.getString(1));
                dfn = (data.getString(2));
            }
        }
        else if (str.equalsIgnoreCase("lidong")) {
//            get word here
            Cursor data = wordDB.getData("Lidong");
            while(data.moveToNext()){
                wrd = (data.getString(1));
                dfn = (data.getString(2));
            }
        }
//        set textview here
        else {
            wrd="Word does not exist";
            dfn="";
        }
        word.setText(wrd);
        defn.setText(dfn);
        currentWord = wrd;
    }

    protected void onCreate(Bundle savedInstanceState) {
        Log.d("mtd","created word view");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        final ImageButton close=(ImageButton)findViewById(R.id.word_close);
        buttonSpeak = (Button) findViewById(R.id.button_speak);
        buttonSpeak.setOnClickListener(this);
        tts = new TextToSpeech(this, this);

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

    @Override
    public void onBackPressed() {
        Log.d("mtd","back pressed");
//
//        Intent i=new Intent(getApplicationContext(),MainActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        startActivity(i);
//        finish();
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mtd","herezy");

        Intent intent=getIntent();
        String str=(String)intent.getStringExtra("selectedText");
        setText(str);

        Log.d("mtd","out resume");
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_ENTER) {
            setText(((EditText)findViewById(R.id.word_textbox)).getText().toString());
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id=item.getItemId();
        if (res_id==android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_speak:
                tts.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null);
                break;
        }
    }

    @Override
    public void onInit(int status) {
        if( status == TextToSpeech.SUCCESS){
            Locale US = tts.getLanguage();
            int result = tts.setLanguage(US);
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.d("mtd", "Language not supported");
            }else{

            }
        }
    }
}