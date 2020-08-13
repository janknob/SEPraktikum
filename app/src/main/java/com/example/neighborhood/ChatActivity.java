package com.example.neighborhood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.neighborhood.Adapter.MessageAdapter;
import com.example.neighborhood.Model.Chat;
import com.example.neighborhood.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    Button send;
    EditText write;
    FirebaseUser fuser;
    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        send = findViewById(R.id.send_btn);
        write = findViewById(R.id.write_text);
        recyclerView = findViewById(R.id.chat_hist);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        intent = getIntent();
        //Username des Profils, mit dem gechatted wird
        final String userid = intent.getStringExtra("userid");


        //Listener für den Sende-Button
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = write.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(fuser.getUid(), userid, msg);
                } else {
                    Toast.makeText(ChatActivity.this, "You cant send empty Messages", Toast.LENGTH_SHORT).show();
                }
                write.setText("");
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        //immer wenn ein neues ChatObjekt in die Datenbank eingefügt wird, wird die recyclerView aktualisiert
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readMessages(fuser.getUid(), userid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Die Methode, die benutzt wird, wenn man auf den Sende-Button klickt, um die Nachricht in die Datenbank zu speichern
    private void sendMessage(String sender, String receiver, String message) {

        Chat chat = new Chat(sender, receiver, message);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Chat").push().setValue(chat);

    }

    ///Die Methode, die benutzt wird, immer wenn ein ChatObjekt der Datenbank hinzugefügt wird, um alle Nachrichten, die an den user gerichtet sind und von diesem kommen in der recyclerView anzuzeigen
    private void readMessages(final String myId, final String userId) {
        mChat = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("Chat");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) || chat.getReceiver().equals(userId) && chat.getSender().equals(myId)) {
                        System.out.println(chat.getReceiver());
                        System.out.println(chat.getSender());
                        mChat.add(chat);
                    }
                    System.out.println("adapter gets adapted");
                    messageAdapter = new MessageAdapter(ChatActivity.this, mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
