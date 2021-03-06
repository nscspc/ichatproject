package com.example.ichatsocialmedaiapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.ichatsocialmedaiapp.CommentActivity;
import com.example.ichatsocialmedaiapp.MainActivity;
import com.example.ichatsocialmedaiapp.Model.Users;
import com.example.ichatsocialmedaiapp.Model.notificationModel;
import com.example.ichatsocialmedaiapp.R;
import com.example.ichatsocialmedaiapp.databinding.NotificationTabRvDesignBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class notificationTabRvAdapter extends RecyclerView.Adapter<notificationTabRvAdapter.viewHolder>{

    ArrayList<notificationModel> list;
    Context context;
//    ViewPager vp;
//    (MainActivity)getActivity().changeBgColor(bgGrey);

    public notificationTabRvAdapter(ArrayList<notificationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notification_tab_rv_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        notificationModel model = list.get(position);

        String type = model.getType();
//        Boolean checkOpen = model.isCheckOpen();
//        if (checkOpen == true)
//        {
//            holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        }

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(model.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get()
                                .load(users.getProfilePhoto())
                                .placeholder(R.drawable.coverimageicon)
                                .into(holder.binding.profilephoto);

                        if (type.equals("like"))
                        {
                            holder.binding.notificationData.setText(Html.fromHtml("<b>"+model.getNotificationBy()+"</b>"+" liked your post"));
//                            Toast.makeText()
                        }
                        else if (type.equals("comment"))
                        {
                            holder.binding.notificationData.setText(Html.fromHtml("<b>"+users.getUserName()+"</b>"+" commented on your post"));
                        }
                        else
                        {
                            holder.binding.notificationData.setText(Html.fromHtml("<b>"+users.getUserName()+"</b>"+ " start following you"));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        holder.binding.openNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                notificationTabRvAdapter.this.notify();

                if (!type.equals("follow"))
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("notification")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child(model.getNotificationID())
                            .child("checkOpen")
                            .setValue(true);
                    holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("postId",model.getPostID());
                    intent.putExtra("postedBy",model.getPostedBy());
                    intent.putExtra("no","11");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
//                    vp.setOffscreenPageLimit(0);
//                    notificationTabRvAdapter adapter = new notificationTabRvAdapter(list, context.getApplicationContext());
//                    adapter.notifyDataSetChanged();
//                    Intent refresh= new Intent(context,context.getClass());
//                    context.startActivity(refresh);

                }
            }
        });

        Boolean checkOpen2 = model.isCheckOpen();
        if (checkOpen2 == true)
        {
            holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

//        holder.profile.setImageResource(model.getProfile());
//        holder.notificationdata.setText(Html.fromHtml(model.getNotificationdata()));
//        holder.notificationtime.setText(model.getNotificationtime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        NotificationTabRvDesignBinding binding;

//        ImageView profile;
//        TextView notificationdata,notificationtime;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NotificationTabRvDesignBinding.bind(itemView);
//            vp=itemView.findViewById(R.id.viewpager_);
//            profile=itemView.findViewById(R.id.profilephoto);
//            notificationdata=itemView.findViewById(R.id.notification_data);
//            notificationtime=itemView.findViewById(R.id.notification_time);
        }
    }

}
