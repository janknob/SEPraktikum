package com.example.neighborhood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.neighborhood.Model.Post;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>
{
    public Context mContext;
    public List<Post> mPost;

    private FirebaseUser firebaseUser;

    public  PostAdapter (Context mContext, List<Post> mPost)
    {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = mPost.get(i);
        viewHolder.username.setText(mPost.get(i).getPublisher());
        publisherInfo(viewHolder.image_profile, viewHolder.username, post.getPublisher());

    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView image_profile, like, dislike;
        public TextView username, post_text, likes;

        public ViewHolder(View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            like = itemView.findViewById(R.id.like);
            dislike = itemView.findViewById(R.id.dislike);
            username = itemView.findViewById(R.id.username);
            post_text = itemView.findViewById(R.id.post_text);
            likes = itemView.findViewById(R.id.likes);

        }
    }
    private void publisherInfo (final ImageView image_profile, final TextView username, String userid )
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot activitySnaphot : dataSnapshot.getChildren()) {
                    User user = activitySnaphot.getValue(User.class);
                    Glide.with(mContext).load(user.getImgurl()).into(image_profile);
                    username.setText(user.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
