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
import android.widget.Button;
import android.widget.EditText;

import me.thesis.mtd_app.db.DBHandler;
import me.thesis.mtd_app.service.MTDService;

public class AddWordFragment extends Fragment {

    View mView;
    private DBHandler dbHandler;
    private MTDService mService=null;
    private boolean isBound=false;
    private EditText word;
    private EditText definition;
    private EditText phonetic;
    private Button save_button;

    private ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            if(service.toString().equals("MTD") && mService==null) {
                mService = ((MTDService.LocalBinder) service).getService();
                dbHandler = mService.getDBHandler();
                Log.d("mtd-app","************************************mService initialized");
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) { mService=null;}
    };

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_addword,container,false);

        word = (EditText)mView.findViewById(R.id.word);
        definition = (EditText)mView.findViewById(R.id.definition);
        phonetic = (EditText)mView.findViewById(R.id.definition);
        save_button = (Button)mView.findViewById(R.id.save);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHandler.addWord(word.getText().toString(),definition.getText().toString(),0,"Waray",0,1)){
                    Log.d("mtd-app", "****************Success on adding new user generated word");
                };
            }
        });
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
}
