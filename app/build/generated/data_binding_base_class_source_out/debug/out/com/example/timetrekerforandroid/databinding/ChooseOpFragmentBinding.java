// Generated by view binder compiler. Do not edit!
package com.example.timetrekerforandroid.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.timetrekerforandroid.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ChooseOpFragmentBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppCompatButton btnSk;

  @NonNull
  public final View line;

  @NonNull
  public final LinearLayout linear;

  @NonNull
  public final TextView name;

  @NonNull
  public final RecyclerView rv;

  private ChooseOpFragmentBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppCompatButton btnSk, @NonNull View line, @NonNull LinearLayout linear,
      @NonNull TextView name, @NonNull RecyclerView rv) {
    this.rootView = rootView;
    this.btnSk = btnSk;
    this.line = line;
    this.linear = linear;
    this.name = name;
    this.rv = rv;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ChooseOpFragmentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ChooseOpFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.choose_op_fragment, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ChooseOpFragmentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_sk;
      AppCompatButton btnSk = ViewBindings.findChildViewById(rootView, id);
      if (btnSk == null) {
        break missingId;
      }

      id = R.id.line;
      View line = ViewBindings.findChildViewById(rootView, id);
      if (line == null) {
        break missingId;
      }

      id = R.id.linear;
      LinearLayout linear = ViewBindings.findChildViewById(rootView, id);
      if (linear == null) {
        break missingId;
      }

      id = R.id.name;
      TextView name = ViewBindings.findChildViewById(rootView, id);
      if (name == null) {
        break missingId;
      }

      id = R.id.rv;
      RecyclerView rv = ViewBindings.findChildViewById(rootView, id);
      if (rv == null) {
        break missingId;
      }

      return new ChooseOpFragmentBinding((ConstraintLayout) rootView, btnSk, line, linear, name,
          rv);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}