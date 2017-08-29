package me.thesis.mtd_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import me.thesis.mtd_app.db.DBHandler;

public class WordView extends AppCompatActivity {

    TextView word;
    TextView defn;

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
            wrd="Ginhihigugma";
            dfn="v.\n minamahal";
        }
        else if (str.equalsIgnoreCase("misay")) {
//            get word here
            wrd="Misay";
            dfn="n.\n pusa";
        }
        else if (str.equalsIgnoreCase("ngayon")) {
//            get word here
            wrd="Ngayon";
            dfn="adv.\n yana";
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

    protected void onCreate(Bundle savedInstanceState) {
        Log.d("mtd","created word view");
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
}