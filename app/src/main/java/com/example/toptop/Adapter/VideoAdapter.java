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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chibde.visualizer.CircleBarVisualizer;
import com.example.toptop.Models.MediaObject;
import com.example.toptop.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.lang.reflect.Array;
import java.time.format.DateTimeFormatter;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoAdapter extends FirebaseRecyclerAdapter<MediaObject,VideoAdapter.myviewholder>
{
    Context context;
    public VideoAdapter(@NonNull FirebaseRecyclerOptions<MediaObject> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull MediaObject model) {
        context = holder.itemView.getContext();
        holder.setdata(model);
        Glide.with(context).load(model.getImg()).into(holder.img);
        Glide.with(context).load(model.getImg()).into(holder.img2);
        holder.amthanh.startAnimation(holder.a);
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                holder.circleImageView.animate().rotationBy(360). withEndAction(this).setDuration(10000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };
        holder.circleImageView.animate().rotationBy(360). withEndAction(runnable).setDuration(10000)
                .setInterpolator(new LinearInterpolator()).start();
//        holder.circleBarVisualizer.setColor(ContextCompat.getColor(context, R.color.custom_color_nav));
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        CircleBarVisualizer circleBarVisualizer;
        Animation a;
        MediaPlayer mediaPlayer;
        private CircleImageView circleImageView;
        VideoView videoView;
        ImageView img,img2;
        TextView users,des,tim,comeen,amthanh;
        ProgressBar progressBar;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
//            circleBarVisualizer = itemView.findViewById(R.id.visualizer);
            circleImageView =itemView.findViewById(R.id.img_amthanh);
            videoView=itemView.findViewById(R.id.videoview);
            users=itemView.findViewById(R.id.tv_users);
            des=itemView.findViewById(R.id.tv_mota);
            tim=itemView.findViewById(R.id.tv_soluot);
            img=itemView.findViewById(R.id.user);
            img2=itemView.findViewById(R.id.img_amthanh);
            comeen=itemView.findViewById(R.id.tv_commen);
            amthanh=itemView.findViewById(R.id.tv_amthanh);
            progressBar =itemView.findViewById(R.id.progressBar);
            a = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.amin_right);
        }
        void setdata(MediaObject media) {
            videoView.setVideoPath(media.getMedia_url());
            users.setText(media.getUsers());
            des.setText(media.getDescription());
            tim.setText(media.getTim());
            comeen.setText(media.getComment());
            amthanh.setText(media.getSound());
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    progressBar.setVisibility(View.GONE);
                    mp.start();

                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });
        }
    }

}
