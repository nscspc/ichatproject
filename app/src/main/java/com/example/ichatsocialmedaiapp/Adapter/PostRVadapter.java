package com.example.ichatsocialmedaiapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ichatsocialmedaiapp.Model.Post;
import com.example.ichatsocialmedaiapp.CommentActivity;
import com.example.ichatsocialmedaiapp.Model.Post;
import com.example.ichatsocialmedaiapp.Model.Users;
import com.example.ichatsocialmedaiapp.Model.notificationModel;
import com.example.ichatsocialmedaiapp.R;
import com.example.ichatsocialmedaiapp.databinding.HomeRvSampleBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class PostRVadapter extends RecyclerView.Adapter<PostRVadapter.viewHolder> {

    ArrayList<Post> list;
    Context context;

    public PostRVadapter(ArrayList<Post> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_rv_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Post model = list.get(position);
        Picasso.get()
                .load(model.getPostImage())
                .placeholder(R.drawable.coverimageicon)
                .into(holder.binding.postedImage);

        String description = model.getPostDescription();
        if (!description.equals("")){
            holder.binding.postHomeDescription.setVisibility(View.VISIBLE);
        }
        holder.binding.postHomeDescription.setText(model.getPostDescription());

        holder.binding.like.setText(model.getPostLike()+"");

        holder.binding.comment.setText(model.getCommentsCount()+"");

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(model.getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                Picasso.get()
                        .load(users.getProfilePhoto())
                        .placeholder(R.drawable.coverimageicon)
                        .into(holder.binding.profilephoto);

                holder.binding.userName.setText(users.getUserName());
                holder.binding.about.setText(users.getProfession());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(model.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liked_thumbs_up,0,0,0);
                        }
                        else {
                            holder.binding.like.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(model.getPostId())
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("posts")
                                                    .child(model.getPostId())
                                                    .child("postLike")
                                                    .setValue(model.getPostLike()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liked_thumbs_up,0,0,0);

                                                    notificationModel notificationmodel = new notificationModel();
                                                    notificationmodel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                    notificationmodel.setNotificationAt(new Date().getTime());
                                                    notificationmodel.setPostID(model.getPostId());
                                                    notificationmodel.setPostedBy(model.getPostedBy());
                                                    notificationmodel.setType("like");

                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("notification")
                                                            .child(model.getPostId())
                                                            .push()
                                                            .setValue(notificationmodel);
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        holder.binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId",model.getPostId());
                intent.putExtra("postedBy",model.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


//        holder.profile.setImageResource(homeRVmodel.getProfile());
//        holder.postImage.setImageResource(homeRVmodel.getPostImage());
////        holder.save.setImageResource(homeRVmodel.getSave());
//        holder.name.setText(homeRVmodel.getName());
//        holder.about.setText(homeRVmodel.getAbout());
//        holder.like.setText(homeRVmodel.getLike());
//        holder.comment.setText(homeRVmodel.getComment());
//        holder.share.setText(homeRVmodel.getShare());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        HomeRvSampleBinding binding;

//        ImageView profile,postImage,save;
//        TextView name,about,like,comment,share;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = HomeRvSampleBinding.bind(itemView);

//            profile=itemView.findViewById(R.id.profilephoto);
//            postImage=itemView.findViewById(R.id.posted_image);
//            save=itemView.findViewById(R.id.save);
//            name=itemView.findViewById(R.id.userName);
//            about=itemView.findViewById(R.id.about);
//            like=itemView.findViewById(R.id.like);
//            comment=itemView.findViewById(R.id.comment);
//            share=itemView.findViewById(R.id.share);

        }
    }

}
