package com.example.timetrekerforandroid.util;

import android.content.Context;
import android.util.Log;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;

public class ScannerManager implements EMDKManager.EMDKListener {

    private static final String TAG = "ScannerManager";
    private static ScannerManager instance;
    private EMDKManager emdkManager;
    private BarcodeManager barcodeManager;
    private Scanner scanner;
    private boolean isScannerReady = false;

    private ScannerDataListener scannerDataListener;
    private ScannerStatusListener scannerStatusListener;

    private Context context;

    private ScannerManager(Context context) {
        this.context = context.getApplicationContext();
        EMDKResults results = EMDKManager.getEMDKManager(this.context, this);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            Log.e(TAG, "Failed to initialize EMDKManager: " + results.statusCode);
        }
    }

    public static synchronized ScannerManager getInstance(Context context) {
        if (instance == null) {
            instance = new ScannerManager(context);
        }
        return instance;
    }

    public void setScannerDataListener(ScannerDataListener listener) {
        this.scannerDataListener = listener;
    }

    public void setScannerStatusListener(ScannerStatusListener listener) {
        this.scannerStatusListener = listener;
    }

    private void initializeScanner() {
        if (barcodeManager != null) {
            scanner = barcodeManager.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT);
            if (scanner != null) {
                try {
                    scanner.triggerType = Scanner.TriggerType.HARD;
                    scanner.addDataListener(dataListener);
                    scanner.addStatusListener(statusListener);
                    scanner.enable();
                    isScannerReady = true;
                    Log.d(TAG, "Scanner initialized and enabled.");
                } catch (ScannerException e) {
                    Log.e(TAG, "Scanner initialization failed: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Scanner device is null.");
            }
        } else {
            Log.e(TAG, "BarcodeManager is null.");
        }
    }

    public void startScanning() {
        if (scanner != null && isScannerReady) {
            try {
                scanner.read();
                Log.d(TAG, "Started scanning.");
            } catch (ScannerException e) {
                Log.e(TAG, "Error starting scan: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Scanner is not ready or null.");
        }
    }

    public void stopScanning() {
        if (scanner != null) {
            try {
                scanner.cancelRead();
                Log.d(TAG, "Stopped scanning.");
            } catch (ScannerException e) {
                Log.e(TAG, "Error stopping scan: " + e.getMessage());
            }
        }
    }

    public void releaseScanner() {
        if (scanner != null) {
            try {
                scanner.removeDataListener(dataListener);
                scanner.removeStatusListener(statusListener);
                scanner.disable();
                isScannerReady = false;
                Log.d(TAG, "Scanner released.");
            } catch (ScannerException e) {
                Log.e(TAG, "Error releasing scanner: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Scanner is null. Cannot release.");
        }
    }

    private final Scanner.DataListener dataListener = new Scanner.DataListener() {
        @Override
        public void onData(ScanDataCollection dataCollection) {
            if (dataCollection != null && dataCollection.getResult() == ScannerResults.SUCCESS) {
                for (ScanDataCollection.ScanData scanData : dataCollection.getScanData()) {
                    String barcodeData = scanData.getData();
                    String barcodeType = scanData.getLabelType().toString();
                    if (scannerDataListener != null) {
                        scannerDataListener.onDataReceived(barcodeData, barcodeType);
                    }
                }
            } else {
                if (scannerDataListener != null) {
                    scannerDataListener.onScanFailed("Failed to read barcode.");
                }
            }
        }
    };

    private final Scanner.StatusListener statusListener = new Scanner.StatusListener() {
        @Override
        public void onStatus(StatusData statusData) {
            StatusData.ScannerStates state = statusData.getState();
            if (scannerStatusListener != null) {
                switch (state) {
                    case IDLE:
                        scannerStatusListener.onStatusChanged("Scanner is idle.");
                        break;
                    case SCANNING:
                        scannerStatusListener.onStatusChanged("Scanner is scanning.");
                        break;
                    case WAITING:
                        scannerStatusListener.onStatusChanged("Scanner is waiting for trigger.");
                        break;
                    case DISABLED:
                        scannerStatusListener.onStatusChanged("Scanner is disabled.");
                        break;
                    case ERROR:
                        scannerStatusListener.onStatusChanged("Scanner encountered an error.");
                        break;
                }
            }
        }
    };

    @Override
    public void onOpened(EMDKManager emdkManager) {
        this.emdkManager = emdkManager;
        barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);
        initializeScanner();
    }

    @Override
    public void onClosed() {
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
        isScannerReady = false;
    }

    public interface ScannerDataListener {
        void onDataReceived(String barcodeData, String barcodeType);
        void onScanFailed(String errorMessage);
    }

    public interface ScannerStatusListener {
        void onStatusChanged(String statusMessage);
    }
}
