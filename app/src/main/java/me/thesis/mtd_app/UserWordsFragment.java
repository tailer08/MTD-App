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

import me.thesis.mtd_app.db.DBHandler;
import me.thesis.mtd_app.service.MTDService;

/**
 * Created by Khalile on 12/1/2017.
 */

public class UserWordsFragment extends Fragment {

    View mView;
    private boolean isLoggedIn=false;
    private DBHandler dbHandler;
    private MTDService mService=null;
    private boolean isBound=false;

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
        mView=inflater.inflate(R.layout.fragment_userwords,container,false);

        Button addWordButton = (Button)mView.findViewById(R.id.add_word);
        addWordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (isLoggedIn) {
                    Log.d("mtd-app", "***********entering add word");
                    AddWordFragment addWordFragment=new AddWordFragment();
                    getActivity().getFragmentManager().beginTransaction().
                            replace(R.id.content_frame,addWordFragment,null).
                            addToBackStack(null).commit();
                } else {
                    Log.d("mtd-app", "***********Entering login");
                    LoginFragment loginFragment = new LoginFragment();
                    getFragmentManager().beginTransaction().replace(R.id.content_frame,
                            loginFragment).commit();
                }
            }
        });
        return mView;
    }


    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
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
