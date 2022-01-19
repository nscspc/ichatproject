package com.example.ichatsocialmedaiapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ichatsocialmedaiapp.Model.MessagesModel;
import com.example.ichatsocialmedaiapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ChattingMessagingAdapter extends RecyclerView.Adapter{

    ArrayList<MessagesModel> messagesModels;
    Context context;

    String receiverid;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChattingMessagingAdapter(ArrayList<MessagesModel> messagesModels, Context context) {
        this.messagesModels = messagesModels;
        this.context = context;
    }

    public ChattingMessagingAdapter(ArrayList<MessagesModel> messagesModels, Context context, String receiverid) {
        this.messagesModels = messagesModels;
        this.context = context;
        this.receiverid = receiverid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderviewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return new ReceiverviewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (messagesModels.get(position).getUid().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }
        else
        {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessagesModel messagesModel = messagesModels.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete the message")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + receiverid;
                                database.getReference().child("chats").child(senderRoom)
                                        .child(messagesModel.getMessageId())
                                        .setValue(null);
                            }
                        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).show();

                return false;
            }
        });

        if (holder.getClass() == SenderviewHolder.class)
        {
            ((SenderviewHolder)holder).sendermsg.setText(messagesModel.getMessage());
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(messagesModel.getTimestamp() * 1000L);
            String timehms = DateFormat.format("hh:mm:ss", cal).toString();
            ((SenderviewHolder)holder).sendertime.setText(timehms);
        }
        else
        {
            ((ReceiverviewHolder)holder).receivermsg.setText(messagesModel.getMessage());
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(messagesModel.getTimestamp() * 1000L);
            String timehms = DateFormat.format("hh:mm:ss", cal).toString();
            ((ReceiverviewHolder)holder).receivertime.setText(timehms);
        }


    }

    @Override
    public int getItemCount() {
        return messagesModels.size();
    }

    public class ReceiverviewHolder extends RecyclerView.ViewHolder{

        TextView receivermsg,receivertime;

        public ReceiverviewHolder(@NonNull View itemView) {
            super(itemView);

            receivermsg = itemView.findViewById(R.id.receivertxt);
            receivertime = itemView.findViewById(R.id.receivertime);

        }
    }

    public class SenderviewHolder extends RecyclerView.ViewHolder {

        TextView sendermsg,sendertime;

        public SenderviewHolder(@NonNull View itemView) {
            super(itemView);

            sendermsg = itemView.findViewById(R.id.sendertxt);
            sendertime = itemView.findViewById(R.id.sendertime);

        }
    }


}
