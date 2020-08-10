package com.example.neighborhood;


import androidx.fragment.app.Fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;


public class ProfileActivity extends Fragment {

    TextView tv_name, tv_place, tv_age, tv_sex, tv_desc;
    View view;
    DatabaseReference reff;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);


        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_place = (TextView) view.findViewById(R.id.tv_place);
        tv_age = (TextView) view.findViewById(R.id.tv_age);
        tv_sex = (TextView) view.findViewById(R.id.tv_sex);
        tv_desc = (TextView) view.findViewById(R.id.tv_description);

        tv_name.setText("Paul");
        tv_age.setText("45");

        return view;
    }
}