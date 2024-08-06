// Generated by view binder compiler. Do not edit!
package com.example.timetrekerforandroid.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.timetrekerforandroid.R;
import com.google.android.material.tabs.TabLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class StartFragmentBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final LinearLayout linear;

  @NonNull
  public final Switch switchId;

  @NonNull
  public final TabLayout tab;

  private StartFragmentBinding(@NonNull ConstraintLayout rootView, @NonNull LinearLayout linear,
      @NonNull Switch switchId, @NonNull TabLayout tab) {
    this.rootView = rootView;
    this.linear = linear;
    this.switchId = switchId;
    this.tab = tab;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static StartFragmentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static StartFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.start_fragment, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static StartFragmentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.linear;
      LinearLayout linear = ViewBindings.findChildViewById(rootView, id);
      if (linear == null) {
        break missingId;
      }

      id = R.id.switch_id;
      Switch switchId = ViewBindings.findChildViewById(rootView, id);
      if (switchId == null) {
        break missingId;
      }

      id = R.id.tab;
      TabLayout tab = ViewBindings.findChildViewById(rootView, id);
      if (tab == null) {
        break missingId;
      }

      return new StartFragmentBinding((ConstraintLayout) rootView, linear, switchId, tab);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
