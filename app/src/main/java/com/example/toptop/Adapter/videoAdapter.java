package com.example.toptop.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toptop.CommentActivity;
import com.example.toptop.HomeActivity;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.My_interface.Onclick_Item_Video_profile;
import com.example.toptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class videoAdapter extends RecyclerView.Adapter<videoAdapter.videoViewHolder> {
    private List<MediaObjectt> mediaObjecttList;
    private Onclick_Item_Video_profile iclick_Item_video;
    Context context;
    //
    String myuId;
    private DatabaseReference likeRef;
    private DatabaseReference videosRef;
    private DatabaseReference FollowRef;
    private DatabaseReference userRef;
    boolean mProcessLike = false;
    boolean mProcessfollow = false;
    public videoAdapter(List<MediaObjectt> mediaObjecttList, Onclick_Item_Video_profile iclick_Item_video, Context context) {
        this.mediaObjecttList = mediaObjecttList;
        this.iclick_Item_video = iclick_Item_video;
        this.context = context;
        myuId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        likeRef = FirebaseDatabase.getInstance().getReference().child("likes");
        videosRef = FirebaseDatabase.getInstance().getReference().child("videos");
        FollowRef=FirebaseDatabase.getInstance().getReference().child("follows");
        userRef=FirebaseDatabase.getInstance().getReference().child("users");
    }


    @NonNull
    @Override
    public videoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main, parent, false);
        return new videoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull videoViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MediaObjectt media = mediaObjecttList.get(position);
        if (media == null) {
            return;
        }
        String plikes=mediaObjecttList.get(position).getVideo_heart();
        holder.tim.setText(plikes);
        holder.users.setText(media.getUser_name());
        holder.videoView.setVideoPath(media.getVideo_uri());
        holder.des.setText(media.getVideo_des());
        holder.hasttask.setText(media.getHast_task_name());
        holder.comeen.setText(media.getVideo_comment());
        holder.amthanh.setText(media.getSound_name());
        Glide.with(context).load(media.getProfileImage()).into(holder.img);
        Glide.with(context).load(media.getProfileImage()).into(holder.img2);
        holder.amthanh.startAnimation(holder.a);
      //
        //
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
        holder.img_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int plikes = Integer.parseInt(mediaObjecttList.get(position).getVideo_heart());
                mProcessLike = true;
                String videoId = mediaObjecttList.get(position).getVideo_id();
                likeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mProcessLike) {
                            if (snapshot.child(videoId).hasChild(myuId)) {
                                videosRef.child(videoId).child("video_heart").setValue("" + (plikes - 1));
                                likeRef.child(videoId).child(myuId).removeValue();
                                mProcessLike = false;
                            } else {
                                videosRef.child(videoId).child("video_heart").setValue("" + (plikes + 1));
                                likeRef.child(videoId).child(myuId).setValue("Liked");
                                mProcessLike=false;
                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        final String videoId = mediaObjecttList.get(position).getVideo_id();
        setlikes(holder,videoId);
        holder.img_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProcessfollow=true;
                String muserId = mediaObjecttList.get(position).getUser_id();
                FollowRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(mProcessfollow){
                        if (snapshot.child(muserId).hasChild(myuId)) {
                            FollowRef.child(muserId).child(myuId).removeValue();
                            mProcessfollow = false;

                        } else {
                            FollowRef.child(muserId).child(myuId).setValue("1");
                            mProcessfollow = false;
                        }
                    }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
       final String muserId = mediaObjecttList.get(position).getUser_id();
       setfollows(holder,muserId);
       holder.img_comments.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Activity activity = (Activity) context;
               Intent intent=new Intent(context, CommentActivity.class);
               intent.putExtra("oj",media.getVideo_id());
               intent.putExtra("ojj",media.getUser_id());
               intent.putExtra("ojjj",media.getUser_name());
               activity.startActivity(intent);
               //hieu ung
               activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
               //
           }
       });
    }
    private void setlikes(videoViewHolder holder, String videoId) {
        likeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(videoId).hasChild(myuId)){
                    holder.img_heart.setColorFilter(ContextCompat.getColor(context,R.color.red), PorterDuff.Mode.MULTIPLY);
                }
                else {
                    holder.img_heart.setColorFilter(ContextCompat.getColor(context,R.color.white), PorterDuff.Mode.MULTIPLY);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setfollows(videoViewHolder holder, String muserId) {
        FollowRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(muserId).hasChild(myuId)){
                    holder.img_follow.setVisibility(View.INVISIBLE);
                }
                else {
                    holder.img_follow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        //        private ConstraintLayout layout_main;
        Animation a;
        MediaPlayer mediaPlayer;
        private CircleImageView circleImageView;
        VideoView videoView;
        ImageView img, img2,img_heart,img_follow,img_comments;
        TextView users, des, tim, comeen, amthanh, hasttask;
        ProgressBar progressBar;
        MediaController controller;
        private HomeActivity mhomeActivity;

        public videoViewHolder(@NonNull View itemView) {
            super(itemView);
            img_heart=itemView.findViewById(R.id.img_heart_main);
            img_follow=itemView.findViewById(R.id.img_follow);
            img_comments=itemView.findViewById(R.id.img_comments);
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
            mhomeActivity=(HomeActivity)context;

        }

        void setcontroleer() {

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
