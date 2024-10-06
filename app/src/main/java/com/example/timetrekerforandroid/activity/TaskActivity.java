package com.example.timetrekerforandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.timetrekerforandroid.R;
import com.example.timetrekerforandroid.fragment.start.InfoArticleFragment;
import com.example.timetrekerforandroid.util.ScannerController;
import com.symbol.emdk.barcode.ScannerException;

public class TaskActivity extends BaseActivity implements ScannerController.ScannerCallback {

    private ScannerController scannerController;
    private String article, stuff_name, size_order, articul_syrya, kolvo_syrya;
    private boolean syryo;

    @Override
    protected void initViews(@Nullable Bundle saveInstanceState) {
        // Initialize ScannerController
//        scannerController = new ScannerController(this, this);

        Intent intent = getIntent();
        if (intent != null) {
            article = intent.getStringExtra("article");
            stuff_name = intent.getStringExtra("name_stuff");
            size_order = intent.getStringExtra("size");
            syryo = intent.getBooleanExtra("syryo", false);
            articul_syrya = intent.getStringExtra("articul_syrya");
            kolvo_syrya = intent.getStringExtra("articul_kolvo");
        }


//        replaceFragment(InfoArticleFragment.Companion.newInstance(stuff_name, article, size_order, syryo, articul_syrya, kolvo_syrya), true);
    }

    @Override
    protected int layoutResId() {
        return R.layout.task_activity;
    }

    @Override
    protected int titleResId() {
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (scannerController != null) {
            if (!scannerController.isScannerActive()) {
                scannerController.resumeScanner();
                Log.d("TaskActivity", "Scanner resumed");
            } else {
                Log.d("TaskActivity", "Scanner already active");
            }
        } else {
            scannerController = new ScannerController(this, this);
            Log.d("TaskActivity", "Scanner controller re-initialized");
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (scannerController != null) {
            scannerController.releaseScanner();
            Log.d("TaskActivity", "Scanner released");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scannerController != null) {
            scannerController.releaseScanner();
            scannerController = null;
            Log.d("TaskActivity", "Scanner controller released and set to null");
        }
    }

    @Override
    public void onDataReceived(String barcodeData) {
        Log.d("TaskActivity", "Data received: " + barcodeData);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (currentFragment instanceof InfoArticleFragment) {
            ((InfoArticleFragment) currentFragment).onDataReceived(barcodeData);
        }
    }

    @Override
    public void onScanFailed(String errorMessage) {
        Log.e("TaskActivity", "Scan failed: " + errorMessage);
    }

    public ScannerController getScannerController() {
        return scannerController;
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
    }
}
