package com.example.timetrekerforandroid.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.timetrekerforandroid.R;
import com.example.timetrekerforandroid.fragment.AddInformationFragment;
import com.example.timetrekerforandroid.fragment.AuthFragment;
import com.example.timetrekerforandroid.fragment.StartFragment;
import com.example.timetrekerforandroid.fragment.edit.EditForWbFragment;
import com.example.timetrekerforandroid.fragment.navigation.MasterEditFragment;
import com.example.timetrekerforandroid.fragment.start.InfoArticleFragment;
import com.example.timetrekerforandroid.fragment.ScanSHKBoxFragment;
import com.example.timetrekerforandroid.fragment.navigation.EditFragment;
import com.example.timetrekerforandroid.fragment.navigation.PakingFragment;
import com.example.timetrekerforandroid.fragment.navigation.TasksFragment;
import com.example.timetrekerforandroid.fragment.wps.Wps1Fragment;
import com.example.timetrekerforandroid.fragment.wps.Wps2Fragment;
import com.example.timetrekerforandroid.util.SPHelper;
import com.example.timetrekerforandroid.util.ScannerController;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StartActivity extends BaseActivity implements ScannerController.ScannerCallback {

    private ScannerController scannerController;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void initViews(@Nullable Bundle saveInstanceState) {
        scannerController = new ScannerController(this, this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();

        replaceFragment(AuthFragment.newInstance(), false);
        updateBottomNavigationVisibility();
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    replaceFragment(TasksFragment.Companion.newInstance(SPHelper.getNameTask()), false);
                    return true;
                case R.id.nav_packing:
                    replaceFragment(PakingFragment.Companion.newInstance(), false);
                    return true;
                case R.id.nav_edit:
                    replaceFragment(EditFragment.Companion.newInstance(), false);
                    return true;
                case R.id.nav_edit_ldu:
                    replaceFragment(MasterEditFragment.Companion.newInstance(), false);
                    return true;
                default:
                    return false;
            }
        });
    }

    @Override
    protected int layoutResId() {
        return R.layout.start_activity;
    }

    @Override
    protected int titleResId() {
        return 0;
    }

    public ScannerController getScannerController() {
        return scannerController;
    }

    @Override
    public void onDataReceived(String barcodeData) {
        Log.d("StartActivity", "Data received: " + barcodeData);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (currentFragment instanceof AuthFragment) {
            ((AuthFragment) currentFragment).onDataReceived(barcodeData);
        } else if (currentFragment instanceof TasksFragment) {
            ((TasksFragment) currentFragment).onDataReceived(barcodeData);
        } else if (currentFragment instanceof InfoArticleFragment) {
            ((InfoArticleFragment) currentFragment).onDataReceived(barcodeData);
        } else if (currentFragment instanceof ScanSHKBoxFragment) {
            ((ScanSHKBoxFragment) currentFragment).onDataReceived(barcodeData);
        } else if (currentFragment instanceof AddInformationFragment) {
            ((AddInformationFragment) currentFragment).onDataReceived(barcodeData);
        } else if (currentFragment instanceof Wps1Fragment){
            ((Wps1Fragment) currentFragment).onDataReceived(barcodeData);
        } else if (currentFragment instanceof Wps2Fragment){
            ((Wps2Fragment) currentFragment).onDataReceived(barcodeData);
        } else if(currentFragment instanceof EditForWbFragment){
            ((EditForWbFragment) currentFragment).onDataReceived(barcodeData);
        } else if (currentFragment instanceof MasterEditFragment) {
            ((MasterEditFragment) currentFragment).onDataReceived(barcodeData);
        }else if (currentFragment instanceof PakingFragment) {
            ((PakingFragment) currentFragment).onDataReceived(barcodeData);
        }
    }

    @Override
    public void onScanFailed(String errorMessage) {
        Log.e("StartActivity", "Scan failed: " + errorMessage);
    }

    public void replaceFragment(Fragment fragment, boolean addToStack) {
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        if (addToStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commit();

        getSupportFragmentManager().executePendingTransactions();  // Убедитесь, что транзакция завершена
        updateBottomNavigationVisibility(); // Обновляем видимость навигации после завершения транзакции
    }

    public void updateBottomNavigationVisibility(){
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        Log.d("DDDDDD", String.valueOf(currentFragment));
        if ((currentFragment instanceof AuthFragment) || (currentFragment instanceof StartFragment) || (currentFragment == null)) {
            bottomNavigationView.setVisibility(View.GONE);
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (scannerController != null) {
            scannerController.resumeScanner();
            Log.d("StartActivity", "Scanner resumed");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (scannerController != null) {
            scannerController.releaseScanner();
            Log.d("StartActivity", "Scanner released");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scannerController != null) {
            scannerController.releaseScanner();
            scannerController = null;
            Log.d("StartActivity", "Scanner controller released and set to null");
        }
    }
}
