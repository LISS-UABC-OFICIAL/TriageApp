package com.example.registroincidentes;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.registroincidentes.fragments.PersonalDataFragment;
import com.example.registroincidentes.fragments.TriageFragment;

/* Esta clase se utiliza para manejar la transición entre los dos
   fragments de la activity perfilHerido */

public class MyViewPagerAdapter2 extends FragmentStateAdapter{

    public MyViewPagerAdapter2(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new TriageFragment();
            case 1:
                return new PersonalDataFragment();
            default:
                return new TriageFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
