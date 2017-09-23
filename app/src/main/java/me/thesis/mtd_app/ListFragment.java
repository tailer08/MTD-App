package me.thesis.mtd_app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListFragment extends Fragment {

    private View mView;
    private String param;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.activity_letterlist,container,false);

        final ListView listView=(ListView) mView.findViewById(R.id.letters);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(
                getActivity(),
                R.layout.custom_textview,
                getResources().getStringArray(R.array.alphabet));
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WordFragment wordFragment=new WordFragment();
                Bundle b=new Bundle();
                b.putString("state",param);
                b.putString("letter",listView.getItemAtPosition(i).toString());
                wordFragment.setArguments(b);
                getActivity().getFragmentManager().beginTransaction().
                        replace(R.id.content_frame,wordFragment,null).addToBackStack(null).commit();
            }
        });
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        param=getArguments().getString("language");
    }
}