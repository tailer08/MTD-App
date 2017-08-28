package me.thesis.mtd_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Favorite extends AppCompatActivity {

    ListView lv;
    String[] word={"Aso","Ginhihigugma","Misay","Ngayon","Lidong"};
    String[] defn={"n.\nayam","v.\nminamahal","n.\npusa","adv.\nyana","n.\nbilog"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fave);

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
