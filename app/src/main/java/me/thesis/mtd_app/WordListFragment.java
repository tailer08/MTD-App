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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import me.thesis.mtd_app.db.DBHandler;
import me.thesis.mtd_app.service.MTDService;

public class WordListFragment extends Fragment {

    private static View mView;
    private static ListView listView;

    DBHandler dbHandler;
    private String param,letter, condition="empty";
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
                setList();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) { mService=null;}
    };

    private void setList() {
        if (condition!=null) {
            Log.d("mtd-app","condition="+condition);
            show(condition+" ORDER BY word");
        } else if (param.equals("Favorite")) {
            Log.d("mtd-app","at favoritE");
            show("favorite=1 ORDER BY word");
        } else if (param.equals("Recent")) {
            Log.d("mtd-app","at recent");
            show("lookup>=1 ORDER BY lookup");
        } else if (!param.equals("Search")) {
            Log.d("mtd-app","at latter kena");
            show("word LIKE '"+letter+"%' AND language LIKE '"+param+"' ORDER BY word");
        }
    }

    private void show(String filter) {
        if (dbHandler!=null) {
            list.removeAll(list);
            list.addAll(dbHandler.searchWords(filter));
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

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle b=getArguments();
        param = b.getString("state");
        letter=b.getString("letter");

        if (b.getString("condition")!=null && !b.getString("condition").equals(condition)) {
            condition=b.getString("condition");
        } else {
            condition=null;
        }

        if (!isBound) {
            getActivity().bindService(new Intent(getActivity(), MTDService.class),
                    mConnection,
                    Context.BIND_AUTO_CREATE);
            isBound=true;
        } else {
            setList();
        }
    }

    @Override
    public void onDestroy() {
        if (isBound) {
            isBound=false;
            getActivity().unbindService(mConnection);
        }

        super.onDestroy();
        Log.d("mtd-app","destroyed fragment WordListFragment");
    }
}