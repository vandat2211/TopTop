package com.example.toptop.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.toptop.Fragment.Home_customer_Fragment;
import com.example.toptop.Fragment.Home_video_Fragment;
import com.example.toptop.Fragment.Home_Fragment;

public class TablayoutAdapter extends FragmentStateAdapter{


    public TablayoutAdapter(@NonNull Home_Fragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Home_video_Fragment();
            case 1:
                return new Home_customer_Fragment();
            default:
                return new Home_video_Fragment() ;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
