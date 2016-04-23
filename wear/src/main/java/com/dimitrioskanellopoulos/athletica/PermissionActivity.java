package com.dimitrioskanellopoulos.athletica;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class PermissionActivity extends WearableActivity {

    private static final String TAG = "PermissionActivity";

    private static final String[] wantedPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BODY_SENSORS};

    private static final int PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_face_permissions);
        setAmbientEnabled();
    }

    public void onClickEnablePermission(View view) {
        requestPermissions(wantedPermissions);
    }

    public void requestPermissions(String[] wantedPermissions) {
        Log.d(TAG, "onClickEnablePermission()");
        ActivityCompat.requestPermissions(this, wantedPermissions, PERMISSION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult()");

        // It's not out code
        if (requestCode != PERMISSION_REQUEST){
            return;
        }

        // We did not have any grant results
        if (grantResults.length <= 0){
            return;
        }

        ArrayList toRequestAgain = new ArrayList<>();

        for (Integer grantResult : grantResults){

            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                // Check next this one is granted
                continue;
            }

            // Permission was not granted
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BODY_SENSORS)) {
                Toast.makeText(this, "Please go to settings and allow this permission if you want to get heart rate", Toast.LENGTH_LONG).show();
                //Never ask again selected, or device policy prohibits the app from having that permission.
                //So, disable that feature, or fall back to another situation...
                // user denied flagging NEVER ASK AGAIN
                // you can either enable some fall back,
                // disable features of your app
                // or open another dialog explaining
                // again the permission and directing to
                // the app setting
                continue;
            }

            toRequestAgain.add(permissions[0]);
            Toast.makeText(this, "Please allow body sensor permission for heart rate", Toast.LENGTH_LONG).show();
            finish();
            // user denied WITHOUT never ask again
            // this is a good place to explain the user
            // why you need the permission and ask if he want
            // to accept it (the rationale)
        }
    }
}