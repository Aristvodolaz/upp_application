package com.example.timetrekerforandroid.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import com.example.timetrekerforandroid.R;

import static com.example.timetrekerforandroid.R.style.AlertDialogCustom;

public class CancelReasonDialog extends DialogFragment {
    private OnCancelReasonSelected onCancelReasonSelected;

    public CancelReasonDialog(OnCancelReasonSelected onCancelReasonSelected) {
        this.onCancelReasonSelected = onCancelReasonSelected;
    }

    public interface OnCancelReasonSelected {
        void onReasonSelected(String reason, String comment);
    }

    public static CancelReasonDialog newInstance(OnCancelReasonSelected onCancelReasonSelected) {
        return new CancelReasonDialog(onCancelReasonSelected);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate custom view with EditText for comment input
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_cancel_reson, null);

        // Reference to the EditText for the comment input
        EditText commentEditText = dialogView.findViewById(R.id.commentEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), AlertDialogCustom));
        builder.setTitle("Причины отмены")
                .setView(dialogView)  // Set the custom view with the EditText
                .setItems(new String[]{
                        "Не соответствует СГ",
                        "Недостача",
                        "Недовложение",
                        "Брак",
                        "Вычерк склада"
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String reason = "";
                        switch (which) {
                            case 0:
                                reason = "Не соответствует СГ";
                                break;
                            case 1:
                                reason = "Недостача";
                                break;
                            case 2:
                                reason = "Недовложение";
                                break;
                            case 3:
                                reason = "Брак";
                                break;
                            case 4:
                                reason = "Вычерк склада";
                                break;
                        }

                        // Get the entered comment from EditText
                        String comment = commentEditText.getText().toString().trim();
                        onCancelReasonSelected.onReasonSelected(reason, comment); // Pass both reason and comment
                    }
                })
                .setNegativeButton(Html.fromHtml("<font color='#000000'>Отмена</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}
