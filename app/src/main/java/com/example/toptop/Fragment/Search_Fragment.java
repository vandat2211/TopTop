package com.example.toptop.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.Adapter.Search_deltail_Adapter;
import com.example.toptop.Models.Deltail;
import com.example.toptop.Models.Hasttask;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Search_Fragment extends Fragment {
    private RecyclerView rcv_search_deltail;
    private TextView tv_tool_bar;
    private Search_deltail_Adapter adapter;
    String name_hasttask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search,container,false);
        rcv_search_deltail=view.findViewById(R.id.recyclerView_search_deltail);
        tv_tool_bar=view.findViewById(R.id.tv_tool_bar);
        tv_tool_bar.setText("Kham pha");
        adapter=new Search_deltail_Adapter(getContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rcv_search_deltail.setLayoutManager(linearLayoutManager);
        adapter.setdata(getListdeltail());
        rcv_search_deltail.setAdapter(adapter);
        return view;
    }

    private List<Deltail> getListdeltail() {
        Bundle bundle=getArguments();
        String a= (String) bundle.get("hask_task");
         List<Deltail> list=new ArrayList<>();
         List<MediaObjectt>mediaObjecttList=new ArrayList<>();

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("videos");
        Query myquery=myRef.orderByChild("hast_task_name").equalTo(a);
        myquery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                list.clear();
                MediaObjectt mediaObjectt=snapshot.getValue(MediaObjectt.class);
                if(mediaObjectt!=null){
                    mediaObjecttList.add(mediaObjectt);
                    list.add(new Deltail("#tet",mediaObjecttList));
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MediaObjectt mediaObjectt=snapshot.getValue(MediaObjectt.class);
                if(mediaObjectt==null || mediaObjecttList==null || mediaObjecttList.isEmpty()){
                    return;
                }
                for (int i=0;i<mediaObjecttList.size();i++){
                    if( mediaObjectt.getVideo_id()==mediaObjecttList.get(i).getVideo_id()){
                        mediaObjecttList.set(i,mediaObjectt);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
         return list;
    }
}
