package me.thesis.mtd_app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Khalile on 12/1/2017.
 */

public class UserWordsFragment extends Fragment {

    View mView;
    private boolean isLoggedIn=false;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_userwords,container,false);

        Button addWordButton = (Button)mView.findViewById(R.id.add_word);
        addWordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (isLoggedIn) {
                    AddWordFragment addWordFragment=new AddWordFragment();
                    getActivity().getFragmentManager().beginTransaction().
                            replace(R.id.content_frame,addWordFragment,null).
                            addToBackStack(null).commit();
                } else {
                    LoginFragment loginFragment = new LoginFragment();
                    getFragmentManager().beginTransaction().replace(R.id.content_frame,
                            loginFragment).commit();
                }
            }
        });
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        String tmp=getArguments().getString("status");

        if (tmp!=null && tmp.equals("logged in")) {
            isLoggedIn=true;
        }
    }
}
