package com.example.toptop;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.Models.userObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class Edit_Profile_Activity extends AppCompatActivity {
    public static final int MY_REQUEST_CODE = 10;
    private ImageView img_avata;
    EditText ed_fullname, ed_sdt;
    Button bt_update;
    private Uri uri;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser usert = FirebaseAuth.getInstance().getCurrentUser();
    //Firebase
    FirebaseStorage storage;
    String child = usert.getPhoneNumber();
    StorageReference storageReference;
    private MediaObjectt media;
    private ProgressDialog progressDialog;
    final private ActivityResultLauncher<Intent> mactivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent == null) {
                    return;
                }
                uri = intent.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    setBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        init();
        setUserInfomation();
        initListener();

    }

    private void setUserInfomation() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(usert.getPhoneNumber());
        if (usert == null) {
            return;
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userObject user = snapshot.getValue(userObject.class);
                ed_fullname.setText(user.getUser_name());
                ed_sdt.setText(usert.getPhoneNumber());
                Glide.with(Edit_Profile_Activity.this).load(usert.getPhotoUrl()).error(R.drawable.avatar).into(img_avata);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void init() {
        img_avata = findViewById(R.id.img_update_avata);
        ed_fullname = findViewById(R.id.ed_update_fullname);
        ed_sdt = findViewById(R.id.ed_update_sdt);
        bt_update = findViewById(R.id.bt_update_profile);
        progressDialog = new ProgressDialog(this);

    }

    private void initListener() {
        img_avata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermisson();
            }
        });
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();
                onClickUpdateProfile();

            }
        });
    }

    private void onClickRequestPermisson() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permisstions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            this.requestPermissions(permisstions, MY_REQUEST_CODE);
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mactivityResultLauncher.launch(Intent.createChooser(intent, "Sellect picture"));
    }

    private void onClickUpdateProfile() {
        String strFullname = ed_fullname.getText().toString().trim();
        if (usert == null) {
            return;
        }
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strFullname)
                .setPhotoUri(uri)
                .build();

        usert.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            DatabaseReference myrefe = database.getReference("users").child(child).child("user_name");
                            myrefe.setValue(strFullname);
                                    String key=String.valueOf(media.getVideo_id());
                                    MediaObjectt media=new MediaObjectt(strFullname);
                                    DatabaseReference img1 = database.getReference("videos");
                                    img1.updateChildren(media.toMap());
                            uploadImage();

                        }
                    }
                });

    }

    private void uploadImage() {
        if (uri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String strFullname = ed_fullname.getText().toString().trim();
                                    DatabaseReference img = database.getReference("users").child(child).child("profileImage");
                                    img.setValue(uri.toString());
//                                    MediaObjectt media=new MediaObjectt(strFullname,uri.toString());
//                                    DatabaseReference img1 = database.getReference("videos");
//                                    img1.child(String.valueOf(media.getVideo_id())).updateChildren(media.toMap());

                                }
                            });
                            progressDialog.dismiss();

                            Toast.makeText(Edit_Profile_Activity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Edit_Profile_Activity.this, HomeActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Edit_Profile_Activity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Vui long cho phep truy cap", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void setBitmap(Bitmap bitmapImage) {
        img_avata.setImageBitmap(bitmapImage);
    }

    public void onBack(View view) {

        startActivity(new Intent(Edit_Profile_Activity.this, HomeActivity.class));
    }

    public void setMedia(MediaObjectt media) {
        this.media = media;
    }
}