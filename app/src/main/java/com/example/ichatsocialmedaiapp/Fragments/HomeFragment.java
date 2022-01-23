package com.example.ichatsocialmedaiapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.ichatsocialmedaiapp.Adapter.PostRVadapter;
import com.example.ichatsocialmedaiapp.Adapter.SpinnerCustomAdapter;
import com.example.ichatsocialmedaiapp.Adapter.StoryAdapter;
import com.example.ichatsocialmedaiapp.AddPostActivity;
import com.example.ichatsocialmedaiapp.ChattingMessaging;
import com.example.ichatsocialmedaiapp.CommentActivity;
import com.example.ichatsocialmedaiapp.Login;
import com.example.ichatsocialmedaiapp.Model.Post;
import com.example.ichatsocialmedaiapp.Model.StoryModel;
import com.example.ichatsocialmedaiapp.Model.UserStories;
import com.example.ichatsocialmedaiapp.Model.designLayout_java_class;
import com.example.ichatsocialmedaiapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {


    Toolbar toolbar;
    RecyclerView storyRv;
    ArrayList<StoryModel> list;

    ShimmerRecyclerView homeRv;
    ArrayList<Post> homepostlist;

    ImageView more;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    ImageView addStoryImage;
    RoundedImageView storyimage;

    ProgressDialog dialog;

    //private Spinner spinner;
    List<designLayout_java_class> customdesignlist=new ArrayList<designLayout_java_class>();
    private SpinnerCustomAdapter spinnerCustomAdapter;

//    ActivityResultLauncher<String> galleryLauncher;

    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        dialog = new ProgressDialog(getContext());
        setHasOptionsMenu(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = view.findViewById(R.id.toolbar_home);
        toolbar.setTitle("ichat");
        toolbar.inflateMenu(R.menu.signout_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.addpost) {
                    Intent intent=new Intent(getActivity(), AddPostActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId() == R.id.signout){
                    auth.signOut();
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                    getActivity().finish();
                }

                return true;
            }
        });

        homeRv = view.findViewById(R.id.home_post_rv);
        homeRv.showShimmerAdapter();

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Story Uploading");
        dialog.setMessage("Please wait....");
        dialog.setCancelable(false);

        storyRv = view.findViewById(R.id.storyRV);

        list = new ArrayList<>();
//        list.add(new StoryModel(R.drawable.deaf,R.drawable.videos_icon,R.drawable.deaf,"Sweety"));
        StoryAdapter adapter = new StoryAdapter(list,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        storyRv.setLayoutManager(linearLayoutManager);
        storyRv.setNestedScrollingEnabled(false);
        storyRv.setAdapter(adapter);
        HorizontalScrollView horizontalScrollView=view.findViewById(R.id.horizontalScrollView);
        horizontalScrollView.setNestedScrollingEnabled(false);
        database.getReference()
                .child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    list.clear();
                    for (DataSnapshot storySnapshot : snapshot.getChildren()) {
                        StoryModel storyModel = new StoryModel();
                        storyModel.setStoryBy(storySnapshot.getKey());// for user name we get the id of the user.
                        storyModel.setStoryAt(storySnapshot.child("postedBy").getValue(Long.class));// for the time at which story was uploaded.

                        ArrayList<UserStories> stories = new ArrayList<>();
                        for (DataSnapshot snapshot1 : storySnapshot.child("userStories").getChildren()){
                            UserStories userStories = snapshot1.getValue(UserStories.class);
                            stories.add(userStories);
                        }
                        storyModel.setStories(stories);
                        list.add(storyModel);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        homeRv = view.findViewById(R.id.home_post_rv);
        homepostlist = new ArrayList<>();
//        homepostlist.add(new Post(R.drawable.profile_logo,R.drawable.profile_logo,R.drawable.saved,"Sweety","about Sweety","546","45","7"));
        PostRVadapter postRVadapter=new PostRVadapter(homepostlist,getContext());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        homeRv.setLayoutManager(linearLayoutManager1);
        homeRv.setNestedScrollingEnabled(false);//for continuous scrolling .
//        homeRv.setAdapter(postRVadapter);

        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                homepostlist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Post post = dataSnapshot.getValue(Post.class);
                    post.setPostId(dataSnapshot.getKey());// setting key(key is the unique id's in the post of the users) of the post.
                    homepostlist.add(post);
                }
                homeRv.hideShimmerAdapter();
                homeRv.setAdapter(postRVadapter);
                postRVadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addStoryImage = view.findViewById(R.id.addStoryImg);
        storyimage=view.findViewById(R.id.story);
//        addStoryImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                galleryLauncher.launch("image/*");
//            }
//        });
//
//        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
//            @Override
//            public void onActivityResult(Uri result) {
//                addStoryImage.setImageURI(result);
//            }
//        });

        addStoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,17);
            }
        });



        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getData()!=null)
        {
            Uri uri=data.getData();
            storyimage.setImageURI(uri);

            dialog.show();

            //reference is the path.
            final StorageReference reference = storage.getReference()
                    .child("stories")
                    .child(FirebaseAuth.getInstance().getUid())
                    .child(new Date().getTime()+"");
            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            StoryModel storyModel = new StoryModel();
                            storyModel.setStoryAt(new Date().getTime());

                            database.getReference()
                                    .child("stories")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("postedBy")
                                    .setValue(storyModel.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    UserStories stories=new UserStories(uri.toString(),storyModel.getStoryAt());
                                    database.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("userStories")
                                            .push()
                                            .setValue(stories).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialog.dismiss();
                                        }
                                    });
                                    //here push() function set a new node with random characters.
                                }
                            });
                        }
                    });
                }
            });


        }

    }

}