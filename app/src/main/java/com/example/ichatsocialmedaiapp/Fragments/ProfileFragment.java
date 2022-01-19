package com.example.ichatsocialmedaiapp.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.ichatsocialmedaiapp.Adapter.FollowerRVadapter;
import com.example.ichatsocialmedaiapp.Adapter.PostRVadapter;
import com.example.ichatsocialmedaiapp.Login;
import com.example.ichatsocialmedaiapp.Model.Follow;
import com.example.ichatsocialmedaiapp.Model.Post;
import com.example.ichatsocialmedaiapp.Model.Users;
import com.example.ichatsocialmedaiapp.R;
import com.example.ichatsocialmedaiapp.databinding.FragmentProfileBinding;
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

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    RecyclerView recyclerView;
    ArrayList<Follow> list;

    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;

    ShimmerRecyclerView myPostRv;
    ArrayList<Post> myPostlist;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);


//        myPostRfindViewById(R.id.home_post_rv);
        binding.myPostRv.showShimmerAdapter();
        myPostlist = new ArrayList<>();
//        homepostlist.add(new Post(R.drawable.profile_logo,R.drawable.profile_logo,R.drawable.saved,"Sweety","about Sweety","546","45","7"));
        PostRVadapter postRVadapter=new PostRVadapter(myPostlist,getContext());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        binding.myPostRv.setLayoutManager(linearLayoutManager1);
        binding.myPostRv.setNestedScrollingEnabled(false);//for continuous scrolling .
//        homeRv.setAdapter(postRVadapter);

        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myPostlist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Post post = dataSnapshot.getValue(Post.class);
                    if(post.getPostedBy().equals(FirebaseAuth.getInstance().getUid())){
                    post.setPostId(dataSnapshot.getKey());// setting key(key is the unique id's in the post of the users) of the post.
                    myPostlist.add(post);
                    }
                }
                binding.myPostRv.hideShimmerAdapter();
                binding.myPostRv.setAdapter(postRVadapter);
                postRVadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        binding.settingsprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"signout",Toast.LENGTH_SHORT).show();
                auth.signOut();

                Intent intent=new Intent(getContext(), Login.class);
                startActivity(intent);
            }
        });

        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//by this we got all the data of the id in snapshot form.
                if (snapshot.exists()){
                    Users users=snapshot.getValue(Users.class);

                    Picasso.get().load(users.getProfilePhoto()).placeholder(R.drawable.coverimage).into(binding.profilephoto);
                    Picasso.get().load(users.getCoverPhoto()).placeholder(R.drawable.coverimage).into(binding.coverphoto);
                    //placeholder() :-
                    /*
                    => it is used for showing some other image at that place if image does not get loaded.
                    */
                    binding.profileUsername.setText(users.getUserName());
                    binding.profileProfession.setText(users.getProfession());
                    binding.followers.setText(users.getFollowerCount()+"");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        recyclerView=view.findViewById(R.id.friendslistRV);
        list=new ArrayList<>();
//        list.add(new Follow(R.drawable.addphoto_icon));
//        list.add(new Follow(R.drawable.story_shade));
//        list.add(new Follow(R.drawable.art));
//        list.add(new Follow(R.drawable.deaf));
//        list.add(new Follow(R.drawable.deaf));
//        list.add(new Follow(R.drawable.deaf));
//        list.add(new Follow(R.drawable.deaf));

        FollowerRVadapter followerRVadapter =new FollowerRVadapter(list,getContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.friendslistRV.setLayoutManager(linearLayoutManager);
        binding.friendslistRV.setAdapter(followerRVadapter);

        database.getReference().child("Users")
                .child(auth.getUid())
                .child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Follow follow = dataSnapshot.getValue(Follow.class);
                    list.add(follow);
                }
                followerRVadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.changecoverpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,11);
            }
        });

        binding.changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent();
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                intent1.setType("image/*");
                startActivityForResult(intent1,12);
            }
        });

        return binding.getRoot();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                binding.coverphoto.setImageURI(uri);

                final StorageReference reference = storage.getReference().child("cover_photo").child(FirebaseAuth.getInstance().getUid());
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(getContext(),"Cover photo Saved",Toast.LENGTH_SHORT).show();

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference().child("Users").child(auth.getUid()).child("coverPhoto").setValue(uri.toString());
                                Toast.makeText(getContext(), "Cover photo Saved", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
        if (requestCode==12){
            if (data.getData() != null) {
                Uri uri = data.getData();
                binding.profilephoto.setImageURI(uri);

                final StorageReference reference = storage.getReference().child("profile_photo").child(FirebaseAuth.getInstance().getUid());
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(getContext(),"Cover photo Saved",Toast.LENGTH_SHORT).show();

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference().child("Users").child(auth.getUid()).child("profilePhoto").setValue(uri.toString());
                                Toast.makeText(getContext(), "Profile photo Saved", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }




    }
}