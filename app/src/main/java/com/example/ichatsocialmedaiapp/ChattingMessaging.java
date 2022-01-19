package com.example.ichatsocialmedaiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.ichatsocialmedaiapp.Adapter.ChattingMessagingAdapter;
import com.example.ichatsocialmedaiapp.Model.MessagesModel;
import com.example.ichatsocialmedaiapp.databinding.ActivityChattingMessagingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChattingMessaging extends AppCompatActivity {


    ActivityChattingMessagingBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    int i;
    boolean checkspace=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChattingMessagingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.send.setVisibility(View.GONE);

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        String senderId = auth.getUid();//the user logged in the whatsapp  is the sender and others are receivers.
        String receiverId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.userNameMessage.setText(userName);
        Picasso.get()
                .load(profilePic)
                .placeholder(R.drawable.userprofilepicdefault)
                .into(binding.profileImage);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChattingMessaging.this, MainActivity.class);
                startActivity(intent);
            }
        });


        final ArrayList<MessagesModel> messagesModels = new ArrayList<>();
        final ChattingMessagingAdapter chattingMessagingAdapter = new ChattingMessagingAdapter(messagesModels, this, receiverId);
        binding.chatmessagingRV.setAdapter(chattingMessagingAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatmessagingRV.setLayoutManager(layoutManager);


        final String senderRoom = senderId + receiverId;
        final String receiverRoom = receiverId + senderId;

        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagesModels.clear();
                        //loop will run till it receives children from snapshot.
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MessagesModel model = dataSnapshot.getValue(MessagesModel.class);
                            model.setMessageId(dataSnapshot.getKey());
                            messagesModels.add(model);
                        }
                        chattingMessagingAdapter.notifyDataSetChanged();//to update recyclerview at runtime.
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String message = binding.etMessage.getText().toString();
                for(i = 0;i<message.length();i++)
                {
                    if(!(message.substring(i).equals(" ")))
                    {
                        checkspace=true;
                        break;
                    }
                }
                if (!(message.isEmpty()) && checkspace) {
                    binding.send.setEnabled(true);
                    binding.send.setVisibility(View.VISIBLE);
                } else {
                    binding.send.setEnabled(false);
                    binding.send.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.etMessage.getText().toString();

                final MessagesModel model = new MessagesModel(senderId, message);
                model.setTimestamp(new Date().getTime());

                binding.etMessage.setText("");

                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });


    }

}