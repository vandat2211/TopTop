package com.example.toptop.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.Models.userObject;
import com.example.toptop.My_interface.Onclick_Item_Video_profile;
import com.example.toptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class videoAdapter extends RecyclerView.Adapter<videoAdapter.videoViewHolder> {
    private List<MediaObjectt> mediaObjecttList;
    private Onclick_Item_Video_profile iclick_Item_video;
    Context context;
    public videoAdapter(List<MediaObjectt> mediaObjecttList, Onclick_Item_Video_profile iclick_Item_video, Context context) {
        this.mediaObjecttList = mediaObjecttList;
        this.iclick_Item_video = iclick_Item_video;
        this.context = context;
    }




    @NonNull
    @Override
    public videoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main, parent, false);
        return new videoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull videoViewHolder holder, int position) {
        MediaObjectt media = mediaObjecttList.get(position);
        if (media == null) {
            return;
        }
        holder.users.setText(media.getUser_name());
        holder.videoView.setVideoPath(media.getVideo_uri());
        holder.des.setText(media.getVideo_des());
        holder.hasttask.setText(media.getHast_task_name());
        holder.tim.setText(media.getVideo_heart());
        holder.comeen.setText(media.getVideo_comment());
        holder.amthanh.setText(media.getSound_name());
        Glide.with(context).load(media.getProfileImage()).into(holder.img);
        Glide.with(context).load(media.getProfileImage()).into(holder.img2);
        holder.amthanh.startAnimation(holder.a);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclick_Item_video.onClickItemVideo(media);
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                holder.circleImageView.animate().rotationBy(360).withEndAction(this).setDuration(10000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };
        holder.circleImageView.animate().rotationBy(360).withEndAction(runnable).setDuration(10000)
                .setInterpolator(new LinearInterpolator()).start();
        holder.setcontroleer();


    }

    @Override
    public int getItemCount() {
        if (mediaObjecttList != null) {
            return mediaObjecttList.size();
        }
        return 0;
    }

    public class videoViewHolder extends RecyclerView.ViewHolder {
//        private ConstraintLayout layout_main;
        Animation a;
        MediaPlayer mediaPlayer;
        private CircleImageView circleImageView;
        VideoView videoView;
        ImageView img, img2;
        TextView users, des, tim, comeen, amthanh,hasttask;
        ProgressBar progressBar;
        MediaController controller;

        public videoViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.img_amthanh);
            videoView = itemView.findViewById(R.id.videoview);
            users = itemView.findViewById(R.id.tv_users);
            des = itemView.findViewById(R.id.tv_mota);
            hasttask = itemView.findViewById(R.id.tv_hast_taskk);
            tim = itemView.findViewById(R.id.tv_soluot);
            img = itemView.findViewById(R.id.user);
            img2 = itemView.findViewById(R.id.img_amthanh);
            comeen = itemView.findViewById(R.id.tv_commen);
            amthanh = itemView.findViewById(R.id.tv_amthanh);
            progressBar = itemView.findViewById(R.id.progressBar);
            a = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.amin_right);
//            layout_main=itemView.findViewById(R.id.layout_main);

        }
       void setcontroleer(){

           videoView.setOnPreparedListener(mp -> {
               progressBar.setVisibility(View.GONE);
               mp.start();

           });
           videoView.setOnCompletionListener(mp -> mp.start());
           controller = new MediaController(context);
           videoView.setMediaController(controller);
           controller.setAnchorView(videoView);

//        holder.controller.setPadding(0,0,0,1000);
       }

    }


}
