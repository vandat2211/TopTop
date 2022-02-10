package com.example.toptop.Fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.example.toptop.MainActivity;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.transform.Result;


public class Create_Fragment extends Fragment {
    private EditText eddess,edhasttask;
    private VideoView videoViewpost;
    private Button btupload_videi;
    private FloatingActionButton pickvideoFab;
    //
    private static final int VIDEO_PICK_GALLERY_CODE=100;
    private static final int VIDEO_PICK_CAMERA_CODE=101;
    private static final int CAMERA_REQUEST_CODE=102;
    //
    private String[] cameraPermissions;
    private Uri videouri;//uri of piked video
    private ProgressDialog progressDialog;
    private String des,hast_task;
    private Spinner spinner;
    //user info
    String name,email,uid,dp;
    //
    FirebaseAuth firebaseAuth;
    DatabaseReference userdbref;
    //
    public static final String TAG5 = Home_customer_Fragment.class.getName();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        getParentFragmentManager().popBackStack();
        eddess=view.findViewById(R.id.ed_des_video);
        spinner=view.findViewById(R.id.spinner1);
        videoViewpost=view.findViewById(R.id.video_post);
        btupload_videi=view.findViewById(R.id.bt_uploadvideo);
        pickvideoFab=view.findViewById(R.id.pickVideoFab);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("UpLoading Video");
        progressDialog.setCanceledOnTouchOutside(false);
        //
        ArrayList<String> dsDiadiem =new ArrayList<String>();
        dsDiadiem.add("#tet");
        dsDiadiem.add("#vui ve");
        dsDiadiem.add("#buon");
        dsDiadiem.add("#cover");
        dsDiadiem.add("#hoc");
        dsDiadiem.add("#nha");
        ArrayAdapter adapterr =new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,dsDiadiem);
        //cho chu cach ra
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //do adapter vao spinner
        spinner.setAdapter(adapterr);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hast_task=dsDiadiem.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //
        firebaseAuth=FirebaseAuth.getInstance();
        checkUserStatus();
        //get some info of current user to include in post
        userdbref=FirebaseDatabase.getInstance().getReference("users");
        Query myquery=userdbref.orderByChild("user_id").equalTo(uid);
        myquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    name=""+ds.child("user_name").getValue();
                    dp=""+ds.child("profileImage").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //camera permissions
        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //click,upload video
        btupload_videi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                des=eddess.getText().toString().trim();
                if(TextUtils.isEmpty(des)){
                    Toast.makeText(getActivity(),"des is required...",Toast.LENGTH_SHORT).show();
                }
                else if(videouri==null){
                    //video is not picked
                    Toast.makeText(getActivity(),"Pick a video before you can upload...",Toast.LENGTH_SHORT).show();
                }
                else {
                    //upload video
                    uploadVideoFirebase(); }
            }
        });
        //click get video from camera/gallery
        pickvideoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    VideoPickDialog();
            }
        });

        return view;
    }
    private void uploadVideoFirebase() {
//show process
        progressDialog.show();
        //timestamp
        String timestamp=""+System.currentTimeMillis();
        //file path and name in firebase storage
        String filePathAndName="Video/"+"video_"+timestamp;
        //storage  reference
        StorageReference storageReference= FirebaseStorage.getInstance().getReference(filePathAndName);
        //upload  video
        storageReference.putFile(videouri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //video upload,get url of upload video
                        Task<Uri>uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri dowloadUri=uriTask.getResult();
                        if(uriTask.isSuccessful()){
                            //url of uploaded video is received

                            //
                            HashMap<String,Object>hashMap=new HashMap<>();
                            hashMap.put("user_name",name);
                            hashMap.put("profileImage",dp);
                            hashMap.put("user_id",uid);
                            hashMap.put("video_des",""+des);
                            hashMap.put("video_id",""+timestamp);
                            hashMap.put("timestamp",""+timestamp);
                            hashMap.put("sound_id","");
                            hashMap.put("hast_task_name",""+hast_task);
                            hashMap.put("sound_name","");
                            hashMap.put("video_heart",""+0);
                            hashMap.put("video_comment",""+0);
                            hashMap.put("video_uri",""+dowloadUri);

                            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("videos");
                            reference.child(timestamp)
                                    .setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                        //video details added to db
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(),"Video Upload...",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        //fail adding details to db
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    //fail uploaling to storage
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void VideoPickDialog() {
        //option to display in dialog
        String[]options={"Camera","Gallery"};
        //Dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Pick Video From").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(i==0){
                    //camera click
                    if(!checkCameraPermission()){
                        //camera permission not allowed,reques it
                        requestcameraPermission();
                    }
                    else {
                        //permission already allowed,take picture
                        videoPickCamera();
                    }
                }else if(i==1){
                    //gallery click
                    videoPickGallery();
                }
            }
        }).show();
    }
    private void requestcameraPermission(){
        //request camera permission
        ActivityCompat.requestPermissions(getActivity(),cameraPermissions,CAMERA_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){
        boolean result1= ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        boolean result2= ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WAKE_LOCK)== PackageManager.PERMISSION_GRANTED;
        return result1 && result2;
    }
    private void videoPickGallery(){
        //pick video from gallery-intent
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select Videos"),VIDEO_PICK_GALLERY_CODE);
    }
    private void videoPickCamera(){
        //pick video from camera-intent
        Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,VIDEO_PICK_CAMERA_CODE);
    }
    private  void setVideoToVideoView(){
        MediaController mediaController=new MediaController(getContext());
        mediaController.setAnchorView(videoViewpost);
        //set media controller to videoView
        videoViewpost.setMediaController(mediaController);
        //set video uri
        videoViewpost.setVideoURI(videouri);
        videoViewpost.requestFocus();
        videoViewpost.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoViewpost.pause();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if (grantResults.length>0){
                    //check permission alowed or not
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted&&storageAccepted){
                        //both permissions  allowed
                        videoPickCamera();
                    }
                    else {
                        //both or one of those denied
                        Toast.makeText(getContext(),"Camera && Storage permission are required",Toast.LENGTH_SHORT).show();
                    }

                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //called after picking video from camera/gallery
        if(resultCode==-1){
        if(requestCode==VIDEO_PICK_GALLERY_CODE){
            videouri=data.getData();
            //show picked video in Videoview
            setVideoToVideoView();
        }
        else if(requestCode==VIDEO_PICK_CAMERA_CODE ){
            videouri=data.getData();
            setVideoToVideoView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }}
    private void checkUserStatus(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
            uid=user.getUid();
        }else{
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkUserStatus();
    }

}