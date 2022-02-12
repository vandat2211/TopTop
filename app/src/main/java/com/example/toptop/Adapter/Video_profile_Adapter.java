package com.example.toptop.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.My_interface.OnLongclick_Item_Video_profile;
import com.example.toptop.My_interface.Onclick_Item_Video_profile;
import com.example.toptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Video_profile_Adapter extends RecyclerView.Adapter<Video_profile_Adapter.videoViewHolder>{
    private List<MediaObjectt> mediaObjecttList;
    Context context;
    private Onclick_Item_Video_profile iclick_Item_video;
    private OnLongclick_Item_Video_profile longclick_item_video_profile;

    public Video_profile_Adapter(List<MediaObjectt> mediaObjecttList,Onclick_Item_Video_profile iclick_Item_video,OnLongclick_Item_Video_profile longclick_item_video_profile,Context context) {
        this.mediaObjecttList = mediaObjecttList;
        this.context = context;
        this.iclick_Item_video=iclick_Item_video;
        this.longclick_item_video_profile=longclick_item_video_profile;
    }

    @NonNull
    @Override
    public Video_profile_Adapter.videoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_profile, parent, false);
        return new Video_profile_Adapter.videoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Video_profile_Adapter.videoViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose");
                builder.setMessage("You want to Edit or Delete");
                //delete button
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteMes(position);
                    }
                });
                builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        longclick_item_video_profile.onClickItemVideo(media);
                    }
                });
//            builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //dismiss dialog
//                        dialog.dismiss();
//                    }
//                });

                builder.create().show();
                return false;
            }
        });
    }

    private void DeleteMes(int position) {
        String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String videoid=mediaObjecttList.get(position).getVideo_id();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("videos");
        Query queryy = ref.orderByChild("video_id").equalTo(videoid);
        queryy.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("user_id").getValue().equals(myUid)) {
                        //remove the message from chats
                        ds.getRef().removeValue();
                        Toast.makeText(context, "Video delete...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "You can't this video.", Toast.LENGTH_SHORT).show();
                    }

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
        private ConstraintLayout layout;
        ImageView imheart;
        MediaPlayer mediaPlayer;
        VideoView videoView;
        TextView heart;

        public videoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoview1);
           heart=itemView.findViewById(R.id.tv_soluotthich);
           imheart=itemView.findViewById(R.id.img_heartt);
           layout=itemView.findViewById(R.id.layout_profile_video);
        }

    }
}
