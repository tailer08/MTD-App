package me.thesis.mtd_app;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import me.thesis.mtd_app.db.DBHandler;
import me.thesis.mtd_app.db.Word;
import me.thesis.mtd_app.service.MTDService;

public class WordFragment extends Fragment implements TextToSpeech.OnInitListener, DefnAdapter.CallBack {

    private View mView;
    private MTDService mService=null;
    private boolean isBound=false;

    private TextView word;
    private TextToSpeech tts;
    private ImageButton favorite,sound;
    private ListView listView;
    private DefnAdapter defnAdapter;

    private Word w;
    private String param;
    private ArrayList<String> defn;

    private ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            if(service.toString().equals("MTD") && mService==null) {
                mService = ((MTDService.LocalBinder) service).getService();
                Log.d("mtd-app","mService initialized");
                showWord();
                editLookup();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) { mService=null;}
    };

    private void updateDefn() {
        defn.removeAll(defn);
        defn.addAll(Arrays.asList(w.getDefn().split("!!")));
        defnAdapter.notifyDataSetChanged();
    }

    private void showWord() {
        Cursor c=(mService.getDBHandler()).getData(param);
        c.moveToFirst();

        word.clearComposingText();

        w=new Word(c);
        word.setText(w.getWord());
        updateDefn();

        if (w.getFavorite()==1) {
            favorite.setImageResource(R.drawable.star_on);
        }
    }

    private void editFavorite() {
        DBHandler dbTemp=mService.getDBHandler();

        if (w.getFavorite()==0) {
            favorite.setImageResource(R.drawable.star_on);
            dbTemp.updateFavorite(w,1);
        } else {
            favorite.setImageResource(R.drawable.star_off);
            dbTemp.updateFavorite(w,0);
        }
    }

    private void editLookup() {
        DBHandler dbTemp=mService.getDBHandler();
        dbTemp.updateLookup(w,w.getLookup()+1);
    }

    public void speak(String text) {
        Log.d("mtd-app","beb be alayb");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
//            Locale US = tts.getLanguage();
            int result = tts.setLanguage(new Locale("fil"));
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.d("mtd", "Language not supported");
            }else{}
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_wordview,container,false);

        word=(TextView) mView.findViewById(R.id.word_main);

        listView=(ListView) mView.findViewById(R.id.word_list);
        defn=new ArrayList<String>();

        defnAdapter=new DefnAdapter(mView.getContext(),defn,this);
        listView.setAdapter(defnAdapter);

        tts=new TextToSpeech(getActivity(),this);
        favorite=(ImageButton) mView.findViewById(R.id.favorite);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFavorite();
            }});

        sound=(ImageButton) mView.findViewById(R.id.sound);
        sound.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                speak(w.getWord());
            }});

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        param=getArguments().getString("word");
        if (!isBound) {
            getActivity().bindService(new Intent(getActivity(), MTDService.class),
                    mConnection,
                    Context.BIND_AUTO_CREATE);
            isBound=true;
        } else {
            showWord();
            editLookup();
        }

        if (tts==null) {
            tts=new TextToSpeech(getActivity(),this);
        }
    }

    @Override
    public void onDestroy() {
        if (isBound) {
            isBound=false;
            getActivity().unbindService(mConnection);
        }

        if (tts!=null) {
            tts.stop();
            tts.shutdown();
            Log.d("mtd-app","TTS Destroyed");
        }

        super.onDestroy();
    }

    @Override
    public void speakToFragment(String word) {
        speak(mService.getDBHandler().getPhonetic(word));
    }
}