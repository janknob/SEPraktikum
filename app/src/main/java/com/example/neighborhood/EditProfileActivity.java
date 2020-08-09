package com.example.neighborhood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.neighborhood.Fragment.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    Button mBtn_saveProfile;
    EditText mEditName, mEditPlace, mEditSex, mEditAge, mEditDesc;
    String newName, newPlace, newSex, newAge, newDec;
    private DatabaseReference userRef;
    private FirebaseDatabase database;
    private static final String USER = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //initialize Hooks
        mBtn_saveProfile = (Button) findViewById(R.id.btn_saveProfile);
        mEditName = findViewById(R.id.edit_name);
        mEditPlace = findViewById(R.id.edit_place);
        mEditSex = findViewById(R.id.edit_sex);
        mEditAge = findViewById(R.id.edit_age);
        mEditDesc = findViewById(R.id.edit_desc);

        //DB references
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(USER);



        //Get current Information
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                for (DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.child("id").getValue().equals(user.getUid())) {
                        mEditName.setText(ds.child("username").getValue(String.class));
                        mEditPlace.setText(ds.child("place").getValue(String.class));
                        mEditAge.setText(ds.child("age").getValue(String.class));
                        mEditSex.setText(ds.child("sex").getValue(String.class));
                        mEditDesc.setText(ds.child("desc").getValue(String.class));

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Button Click
        mBtn_saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivity(intent);
                applyChanges();
            }
        });
    }

    //save changes
    void applyChanges(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String USERID = user.getUid();

        newName = mEditName.getText().toString();
        newPlace = mEditPlace.getText().toString();
        newSex = mEditSex.getText().toString();
        newAge = mEditAge.getText().toString();
        newDec = mEditDesc.getText().toString();

        //DB Changes
        userRef.child(USERID).child("username").setValue(newName);
        userRef.child(USERID).child("place").setValue(newPlace);
        userRef.child(USERID).child("sex").setValue(newSex);
        userRef.child(USERID).child("age").setValue(newAge);
        userRef.child(USERID).child("desc").setValue(newDec);



        //sending confirmation toast
        Context context = getApplicationContext();
        CharSequence text = "Änderungen wurden übernommen" + USERID;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}