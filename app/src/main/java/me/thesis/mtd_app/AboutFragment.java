package me.thesis.mtd_app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {
    View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_about,container,false);

        final TextView tv= (TextView)mView.findViewById(R.id.text_about);
        tv.setText("Thank you for using our app!\n\n\n" +
                "Mother Tongue Dictionary is an offline " +
                "Waray-Tagalog dictionary that allows users to" +
                " search for the equivalent of Waray-Waray or Tagalog words and "+
                "add new Waray-Waray words the user learned over time.\n\n\n");

        return mView;
    }
}