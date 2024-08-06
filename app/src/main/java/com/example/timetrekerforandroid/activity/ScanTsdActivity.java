package com.example.timetrekerforandroid.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.timetrekerforandroid.R;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;

import java.util.List;

public class ScanTsdActivity extends AppCompatActivity implements EMDKManager.EMDKListener, Scanner.DataListener, Scanner.StatusListener {


    private EMDKManager emdkManager;
    private BarcodeManager barcodeManager;
    private Scanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_activity);

        EMDKResults emdkResults = EMDKManager.getEMDKManager(getApplicationContext(), this);
        if (emdkResults.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            Toast.makeText(this, "Error initializing EMDK", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOpened(EMDKManager manager) {
        emdkManager = manager;
        barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);
        initializeScanner();
    }

    @Override
    public void onClosed() {
        releaseScanner();
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    private void initializeScanner() {
        if (scanner == null) {
            scanner = barcodeManager.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT);
            scanner.addDataListener(this);
            scanner.addStatusListener(this);
            scanner.triggerType = Scanner.TriggerType.HARD;
            try {
                scanner.enable();
            } catch (ScannerException e) {
                Toast.makeText(this, "Error enabling scanner", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void releaseScanner() {
        if (scanner != null) {
            try {
                scanner.disable();
            } catch (ScannerException e) {
                Toast.makeText(this, "Error disabling scanner", Toast.LENGTH_SHORT).show();
            }
            scanner.removeDataListener(this);
            scanner.removeStatusListener(this);
            scanner = null;
        }
    }

    @Override
    public void onData(ScanDataCollection scanDataCollection) {
        if (scanDataCollection != null && scanDataCollection.getResult() == ScannerResults.SUCCESS) {
            List<ScanDataCollection.ScanData> scanDataList = scanDataCollection.getScanData();
            for (ScanDataCollection.ScanData data : scanDataList) {
                final String barcodeData = data.getData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScanTsdActivity.this, "Scanned Data: " + barcodeData, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public void onStatus(StatusData statusData) {
        if (statusData != null) {
            switch (statusData.getState()) {
                case IDLE:
                    try {
                        scanner.read();
                    } catch (ScannerException e) {
                        Toast.makeText(this, "Error starting scan", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case WAITING:
                    // Scanner is waiting for trigger press
                    break;
                case SCANNING:
                    // Scanner is scanning
                    break;
                case DISABLED:
                    // Scanner is disabled
                    break;
                case ERROR:
                    Toast.makeText(this, "Scanner error occurred", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseScanner();
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }
}