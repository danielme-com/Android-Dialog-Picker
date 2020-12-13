package com.danielme.android.dialogfragmentpicker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogSinglePickerFragment extends AppCompatDialogFragment {

  private static final int UNSELECTED = -1;
  private static final String ARG_SELECTED = "ARG_SELECTED";
  private static final String ARG_ITEMS = "ARG_ITEMS";
  private static final String ARG_TITLE = "ARG_TITLE";
  private static final String STATE_SELECTED = "STATE_SELECTED";

  private DialogPickerFragmentListener listener;

  private int selected;

  public static DialogSinglePickerFragment newInstance(Integer selection, String[] items, String title) {
    DialogSinglePickerFragment fragment = new DialogSinglePickerFragment();
    Bundle args = new Bundle();
    if (selection != null) {
      args.putInt(ARG_SELECTED, selection);
    }
    args.putStringArray(ARG_ITEMS, items);
    args.putString(ARG_TITLE, title);
    fragment.setArguments(args);
    return fragment;
  }

  // Override the Fragment.onAttach() method to instantiate the DialogListener
  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    // Verify that the host activity implements the callback interface
    try {
      // Instantiate the DialogListener so we can send events to the host
      listener = (DialogPickerFragmentListener) context;
    } catch (ClassCastException e) {
      // The activity doesn't implement the interface, throw exception
      throw new ClassCastException(context.toString()
              + " must implement DialogSinglePickerFragment");
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    if (savedInstanceState != null) { //rotation
      selected = savedInstanceState.getInt(STATE_SELECTED, UNSELECTED);
    } else if (getArguments() != null) {
      selected = getArguments().getInt(ARG_SELECTED, UNSELECTED);
    }

    final String[] brands = getArguments().getStringArray(ARG_ITEMS);

    return new MaterialAlertDialogBuilder(getContext())
            .setTitle(getArguments().getString(ARG_TITLE))
            .setSingleChoiceItems(brands, selected, (dialog, which) -> selected = which)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
              if (selected != UNSELECTED) {
                listener.getSelected(brands[selected]);
                //Toast.makeText(getContext(), brands[selected], Toast.LENGTH_SHORT).show();
              }
            })
            .create();
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(STATE_SELECTED, selected);
  }

}
