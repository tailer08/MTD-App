package me.thesis.mtd_app.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import me.thesis.mtd_app.R;

public class Adapter extends ArrayAdapter<String> {

    String[] words={};
    String[] defn={};

    Context c;
    LayoutInflater inflater;

    public Adapter(Context c, String[] words, String[] defn) {
        super(c, R.layout.fave_fragment,words);
        this.c=c;
        this.words=words;
        this.defn=defn;
    }

    public class ViewHolder {
        TextView word;
        TextView def;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null) {
            inflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.fave_fragment,null);
        }

        final ViewHolder holder=new ViewHolder();
        holder.word=(TextView)convertView.findViewById(R.id.fave_word);
        holder.def=(TextView)convertView.findViewById(R.id.fave_defn);

        holder.word.setText(words[position]);
        holder.def.setText(defn[position]);
        return convertView;
    }
}