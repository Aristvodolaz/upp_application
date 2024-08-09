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
import com.example.timetrekerforandroid.util.ScannerManager;
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

public class AuthFragment extends BaseFragment implements AuthView, ScannerManager.ScannerDataListener, ScannerManager.ScannerStatusListener {

    private AuthPresenter presenter;
    private WaitDialog mWaitDialog;
    private ScannerManager scannerManager;

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Override
    protected void initViews() {
        super.initViews();
        presenter = new AuthPresenter(this);
    }

    private void showDialog() {
        mWaitDialog = WaitDialog.newInstance();
        mWaitDialog.setCancelable(false);
        mWaitDialog.show(getFragmentManager(), WaitDialog.TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerManager = ScannerManager.getInstance(getActivity().getApplicationContext());
        scannerManager.setScannerDataListener(this);
        scannerManager.setScannerStatusListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerManager.releaseScanner();
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerManager.startScanning();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerManager.releaseScanner();
    }

    @Override
    public void onDataReceived(String barcodeData, String barcodeType) {
        getActivity().runOnUiThread(() -> {
            showDialog();
            presenter.getAuthUser(barcodeData.substring(1, barcodeData.length() - 1));
            Toast.makeText(getContext(), "Scanned Data: " + barcodeData.substring(1, barcodeData.length() - 1), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onScanFailed(String errorMessage) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onStatusChanged(String statusMessage) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), statusMessage, Toast.LENGTH_SHORT).show();
        });
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
}
