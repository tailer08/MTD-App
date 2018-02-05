package me.thesis.mtd_app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelpFragment extends Fragment {
    View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_help,container,false);

        final TextView tv= (TextView)mView.findViewById(R.id.text_help);
        tv.setText("Search – allow user to find word.\n" +
                "Recent –view the recent search word.\n" +
                "Favorites – shows the favorites word.\n" +
                "Tagalog – Waray (A - Z) – shows the word list.\n" +
                "User Generates Words ¬– allow useradmin to add word.\n" +
                "About – shows the description of the system and the developer.\n" +
                "Help – shows the user guide of the system.\n" +
                "Feedback - shows the email of the admin-developer.\n" +
                "Admin – shows the login and signup form.\n");

        return mView;
    }
}