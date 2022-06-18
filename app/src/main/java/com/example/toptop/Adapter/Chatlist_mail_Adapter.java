package com.example.toptop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.ChatActivity;
import com.example.toptop.Models.userObject;
import com.example.toptop.My_interface.Onclick_user_mail;
import com.example.toptop.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Chatlist_mail_Adapter extends RecyclerView.Adapter<Chatlist_mail_Adapter.MyHolder>implements Filterable {
    Context context;
    List<userObject> userObjectList;
    List<userObject> userObjectList1;
    private HashMap<String, String> lastMessageMap;
    private Onclick_user_mail onclick_user_mail;

    public Chatlist_mail_Adapter(Context context, List<userObject> userObjectList,Onclick_user_mail onclick_user_mail) {
        this.context = context;
        this.userObjectList = userObjectList;
        this.userObjectList1=userObjectList;
        lastMessageMap = new HashMap<>();
        this.onclick_user_mail=onclick_user_mail;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        userObject hisuser = userObjectList.get(position);
        String hisUid = userObjectList.get(position).getUser_id();
        String userImage = userObjectList.get(position).getProfileImage();
        String username = userObjectList.get(position).getUser_name();
        String lastMes = lastMessageMap.get(hisUid);

        holder.name.setText(username);
        if (lastMes == null || lastMes.equals("default")) {
            holder.lastMessage.setVisibility(View.GONE);
        } else  {
            holder.lastMessage.setVisibility(View.VISIBLE);
            holder.lastMessage.setText(lastMes);
        }
        try {
            Picasso.get().load(userImage).placeholder(R.drawable.avatar).into(holder.imgprofile);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.avatar).into(holder.imgprofile);
        }
        if (userObjectList.get(position).getOnlineStatus().equals("online")) {
           holder.onlineStatus.setImageResource(R.drawable.ic_baseline_circle_24_on);
        } else holder.onlineStatus.setImageResource(R.drawable.ic_baseline_circle_24);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(context, ChatActivity.class);
        intent.putExtra("hisUid",hisUid);
        context.startActivities(new Intent[]{intent});
    }
});
        holder.imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_user_mail.onClickItemUser(hisuser);
                Activity activity = (Activity) context;
                activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
    }
    public void setLastMessageMap(String userId,String lastMes){
        lastMessageMap.put(userId,lastMes);
}
    @Override
    public int getItemCount() {
        return userObjectList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String ssearch=constraint.toString();
                if(ssearch.isEmpty()){
                    userObjectList=userObjectList1;
                }else {
                    List<userObject> list=new ArrayList<>();
                    for(userObject us:userObjectList1){
                        if(us.getUser_name().toLowerCase().contains(ssearch.toLowerCase())){
                            list.add(us);
                        }
                    }
                    userObjectList=list;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=userObjectList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userObjectList=(List<userObject>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView imgprofile, onlineStatus;
        TextView name, lastMessage;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imgprofile = itemView.findViewById(R.id.img_user_mail_chatlist);
            onlineStatus = itemView.findViewById(R.id.onlineStatus_chatlist);
            name = itemView.findViewById(R.id.nametv_chatlist);
            lastMessage = itemView.findViewById(R.id.mes_chatlist);
        }


    }
}
