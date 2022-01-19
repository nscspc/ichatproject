package com.example.ichatsocialmedaiapp.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ichatsocialmedaiapp.Model.VIdeoModel;
import com.example.ichatsocialmedaiapp.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

List<VIdeoModel> vIdeoModelList;
Context context;
int i;

    public VideoAdapter(List<VIdeoModel> vIdeoModelList, Context context) {
        this.vIdeoModelList = vIdeoModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.short_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.videoView.setVideoPath(vIdeoModelList.get(position).getVideoUrl());
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                float videoRatio=mp.getVideoWidth()/(float) mp.getVideoHeight();
                float screenRatio=holder.videoView.getWidth()/(float) holder.videoView.getHeight();
                float scale=videoRatio/screenRatio;
                if (scale>=1f){
                    holder.videoView.setScaleX(scale+1);
                }else {
                    holder.videoView.setScaleY(1f/scale);
                }
            }
        });
        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
//                while (i<10)
//                {
//                    mp.start();
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                };
                mp.start();

            }
        });
    }

    @Override
    public int getItemCount() {
        return vIdeoModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
VideoView videoView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView=itemView.findViewById(R.id.VideoView);
        }
    }

}
