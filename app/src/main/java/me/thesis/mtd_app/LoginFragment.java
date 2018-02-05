package me.thesis.mtd_app;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import me.thesis.mtd_app.db.DBHandler;
import me.thesis.mtd_app.service.MTDService;

/**
 * Created by Khalile on 11/28/2017.
 */

public class LoginFragment extends Fragment {
    View mView;
    private EditText username;
    private EditText password;
    private Button login_button;
    private Button signup_button;
    private TextView result;
    private MTDService mService=null;
    private boolean isBound=false;
    private boolean isLoggedIn = false;
    DBHandler dbHandler;

    @Nullable

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
        mView=inflater.inflate(R.layout.fragment_login,container,false);

        username        = (EditText) mView.findViewById(R.id.username);
        password        = (EditText) mView.findViewById(R.id.password);
        result          = (TextView)mView.findViewById((R.id.result));
        login_button    = (Button)mView.findViewById(R.id.login_button);
        signup_button   = (Button)mView.findViewById(R.id.signup_button);
        username.setTextColor(Color.WHITE);
        username.setHintTextColor(Color.GRAY);
        password.setTextColor(Color.WHITE);
        password.setHintTextColor(Color.GRAY);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(login_button.getText().equals("Login")){
                    if (validate(username.getText().toString(), password.getText().toString())) {
                        isLoggedIn = true;
                        login_button.setText("Logout");
                        username.setFocusable(false);
                        username.setClickable(false);
                        username.setFocusableInTouchMode(false);
                        password.setFocusable(false);
                        password.setClickable(false);
                        password.setFocusableInTouchMode(false);
                        signup_button.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        Intent i=new Intent(getActivity(),MTDService.class);
                        i.setAction(MTDService.ACTION_LOG_IN);
                        getActivity().startService(i);
                    }
                }else{
                    isLoggedIn = false;
                    login_button.setText("Login");
                    username.setFocusable(true);
                    username.setClickable(true);
                    username.setFocusableInTouchMode(true);
                    username.setText("");
                    password.setFocusable(true);
                    password.setClickable(true);
                    password.setText("");
                    password.setFocusableInTouchMode(true);
                    signup_button.setVisibility(View.VISIBLE);
                    result.setText("Enter Username and Password");
                    result.setTextColor(Color.WHITE);
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser(username.getText().toString(), password.getText().toString());
            }
        });
        return mView;
    }

    private boolean validate(String username, String password){
        Cursor data = dbHandler.getUsersData(username);
        if(data.getCount() != 0) {
            data.moveToFirst();
            String origPassword = data.getString(data.getColumnIndex("password"));
            if(password.equals(origPassword)){
                result.setText("Success login");
                result.setTextColor(Color.GREEN);
                return true;
            }else{
                result.setText("Invalid login: Password is not correct");
                result.setTextColor(Color.RED);
                return false;
            }

        } else{
            result.setText("Invalid login: Username is not in the database");
            result.setTextColor(Color.RED);
            return false;
        }
    }

    private void addUser(String username, String password){
        if(username.length() >= 6 && password.length() >= 6){
            boolean dbAddUser = dbHandler.addUser(username, password);
            if(dbAddUser){
                result.setText("Successfully registered a user");
                result.setTextColor(Color.GREEN);
            }else{
                result.setText("Unsuccessful: Username already used");
                result.setTextColor(Color.RED);
            }
        }else if(username.length() < 6 && password.length() >= 6 ){
            result.setText("Unsuccessful: Username must contain atleast 6 characters");
            result.setTextColor(Color.RED);
            this.username.setTextColor(Color.RED);
            this.password.setTextColor(Color.GREEN);
        }else if(username.length() >= 6 && password.length() < 6){
            result.setText("Unsuccessful: password must contain atleast 6 characters");
            result.setTextColor(Color.RED);
            this.password.setTextColor(Color.RED);
            this.username.setTextColor(Color.GREEN);
        }else if(username.length() < 6 && password.length() < 6){
            result.setText("Unsuccessful: username and password must contain atleast 6 characters");
            this.password.setTextColor(Color.RED);
            this.username.setTextColor(Color.RED);
        }

    }

    public boolean isLoggedIn() {
        return isLoggedIn;
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
        String tmp = null;
        try {
            tmp = getArguments().getString("status");
        }catch (Exception e){}

        if (tmp!=null && tmp.equals("logged in")) {
            login_button.setText("Logout");
            username.setFocusable(false);
            username.setClickable(false);
            password.setFocusable(false);
            password.setClickable(false);
            signup_button.setVisibility(View.GONE);
            result.setText("Success login");
            result.setTextColor(Color.GREEN);
        }else{
            this.login_button.setEnabled(true);
            username.setFocusable(true);
            username.setClickable(true);
            password.setFocusable(true);
            password.setClickable(true);
        }

        if(!isLoggedIn()){
            username.setText("");
            password.setText("");
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
