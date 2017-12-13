package me.thesis.mtd_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class DefnAdapter extends ArrayAdapter {

    public interface CallBack {
        void speakToFragment(String s);
    }

    private ArrayList<String> defn;
    private CallBack mCallBack;

    public DefnAdapter(@NonNull Context context, ArrayList<String> defn, CallBack callBack) {
        super(context, 0, defn);
        this.defn=defn;
        this.mCallBack=callBack;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        String s=(String)getItem(position);

        if (convertView==null) {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.defn_view,parent,false);
        }

        ImageButton button=(ImageButton)convertView.findViewById(R.id.word_sound);
        button.setVisibility(View.GONE);
        final TextView textView=(TextView)convertView.findViewById(R.id.word_defn);
        if (s.startsWith("W")) {
            s = s.replace("W", "");
            button.setVisibility(View.VISIBLE);
        }
        textView.setText(s);

        final View finalConvertView = convertView;
        final String finalS = s;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.speakToFragment(finalS.split(". ")[1].replace(" \\\\(.*\\\\)",""));
            }
        });
        return convertView;
    }
}