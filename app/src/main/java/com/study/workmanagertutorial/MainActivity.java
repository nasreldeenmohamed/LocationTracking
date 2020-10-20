package com.study.workmanagertutorial;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.work.BackoffPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE_STATUS = "message_status";
    private static final int REQUEST_LOCATION = 1;
    private PeriodicWorkRequest work;
    private WorkManager mWorkManager;
    private TextView textView;

    private LocationUtils locationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_LOCATION);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        }

        textView = findViewById(R.id.textView);

        mWorkManager = WorkManager.getInstance();

        locationUtils = LocationUtils.getInstance(this);

//        final OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).build();

        work = new PeriodicWorkRequest.Builder(NotificationWorker.class, 15, TimeUnit.MINUTES)
                .addTag(MESSAGE_STATUS) // setting a backoff on case the work needs to retry
                .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                .build();


        findViewById(R.id.btnStartWorkManager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationUtils.isGPSEnabled()) {
                    mWorkManager.enqueue(work);
                } else {
                    showGPSDialogWhenDisabled();
                }
            }
        });

        mWorkManager.getWorkInfoByIdLiveData(work.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    textView.append(state.toString() + "\n");
                }
            }
        });

        Button btnStopWorkManager = findViewById(R.id.btnStopWorkManager);
        btnStopWorkManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWorkManager.cancelAllWork();
            }
        });

    }

    private void showGPSDialogWhenDisabled() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}