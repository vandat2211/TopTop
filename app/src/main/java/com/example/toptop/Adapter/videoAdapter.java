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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.Models.userObject;
import com.example.toptop.R;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class videoAdapter extends RecyclerView.Adapter<videoAdapter.videoViewHolder> {
    private List<MediaObjectt> mediaObjecttList;

    public videoAdapter(List<MediaObjectt> mediaObjecttList, Context context) {
        this.mediaObjecttList = mediaObjecttList;
        this.context = context;
    }

    Context context;
    public videoAdapter(List<MediaObjectt> mediaObjecttList) {
        this.mediaObjecttList = mediaObjecttList;
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
        holder.tim.setText("" + media.getVideo_heart());
        holder.comeen.setText("" + media.getVideo_comment());
        holder.amthanh.setText(media.getSound_name());
        Glide.with(context).load(media.getProfileImage()).into(holder.img);
        Glide.with(context).load(media.getProfileImage()).into(holder.img2);
        holder.amthanh.startAnimation(holder.a);

        holder.videoView.setOnPreparedListener(mp -> {
            holder.progressBar.setVisibility(View.GONE);
            mp.start();

        });
        holder.videoView.setOnCompletionListener(mp -> mp.start());

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                holder.circleImageView.animate().rotationBy(360). withEndAction(this).setDuration(10000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };
        holder.circleImageView.animate().rotationBy(360). withEndAction(runnable).setDuration(10000)
                .setInterpolator(new LinearInterpolator()).start();

    }

    @Override
    public int getItemCount() {
        if (mediaObjecttList != null) {
            return mediaObjecttList.size();
        }
        return 0;
    }

    public class videoViewHolder extends RecyclerView.ViewHolder {

        Animation a;
        MediaPlayer mediaPlayer;
        private CircleImageView circleImageView;
        VideoView videoView;
        ImageView img, img2;
        TextView users, des, tim, comeen, amthanh;
        ProgressBar progressBar;

        public videoViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.img_amthanh);
            videoView = itemView.findViewById(R.id.videoview);
            users = itemView.findViewById(R.id.tv_users);
            des = itemView.findViewById(R.id.tv_mota);
            tim = itemView.findViewById(R.id.tv_soluot);
            img = itemView.findViewById(R.id.user);
            img2 = itemView.findViewById(R.id.img_amthanh);
            comeen = itemView.findViewById(R.id.tv_commen);
            amthanh = itemView.findViewById(R.id.tv_amthanh);
            progressBar = itemView.findViewById(R.id.progressBar);
            a = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.amin_right);
        }

    }


}
