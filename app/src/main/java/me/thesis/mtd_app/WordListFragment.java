package me.thesis.mtd_app;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private String letter, condition, state;
    private MTDService mService=null;
    private boolean isBound=false;
    private ArrayList<String> list=new ArrayList<String>();
    private ArrayAdapter<String> wordAdapter;

    Button reset;

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
        } else if (state.equals("Favorites")) {
            Log.d("mtd-app","at favoritE");
            show("favorite=1 ORDER BY word");
        } else if (state.equals("Recent")) {
            Log.d("mtd-app","at recent");
            show("lookup>=1 ORDER BY lookup");
        } else if (!state.equals("Search")) {
            Log.d("mtd-app","at latter kena");
            show("word LIKE '"+letter+"%' AND language LIKE '"+state+"' ORDER BY word");
        }
    }

    private void show(String filter) {
        /* Karl: adding word manually */
//        if(dbHandler.addWord("Pusa", "misay", "Tagalog", 0)){
//            Toast.makeText(getActivity(),"added to database.",Toast.LENGTH_LONG).show();
//        }else{
//            Log.d("mtd-app", "UNSUCCESSFUL ");
//        }
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

        reset=(Button)mView.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.removeAll(list);
                wordAdapter.notifyDataSetChanged();
                dbHandler.resetLookup();
            }
        });

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

        final EditText et=(EditText)mView.findViewById(R.id.word_search);

        et.setOnKeyListener(new View.OnKeyListener(){

            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction()==KeyEvent.ACTION_UP) {

                    if (state.equals("Search")) {
                        condition="word LIKE '%"+et.getText().toString()+"%'";
                    } else if (state.equals("Favorite")) {
                        condition="favorite=1 AND word LIKE '"+et.getText().toString()+"%'";
                    } else if (state.equals("Recent")) {
                        condition="word LIKE '%"+et.getText().toString()+"%' AND lookup>=1";
                    } else {
                        condition = "language LIKE '" + state + "' AND " +
                                "word LIKE '" + letter + "%' AND " +
                                "word LIKE '" + et.getText().toString() + "%'";
                    }

                    if (condition==null) {
                        return false;
                    } else {
                        setList();
                    }

                    et.setText("");
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    ((InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE)).
                            hideSoftInputFromWindow(et.getWindowToken(), 0);
                }
            }});

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

        state = getArguments().getString("state");
        letter=getArguments().getString("letter");

        if (state.equals("Recent")) {
            reset.setVisibility(View.VISIBLE);
            reset.setClickable(true);
        } else {
            reset.setVisibility(View.INVISIBLE);
            reset.setClickable(false);
        }

        if (letter != null) {
            ((MainActivity)getActivity()).updateActionBar(letter);
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
        condition=null;
        Log.d("mtd-app","destroyed fragment WordListFragment");
    }
}