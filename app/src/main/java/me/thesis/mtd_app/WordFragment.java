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
import android.widget.ImageView;
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
    private ImageView gif;
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

        if(w.getWord().equals("Abante"))
            gif.setImageResource(R.drawable.abante);
        else if(w.getWord().equals("Alisin"))
            gif.setImageResource(R.drawable.alisin);
        else if(w.getWord().equals("Amoy"))
            gif.setImageResource(R.drawable.amoy);
        else if(w.getWord().equals("Apoy"))
            gif.setImageResource(R.drawable.apoy);
        else if(w.getWord().equals("Away"))
            gif.setImageResource(R.drawable.away);
        else if(w.getWord().equals("Bagsak"))
            gif.setImageResource(R.drawable.bagsak);
        else if(w.getWord().equals("Balutan"))
            gif.setImageResource(R.drawable.balutan);
        else if(w.getWord().equals("Batuhin"))
            gif.setImageResource(R.drawable.batuhin);
        else if(w.getWord().equals("Dala"))
            gif.setImageResource(R.drawable.dala);
        else if(w.getWord().equals("Gapos"))
            gif.setImageResource(R.drawable.gapos);
        else if(w.getWord().equals("Gibain"))
            gif.setImageResource(R.drawable.gibain);
        else if(w.getWord().equals("Guluhin"))
            gif.setImageResource(R.drawable.guluhin);
        else if(w.getWord().equals("Gumiwang"))
            gif.setImageResource(R.drawable.gumiwang);
        else if(w.getWord().equals("Habi"))
            gif.setImageResource(R.drawable.habi);
        else if(w.getWord().equals("Habol"))
            gif.setImageResource(R.drawable.habol);
        else if(w.getWord().equals("Hawak"))
            gif.setImageResource(R.drawable.hawak);
        else if(w.getWord().equals("Himas"))
            gif.setImageResource(R.drawable.himas);
        else if(w.getWord().equals("Hukay"))
            gif.setImageResource(R.drawable.hukay);
        else if(w.getWord().equals("Huminga"))
            gif.setImageResource(R.drawable.huminga);
        else if(w.getWord().equals("Ikot"))
            gif.setImageResource(R.drawable.ikot);
        else if(w.getWord().equals("Inumin"))
            gif.setImageResource(R.drawable.inumin);
        else if(w.getWord().equals("Ipon"))
            gif.setImageResource(R.drawable.ipon);
        else if(w.getWord().equals("Itago"))
            gif.setImageResource(R.drawable.itago);
        else if(w.getWord().equals("Iwasan"))
            gif.setImageResource(R.drawable.iwasan);
        else if(w.getWord().equals("Kalkulahin"))
            gif.setImageResource(R.drawable.kalkulahin);
        else if(w.getWord().equals("Kumagat"))
            gif.setImageResource(R.drawable.kumagat);
        else if(w.getWord().equals("Lakad"))
            gif.setImageResource(R.drawable.lakad);
        else if(w.getWord().equals("Lakbay"))
            gif.setImageResource(R.drawable.lakbay);
        else if(w.getWord().equals("Laktaw"))
            gif.setImageResource(R.drawable.laktaw);
        else if(w.getWord().equals("Laro"))
            gif.setImageResource(R.drawable.laro);
        else if(w.getWord().equals("Lasa"))
            gif.setImageResource(R.drawable.lasa);
        else if(w.getWord().equals("Liksi"))
            gif.setImageResource(R.drawable.liksi);
        else if(w.getWord().equals("Lukso"))
            gif.setImageResource(R.drawable.lukso);
        else if(w.getWord().equals("Maanod"))
            gif.setImageResource(R.drawable.maanod);
        else if(w.getWord().equals("Magalak"))
            gif.setImageResource(R.drawable.magalak);
        else if(w.getWord().equals("Maghubad"))
            gif.setImageResource(R.drawable.maghubad);
        else if(w.getWord().equals("Magkalat"))
            gif.setImageResource(R.drawable.magkalat);
        else if(w.getWord().equals("Magkita"))
            gif.setImageResource(R.drawable.magkita);
        else if(w.getWord().equals("Maglaba"))
            gif.setImageResource(R.drawable.maglaba);
        else if(w.getWord().equals("Magligtas"))
            gif.setImageResource(R.drawable.magligtas);
        else if(w.getWord().equals("Magmarka"))
            gif.setImageResource(R.drawable.magmarka);
        else if(w.getWord().equals("Magpahayag"))
            gif.setImageResource(R.drawable.magpahayag);
        else if(w.getWord().equals("Magsalita"))
            gif.setImageResource(R.drawable.magsalita);
        else if(w.getWord().equals("Maingay"))
            gif.setImageResource(R.drawable.maingay);
        else if(w.getWord().equals("Maligo"))
            gif.setImageResource(R.drawable.maligo);
        else if(w.getWord().equals("Mamuri"))
            gif.setImageResource(R.drawable.mamuri);
        else if(w.getWord().equals("Mangasiwa"))
            gif.setImageResource(R.drawable.mangasiwa);
        else if(w.getWord().equals("Mangaso"))
            gif.setImageResource(R.drawable.mangaso);
        else if(w.getWord().equals("Manmanan"))
            gif.setImageResource(R.drawable.manmanan);
        else if(w.getWord().equals("Masugid"))
            gif.setImageResource(R.drawable.masugid);
        else if(w.getWord().equals("Nagagalit"))
            gif.setImageResource(R.drawable.nagagalit);
        else if(w.getWord().equals("Nagmamadali"))
            gif.setImageResource(R.drawable.nagmamadali);
        else if(w.getWord().equals("Nagsisisi"))
            gif.setImageResource(R.drawable.nagsisisi);
        else if(w.getWord().equals("Nakakatuwa"))
            gif.setImageResource(R.drawable.nakakatuwa);
        else if(w.getWord().equals("Nakakayamot"))
            gif.setImageResource(R.drawable.nakakayamot);
        else if(w.getWord().equals("Opera"))
            gif.setImageResource(R.drawable.opera);
        else if(w.getWord().equals("Order"))
            gif.setImageResource(R.drawable.order);
        else if(w.getWord().equals("Pagkain"))
            gif.setImageResource(R.drawable.pagkain);
        else if(w.getWord().equals("Palakpak"))
            gif.setImageResource(R.drawable.palakpak);
        else if(w.getWord().equals("Palitan"))
            gif.setImageResource(R.drawable.palitan);
        else if(w.getWord().equals("Patayan"))
            gif.setImageResource(R.drawable.patayan);
        else if(w.getWord().equals("Payuhan"))
            gif.setImageResource(R.drawable.payuhan);
        else if(w.getWord().equals("Pumatay"))
            gif.setImageResource(R.drawable.pumatay);
        else if(w.getWord().equals("Pumayag"))
            gif.setImageResource(R.drawable.pumayag);
        else if(w.getWord().equals("Punas"))
            gif.setImageResource(R.drawable.punas);
        else if(w.getWord().equals("Ramdam"))
            gif.setImageResource(R.drawable.ramdam);
        else if(w.getWord().equals("Sabit"))
            gif.setImageResource(R.drawable.sabit);
        else if(w.getWord().equals("Sakripisyo"))
            gif.setImageResource(R.drawable.sakripisyo);
        else if(w.getWord().equals("Salungatin"))
            gif.setImageResource(R.drawable.salungatin);
        else if(w.getWord().equals("Sawi"))
            gif.setImageResource(R.drawable.sawi);
        else if(w.getWord().equals("Saya"))
            gif.setImageResource(R.drawable.saya);
        else if(w.getWord().equals("Sindak"))
            gif.setImageResource(R.drawable.sindak);
        else if(w.getWord().equals("Tadjak"))
            gif.setImageResource(R.drawable.tadjak);
        else if(w.getWord().equals("Takbo"))
            gif.setImageResource(R.drawable.takbo);
        else if(w.getWord().equals("Tuksuhin"))
            gif.setImageResource(R.drawable.tuksuhin);
        else if(w.getWord().equals("Umakay"))
            gif.setImageResource(R.drawable.umakay);
        else if(w.getWord().equals("Umayaw"))
            gif.setImageResource(R.drawable.umayaw);
        else if(w.getWord().equals("Yakap"))
            gif.setImageResource(R.drawable.yakap);
        else if(w.getWord().equals("Yamot"))
            gif.setImageResource(R.drawable.yamot);
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

            Locale loc = new Locale("uk_UA");
            Log.i("mtd",Arrays.toString(loc.getAvailableLocales()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_wordview,container,false);

        word=(TextView) mView.findViewById(R.id.word_main);

        gif = (ImageView)mView.findViewById(R.id.gif);
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
                if (w.getLanguage().equals("Waray")) {
                    speakToFragment(w.getWord());
                } else {
                    speak(w.getWord());
                }
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