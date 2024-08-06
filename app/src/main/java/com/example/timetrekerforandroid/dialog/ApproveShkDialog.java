package com.example.timetrekerforandroid.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import static com.example.timetrekerforandroid.R.style.AlertDialogCustom;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import com.example.timetrekerforandroid.util.SPHelper;

public class ApproveShkDialog extends DialogFragment {
    private String shk;
    private OnSendNewShk onSendNewShk;

    public ApproveShkDialog(String shk, OnSendNewShk onSendNewShk) {
        this.shk = shk;
        this.onSendNewShk = onSendNewShk;
    }

    public interface OnSendNewShk{
        void sendNewShk(String shk);
    }


    public static ApproveShkDialog newInstance(String shk, OnSendNewShk sendNewShk) {
        return new ApproveShkDialog(shk,sendNewShk);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), AlertDialogCustom));
        builder.setTitle("Изменения ШК")
                .setMessage("Подтверждаете ли вы перезапись ШК: " + shk)
                .setNegativeButton(Html.fromHtml("<font color='#000000'>Нет</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton(Html.fromHtml("<font color='#F31616'>Да</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onSendNewShk.sendNewShk(shk);
                        dialogInterface.cancel();
                    }
                });



        return builder.create();
    }
}
