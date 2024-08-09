package com.example.timetrekerforandroid.util;

import android.content.Context;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;

public class ScannerManager implements EMDKManager.EMDKListener {
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
            // Обработка ошибки инициализации EMDKManager
        }
    }
    public static ScannerManager getInstance(Context context) {
        if (instance == null) {
            instance = new ScannerManager(context.getApplicationContext());
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
            try {
                scanner.addDataListener(dataListener);
                scanner.addStatusListener(statusListener);
                scanner.enable();
                isScannerReady = true;
            } catch (ScannerException e) {
                e.printStackTrace();
            }
        }
    }

    public void startScanning() {
        if (scanner != null && isScannerReady) {
            try {
                scanner.read();
            } catch (ScannerException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopScanning() {
        if (scanner != null) {
            try {
                scanner.cancelRead();
            } catch (ScannerException e) {
                e.printStackTrace();
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
            } catch (ScannerException e) {
                e.printStackTrace();
            }
        }
    }

    private Scanner.DataListener dataListener = new Scanner.DataListener() {
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

    private Scanner.StatusListener statusListener = new Scanner.StatusListener() {
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
