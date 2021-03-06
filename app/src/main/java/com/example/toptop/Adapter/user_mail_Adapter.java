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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toptop.ChatActivity;
import com.example.toptop.Models.userObject;
import com.example.toptop.My_interface.Onclick_user_mail;
import com.example.toptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class user_mail_Adapter extends RecyclerView.Adapter<user_mail_Adapter.MyHolder> implements Filterable {
Context context;
List<userObject> userObjectList;
List<userObject> userObjectList1;
private Onclick_user_mail onclick_user_mail;
String myuId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public user_mail_Adapter(Context context, List<userObject> userObjectList,Onclick_user_mail onclick_user_mail) {
        this.context = context;
        this.userObjectList = userObjectList;
        this.userObjectList1=userObjectList;
        this.onclick_user_mail=onclick_user_mail;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new user_mail_Adapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        userObject user = userObjectList.get(position);
        if (user == null) {
            return;
        }
        holder.name_user_mail.setText(user.getUser_name());
        holder.email_user_mail.setText(user.getEmail());
        Glide.with(context).load(user.getProfileImage()).error(R.drawable.avatar).into(holder.img_user_mail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference myRefcheck = FirebaseDatabase.getInstance().getReference("follows");
                myRefcheck.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(user.getUser_id()).hasChild(myuId) && snapshot.child(myuId).hasChild(user.getUser_id())) {
                            Activity activity = (Activity) context;
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("hisUid", user.getUser_id());
                            context.startActivities(new Intent[]{intent});
                            activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);

                        }
                        else if(!snapshot.child(user.getUser_id()).hasChild(myuId)&&snapshot.child(myuId).hasChild(user.getUser_id())) {
                            Toast.makeText(context,"Ban ko the chat voi " +user.getUser_name() +". vi ban chua folow " +user.getUser_name(),Toast.LENGTH_SHORT).show();
                        }else if(snapshot.child(user.getUser_id()).hasChild(myuId)&&!snapshot.child(myuId).hasChild(user.getUser_id())) {
                            Toast.makeText(context,"Ban ko the chat voi " + user.getUser_name()+". Vi "+user.getUser_name()+" chua folow ban. ",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(context, "Ban ko the chat voi " + user.getUser_name()+ ". vi ca 2 chua folow nhau ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        holder.img_user_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_user_mail.onClickItemUser(user);
                Activity activity = (Activity) context;
                activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
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

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView img_user_mail;
        TextView name_user_mail,email_user_mail;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            img_user_mail=itemView.findViewById(R.id.img_user_mail);
            name_user_mail=itemView.findViewById(R.id.nametv);
            email_user_mail=itemView.findViewById(R.id.emailtv);
        }
    }
}
