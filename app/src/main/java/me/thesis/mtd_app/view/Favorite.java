package me.thesis.mtd_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import me.thesis.mtd_app.R;
import me.thesis.mtd_app.db.DBHandler;
import me.thesis.mtd_app.db.Word;

public class Favorite extends AppCompatActivity {

    ListView lv;
    ArrayList<Word> list=new ArrayList<Word>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fave);

        DBHandler dbHandler=new DBHandler(this.getApplicationContext());
        list=dbHandler.getFavorite();

        lv = (ListView) findViewById(R.id.fave);
        Adapter adapter=new Adapter(this,list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(Favorite.this,WordView.class);
                intent.putExtra("selectedText",list.get(i).getWord());
                startActivity(intent);
                finish();
            }
        });
    }
}
