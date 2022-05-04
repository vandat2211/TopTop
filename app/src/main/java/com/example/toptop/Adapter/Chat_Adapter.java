package com.example.toptop.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toptop.ChatActivity;
import com.example.toptop.Models.Chat;
import com.example.toptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Chat_Adapter extends RecyclerView.Adapter<Chat_Adapter.MyHolder> {
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<Chat> chatList;
    String imageUrl;
    FirebaseUser fuser;

    public Chat_Adapter(Context context, List<Chat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (i == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        String mes = chatList.get(position).getMessage();
        String timestamp = chatList.get(position).getTimestamp();
        String type = chatList.get(position).getType();
        // convert timestamp to dd/mm/yyyy hh:mm am/pm
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp));
        String datetime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
        //
        if(type.equals("text")){
             holder.tv_message.setVisibility(View.VISIBLE);
             holder.mes_img.setVisibility(View.GONE);
            holder.tv_message.setText(mes);
        }
else{
            holder.tv_message.setVisibility(View.GONE);
            holder.mes_img.setVisibility(View.VISIBLE);
            Picasso.get().load(mes).placeholder(R.drawable.ic_baseline_image_24).into(holder.mes_img);
        }

        holder.tv_time.setText(datetime);
        try {
            Picasso.get().load(imageUrl).into(holder.img);
        } catch (Exception e) {

        }

        //click to show delete dialog
        holder.messagelayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete message ?");
                //delete button
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteMes(position);
                    }
                });
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss dialog
                        dialog.dismiss();
                    }
                });
                //
                builder.create().show();
                return false;
            }
        });


        //set seen/delivered status
        if (position == chatList.size() - 1) {
            if (chatList.get(position).isSeen()) {
                holder.tv_isSeen.setText("Seen");
            } else {
                holder.tv_isSeen.setText("Delivered");
            }
        } else {
            holder.tv_isSeen.setVisibility(View.GONE);
        }
    }

    private void DeleteMes(int position) {
        String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String msgTimestamp = chatList.get(position).getTimestamp();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("chats");
        Query queryy = ref.orderByChild("timestamp").equalTo(msgTimestamp);
        queryy.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("sender").getValue().equals(myUid)) {
                        //remove the message from chats
                        ds.getRef().removeValue();
                        //set the value of message"this message was delete...
//                        HashMap<String,Object>hashMap=new HashMap<>();
//                        hashMap.put("message","This message was delete...");
//                        ds.getRef().updateChildren(hashMap);


                        Toast.makeText(context, "Message delete...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "You can't this message.", Toast.LENGTH_SHORT).show();
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
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView img,mes_img;
        TextView tv_message, tv_time, tv_isSeen;
        LinearLayout messagelayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgnameuser);
            mes_img=itemView.findViewById(R.id.mes_img);
            tv_message = itemView.findViewById(R.id.tv_mess);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_isSeen = itemView.findViewById(R.id.tv_isseent);
            messagelayout = itemView.findViewById(R.id.messagelayout);

        }
    }
}
