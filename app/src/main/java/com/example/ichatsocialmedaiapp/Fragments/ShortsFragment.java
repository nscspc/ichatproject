package com.example.ichatsocialmedaiapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ichatsocialmedaiapp.Adapter.VideoAdapter;
import com.example.ichatsocialmedaiapp.Model.VIdeoModel;
import com.example.ichatsocialmedaiapp.R;
import com.example.ichatsocialmedaiapp.databinding.FragmentShortsBinding;

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
        // Inflate the layout for this fragment
        List<VIdeoModel> vIdeoModelList=new ArrayList<>();
        getContext().getTheme().applyStyle(R.style.FullScreen, true);
        binding= FragmentShortsBinding.inflate(inflater, container, false);
        vIdeoModelList.add(new VIdeoModel("android.resource://" +getActivity().getPackageName() + "/" + R.raw.a));
        vIdeoModelList.add(new VIdeoModel("android.resource://" +getActivity().getPackageName() + "/" + R.raw.falling));
        vIdeoModelList.add(new VIdeoModel("android.resource://" +getActivity().getPackageName() + "/" + R.raw.b));
        vIdeoModelList.add(new VIdeoModel("android.resource://" +getActivity().getPackageName() + "/" + R.raw.d));
        vIdeoModelList.add(new VIdeoModel("android.resource://" +getActivity().getPackageName() + "/" + R.raw.e));
        vIdeoModelList.add(new VIdeoModel("android.resource://" +getActivity().getPackageName() + "/" + R.raw.f));
        vIdeoModelList.add(new VIdeoModel("android.resource://" +getActivity().getPackageName() + "/" + R.raw.falling));

        VideoAdapter adapter=new VideoAdapter(vIdeoModelList,getActivity());
        binding.viewgager.setAdapter(adapter);
        return binding.getRoot();
    }
}