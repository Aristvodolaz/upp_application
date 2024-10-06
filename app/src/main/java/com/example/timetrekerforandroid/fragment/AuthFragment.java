package com.example.timetrekerforandroid.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.timetrekerforandroid.R;
import com.example.timetrekerforandroid.activity.StartActivity;
import com.example.timetrekerforandroid.presenter.AuthPresenter;
import com.example.timetrekerforandroid.util.SPHelper;
import com.example.timetrekerforandroid.util.ScannerController;
import com.example.timetrekerforandroid.util.WaitDialog;
import com.example.timetrekerforandroid.view.AuthView;

public class AuthFragment extends BaseFragment implements AuthView, ScannerController.ScannerCallback {

    private AuthPresenter presenter;
    private WaitDialog mWaitDialog;
    private ScannerController scannerController;

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Override
    protected void initViews() {
        super.initViews();
        presenter = new AuthPresenter(this);
        // Получите экземпляр сканера из активности
        if (getActivity() instanceof StartActivity) {
            scannerController = ((StartActivity) getActivity()).getScannerController();
        }
    }

    private void showDialog() {
        mWaitDialog = WaitDialog.newInstance();
        mWaitDialog.setCancelable(false);
        mWaitDialog.show(getFragmentManager(), WaitDialog.TAG);
    }

    @Override
    public void onDataReceived(String barcodeData) {
        getActivity().runOnUiThread(() -> {
            showDialog();
            // Удаление внешних символов
            String cleanedData = barcodeData.substring(1, barcodeData.length() - 1);
            presenter.getAuthUser(cleanedData);
            Toast.makeText(getContext(), "Scanned Data: " + cleanedData, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onScanFailed(String errorMessage) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (scannerController != null) {
                scannerController.resumeScanner();
            }
        } catch (Exception e) {
            Log.e("AuthFragment", "Error resuming scanner", e);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (scannerController != null) {
                scannerController.releaseScanner();
            }
        } catch (Exception e) {
            Log.e("AuthFragment", "Error releasing scanner", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (scannerController != null) {
                scannerController.releaseScanner();
                scannerController = null;
            }
        } catch (Exception e) {
            Log.e("AuthFragment", "Error destroying scanner", e);
        }
        if (mWaitDialog != null) {
            mWaitDialog.dismiss();
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.hello_fragment;
    }

    @Override
    public void successMessage(@NonNull String msg) {
        SPHelper.setNameEmployer(msg);
        if (mWaitDialog != null) {
            mWaitDialog.dismiss();
        }
        Toast.makeText(getContext(), "Пользователь успешно авторизован", Toast.LENGTH_SHORT).show();
        // Замена фрагмента
        if (getActivity() instanceof StartActivity) {
            ((StartActivity) getActivity()).replaceFragment(StartFragment.Companion.newInstance(), true);
        }
    }

    @Override
    public void errorMessage(@NonNull String msg) {
        if (mWaitDialog != null) {
            mWaitDialog.dismiss();
        }
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
