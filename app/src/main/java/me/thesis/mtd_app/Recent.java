package me.thesis.mtd_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import me.thesis.mtd_app.db.DBHandler;

public class Recent extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recents);
//
//        TextView tx=(TextView)findViewById(R.id.recent_mtd);
//        Typeface custom_font= Typeface.createFromAsset(getAssets(),"fonts/Roboto-Bold.ttf");
//        tx.setTypeface(custom_font);
        DBHandler db = new DBHandler(this);
        Cursor data = db.getRecentData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            listData.add(data.getString(1));
        }

        final ListView lv=(ListView)findViewById(R.id.list_recent);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.word_fragment,R.id.textView,listData);
        lv.setAdapter(arrayAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str=lv.getItemAtPosition(i).toString();

                Log.d("mtd",str);
                Intent intent=new Intent(Recent.this,WordView.class);
                intent.putExtra("selectedText",str);
                startActivity(intent);
                finish();
            }
        });
    }
}