package com.example.neighborhood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.neighborhood.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    Button mBtn_saveProfile;
    EditText mEditName, mEditPlace, mEditSex, mEditAge, mEditDesc;
    String newName, newPlace, newSex, newAge, newDec;
    ImageView prof_image;
    private DatabaseReference userRef;
    private FirebaseDatabase database;
    private static final String USER = "Users";
    private Uri mImageUri;
    private StorageTask uploadTask;
    StorageReference storageReference;
    FirebaseUser firebaseUser;

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
        prof_image = findViewById(R.id.prof_image);

        //DB references
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(USER);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        //Get current Information
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.child("id").getValue().equals(user.getUid())) {
                        User user1 = ds.getValue(User.class);
                        Glide.with(getApplicationContext()).load(user1.getImgurl()).into(prof_image);
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
        prof_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    CropImage.activity().setAspectRatio(1,1).setCropShape(CropImageView.CropShape.OVAL).start(EditProfileActivity.this);
            }
        });
    }

    // Method for getting the File Extension of the image
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    // Method for uploading the image from the app to the database storage
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Wird hochgeladen");
        pd.show();

        if (mImageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));

            uploadTask = fileReference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }

            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task <Uri> task) {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        String myUri = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imgurl", ""+myUri);

                        reference.updateChildren(hashMap);
                        pd.dismiss();
                    }
                    else
                    {
                        Toast.makeText(EditProfileActivity.this, "Upload Fehlgeschlagen", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(EditProfileActivity.this, "Kein Bild ausgewählt", Toast.LENGTH_SHORT).show();
        }
    }

    // Result Method for uploading an Image, Failed or succeeded
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            uploadImage();
        }
        else {
            Toast.makeText(EditProfileActivity.this, "Etwas ist Schief gelaufen", Toast.LENGTH_SHORT).show();
        }
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
        userRef.child(USERID).child("username").setValue(newName.toLowerCase()).toString().toLowerCase();
        userRef.child(USERID).child("place").setValue(newPlace);
        userRef.child(USERID).child("sex").setValue(newSex);
        userRef.child(USERID).child("age").setValue(newAge);
        userRef.child(USERID).child("desc").setValue(newDec);

        //sending confirmation toast
        Context context = getApplicationContext();
        CharSequence text = "Änderungen wurden übernommen";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}