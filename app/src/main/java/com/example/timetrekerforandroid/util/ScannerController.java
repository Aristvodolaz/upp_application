package com.example.timetrekerforandroid.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;

public class ScannerController implements EMDKManager.EMDKListener, Scanner.DataListener, Scanner.StatusListener {

    private static final String TAG = "ScannerController";

    private EMDKManager emdkManager;
    private BarcodeManager barcodeManager;
    private Scanner scanner;
    private Context context;
    private ScannerCallback callback;
    private boolean isScannerInitialized = false;

    public ScannerController(Context context, ScannerCallback callback) {
        this.context = context.getApplicationContext();
        this.callback = callback;
        EMDKResults results = EMDKManager.getEMDKManager(this.context, this);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            Log.e(TAG, "Failed to initialize EMDKManager: " + results.statusCode);
            Toast.makeText(context, "Error initializing EMDK: " + results.statusCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
        this.emdkManager = emdkManager;
        initBarcodeManager();
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

    private void initBarcodeManager() {
        barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);
        if (barcodeManager != null) {
            Log.d(TAG, "BarcodeManager initialized.");
        } else {
            Log.e(TAG, "Failed to initialize BarcodeManager.");
            Toast.makeText(context, "Error initializing BarcodeManager", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeScanner() {
        if (barcodeManager != null && !isScannerInitialized) {
            try {
                scanner = barcodeManager.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT);
                if (scanner != null) {
                    scanner.addDataListener(this);
                    scanner.addStatusListener(this);
                    scanner.triggerType = Scanner.TriggerType.HARD;
                    scanner.enable();
                    isScannerInitialized = true;
                    Log.d(TAG, "Scanner initialized and enabled.");
                } else {
                    Log.e(TAG, "Scanner is null.");
                    Toast.makeText(context, "Scanner is null.", Toast.LENGTH_SHORT).show();
                }
            } catch (ScannerException e) {
                Log.e(TAG, "Error enabling scanner: " + e.getMessage(), e);
                e.printStackTrace(); // Полный стек для анализа
                Toast.makeText(context, "Error enabling scanner: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e(TAG, "Unexpected error: " + e.getMessage(), e);
                e.printStackTrace();
                Toast.makeText(context, "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isScannerActive() {
        return scanner != null && scanner.isEnabled();
    }



    public void releaseScanner() {
        if (scanner != null) {
            try {
                scanner.disable();
                isScannerInitialized = false;
                Log.d(TAG, "Scanner disabled.");
            } catch (ScannerException e) {
                Log.e(TAG, "Error disabling scanner: " + e.getMessage());
                Toast.makeText(context, "Error disabling scanner: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            scanner.removeDataListener(this);
            scanner.removeStatusListener(this);
            scanner = null;
        }
    }

    @Override
    public void onData(ScanDataCollection scanDataCollection) {
        if (scanDataCollection != null && scanDataCollection.getResult() == ScannerResults.SUCCESS) {
            for (ScanDataCollection.ScanData data : scanDataCollection.getScanData()) {
                final String barcodeData = data.getData();
                if (callback != null) {
                    callback.onDataReceived(barcodeData);
                }
            }
        } else {
            if (callback != null) {
                callback.onScanFailed("Failed to read barcode.");
            }
        }
    }

    @Override
    public void onStatus(StatusData statusData) {
        if (statusData != null) {
            switch (statusData.getState()) {
                case IDLE:
                    Log.d(TAG, "Scanner is idle. Ready to scan.");
                    startScanning();
                    break;
                case WAITING:
                    Log.d(TAG, "Scanner is waiting for trigger.");
                    break;
                case SCANNING:
                    Log.d(TAG, "Scanner is scanning.");
                    break;
                case DISABLED:
                    Log.d(TAG, "Scanner is disabled.");
                    break;
                case ERROR:
                    Log.e(TAG, "Scanner error: " + statusData.getState().name());
                    if (callback != null) {
                        callback.onScanFailed("Scanner error occurred.");
                    }
                    break;
            }
        }
    }

    public void startScanning() {
        if (scanner != null) {
            try {
                scanner.read();
                Log.d(TAG, "Scanner started scanning.");
            } catch (ScannerException e) {
                Log.e(TAG, "Error starting scan: " + e.getMessage());
                if (callback != null) {
                    callback.onScanFailed("Error starting scan.");
                }
            }
        } else {
            Log.e(TAG, "Scanner is null.");
        }
    }

    public void resumeScanner() {
        if (scanner != null) {
            startScanning();
        } else {
            initializeScanner();
        }
    }

    public interface ScannerCallback {
        void onDataReceived(String barcodeData);
        void onScanFailed(String errorMessage);
    }
}
