package com.example.toptop.Adapter;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.My_interface.Onclick_Item_Video_profile;
import com.example.toptop.R;

import java.util.List;

public class Video_customer_Adapter extends RecyclerView.Adapter<Video_customer_Adapter.VideoCustomerViewHolder> {
    private List<MediaObjectt> mediaObjecttList;

    public Video_customer_Adapter(List<MediaObjectt> mediaObjecttList, Onclick_Item_Video_profile iclick_Item_video) {
        this.mediaObjecttList = mediaObjecttList;
        this.iclick_Item_video = iclick_Item_video;
    }

    private Onclick_Item_Video_profile iclick_Item_video;
    @NonNull
    @Override
    public VideoCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_profile, parent, false);
        return new Video_customer_Adapter.VideoCustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoCustomerViewHolder holder, int position) {
        MediaObjectt media = mediaObjecttList.get(position);
        if (media == null) {
            return;
        }

        holder.videoView.setVideoPath(media.getVideo_uri());
        holder.heart.setText("" + media.getVideo_heart());
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

    public class VideoCustomerViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout layout;
        MediaPlayer mediaPlayer;
        VideoView videoView;
        TextView heart;
        public VideoCustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoview1);
            heart=itemView.findViewById(R.id.tv_soluotthich);
            layout=itemView.findViewById(R.id.layout_profile_video);
        }
    }
}
