package com.example.timetrekerforandroid.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.timetrekerforandroid.R;
import com.example.timetrekerforandroid.fragment.AuthFragment;
import com.example.timetrekerforandroid.fragment.StartFragment;
import com.example.timetrekerforandroid.fragment.hello_fragments.HelloFragment;

public class StartActivity extends BaseActivity {
    @Override
    protected void initViews(@Nullable Bundle saveInstanceState) {
        replaceFragment(AuthFragment.newInstance(), true);
    }

    @Override
    protected int layoutResId() {
        return R.layout.start_activity;
    }

    @Override
    protected int titleResId() {
        return 0;
    }

    public void replaceFragment(Fragment fragment, boolean addToStack){
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        if(addToStack) fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }
}
