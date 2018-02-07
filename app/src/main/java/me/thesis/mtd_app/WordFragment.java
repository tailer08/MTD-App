package me.thesis.mtd_app;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import me.thesis.mtd_app.db.DBHandler;
import me.thesis.mtd_app.db.Word;
import me.thesis.mtd_app.service.MTDService;
import pl.droidsonroids.gif.GifDrawable;

public class WordFragment extends Fragment implements TextToSpeech.OnInitListener,
        DefnAdapter.CallBack,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private View mView;
    private MTDService mService=null;
    private boolean isBound=false;
    private boolean isObserving;

    private TextView word;
    private ImageView gif;
    private TextToSpeech tts;
    private ImageButton favorite,sound;
    private Button deleteButton, editButton;
    private ListView listView;
    private DefnAdapter defnAdapter;
    private DBHandler db;

    private Word w;
    private String param;
    private ArrayList<String> defn;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE=0;

    private ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            if(service.toString().equals("MTD") && mService==null) {
                mService = ((MTDService.LocalBinder) service).getService();
                db =mService.getDBHandler();
                Log.d("mtd-app","mService initialized");
                showWord();
                editLookup();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) { mService=null;}
    };

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String i=intent.getStringExtra("status");
            if (i.equals("ok")) {
                AddWordFragment addWordFragment=new AddWordFragment();
                Bundle b=new Bundle();
                b.putString("state","editWord");
                b.putString("word",w.getWord());
                addWordFragment.setArguments(b);
                getActivity().getFragmentManager().beginTransaction().
                        replace(R.id.content_frame, addWordFragment,null).addToBackStack("editword").commit();
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Only an Administrator can add new words. \nLogin first at Admin Page on the menu.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }
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

        if (w.getGIF()!=null) {
            if (!w.getGIF().contains("/")) {
                gif.setImageResource(getActivity().getApplicationContext().
                        getResources().getIdentifier("drawable/" + w.getGIF(),
                        null, getActivity().getApplicationContext().getPackageName()));
            }
            else {
                try {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                        GifDrawable g=new GifDrawable(getActivity().getContentResolver(), Uri.parse(w.getGIF()));
                        g.start();
                        gif.setImageDrawable(g);
                        gif.setVisibility(View.VISIBLE);
                    } else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        /* Showing delete button for userwords */
        if(w.getUserWord() == 0){
            deleteButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
        } else {
            deleteButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
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

            Locale loc = new Locale("fil");
            Log.i("mtd",Arrays.toString(loc.getAvailableLocales()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_wordview,container,false);

        word=(TextView) mView.findViewById(R.id.word_main);
        deleteButton = (Button)mView.findViewById(R.id.delete_button);
        editButton = (Button)mView.findViewById(R.id.edit_button);

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
                speakToFragment(w.getWord());
            }});

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( db.deleteWord(w.getWord()) ){
                    Toast.makeText(getActivity(),word.getText().toString()+" removed from words database.",Toast.LENGTH_LONG).show();
                    getActivity().getFragmentManager().popBackStack();
                }else{
                    Toast.makeText(getActivity(),word.getText().toString()+" unable to removed from database.",Toast.LENGTH_LONG).show();
                }

                if( db.deletePhonetic(w.getWord()) ){
                    Toast.makeText(getActivity(),word.getText().toString()+" removed from phonetics database.",Toast.LENGTH_LONG).show();
                    getActivity().getFragmentManager().popBackStack();
                }else{
                    Toast.makeText(getActivity(),word.getText().toString()+" unable to removed from phonetic.",Toast.LENGTH_LONG).show();
                }
            }
        });
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),MTDService.class);
                i.setAction(MTDService.ACTION_ADMIN);
                getActivity().startService(i);
            }});

        IntentFilter filter=new IntentFilter();
        filter.addAction(MTDService.CHECK_ADMIN);
        getActivity().registerReceiver(receiver,filter);
        return mView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    try {
                        GifDrawable g=new GifDrawable(getActivity().getContentResolver(), Uri.parse(w.getGIF()));
                        g.start();
                        gif.setImageDrawable(g);
                        gif.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(),"Permission denied.",Toast.LENGTH_LONG).show();
                }
            }
        }
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
            isObserving=true;
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

        if (isObserving) {
            getActivity().unregisterReceiver(receiver);
            isObserving=false;
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
        Resources res = getActivity().getResources();
        String wordToSpeak = word;

        if (wordToSpeak.contains(" ")) {
            wordToSpeak=wordToSpeak.replace(" ","");
        } else if (wordToSpeak.contains("-")) {
            wordToSpeak=wordToSpeak.replace("-","");
        }

        int soundId = res.getIdentifier(wordToSpeak.toLowerCase(),"raw", getActivity().getPackageName());
        MediaPlayer wordSound = null;
        try {
            wordSound = MediaPlayer.create(getActivity(), soundId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(wordSound == null){
            Log.d("mtd-app", "wordSound not found");
        }else{
            wordSound.start();
        }
//        Log.d("mtd-app","word to speak="+word);
//        speak(mService.getDBHandler().getPhonetic(word));
    }
}