package me.thesis.mtd_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import me.thesis.mtd_app.R;
import me.thesis.mtd_app.db.DBHandler;

public class Recent extends AppCompatActivity {

    ArrayList<String> results=new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recents);
//
//        TextView tx=(TextView)findViewById(R.id.recent_mtd);
//        Typeface custom_font= Typeface.createFromAsset(getAssets(),"fonts/Roboto-Bold.ttf");
//        tx.setTypeface(custom_font);

        DBHandler dbHandler=new DBHandler(this.getApplicationContext());
        results=dbHandler.getRecent();

        final ListView lv=(ListView)findViewById(R.id.list_recent);
        ArrayAdapter<String> arrayAdapter=
                new ArrayAdapter<String>(this,R.layout.word_fragment,R.id.textView,results);
        lv.setAdapter(arrayAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str=lv.getItemAtPosition(i).toString();

                Intent intent=new Intent(Recent.this,WordView.class);
                intent.putExtra("selectedText",str);
                startActivity(intent);
                finish();
            }
        });
    }
}