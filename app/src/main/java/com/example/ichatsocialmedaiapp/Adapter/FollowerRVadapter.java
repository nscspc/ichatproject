package com.example.ichatsocialmedaiapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ichatsocialmedaiapp.Model.Follow;
import com.example.ichatsocialmedaiapp.Model.Follow;
import com.example.ichatsocialmedaiapp.Model.Users;
import com.example.ichatsocialmedaiapp.R;
import com.example.ichatsocialmedaiapp.databinding.FriendRvSampleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FollowerRVadapter extends RecyclerView.Adapter<FollowerRVadapter.viewHolder>{

    ArrayList<Follow> list;
    Context context;

    public FollowerRVadapter(ArrayList<Follow> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.friend_rv_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Follow follow= list.get(position);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(follow.getFollowedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users=snapshot.getValue(Users.class);
                Picasso.get()
                        .load(users.getProfilePhoto())
                        .placeholder(R.drawable.coverimageicon)
                        .into(holder.binding.friendpic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        holder.friendpic.setImageResource(model.getFriendpic());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
//        ImageView friendpic;
        FriendRvSampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding=FriendRvSampleBinding.bind(itemView);

//            friendpic=itemView.findViewById(R.id.friendpic);

        }

    }

}
