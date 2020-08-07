package com.example.neighborhood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.neighborhood.Fragment.ProfileFragment;

public class EditProfileActivity extends AppCompatActivity {

    Button mBtn_saveProfile;
    EditText mEditName, mEditPlace, mEditSex, mEditAge, mEditDesc;
    String newName, newPlace, newSex, newAge, newDec;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //initialize
        mBtn_saveProfile = (Button) findViewById(R.id.btn_saveProfile);
        mEditName = findViewById(R.id.edit_name);
        mEditPlace = findViewById(R.id.edit_place);
        mEditSex = findViewById(R.id.edit_sex);
        mEditAge = findViewById(R.id.edit_age);
        mEditDesc = findViewById(R.id.edit_desc);



        mBtn_saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment  = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment);
                applyChanges();
            }
        });
    }

    void applyChanges(){

        newName = mEditName.getText().toString();
        newPlace = mEditPlace.getText().toString();
        newSex = mEditSex.getText().toString();
        newAge = mEditAge.getText().toString();
        newDec = mEditDesc.getText().toString();


    }
}