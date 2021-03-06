package com.danielme.android.dialogfragmentpicker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogMultiplePickerFragment extends AppCompatDialogFragment {

    public static final String BROADCAST_MULTIPICK = "BROADCAST_MULTIPICK";
    public static final String SELECTION = "SELECTION";

    private static final String ARG_SELECTED = "ARG_SELECTED";
    private static final String STATE_SELECTED = "STATE_SELECTED";
    private static final String ARG_ITEMS = "ARG_ITEMS";
    private static final String ARG_TITLE = "ARG_TITLE";

    private boolean[] selected;

    public static DialogMultiplePickerFragment newInstance(String[] items, String title, int... selection) {
        DialogMultiplePickerFragment fragment = new DialogMultiplePickerFragment();
        Bundle args = new Bundle();
        if (selection != null) {
            args.putIntArray(ARG_SELECTED, selection);
        }
        args.putStringArray(ARG_ITEMS, items);
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final CharSequence[] brands = getArguments().getStringArray(ARG_ITEMS);

        if (savedInstanceState != null) { //rotation
            selected = savedInstanceState.getBooleanArray(STATE_SELECTED);
        } else if (getArguments() != null) {
            selected = new boolean[brands.length];
            int[] selection = getArguments().getIntArray(ARG_SELECTED);
            for (int aSelection : selection) {
                selected[aSelection] = true;
            }
        }

       return new MaterialAlertDialogBuilder(getContext()).setTitle(getArguments().getString(ARG_TITLE)).setMultiChoiceItems(brands, selected,
                (dialog, which, isChecked) -> selected[which] = isChecked)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i]) {
                            sb.append(brands[i]).append(" ");
                        }
                    }

                    //Toast.makeText(getContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(BROADCAST_MULTIPICK);
                    intent.putExtra(SELECTION, sb.toString());
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                }).create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBooleanArray(STATE_SELECTED, selected);
    }

}