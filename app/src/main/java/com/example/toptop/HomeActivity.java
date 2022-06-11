package com.example.toptop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.toptop.Fragment.Create_Fragment;
import com.example.toptop.Fragment.Home_Fragment;
import com.example.toptop.Fragment.Home_customer_Fragment;
import com.example.toptop.Fragment.Home_customer_Fragment2;
import com.example.toptop.Fragment.Home_video_Fragment;
import com.example.toptop.Fragment.Alluser_Fragment;
import com.example.toptop.Fragment.Mail_Fragment;
import com.example.toptop.Fragment.Open_Video_Fragment;
import com.example.toptop.Fragment.Profile_Fragment;
import com.example.toptop.Fragment.Search_Fragment;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.Models.userObject;
import com.example.toptop.Register.RegisterUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    LinearLayout layoutBottom_sheet;
    private BottomSheetBehavior bottomSheetBehavior;
    MediaObjectt media;
    String mUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //
        //
        bottomNavigationView = findViewById(R.id.bottom_nav);
        layoutBottom_sheet = findViewById(R.id.bottom_sheet_layout);
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottom_sheet);

        //
        //
        init();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Home_Fragment()).commit();
        //
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = new Home_Fragment();
                        break;
                    case R.id.action_search:
                        fragment = new Search_Fragment();
                        break;
                    case R.id.action_create:
                        fragment = new Create_Fragment();
                        break;
                    case R.id.action_mail:
                        fragment = new Mail_Fragment();
                        break;
                    case R.id.action_profile:
                        fragment = new Profile_Fragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
                return true;
            }
        });
        checkUserStatus();
        FirebaseMessaging.getInstance().subscribeToTopic(mUID);
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        if (on) {
            params.flags |= bits;
        } else {
            params.flags &= ~bits;
        }
        win.setAttributes(params);
    }

    public void logout(View view) {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void Exit(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, androidx.appcompat.R.anim.abc_fade_in));
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this, RegisterUser.class));

    }

    public void Cancle(View view) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void onClickItemfromProfile_GoToOpenVideo_Fragment(MediaObjectt media) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Open_Video_Fragment open_video_fragment = new Open_Video_Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("MediaObjectt", media);
        open_video_fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame, open_video_fragment);
        fragmentTransaction.addToBackStack(Profile_Fragment.TAG1);
        fragmentTransaction.commit();
    }

    public void onClickItemfromCustomer_GoToOpenVideo_Fragment(MediaObjectt media) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Open_Video_Fragment open_video_fragment = new Open_Video_Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("MediaObjectt1", media);
        open_video_fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame, open_video_fragment);
        fragmentTransaction.addToBackStack(Home_customer_Fragment.TAG);
        fragmentTransaction.commit();
    }

    public void onClickfromHomeVideo_GoToHomeCustomer_Fragment(MediaObjectt media) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Home_customer_Fragment home_customer_fragment = new Home_customer_Fragment();
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("MediaObjectt2", media);
        home_customer_fragment.setArguments(bundle1);
        fragmentTransaction.replace(R.id.frame, home_customer_fragment);
        fragmentTransaction.addToBackStack(Home_video_Fragment.TAG2);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        fragmentTransaction.commit();

    }

    public void onClickfromProfile_GoToCreate_Fragment(MediaObjectt media) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Create_Fragment create_fragment = new Create_Fragment();
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("MediaObjectt3", media);
        create_fragment.setArguments(bundle3);
        fragmentTransaction.replace(R.id.frame, create_fragment);
        fragmentTransaction.commit();

    }

    public void onClickfromsearch_GoToOpenVideo_Fragment(MediaObjectt media) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Open_Video_Fragment open_video_fragment = new Open_Video_Fragment();
        Bundle bundle4 = new Bundle();
        bundle4.putSerializable("MediaObjectt4", media);
        open_video_fragment.setArguments(bundle4);
        fragmentTransaction.replace(R.id.frame, open_video_fragment);
        fragmentTransaction.addToBackStack(Search_Fragment.TAG5);
        fragmentTransaction.commit();

    }
    public void onClickfromHomeVide_GoToHomeCustomer_Fragment(userObject user) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Home_customer_Fragment2 home_customer_fragment = new Home_customer_Fragment2();
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("MediaObjectt6", user);
        home_customer_fragment.setArguments(bundle1);
        fragmentTransaction.replace(R.id.frame, home_customer_fragment);
        fragmentTransaction.addToBackStack(Alluser_Fragment.TAG6);
        fragmentTransaction.commit();

    }
    public void customer(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, androidx.appcompat.R.anim.abc_fade_in));
    }

    private void checkUserStatus(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            mUID=user.getUid();
            SharedPreferences sp=getSharedPreferences("SP_USER",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("Current_USERID",mUID);
            editor.apply();
        }else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }

    public void open_all_user(View view) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Alluser_Fragment alluser_fragment = new Alluser_Fragment();
        fragmentTransaction.replace(R.id.frame, alluser_fragment);
        fragmentTransaction.addToBackStack(Mail_Fragment.TAG6);
        fragmentTransaction.commit();
    }
}