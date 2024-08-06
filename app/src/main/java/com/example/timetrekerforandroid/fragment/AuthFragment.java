package com.example.timetrekerforandroid.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.timetrekerforandroid.R;
import com.example.timetrekerforandroid.activity.StartActivity;
import com.example.timetrekerforandroid.presenter.AuthPresenter;
import com.example.timetrekerforandroid.util.SPHelper;
import com.example.timetrekerforandroid.util.WaitDialog;
import com.example.timetrekerforandroid.view.AuthView;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerInfo;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;

import java.util.List;

public class AuthFragment extends BaseFragment implements EMDKManager.EMDKListener, Scanner.DataListener, Scanner.StatusListener, AuthView, BarcodeManager.ScannerConnectionListener {

    private EMDKManager emdkManager;
    private BarcodeManager barcodeManager;
    private Scanner scanner;
    private AuthPresenter presenter;
    private WaitDialog mWaitDialog;
    final Object lock = new Object();

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Override
    protected void initViews() {
        super.initViews();
        presenter = new AuthPresenter(this);
    }
    private void showDialog(){
        mWaitDialog = WaitDialog.newInstance();
        mWaitDialog.setCancelable(false);
        mWaitDialog.show(getFragmentManager(), WaitDialog.TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EMDKResults emdkResults = EMDKManager.getEMDKManager(getActivity().getApplicationContext(), this);
        if (emdkResults.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            Toast.makeText(getContext(), "Error initializing EMDK", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOpened(EMDKManager manager) {
        emdkManager = manager;
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

    @Override
    public void onPause() {
        super .onPause();
//        releaseScanner();
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    private void initializeScanner() {
        if (scanner == null && barcodeManager != null) {
            scanner = barcodeManager.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT);
            scanner.addDataListener(this);
            scanner.addStatusListener(this);
            scanner.triggerType = Scanner.TriggerType.HARD;
            try {
                scanner.enable();
            } catch (ScannerException e) {
                Toast.makeText(getContext(), "Error enabling scanner", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void releaseScanner() {
        if (scanner != null) {
            try {
                scanner.disable();
            } catch (ScannerException e) {
                Toast.makeText(getContext(), "Error disabling scanner", Toast.LENGTH_SHORT).show();
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
                getActivity().runOnUiThread(() -> {
                    showDialog();
                    presenter.getAuthUser(barcodeData.substring(1, barcodeData.length() - 1));
                    Toast.makeText(getContext(), "Scanned Data: " + barcodeData.substring(1, barcodeData.length() - 1), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Error starting scan", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Scanner error occurred", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(emdkManager!=null && scanner!=null){
            try{
                scanner.read();
            } catch (ScannerException e){
                Log.d("EXEPTION", e.getMessage());
            }
        }
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if(scanner!=null){
//            try{
//                scanner.cancelRead();
//            } catch (ScannerException e){
//                Log.d("EXEPTION" , e.getMessage());
//            }
//        }
//    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        if(scanner!=null){
//            try{
//                scanner.disable();
//            } catch (ScannerException e){
//                Log.d("EXEPTION" , e.getMessage());
//            }
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       if(scanner!=null){
           try{
               scanner.removeStatusListener(this);
               scanner.removeDataListener(this);
               scanner.disable();
           } catch (ScannerException e){
               Log.e("EEEXEPTION", e.getMessage());
           }
       }
       scanner = null;

       if(barcodeManager!=null){
           barcodeManager = null;
       }

       if(emdkManager!=null){
           emdkManager.release();
           emdkManager = null;
       }
    }

    private void deInitSacnner(){
        if(scanner != null){
            try{
                scanner.disable();
            } catch (Exception e) {
                Log.d("Exeption", e.getMessage());
            }

            try{
                scanner.removeDataListener(this);
                scanner.removeStatusListener(this);
            } catch (Exception e){
                Log.d("Exeption", e.getMessage());
            }

            try{
                scanner.release();
            } catch (Exception e){
                Log.d("Exeption", e.getMessage());
            }

            scanner = null;
        }
    }



    private  void initBarcodeManager(){
        barcodeManager = (BarcodeManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);

        if(barcodeManager!=null){
            barcodeManager.addConnectionListener(this);
        }
    }
    @Override
    protected int layoutId() {
        return R.layout.hello_fragment;
    }

    @Override
    public void successMessage(@NonNull String msg) {
        SPHelper.setNameEmployer(msg);
        mWaitDialog.dismiss();
        Toast.makeText(getContext(), "Пользователь успешно авторизован", Toast.LENGTH_SHORT).show();
        ((StartActivity) getActivity()).replaceFragment(StartFragment.Companion.newInstance(), false);
    }

    @Override
    public void errorMessage(@NonNull String msg) {
        mWaitDialog.dismiss();
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionChange(ScannerInfo scannerInfo, BarcodeManager.ConnectionState connectionState) {
        switch (connectionState){
            case CONNECTED:
                synchronized (lock){
                    initializeScanner();
                }
                break;
            case DISCONNECTED:
                synchronized (lock){
                    deInitSacnner();
                }
                break;
        }
    }
}
