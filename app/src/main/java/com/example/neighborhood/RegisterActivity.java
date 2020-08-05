package com.example.neighborhood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neighborhood.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username, eMail, password;
    Button  btn_register;
    TextView txt_login;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        eMail = findViewById(R.id.eMail);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);
        txt_login = findViewById(R.id.txt_login);

        auth = FirebaseAuth.getInstance();

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
                    public void onClick (View view)
            {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Bitte warten Sie....");
                pd.show();

                String str_username = username.getText().toString();
                String str_eMail = eMail.getText().toString();
                String str_password = password.getText().toString();

                if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_eMail) || TextUtils.isEmpty(str_password))
                {
                    Toast.makeText(RegisterActivity.this, "Alle Felder müssen ausgefüllt sein!", Toast.LENGTH_SHORT).show();
                }
                else if (str_password.length() < 6)
                {
                    Toast.makeText(RegisterActivity.this, "Das Passwort muss mindestenst 6 Ziffern lang sein!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    register(str_username, str_eMail, str_password);
                }
            }
        });
    }

    private void register(final String username, String eMail, String password){
        auth.createUserWithEmailAndPassword(eMail, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username.toLowerCase());
                    hashMap.put("sex", "");
                    hashMap.put("age", "");
                    hashMap.put("desc", "");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                pd.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else
                {
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "Sie können sich leider nicht mit dieser Email oder Passwort registrieren", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
