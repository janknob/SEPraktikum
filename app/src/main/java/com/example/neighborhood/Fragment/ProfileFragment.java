package com.example.neighborhood.Fragment;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.neighborhood.EditProfileActivity;
import com.example.neighborhood.Model.User;
import com.example.neighborhood.R;
import com.example.neighborhood.data.model.LoggedInUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    private TextView tv_name, tv_place, tv_age, tv_sex, tv_desc;
    Button btn_editProfile;
    View view;
    private DatabaseReference userRef;
    private FirebaseDatabase database;
    private static final String USER = "Users";


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);


        //initialize
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_place = (TextView) view.findViewById(R.id.tv_place);
        tv_age = (TextView) view.findViewById(R.id.tv_age);
        tv_sex = (TextView) view.findViewById(R.id.tv_sex);
        tv_desc = (TextView) view.findViewById(R.id.tv_description);
        btn_editProfile = (Button) view.findViewById(R.id.btn_editProfile);

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(USER);



        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if(ds.child("id").getValue().equals(user.getUid())) {
                            tv_name.setText(ds.child("username").getValue(String.class));
                            tv_place.setText(ds.child("place").getValue(String.class));
                            tv_age.setText(ds.child("age").getValue(String.class));
                            tv_sex.setText(ds.child("sex").getValue(String.class));
                            tv_desc.setText(ds.child("desc").getValue(String.class));
                        }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}