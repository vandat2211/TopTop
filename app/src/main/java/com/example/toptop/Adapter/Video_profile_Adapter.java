package com.example.toptop.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.My_interface.Onclick_Item_Video_profile;
import com.example.toptop.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Video_profile_Adapter extends RecyclerView.Adapter<Video_profile_Adapter.videoViewHolder>{
    private List<MediaObjectt> mediaObjecttList;
    private Onclick_Item_Video_profile iclick_Item_video;

    public Video_profile_Adapter(List<MediaObjectt> mediaObjecttList,Onclick_Item_Video_profile iclick_Item_video) {
        this.mediaObjecttList = mediaObjecttList;
        this.iclick_Item_video=iclick_Item_video;
    }

    @NonNull
    @Override
    public Video_profile_Adapter.videoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_profile, parent, false);
        return new Video_profile_Adapter.videoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Video_profile_Adapter.videoViewHolder holder, int position) {
        MediaObjectt media = mediaObjecttList.get(position);
        if (media == null) {
            return;
        }

        holder.videoView.setVideoPath(media.getVideo_uri());
        holder.heart.setText(media.getVideo_heart());
        holder.videoView.setOnPreparedListener(mp -> {
            mp.start();
          mp.setVolume(0,0);

        });
        holder.videoView.setOnCompletionListener(mp -> mp.start());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclick_Item_video.onClickItemVideo(media);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mediaObjecttList != null) {
            return mediaObjecttList.size();
        }
        return 0;
    }
    public class videoViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layout;
        MediaPlayer mediaPlayer;
        VideoView videoView;
        TextView heart;

        public videoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoview1);
           heart=itemView.findViewById(R.id.tv_soluotthich);
           layout=itemView.findViewById(R.id.layout_profile_video);
        }

    }
}
