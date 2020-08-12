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
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    Button send;
    EditText write;
    RecyclerView chat;
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
        chat = findViewById(R.id.chat_hist);

        recyclerView = findViewById(R.id.chat_hist);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        intent = getIntent();
        final String userid = intent.getStringExtra("userid");

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

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                readMessages(fuser.getUid(), userid);
                //username.setText(user.getNickname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        ref.child("Chats").push().setValue(hashMap);
    }

    private void readMessages(final String myId, final String userId) {
        mChat = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) || chat.getReceiver().equals(userId) && chat.getSender().equals(myId)) {
                        mChat.add(chat);
                    }

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
