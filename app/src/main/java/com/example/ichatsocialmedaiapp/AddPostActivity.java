package com.example.ichatsocialmedaiapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ichatsocialmedaiapp.Model.Post;
import com.example.ichatsocialmedaiapp.Model.Users;
import com.example.ichatsocialmedaiapp.databinding.ActivityAddPostBinding;
import com.example.ichatsocialmedaiapp.databinding.ActivityMainBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
//import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class AddPostActivity extends AppCompatActivity {

    ActivityAddPostBinding binding;
    Uri uri;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        dialog = new ProgressDialog(AddPostActivity.this);

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading");
        dialog.setMessage("please wait .... ");
        dialog.setCancelable(false);// To avoid cancellation of the dialog box.
        dialog.setCanceledOnTouchOutside(false);// To avoid cancellation fo the dialog box on touch.

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Users users=snapshot.getValue(Users.class);
                    Picasso.get()
                            .load(users.getProfilePhoto())
                            .placeholder(R.drawable.coverimageicon)
                            .into(binding.profilephoto);

                    binding.nameSearch.setText(users.getUserName());
                    binding.professionSearch.setText(users.getProfession());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.postDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String description = binding.postDescription.getText().toString();
                if (!description.isEmpty()) {
                    binding.postbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.follow_btn_bg));
                    binding.postbtn.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
                    binding.postbtn.setEnabled(true);
                }
                else{
                    binding.postbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.follow_active_btn));
                    binding.postbtn.setTextColor(getApplicationContext().getResources().getColor(R.color.gray));
                    binding.postbtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        binding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ImagePicker.Companion.with(AddPostActivity.this)
                       .crop().start();
            }
        });


        binding.postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

                final StorageReference reference = storage.getReference().child("posts").child(FirebaseAuth.getInstance().getUid())
                        .child(new Date().getTime()+"");
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Post post = new Post();
                                post.setPostImage(uri.toString());
                                post.setPostedBy(FirebaseAuth.getInstance().getUid());
                                post.setPostDescription(binding.postDescription.getText().toString());
                                post.setPostedAt(new Date().getTime());

                                database.getReference().child("posts")
                                        .push()
                                        .setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Posted Successfully",Toast.LENGTH_SHORT).show();
                                        //push() function to push more then one value in the node.
                                    }
                                });

                                database.getReference().child("MyPosts")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .push()
                                        .setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Posted Successfully",Toast.LENGTH_SHORT).show();
                                        //push() function to push more then one value in the node.
                                    }
                                });


                            }
                        });
                    }
                });
            }
        });


//        return binding.getRoot();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if ("android.intent.action.SEND".equals(action) && type != null && "image/*".equals(type)) {

            Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

            binding.postImage.setImageURI(imageUri);
            binding.postImage.setVisibility(View.VISIBLE);
            binding.postbtn.setEnabled(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getData()!=null)
        {
            uri=data.getData();
            binding.postImage.setImageURI(uri);
            binding.postImage.setVisibility(View.VISIBLE);

            binding.postbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.follow_btn_bg));
            binding.postbtn.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
            binding.postbtn.setEnabled(true);
        }

    }

    }
