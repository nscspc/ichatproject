package com.example.ichatsocialmedaiapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ichatsocialmedaiapp.Adapter.notificationTabRvAdapter;
import com.example.ichatsocialmedaiapp.Model.notificationModel;
import com.example.ichatsocialmedaiapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class notificationTabFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<notificationModel> list;
    FirebaseDatabase database;

    public notificationTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database=FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_tab, container, false);

        recyclerView=view.findViewById(R.id.notificationTabRV);

        list=new ArrayList<>();
//        list.add(new notificationModel(R.drawable.deaf,"<b>Sweety</b> is live right now .... ","few minutes ago"));

        notificationTabRvAdapter adapter = new notificationTabRvAdapter(list,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        database.getReference()
                .child("notification")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            notificationModel notificationmodel = dataSnapshot.getValue(notificationModel.class);
                            notificationmodel.setNotificationID(dataSnapshot.getKey());
                            list.add(notificationmodel);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        return view;

    }
}