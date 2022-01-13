package com.example.toptop;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.toptop.Fragment.Create_Fragment;
import com.example.toptop.Fragment.Home_Fragment;
import com.example.toptop.Fragment.Mail_Fragment;
import com.example.toptop.Fragment.Profile_Fragment;
import com.example.toptop.Fragment.Search_Fragment;
import com.example.toptop.Models.MediaObjectt;
import com.example.toptop.Register.RegisterUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    LinearLayout layoutBottom_sheet;
    private BottomSheetBehavior bottomSheetBehavior;
    MediaObjectt media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //
        bottomNavigationView = findViewById(R.id.bottom_nav);
        layoutBottom_sheet = findViewById(R.id.bottom_sheet_layout);
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottom_sheet);
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

    public void Click_Heart1(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, androidx.appcompat.R.anim.abc_fade_in));

    }
}