package me.thesis.mtd_app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FeedbackFragment extends Fragment {
    View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_feedback,container,false);

        final TextView tv= (TextView)mView.findViewById(R.id.text_feedback);
        tv.setText("For any inquiries, suggestions and reactions, \n" +
                "email us in the following account: \n " +
                "mtdictionary@gmail.com \n" +
                "Thank you for the continous support and patronage \n"+
                "- Mother Tongue Dictionary Developers");

        return mView;
    }
}