package com.example.toptop.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toptop.HomeActivity;
import com.example.toptop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class EnterOtp extends AppCompatActivity {
    private static final String TAG=EnterOtp.class.getName();
    EditText ed_otp;
    Button bt_otp;
    TextView tv_again_otp,tv_phone_number;
    private String mphonenumber;
    private String mverificationId;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private PhoneAuthProvider.ForceResendingToken mforceResendingToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        ed_otp=findViewById(R.id.ed_otp);
        bt_otp=findViewById(R.id.bt_otp);
        tv_again_otp=findViewById(R.id.tv_otp_again);
        tv_phone_number=findViewById(R.id.textVerifyPhoneNumber);
        progressDialog=new ProgressDialog(this);
        getDataIntent();
        mAuth=FirebaseAuth.getInstance();
        bt_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Otp=ed_otp.getText().toString().trim();
                onclickotp(Otp);

            }

           
        });
        tv_again_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(EnterOtp.this, androidx.appcompat.R.anim.abc_fade_in));
                onclickotp_again();
            }
        });
        tv_phone_number.setText("Xác nhận qua số điện thoại: "+mphonenumber);
    }
    private void getDataIntent(){
        mphonenumber=getIntent().getStringExtra("phone_number");
        mverificationId=getIntent().getStringExtra("verification_ID");
    }
    private void onclickotp_again() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mphonenumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)
                        .setForceResendingToken(mforceResendingToken)// Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(EnterOtp.this,"VerificationFailed",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationID, forceResendingToken);
                               mverificationId=verificationID;
                               mforceResendingToken=forceResendingToken;
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void onclickotp(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mverificationId, otp);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressDialog.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser usert = mAuth.getCurrentUser();
                            if(task.getResult().getAdditionalUserInfo().isNewUser()) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                HashMap<Object, String> hashMap = new HashMap<>();
                                hashMap.put("email", "");
                                hashMap.put("user_id", usert.getUid());
                                hashMap.put("user_name", "");
                                hashMap.put("onlineStatus","online");
                                hashMap.put("typingTo","noOne");
                                hashMap.put("user_phone", usert.getPhoneNumber());
                                hashMap.put("profileImage", "");
                                DatabaseReference myrefe1 = database.getReference("users").child(usert.getUid());
                                myrefe1.setValue(hashMap);
                            }
                            gotoHome(usert.getPhoneNumber());


                            // Update UI

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(EnterOtp.this,"The verification code entered was invalid",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void gotoHome(String phoneNumber) {

        Intent intent=new Intent(EnterOtp.this, HomeActivity.class);
        startActivity(intent);

    }

}