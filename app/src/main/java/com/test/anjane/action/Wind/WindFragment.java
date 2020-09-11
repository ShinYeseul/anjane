package com.test.anjane.action.Wind;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.test.anjane.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WindFragment extends Fragment {


    public WindFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wind, container, false);

        return v;
    }


}
