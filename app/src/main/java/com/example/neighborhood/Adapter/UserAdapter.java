package com.example.neighborhood.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.neighborhood.ChatActivity;
import com.example.neighborhood.Model.User;
import com.example.neighborhood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private List<User> mUsers;
    private FirebaseUser firebaseUser;

    public  UserAdapter(Context mContext, List<User> mUsers)
    {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUsers.get(i);

        viewHolder.btn_follow.setVisibility(View.VISIBLE);
        viewHolder.username.setText(user.getUsername());
        Glide.with(mContext).load(user.getImgurl()).into(viewHolder.image_profile);
        friendRequest(user.getId(), viewHolder.btn_follow);

        if (user.getId().equals(firebaseUser.getUid()))
        {
            viewHolder.btn_follow.setVisibility(View.GONE);
        }
        // Method for clicking on profile and get to the Profile
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
        // Changes in the Database for Unbefreundet und befreundet
        viewHolder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.btn_follow.getText().toString().equals("Freundschaftsanfrage"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Unbefreundet").child(firebaseUser.getUid()).child("Befreundet").child(user.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Unbefreundet").child(user.getId()).child("Follower").child(firebaseUser.getUid()).setValue(true);
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("Unbefreundet").child(firebaseUser.getUid()).child("Befreundet").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Unbefreundet").child(user.getId()).child("Follower").child(firebaseUser.getUid()).removeValue();
                }
            }
        });
    }
    // Size of Users who are in the Database
    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    // Class for one User Item in the RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public CircleImageView image_profile;
        public Button btn_follow;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            image_profile = itemView.findViewById(R.id.image_profile);
            btn_follow = itemView.findViewById(R.id.btn_friendRequest);
        }
    }
    // change the Button in the Friendlist on Befreundet and Undbefreundet
    private void friendRequest(final String userid, final Button button)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Unbefreundet").child(firebaseUser.getUid()).child("Befreundet");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(userid).exists())
                {
                    button.setText("Befreundet");
                }
                else
                {
                    button.setText("Freundschaftsanfrage");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
