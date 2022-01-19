package com.example.ichatsocialmedaiapp.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ichatsocialmedaiapp.Model.Comments;
import com.example.ichatsocialmedaiapp.Model.Users;
import com.example.ichatsocialmedaiapp.R;
import com.example.ichatsocialmedaiapp.databinding.CommentsRvSampleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentRVadapter extends RecyclerView.Adapter<CommentRVadapter.viewHolder>{

    Context context;
    ArrayList<Comments> list;

    public CommentRVadapter(Context context, ArrayList<Comments> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comments_rv_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Comments comments=list.get(position);
//        holder.binding.commentname.setText(comments.getCommentBody());
//        String text = TimeAgo.using(comments.getCommentedAt());
//        holder.binding.datetime.setText(time);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(comments.getCommentedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                Picasso.get()
                        .load(users.getProfilePhoto())
                        .placeholder(R.drawable.coverimageicon)
                        .into(holder.binding.profilephoto);
                holder.binding.commentname.setText(Html.fromHtml("<b>"+users.getUserName()+"</b>"+" "+" "+ comments.getCommentBody()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        CommentsRvSampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=CommentsRvSampleBinding.bind(itemView);
        }
    }

}
