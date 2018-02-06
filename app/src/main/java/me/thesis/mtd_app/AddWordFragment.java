package me.thesis.mtd_app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import me.thesis.mtd_app.db.DBHandler;
import me.thesis.mtd_app.db.Word;
import me.thesis.mtd_app.service.MTDService;
import pl.droidsonroids.gif.GifDrawable;

public class AddWordFragment extends Fragment {

    View mView;
    private DBHandler dbHandler;
    private MTDService mService=null;
    private boolean isBound=false;
    private EditText word;
    private EditText definition;
    private EditText phonetic;
    private EditText sample;
    private Button save_button;
    private Button photo_button;
    private ImageView gif;

    private String firstWord;

    private Uri mUri;

    private ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            if(service.toString().equals("MTD") && mService==null) {
                mService = ((MTDService.LocalBinder) service).getService();
                dbHandler = mService.getDBHandler();
                Log.d("mtd-app","************************************mService initialized");

                try {
                    if ((getArguments().getString("state")).equals("editWord")) {
                        Log.d("mtd-app","here at addword for editword");

                        Cursor c=(mService.getDBHandler()).getData(getArguments().getString("word"));
                        c.moveToFirst();

                        Word w=new Word(c);
                        word.setText(w.getWord(), TextView.BufferType.EDITABLE);
                        definition.setText(w.getDefn().split("!!Ex. ")[0], TextView.BufferType.EDITABLE);
                        phonetic.setText(dbHandler.getPhonetic(w.getWord()), TextView.BufferType.EDITABLE);
                        sample.setText(w.getDefn().split("!!Ex. ")[1], TextView.BufferType.EDITABLE);
                        mUri=Uri.parse(w.getGIF());
                        try {
                            GifDrawable g=new GifDrawable(getActivity().getContentResolver(), mUri);
                            g.start();
                            gif.setImageDrawable(g);
                            gif.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        firstWord=word.getText().toString();
                    }
                } catch (NullPointerException e) {
                    Log.d("mtd-app","Not for editing.");
                }
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) { mService=null;}
    };

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_addword,container,false);

        word = (EditText)mView.findViewById(R.id.word);
        definition = (EditText)mView.findViewById(R.id.definition);
        phonetic = (EditText)mView.findViewById(R.id.phonetic);
        sample = (EditText)mView.findViewById(R.id.sample);
        save_button = (Button)mView.findViewById(R.id.save);
        photo_button = (Button)mView.findViewById(R.id.photo);
        gif = (ImageView)mView.findViewById(R.id.add_gif);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHandler.addWord(word.getText().toString(),
                        definition.getText().toString().concat("!!Ex. "+sample.getText().toString()),
                        "Waray",1, mUri.toString()) && firstWord==null) {
                    dbHandler.addPhonetic(word.getText().toString(),phonetic.getText().toString());
                    Log.d("mtd-app", "****************Success on adding new user generated word");
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Toast.makeText(getActivity(),word.getText().toString()+" added to database.",Toast.LENGTH_LONG).show();
                    getActivity().getFragmentManager().popBackStack();
                } else {
                    Log.d("mtd-app","first word="+firstWord);
                    dbHandler.deletePhonetic(firstWord);
                    dbHandler.deleteWord(firstWord);
                    dbHandler.addWord(word.getText().toString(),
                            definition.getText().toString().concat("!!Ex. "+sample.getText().toString()),
                            "Waray",1, mUri.toString());
                    dbHandler.addPhonetic(word.getText().toString(),phonetic.getText().toString());
                    WordFragment wordFragment=new WordFragment();
                    Bundle b=new Bundle();
                    b.putString("word",word.getText().toString());
                    wordFragment.setArguments(b);
                    getActivity().getFragmentManager().popBackStack("userword", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getActivity().getFragmentManager().beginTransaction().
                            replace(R.id.content_frame,wordFragment,null).addToBackStack("addWord").commit();
                }
            }
        });

        photo_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent galleryIntent=new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,1);
            }
        });
        return mView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("mtd-app","got photo");
        if (resultCode==-1 && requestCode==1 && data!=null) {
            mUri=data.getData();
            decodeUri();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isBound) {
            getActivity().bindService(new Intent(getActivity(), MTDService.class),
                    mConnection,
                    Context.BIND_AUTO_CREATE);
            isBound=true;
        } else {}
    }

    @Override
    public void onDestroy() {
        if (isBound) {
            isBound=false;
            getActivity().unbindService(mConnection);
        }
        super.onDestroy();
    }

    public void decodeUri() {
        ParcelFileDescriptor parcelFD=null;

        try {
            Log.d("mtd-app","decoding uri");
            GifDrawable g=new GifDrawable(getActivity().getContentResolver(),mUri);
            g.start();
            gif.setImageDrawable(g);
            gif.setVisibility(View.VISIBLE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
