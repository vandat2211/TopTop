package com.example.toptop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.HomeActivity;
import com.example.toptop.Models.Deltail;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.Models.userObject;
import com.example.toptop.My_interface.Onclick_Item_Video_profile;
import com.example.toptop.R;

import java.util.ArrayList;
import java.util.List;

public class Search_deltail_Adapter extends RecyclerView.Adapter<Search_deltail_Adapter.Search_deltailViewHolder> implements Filterable {

    private Context context;
    private List<Deltail>deltailList;
    List<Deltail> deltailList1;
    private HomeActivity mhomeActivity;
    public Search_deltail_Adapter(Context context) {
        this.context = context;
    }
    public void setdata(List<Deltail> deltailList){
        mhomeActivity=(HomeActivity)context;
        this.deltailList=deltailList;
        this.deltailList1=deltailList;
    }

    @NonNull
    @Override
    public Search_deltailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_hasttask_video, parent, false);
        return new Search_deltailViewHolder(view );
    }

    @Override
    public void onBindViewHolder(@NonNull Search_deltailViewHolder holder, int position) {
        Deltail deltail=deltailList.get(position);
        if(deltail==null){
            return;
        }
        holder.tv_hast_task.setText(deltailList.get(position).getNamehasttask());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        holder.rcv_deltail.setLayoutManager(linearLayoutManager);

        Search_Adapter search_adapter=new Search_Adapter();
        search_adapter.setdata(deltail.getMedia(), new Onclick_Item_Video_profile() {
            @Override
            public void onClickItemVideo(MediaObjectt media) {
                mhomeActivity.onClickfromsearch_GoToOpenVideo_Fragment(media);
            }
        });
        holder.rcv_deltail.setAdapter(search_adapter);
    }

    @Override
    public int getItemCount() {
        if(deltailList!=null){
            return deltailList.size() ;
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String ssearch=constraint.toString();
                if(ssearch.isEmpty()){
                    deltailList=deltailList1;
                }else {
                    List<Deltail> list=new ArrayList<>();
                    for(Deltail us:deltailList1){
                        if(us.getNamehasttask().toLowerCase().contains(ssearch.toLowerCase())){
                            list.add(us);
                        }
                    }
                    deltailList=list;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=deltailList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                deltailList=(List<Deltail>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Search_deltailViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_hast_task;
        private RecyclerView rcv_deltail;
        public Search_deltailViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_hast_task = itemView.findViewById(R.id.tv_name_hasttask);
            rcv_deltail=itemView.findViewById(R.id.recy_video_deltail);
        }
    }
}
