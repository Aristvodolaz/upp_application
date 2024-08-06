package com.example.timetrekerforandroid.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.timetrekerforandroid.R;
import com.example.timetrekerforandroid.fragment.InfoArticleFragment;

public class TaskActivity extends BaseActivity {
    private String article, stuff_name, size_order, articul_syrya, kolvo_syrya;
    private boolean syryo;


    @Override
    protected void initViews(@Nullable Bundle saveInstanceState) {
        Intent intent = getIntent();
        if(intent!=null){
            article = intent.getStringExtra("article");
            stuff_name = intent.getStringExtra("name_stuff");
            size_order = intent.getStringExtra("size");
            syryo = intent.getBooleanExtra("syryo", false);
            articul_syrya = intent.getStringExtra("articul_syrya");
            kolvo_syrya = intent.getStringExtra("articul_kolvo");
        }
        replaceFragment(InfoArticleFragment.Companion.newInstance(stuff_name,article, size_order, syryo, articul_syrya, kolvo_syrya), true);
    }

    public void replaceFragment(Fragment fragment, boolean addToStack){
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        if(addToStack) fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    @Override
    protected int layoutResId() {
        return R.layout.task_activity;
    }

    @Override
    protected int titleResId() {
        return 0;
    }
}
