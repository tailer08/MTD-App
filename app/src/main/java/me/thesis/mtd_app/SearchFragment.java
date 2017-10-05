package me.thesis.mtd_app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

public class SearchFragment extends Fragment {

    private View mView;
    private String state,letter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_search,container,false);

        final EditText et=(EditText)mView.findViewById(R.id.search_textbox);
        et.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction()==KeyEvent.ACTION_UP) {
                    String condition;

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

                    WordListFragment wordListFragment=new WordListFragment();
                    Bundle b=new Bundle();
                    b.putString("condition",condition);
                    b.putString("state",state);
                    b.putString("letter",letter);
                    wordListFragment.setArguments(b);
                    getActivity().getFragmentManager().beginTransaction().
                            replace(R.id.fragment_frame,wordListFragment,null).
                            addToBackStack("Search Fragment").commit();
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
        state=getArguments().getString("state");
        letter=getArguments().getString("letter");

        if (!state.equals("Search")) {
            WordListFragment wordListFragment=new WordListFragment();
            Bundle b=new Bundle();
            b.putString("state",state);

            if (state.equals("Waray") || state.equals("Tagalog")) {
                b.putString("letter",letter);
            }
            wordListFragment.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.fragment_frame,
                    wordListFragment).addToBackStack(null).commit();
        }
        super.onResume();
    }
}