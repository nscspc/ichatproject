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
    String pih;
    String pi2;

    String key1;

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
        String no = intent.getStringExtra("no");
//        description=intent.getStringExtra("description");

        try {
            if (no.equals("11")) {
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
                        pih=post.getPostImage();
                        binding.descriptionComment.setText(post.getPostDescription());
                        binding.like.setText(post.getPostLike() + "");
                        binding.comment.setText(post.getCommentsCount() + "");
//                binding.usernameComment.setText(post.getPostedBy());//it will show id not the name .
//                binding.comment.setText(post.);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            else {

                database.getReference()
                        .child("MyPosts")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(postId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Post post = snapshot.getValue(Post.class);
                        Picasso.get()
                                .load(post.getPostImage())
                                .placeholder(R.drawable.coverimageicon)
                                .into(binding.postedImageComment);
                        pih=post.getPostImage();
                        binding.descriptionComment.setText(post.getPostDescription());
                        binding.like.setText(post.getPostLike() + "");
                        binding.comment.setText(post.getCommentsCount() + "");
//                binding.usernameComment.setText(post.getPostedBy());//it will show id not the name .
//                binding.comment.setText(post.);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

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

                pi2=pih;
                database.getReference()
                        .child("MyPosts")
                        .child(FirebaseAuth.getInstance().getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                                        Post mp = snapshot1.getValue(Post.class);
                                        String key2 = snapshot1.getKey();
//                                        Toast.makeText(CommentActivity.this, key2, Toast.LENGTH_LONG).show();
                                        if ((mp.getPostImage()).equals(pi2)) {

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("MyPosts")
                                                    .child(FirebaseAuth.getInstance().getUid())
                                                    .child(key2)
                                                    .child("comments")
                                                    .push()//if we do not use push() ,then if a single user going to comment again then it will overwrite it instead of adding new comment
                                                    .setValue(comments).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("MyPosts")
                                                            .child(FirebaseAuth.getInstance().getUid())
                                                            .child(key2)
                                                            .child("commentsCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            int commentCount = 0;
                                                            if (snapshot.exists()) {
                                                                commentCount = snapshot.getValue(Integer.class);
                                                            }
                                                            database.getReference()
                                                                    .child("MyPosts")
                                                                    .child(FirebaseAuth.getInstance().getUid())
                                                                    .child(key2)
                                                                    .child("commentsCount")
                                                                    .setValue(commentCount + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    binding.commentET.setText("");// to set the comment box blank after commenting.
//                                                                    Toast.makeText(getApplicationContext(), "Commented", Toast.LENGTH_SHORT).show();

                                                                    if (!((FirebaseAuth.getInstance().getUid()).equals(postedBy))) {
                                                                        notificationModel notificationmodel = new notificationModel();
                                                                        notificationmodel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                        notificationmodel.setNotificationAt(new Date().getTime());
                                                                        notificationmodel.setPostID(FirebaseAuth.getInstance().getUid());
                                                                        notificationmodel.setPostedBy(postedBy);
                                                                        notificationmodel.setType("comment");

                                                                        FirebaseDatabase.getInstance().getReference()
                                                                                .child("notification")
                                                                                .child(postedBy)
                                                                                .push()
                                                                                .setValue(notificationmodel);
                                                                        }
                                                                    }
                                                            });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            });
//                                            pi2="gfjh";
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });







                pi2=pih;
//            loops:
                FirebaseDatabase.getInstance().getReference()
                        .child("posts")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    loops:
                                    for (DataSnapshot ss2 : snapshot.getChildren()) {

//                                        Toast.makeText(CommentActivity.this,"Toast to Check",Toast.LENGTH_LONG).show();


                                        Post model1 = ss2.getValue(Post.class);

                                        if (model1.getPostImage().equals(pi2)) {

                                            key1 = ss2.getKey();
//                                            ch = true;
//                                            commentpost = model1;
//                                            k=key1;
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("posts")
                                                    .child(key1)
                                                    .child("comments")
                                                    .push()//if we do not use push() ,then if a single user going to comment again then it will overwrite it instead of adding new comment
                                                    .setValue(comments).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    database.getReference()
                                                            .child("posts")
                                                            .child(key1)
                                                            .child("commentsCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            int commentCount = 0;
                                                            if (snapshot.exists()) {
                                                                commentCount = snapshot.getValue(Integer.class);
                                                            }
                                                            database.getReference()
                                                                    .child("posts")
                                                                    .child(key1)
                                                                    .child("commentsCount")
                                                                    .setValue(commentCount + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    binding.commentET.setText("");// to set the comment box blank after commenting.
                                                                    Toast.makeText(getApplicationContext(), "Commented", Toast.LENGTH_SHORT).show();

                                                                    if (!(FirebaseAuth.getInstance().getUid().equals(postedBy)))
                                                                    {
                                                                        notificationModel notificationmodel = new notificationModel();
                                                                        notificationmodel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                        notificationmodel.setNotificationAt(new Date().getTime());
                                                                        notificationmodel.setPostID(key1);
                                                                        notificationmodel.setPostedBy(postedBy);
                                                                        notificationmodel.setType("comment");

                                                                        FirebaseDatabase.getInstance().getReference()
                                                                                .child("notification")
                                                                                .child(postedBy)
                                                                                .push()
                                                                                .setValue(notificationmodel);
                                                                    }
                                                                    }
                                                            });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            });
                                            pi2 = "sdlkfj";

                                            break loops;
                                        }

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
        });


                CommentRVadapter adapter = new CommentRVadapter(getApplicationContext(), list);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                binding.commentRV.setLayoutManager(layoutManager);
                binding.commentRV.setAdapter(adapter);

                try {
                    if (no.equals("11")){
                    database.getReference()
                            .child("posts")
                            .child(postId)
                            .child("comments").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            //first deleting data from database which is not in the child of an id and keeping only the data which is under the id's.
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
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
                    else {
                        database.getReference()
                                .child("MyPosts")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(postId)
                                .child("comments").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                list.clear();
                                //first deleting data from database which is not in the child of an id and keeping only the data which is under the id's.
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        finish();
//        return super.onOptionsItemSelected(item);
//    }

}