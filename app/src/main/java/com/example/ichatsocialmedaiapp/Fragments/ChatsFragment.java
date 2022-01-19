package com.example.ichatsocialmedaiapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ichatsocialmedaiapp.Adapter.UsersAdapterChatting;
import com.example.ichatsocialmedaiapp.Model.Users;
import com.example.ichatsocialmedaiapp.Model.UsersChatting;
import com.example.ichatsocialmedaiapp.R;
import com.example.ichatsocialmedaiapp.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    FragmentChatsBinding binding;
    ArrayList<UsersChatting> list=new ArrayList<>();
    FirebaseDatabase database;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);

        UsersAdapterChatting adapter = new UsersAdapterChatting(list , getContext());
        binding.chatRV.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRV.setLayoutManager(layoutManager);

        database.getReference().child("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            UsersChatting users = dataSnapshot.getValue(UsersChatting.class);
//                            users.getUserId(dataSnapshot.getKey());
//                            users.getUserId(dataSnapshot.getKey());
                            users.setUserId(dataSnapshot.getKey());
                            if(!users.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                            {//to hide the current user and show others users only.
                                list.add(users);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return binding.getRoot();

    }
}