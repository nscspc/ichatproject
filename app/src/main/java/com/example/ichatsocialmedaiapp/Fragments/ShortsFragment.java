package com.example.ichatsocialmedaiapp.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ichatsocialmedaiapp.Adapter.VideoAdapter;
import com.example.ichatsocialmedaiapp.Model.VIdeoModel;
import com.example.ichatsocialmedaiapp.R;
import com.example.ichatsocialmedaiapp.databinding.FragmentShortsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ShortsFragment extends Fragment {

    public ShortsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentShortsBinding binding;
//         Inflate the layout for this fragment
        List<VIdeoModel> vIdeoModelList=new ArrayList<>();
        getContext().getTheme().applyStyle(R.style.FullScreen, true);
        binding= FragmentShortsBinding.inflate(inflater, container, false);
        FirebaseStorage.getInstance().getReference();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("shorts/[000486].mp4");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String path=uri.toString();
                vIdeoModelList.add(new VIdeoModel(path));
                VideoAdapter adapter=new VideoAdapter(vIdeoModelList,getActivity());
                binding.viewgager.setAdapter(adapter);
            }
        });

        String path;

        return binding.getRoot();
    }
}