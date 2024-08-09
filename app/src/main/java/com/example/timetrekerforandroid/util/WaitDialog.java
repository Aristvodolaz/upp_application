package com.example.timetrekerforandroid.util;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.timetrekerforandroid.ApplicationLoader;
import com.example.timetrekerforandroid.R;

public class WaitDialog extends DialogFragment {

    public static final String TAG = WaitDialog.class.getSimpleName();

    private TextView mTextView;
    private String mText = ApplicationLoader.getInstance().getResources().getString(R.string.please_wait);

    public static WaitDialog newInstance() {
        WaitDialog dialog = new WaitDialog();
        dialog.setRetainInstance(true);
        return dialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (IllegalStateException ignored) {
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setIndeterminate(true);
        LinearLayout.LayoutParams progressBarParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        progressBarParams.gravity = Gravity.CENTER;
        progressBarParams.leftMargin = Utils.dpToPx(18);
        progressBarParams.topMargin = Utils.dpToPx(18);
        progressBarParams.rightMargin = Utils.dpToPx(18);
        progressBarParams.bottomMargin = Utils.dpToPx(18);
        progressBar.setLayoutParams(progressBarParams);

        mTextView = new TextView(getContext());
        mTextView.setText(mText);
        mTextView.setTextColor(0xff_7c_7c_7c);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textParams.gravity = Gravity.CENTER_VERTICAL;
        textParams.rightMargin = Utils.dpToPx(30);
        mTextView.setLayoutParams(textParams);

        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setBackgroundColor(0xff_ff_ff_ff);
        container.setLayoutParams(LayoutUtils.createViewGroup(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        container.addView(progressBar);
        container.addView(mTextView);

        dialog.setContentView(container);
        return dialog;
    }

    public void setText(String str) {
        mText = str;
        if (mTextView != null)
            mTextView.setText(mText);
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }
}

