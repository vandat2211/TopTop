package com.example.toptop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.My_interface.Onclick_Item_Video_profile;
import com.example.toptop.R;

import java.util.List;

public class Search_Adapter extends RecyclerView.Adapter<Search_Adapter.SearchViewHolder>{
    private List<MediaObjectt> mediaObjecttList;
    private Onclick_Item_Video_profile iclick_Item_video;
    public void setdata(List<MediaObjectt> mediaObjecttList,Onclick_Item_Video_profile iclick_Item_video){
        this.mediaObjecttList=mediaObjecttList;
        this.iclick_Item_video=iclick_Item_video;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_video, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
    MediaObjectt media=mediaObjecttList.get(position);
    if(media==null){
        return;
    }
        holder.videoView.setVideoPath(media.getVideo_uri());
        holder.videoView.setOnPreparedListener(mp -> {
            mp.start();
            mp.pause();
            mp.setVolume(0,0);

        });
        holder.videoView.setOnCompletionListener(mp -> mp.start());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclick_Item_video.onClickItemVideo(media);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mediaObjecttList!=null){
            return mediaObjecttList                                                                                                                                                                                                                                                                                                                                                                                                                         .size();
        }
        return 0;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder  {
        VideoView videoView;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoview_deltail);
        }


    }
}
