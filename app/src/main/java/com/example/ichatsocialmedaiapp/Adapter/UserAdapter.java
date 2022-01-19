package com.example.ichatsocialmedaiapp.Adapter;

import static com.example.ichatsocialmedaiapp.R.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ichatsocialmedaiapp.Model.Follow;
import com.example.ichatsocialmedaiapp.Model.Users;
import com.example.ichatsocialmedaiapp.Model.notificationModel;
import com.example.ichatsocialmedaiapp.R;
import com.example.ichatsocialmedaiapp.databinding.UserSampleBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder>{

    Context context;
    ArrayList<Users> list;

    public UserAdapter(Context context, ArrayList<Users> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(layout.user_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Users users = list.get(position);
        Picasso.get().load(users.getProfilePhoto()).placeholder(drawable.coverimage).into(holder.binding.profilephoto);
        holder.binding.nameSearch.setText(users.getUserName());
        holder.binding.professionSearch.setText(users.getProfession());

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(users.getUserID())
                .child("followers")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    holder.binding.followbtn.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_active_btn));
                    holder.binding.followbtn.setText("following");
                    holder.binding.followbtn.setTextColor(context.getResources().getColor(color.gray));
                    holder.binding.followbtn.setEnabled(false);
                }
                else
                {
                    holder.binding.followbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


//                holder.binding.followbtn.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_active_btn));
//                holder.binding.followbtn.setText("following");
//                holder.binding.followbtn.setTextColor(context.getResources().getColor(R.color.gray));
//                holder.binding.followbtn.setEnabled(false);//to avoid clicking after 1 click.

                            Follow follow = new Follow();
                            follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                            follow.setFollowedAt(new Date().getTime());

                            FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("Users")
                                    .child(users.getUserID())
                                    .child("followers")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(follow)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Users")
                                                    .child(users.getUserID())
                                                    .child("followerCount")
                                                    .setValue(users.getFollowerCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
//                                holder.binding.followbtn.setBackground(context.getResources().getDrawable(drawable.follow_active_btn));
                                                    holder.binding.followbtn.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_active_btn));
                                                    holder.binding.followbtn.setText("following");
                                                    holder.binding.followbtn.setTextColor(context.getResources().getColor(color.gray));
                                                    holder.binding.followbtn.setEnabled(false);
                                                    Toast.makeText(context,"You Followed "+users.getUserName(),Toast.LENGTH_SHORT).show();

                                                    notificationModel notificationmodel = new notificationModel();
                                                    notificationmodel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                    notificationmodel.setNotificationAt(new Date().getTime());
                                                    notificationmodel.setType("follow");

                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("notification")
                                                            .child(users.getUserID())//getting id of the user on which we have clicked to follow.
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





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        UserSampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding=UserSampleBinding.bind(itemView);
        }
    }

}
