package com.example.toptop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.R;

import java.util.List;

public class Search_Adapter extends RecyclerView.Adapter<Search_Adapter.SearchViewHolder>{
    private List<MediaObjectt> mdlist;
    public void setdata(List<MediaObjectt> md){
        this.mdlist=md;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
    MediaObjectt media=mdlist.get(position);
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
    }

    @Override
    public int getItemCount() {
        if(mdlist!=null){
            return mdlist.size();
        }
        return 0;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoview_deltail);
        }
    }
}
