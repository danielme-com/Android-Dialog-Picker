package com.danielme.android.dialogfragmentpicker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends AppCompatActivity implements DialogPickerFragmentListener {

    private TextView textView;
    private MultiPickBroadcastReceiver receiver;
    private String[] brands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        textView = findViewById(R.id.textView);

        brands = new String[]{getString(R.string.htc), getString(R.string.lg), getString(R.string.meizu),
                getString(R.string.motorola), getString(R.string.samsung)};

        setupButtons();

        setupBroadcast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void setupBroadcast() {
        receiver = new MultiPickBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DialogMultiplePickerFragment.BROADCAST_MULTIPICK);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    private void setupButtons() {
        findViewById(R.id.button1).setOnClickListener(v -> {
            DialogSinglePickerFragment dialogFragment =
                    DialogSinglePickerFragment.newInstance(4, brands, getString(R.string.pick_brand));
            dialogFragment.show(getSupportFragmentManager(), DialogSinglePickerFragment.class.getSimpleName());
        });

        findViewById(R.id.button2).setOnClickListener(v -> {
            DialogMultiplePickerFragment dialogFragment =
                    DialogMultiplePickerFragment.newInstance(brands, getString(R.string.pick_brands), 0, 3);
            dialogFragment.show(getSupportFragmentManager(), DialogMultiplePickerFragment.class.getSimpleName());
        });
    }

    @Override
    public void getSelected(String selected) {
        textView.setText(selected);
    }

    class MultiPickBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            textView.setText(intent.getStringExtra(DialogMultiplePickerFragment.SELECTION));
        }
    }
}
