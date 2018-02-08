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
                "Mother Tongue Dictionary is developed by IT Student and is free to use by language communities around Visayas. Development began in June 2017, with most of the work carried out by IT Student of Eastern Visayas State University Tacloban Campus.\n\n\n" +
                "The Application “Mother Tongue Dictionary” has its use for the students that is primarily to search the difficult Filipino and Waray-Waray words for their meanings. Definition area shows the meaning of the word, audio pronunciation, and favorite icon. The application shows also the related words and example " +
                "sentence. Mother Tongue Dictionary has an audio pronunciation that will help the user to pronounce the word properly. The application has a translation function from Filipino to Waray-Waray. It will help the user "+
                "understand the Waray-Waray word through translation. The proponents insert an example of gif animation every in action word.\n\n\n");

        return mView;
    }
}