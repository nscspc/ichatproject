package com.example.ichatsocialmedaiapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ichatsocialmedaiapp.Model.StoryModel;
import com.example.ichatsocialmedaiapp.Model.UserStories;
import com.example.ichatsocialmedaiapp.Model.Users;
import com.example.ichatsocialmedaiapp.R;
import com.example.ichatsocialmedaiapp.databinding.StoryRvDesignBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.viewHolder>{

    ArrayList<StoryModel> list;
    Context context;

    public StoryAdapter(ArrayList<StoryModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_rv_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        StoryModel story = list.get(position);

        if (story.getStories().size()>0) {
            UserStories lastStory = story.getStories().get(story.getStories().size() - 1);//last story at last index.
            Picasso.get()
                    .load(lastStory.getImage())
                    .into(holder.binding.story);
            holder.binding.statuscircle.setPortionsCount(story.getStories().size());

            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(story.getStoryBy()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Users users = snapshot.getValue(Users.class);
                    Picasso.get()
                            .load(users.getProfilePhoto())
                            .placeholder(R.drawable.coverimageicon)
                            .into(holder.binding.profilephoto);
                    holder.binding.name.setText(users.getUserName());
                    holder.binding.story.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ArrayList<MyStory> myStories = new ArrayList<>();

                            for (UserStories stories : story.getStories()) {
                                myStories.add(new MyStory(
                                        stories.getImage())
                                );
                            }

                            new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                    .setStoriesList(myStories) // Required
                                    .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                    .setTitleText(users.getUserName()) // Default is Hidden
                                    .setSubtitleText("") // Default is Hidden
                                    .setTitleLogoUrl(users.getProfilePhoto()) // Default is Hidden
                                    .setStoryClickListeners(new StoryClickListeners() {
                                        @Override
                                        public void onDescriptionClickListener(int position) {
                                            //your action
                                        }

                                        @Override
                                        public void onTitleIconClickListener(int position) {
                                            //your action
                                        }
                                    }) // Optional Listeners
                                    .build() // Must be called before calling show method
                                    .show();

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
//        holder.storyImg.setImageResource(model.getStory());
//        holder.profile.setImageResource(model.getProfile());
//        holder.storyType.setImageResource(model.getStoryType());
//        holder.name.setText(model.getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        StoryRvDesignBinding binding;
//        ImageView storyImg,profile,storyType;
//        TextView name;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=StoryRvDesignBinding.bind(itemView);
//            storyImg = itemView.findViewById(R.id.story);
//            profile = itemView.findViewById(R.id.profilephoto);
//            storyType=itemView.findViewById(R.id.storyType);
//            name = itemView.findViewById(R.id.name);

        }
    }

}
