package me.thesis.mtd_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import me.thesis.mtd_app.db.DBHandler;

public class Favorite extends AppCompatActivity {

    ListView lv;
//    String[] word={"Aso","Ginhihigugma","Misay","Ngayon","Lidong"};
//    String[] defn={"n.\nayam","v.\nminamahal","n.\npusa","adv.\nyana","n.\nbilog"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fave);

        DBHandler db = new DBHandler(this);
        Cursor data = db.getFavoriteData();
        ArrayList<String> listWordData = new ArrayList<>();
        ArrayList<String> listDefnData = new ArrayList<>();
        while(data.moveToNext()){
            listWordData.add(data.getString(1));
            listDefnData.add(data.getString(2));
        }

        String[] word = new String[listWordData.size()];
        word = listWordData.toArray(word);
        String[] defn = new String[listDefnData.size()];
        defn = listDefnData.toArray(defn);

        lv = (ListView) findViewById(R.id.fave);
        Adapter adapter = new Adapter(this, word, defn);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str=lv.getItemAtPosition(i).toString();

                Intent intent=new Intent(Favorite.this,WordView.class);
                intent.putExtra("selectedText",str);
                startActivity(intent);
                finish();
            }
        });
    }
}
