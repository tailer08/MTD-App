package me.thesis.mtd_app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import me.thesis.mtd_app.db.DBHandler;
import me.thesis.mtd_app.service.MTDService;

public class UserWordsFragment extends Fragment {

    private static View mView;
    private static ListView listView;

    private boolean isLoggedIn=false;
    private DBHandler dbHandler;
    private MTDService mService=null;
    private boolean isBound=false;
    private ArrayList<String> list=new ArrayList<String>();
    private ArrayAdapter<String> wordAdapter;

    private ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            if(service.toString().equals("MTD") && mService==null) {
                mService = ((MTDService.LocalBinder) service).getService();
                dbHandler = mService.getDBHandler();
                Log.d("mtd-app","************************************mService initialized");
                setList();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) { mService=null;}
    };

    private void setList() {
        show("userword=1 ORDER BY word");
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

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_userwords,container,false);

//      user_generated_words list
        listView=(ListView)mView.findViewById(R.id.userWordList);
        wordAdapter=new ArrayAdapter<String>(
                getActivity(),
                R.layout.custom_textview,
                list);
        Log.d("mtd-app", "list here: " + wordAdapter);
        listView.setAdapter(wordAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WordFragment wordFragment =new WordFragment();
                Bundle b=new Bundle();
                b.putString("word",listView.getItemAtPosition(i).toString());
                wordFragment.setArguments(b);
                getActivity().getFragmentManager().beginTransaction().
                        replace(R.id.content_frame, wordFragment,null).addToBackStack("userword").commit();
            }});

//      Adding a user_generater_word button
        Button addWordButton = (Button)mView.findViewById(R.id.add_word);
        addWordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (isLoggedIn) {
                    Log.d("mtd-app", "***********entering add word");
                    AddWordFragment addWordFragment=new AddWordFragment();
                    getActivity().getFragmentManager().beginTransaction().
                            replace(R.id.content_frame,addWordFragment,null).
                            addToBackStack("userword").commit();
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
        });

        return mView;

    }

//  updatating login information
    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

//  refreshing list
    public void updateList(){
        Log.d("mtd-app", "********updating list");
        setList();
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

        String tmp=getArguments().getString("status");

        if (tmp!=null && tmp.equals("logged in")) {
            isLoggedIn=true;
        }

        updateList();
    }

    @Override
    public void onDestroy() {
        if (isBound) {
            isBound=false;
            getActivity().unbindService(mConnection);
        }
        super.onDestroy();
    }
}
