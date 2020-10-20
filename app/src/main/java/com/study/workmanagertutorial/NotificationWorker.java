package com.study.workmanagertutorial;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Created on : Oct 10, 2020
 * Author     : Nasr
 */
public class NotificationWorker extends Worker {
    private static final String WORK_RESULT = "work_result";
    private final Context context;

    String latitude, longitude;


    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
//        Data taskData = getInputData();
//        String taskDataString = taskData.getString(MainActivity.MESSAGE_STATUS);
//        showNotification("WorkManager", taskDataString != null ? taskDataString : "Message has been Sent");

//        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            OnGPS();
//        } else {
//        }

        Location location = LocationUtils.getInstance(getApplicationContext()).getLocationSingle().getValue();
        if (location != null) {
            double lat = location.getLatitude();
            double longi = location.getLongitude();
            latitude = String.valueOf(lat);
            longitude = String.valueOf(longi);
            showNotification("Location Updated!",
                    "Lat: " + latitude + " , " + "Lng: " + longitude);
        } else {
            showNotification("Location Failed!", "Unable to find location");
        }

        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();
        return Result.success(outputData);

    }

    private void showNotification(String task, String desc) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        String channelId = "task_channel";
        String channelName = "task_name";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.ic_launcher);

        manager.notify(1, builder.build());

    }


//    private void getLocation() {
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                &&
//                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                &&
//                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        } else {
//
//            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (location != null) {
//                double lat = location.getLatitude();
//                double longi = location.getLongitude();
//                latitude = String.valueOf(lat);
//                longitude = String.valueOf(longi);
//
//                showNotification("Location Updated!",
//                        "Lat: " + latitude + " , " + "Lng: " + longitude);
//            } else {
//                showNotification("Location Failed!", "Can't Get Location..");
//            }
////            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500,
////                    5, new LocationListener() {
////                        @Override
////                        public void onLocationChanged(@NonNull Location location) {
////                            if (location != null) {
////                                double lat = location.getLatitude();
////                                double longi = location.getLongitude();
////                                latitude = String.valueOf(lat);
////                                longitude = String.valueOf(longi);
////                                showNotification("Location Updated!",
////                                        "Lat: " + latitude + " , " + "Lng: " + longitude);
////                            } else {
////                                Log.e("MESSAGE_STATUS", "Unable to find location.");
////                            }
////                        }
////                    }, Looper.getMainLooper());
//        }
//    }

}