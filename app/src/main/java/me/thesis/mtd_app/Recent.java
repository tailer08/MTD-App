package me.thesis.mtd_app;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class Recent extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recents);
//
//        TextView tx=(TextView)findViewById(R.id.recent_mtd);
//        Typeface custom_font= Typeface.createFromAsset(getAssets(),"fonts/Roboto-Bold.ttf");
//        tx.setTypeface(custom_font);
    }
}