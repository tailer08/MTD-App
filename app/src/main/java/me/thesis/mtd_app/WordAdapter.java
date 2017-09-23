package me.thesis.mtd_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import me.thesis.mtd_app.db.Word;

public class WordAdapter extends ArrayAdapter<Word> {

    private ArrayList<Word> dataSet;
    Context mContext;
    MainActivity mView;

    private static class ViewHolder {
        TextView mWord;
        TextView mDefn;
        ImageButton mSound;
        ImageButton mFave;
    }

    public WordAdapter(ArrayList<Word> data, MainActivity mView) {
        super(mView.getApplicationContext(),R.layout.word,data);
        this.dataSet=data;
        this.mView=mView;
    }

    private int lastPosition=-1;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Word word=getItem(position);
        final ViewHolder viewHolder;

        final View result;
        if (convertView==null) {
            viewHolder=new ViewHolder();
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(R.layout.word,parent,false);
            viewHolder.mWord=(TextView)convertView.findViewById(R.id.word_main);
            viewHolder.mDefn=(TextView)convertView.findViewById(R.id.word_defn);
            viewHolder.mSound=(ImageButton)convertView.findViewById(R.id.sound);
            viewHolder.mFave=(ImageButton)convertView.findViewById(R.id.favorite);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder=(ViewHolder)convertView.getTag();
            result=convertView;
        }
        lastPosition=position;

        viewHolder.mWord.setText(word.getWord());
        viewHolder.mDefn.setText(word.getDefn());
        if (word.getFavorite()==1) {
            viewHolder.mFave.setImageResource(R.drawable.star_on);
        } else {
            viewHolder.mFave.setImageResource(R.drawable.star_off);
        }
        viewHolder.mSound.setImageResource(R.drawable.sound_off);

        viewHolder.mSound.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("mtd-app","u has clicked for sound");
                mView.speak(word.getWord());
            }
        });
        viewHolder.mFave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (word.getFavorite()==0) {
                    viewHolder.mFave.setImageResource(R.drawable.star_on);
                    mView.editFavorite(word,1);
                } else {
                    viewHolder.mFave.setImageResource(R.drawable.star_off);
                    mView.editFavorite(word,0);
                }
            }
        });
        return convertView;
    }
}