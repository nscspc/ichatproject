package com.example.ichatsocialmedaiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ichatsocialmedaiapp.Adapter.CommentRVadapter;
import com.example.ichatsocialmedaiapp.Model.Comments;
import com.example.ichatsocialmedaiapp.Model.Post;
import com.example.ichatsocialmedaiapp.Model.Users;
import com.example.ichatsocialmedaiapp.Model.notificationModel;
import com.example.ichatsocialmedaiapp.databinding.ActivityCommentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {

    ActivityCommentBinding binding;
    Intent intent;
    String postId,postedBy;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<Comments> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

//        setSupportActionBar(binding.toolbar);
//        CommentActivity.this.setTitle("Comments");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        intent = getIntent();
        postId=intent.getStringExtra("postId");
        postedBy=intent.getStringExtra("postedBy");
//        description=intent.getStringExtra("description");

        database.getReference()
                .child("posts")
                .child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                Picasso.get()
                        .load(post.getPostImage())
                        .placeholder(R.drawable.coverimageicon)
                        .into(binding.postedImageComment);
                binding.descriptionComment.setText(post.getPostDescription());
                binding.like.setText(post.getPostLike()+"");
                binding.comment.setText(post.getCommentsCount()+"");
//                binding.usernameComment.setText(post.getPostedBy());//it will show id not the name .
//                binding.comment.setText(post.);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference()
                .child("Users")
                .child(postedBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                Picasso.get()
                        .load(users.getProfilePhoto())
                        .placeholder(R.drawable.coverimageicon)
                        .into(binding.profilephoto);
                binding.usernameComment.setText(users.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.commentPostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Comments comments = new Comments();
                comments.setCommentBody(binding.commentET.getText().toString());
                comments.setCommentedAt(new Date().getTime());
                comments.setCommentedBy(FirebaseAuth.getInstance().getUid());

                database.getReference()
                        .child("posts")
                        .child(postId)
                        .child("comments")
                        .push()//if we do not use push() ,then if a single user going to comment again then it will overwrite it instead of adding new comment
                        .setValue(comments).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference()
                                .child("posts")
                                .child(postId)
                                .child("commentsCount").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int commentCount = 0;
                                if (snapshot.exists())
                                {
                                    commentCount=snapshot.getValue(Integer.class);
                                }
                                database.getReference()
                                        .child("posts")
                                        .child(postId)
                                        .child("commentsCount")
                                        .setValue(commentCount+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        binding.commentET.setText("");// to set the comment box blank after commenting.
                                        Toast.makeText(getApplicationContext(),"Commented",Toast.LENGTH_SHORT).show();

                                        notificationModel notificationmodel = new notificationModel();
                                        notificationmodel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                        notificationmodel.setNotificationAt(new Date().getTime());
                                        notificationmodel.setPostID(postId);
                                        notificationmodel.setPostedBy(postedBy);
                                        notificationmodel.setType("comment");

                                        FirebaseDatabase.getInstance().getReference()
                                                .child("notification")
                                                .child(postedBy)
                                                .push()
                                                .setValue(notificationmodel);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
        });


        CommentRVadapter adapter=new CommentRVadapter(this,list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.commentRV.setLayoutManager(layoutManager);
        binding.commentRV.setAdapter(adapter);

        database.getReference()
                .child("posts")
                .child(postId)
                .child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                //first deleting data from database which is not in the child of an id and keeping only the data which is under the id's.
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Comments comments = dataSnapshot.getValue(Comments.class);
                    list.add(comments);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        finish();
//        return super.onOptionsItemSelected(item);
//    }
}