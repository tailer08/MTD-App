package me.thesis.mtd_app;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import me.thesis.mtd_app.db.DBHandler;
import me.thesis.mtd_app.service.MTDService;

public class WordListFragment extends Fragment {

    private static View mView;
    private static ListView listView;

    DBHandler dbHandler;
    private String param,letter;
    private MTDService mService=null;
    private boolean isBound=false;
    private ArrayList<String> list=new ArrayList<String>();
    private ArrayAdapter<String> wordAdapter;

    private ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            if(service.toString().equals("MTD")) {
                mService = ((MTDService.LocalBinder) service).getService();
                dbHandler = mService.getDBHandler();
                Log.d("mtd-app","db is aiiight");

                if (param.equals("Favorite")) {
                    Log.d("mtd-app","at favoritE");
                    show("favorite=1");
                } else if (!param.equals("Search")) {
                    Log.d("mtd-app","at latter kena");
                    show("word LIKE '"+letter+"%'");
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) { mService=null;}
    };

    private void show(String filter) {
        Log.d("mtd-app","filter="+filter);

        if (dbHandler!=null) {
            list.removeAll(list);
            list.addAll(dbHandler.searchWords(filter));
            for (String w: list) {
                Log.d("mtd-app","word="+w);
            }
            wordAdapter.notifyDataSetChanged();
        } else {
            Log.d("mtd-app","dbHandler is null?");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_wordlist,container,false);
        listView=(ListView)mView.findViewById(R.id.words);

        wordAdapter=new ArrayAdapter<String>(
                getActivity(),
                R.layout.custom_textview,
                list);
        listView.setAdapter(wordAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WordFragment wordFragment =new WordFragment();
                Bundle b=new Bundle();
                b.putString("word",listView.getItemAtPosition(i).toString());
                wordFragment.setArguments(b);
                getActivity().getFragmentManager().beginTransaction().
                        replace(R.id.content_frame, wordFragment,null).addToBackStack(null).commit();
            }});

        final EditText et=(EditText)mView.findViewById(R.id.search_textbox);
        et.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction()==KeyEvent.ACTION_UP) {
                    if (param.equals("Search")) {
                        show("word LIKE '%"+et.getText().toString()+"%'");
                    } else if (letter!=null){
                        show("language='"+param+"' and word LIKE '"+letter+"%'");
                    }
                }
                return false;
            }
        });

        ((ImageButton)mView.findViewById(R.id.search_close)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                et.setText("");
            }});
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isBound) {
            getActivity().bindService(new Intent(getActivity(), MTDService.class),
                    mConnection,
                    Context.BIND_AUTO_CREATE);
            isBound=true;
        }

        Bundle b=getArguments();
        param = b.getString("state");
        letter=b.getString("letter");
        Log.d("mtd-app","oi resume si word fragment="+param+" letter="+letter);
    }

    @Override
    public void onDestroy() {
        if (isBound) {
            isBound=false;
            getActivity().unbindService(mConnection);
        }

        super.onDestroy();
        Log.d("mtd-app","destroyed fragment");
    }
}