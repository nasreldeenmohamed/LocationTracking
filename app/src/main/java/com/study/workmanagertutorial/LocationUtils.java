package com.study.workmanagertutorial;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;


public class LocationUtils {
    private static final String TAG = "LocationUtils";
    private static LocationUtils locationUtils;
    private final LocationRequest mLocationRequest;
    private final LocationManager locationManager;
    Context activity;
    private MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private Location location;

    private LocationUtils(Context activity) {
        this.activity = activity;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10 * 1000);
        mLocationRequest.setFastestInterval(2 * 1000);
        mLocationRequest.setNumUpdates(5);

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    public static LocationUtils getInstance(Context context) {
        if (locationUtils == null)
            locationUtils = new LocationUtils(context);
        return locationUtils;
    }

    //    public static MutableLiveData<Location> getLocation(Context activity) {
//        try {
//            mLocationRequest = new LocationRequest();
//            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            mLocationRequest.setInterval(10 * 1000);
//            mLocationRequest.setFastestInterval(2 * 1000);
//            mLocationRequest.setNumUpdates(1);
//
//            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//            Log.e(TAG, "isGPSEnabled: = " + isGPSEnabled);
//            if (!isGPSEnabled) {
////                if (CommonUtils.isAppIsInBackground(activity)) {
////                    CommonUtils.notifyUserToEnableInternetOrGPS(activity, true);
////                } else {
////                    CommonUtils.showSettingsAlert(activity);
////                }
////            }else {
//
//                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                        && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                        && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    Activity#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for Activity#requestPermissions for more details.
//                } else {
//                    mLocationCallback = new LocationCallback() {
//                        @Override
//                        public void onLocationResult(LocationResult locationResult) {
//                            List<Location> mLastLocation = locationResult.getLocations();
//                            if (mLastLocation.size() > 0) {
//                                locationMutableLiveData.setValue(mLastLocation.get(mLastLocation.size() - 1));
//                                Log.e(TAG, " Utils  +++ Location:  " + mLastLocation.get(mLastLocation.size() - 1));
//                                LocationUtils.stopLocationUpdate();
//                            }
//                        }
//                    };
//                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
//                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
//                }
//
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            Log.e(TAG, "  e  " + ex.getMessage());
//        }
//
//        return locationMutableLiveData;
//    }
    public boolean isGPSEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public LiveData<Location> getLocationSingle() {
        try {

            if (!isGPSEnabled()) {
                locationMutableLiveData = null;
            } else {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                }
                LocationCallback mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        List<Location> mLastLocation = locationResult.getLocations();
                        if (mLastLocation.size() > 0) {
                            location = mLastLocation.get(mLastLocation.size() - 1);
                            locationMutableLiveData.setValue(location);
                        }
                    }
                };
                FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
            }
        } catch (Exception ex) {
            locationMutableLiveData = null;
            ex.printStackTrace();
            Log.e(TAG, "  e  " + ex.getMessage());
        }
        return locationMutableLiveData;
    }

//    public static Location getLocationNoLiveData(Context activity) {
//        try {
//            mLocationRequest = new LocationRequest();
//            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            mLocationRequest.setInterval(10 * 1000);
//            mLocationRequest.setFastestInterval(2 * 1000);
//            mLocationRequest.setNumUpdates(5);
//
//            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
////                LogUtils.INSTANCE.LOGD(TAG + "isGPSEnabled: = " + isGPSEnabled);
////                if (!isGPSEnabled) {
////                    if (CommonUtils.isAppIsInBackground(activity)) {
////                        CommonUtils.notifyUserToEnableInternetOrGPS(activity, true);
////                    } else {
////                        CommonUtils.showSettingsAlert(activity);
////                    }
////                }else {
//            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    Activity#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for Activity#requestPermissions for more details.
//            }
//            mLocationCallback = new LocationCallback() {
//                @Override
//                public void onLocationResult(LocationResult locationResult) {
//                    List<Location> mLastLocation = locationResult.getLocations();
//                    if (mLastLocation.size() > 0) {
//                        location = mLastLocation.get(mLastLocation.size() - 1);
//                        locationMutableLiveData.setValue(location);
//                    }
//                }
//            };
//            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
//            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
////                }
//        } catch (Exception ex) {
//            ex.printStackTrace();
////                LogUtils.INSTANCE.LOGD(TAG + "  e  " + ex.getMessage());
//        }
//        return location;
//    }

//    public static void stopLocationUpdate() {
//        if (mFusedLocationClient != null) {
//            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
//        }
//    }

//    public Task getDeviceLocation(Context activity) {
//        getLocation(activity);
//        Task taskLocation = null;
//        if (LocationServices.getFusedLocationProviderClient(activity) != null) {
//            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
//            try {
//                taskLocation = mFusedLocationClient.getLastLocation();
//            } catch (SecurityException e) {
//                Log.d("getLocation", "getDeviceLocation: SecurityException" + e.getMessage());
//            }
//        } else {
////            CommonUtils.showSettingsAlert(activity);
//        }
//        return taskLocation;
//    }


}
