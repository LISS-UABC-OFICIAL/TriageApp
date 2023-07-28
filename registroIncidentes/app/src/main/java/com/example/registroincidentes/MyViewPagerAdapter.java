package com.example.registroincidentes;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.content.Context;
import android.util.AttributeSet;
import androidx.viewpager.widget.ViewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.registroincidentes.fragments.DetailsFragment;
import com.example.registroincidentes.fragments.MapFragment;
import com.example.registroincidentes.fragments.StaffFragment;
import com.example.registroincidentes.fragments.ZonesFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    /*var isPagingEnabled: Boolean = true;

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event:MotionEvent): Boolean {
        return isPagingEnabled && super.onTouchEvent(event);
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return isPagingEnabled && super.onInterceptTouchEvent(event);
    }*/


    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new ZonesFragment();
            case 2:
                return new MapFragment();
            case 3:
                return new StaffFragment();
            default:
                return new DetailsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
