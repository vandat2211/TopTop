package com.example.toptop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.Models.Photo;
import com.example.toptop.R;

import java.util.List;

public class Photo_Viewpager2_Adapter extends RecyclerView.Adapter<Photo_Viewpager2_Adapter.PhotoViewHolder> {
    private List<Photo> photoList;

    public Photo_Viewpager2_Adapter(List<Photo> photoList) {
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = photoList.get(position);
        if (photo == null) {
            return;
        }
        holder.photoimg.setImageResource(photo.getResourceId());
    }

    @Override
    public int getItemCount() {
        if (photoList != null)
            return photoList.size();
        return 0;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView photoimg;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoimg = itemView.findViewById(R.id.img_photo);
        }
    }
}
