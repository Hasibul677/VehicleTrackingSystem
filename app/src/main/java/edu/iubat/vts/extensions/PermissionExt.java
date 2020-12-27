package edu.iubat.vts.extensions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class PermissionExt {
    public final static int PERMISSION_REQUEST_MULTIPLE_PERMISSIONS = 1000;
    public final static int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1001;
    public final static int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1002;
    public final static int PERMISSION_REQUEST_CAMERA = 1003;
    public final static int PERMISSION_REQUEST_RECORD_AUDIO = 1004;
    public final static int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1005;

    private static boolean isBuildVersionM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void requestPermission(@NonNull Activity activity, String permission, int requestCode) {
        if (isBuildVersionM()) {
            ActivityCompat.requestPermissions(activity, new String[] { permission }, requestCode);
        }
    }

    public static void requestPermissions(@NonNull Activity activity, String[] permissions, int requestCode) {
        if (isBuildVersionM()) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
    }

    public static void requestPermissions(@NonNull Activity activity, List<String> permissionList, int requestCode) {
        if (isBuildVersionM()) {
            ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[0]), requestCode);
        }
    }

    public static boolean hasReadExternalStoragePermission(@NonNull Context context) {
        if (isBuildVersionM()) {
            return ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestReadExternalStoragePermission(@NonNull Activity activity) {
        if (isBuildVersionM()) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
                    PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    public static boolean hasWriteExternalStoragePermission(@NonNull Context context) {
        if (isBuildVersionM()) {
            return ContextCompat
                    .checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestWriteExternalStoragePermission(@NonNull Activity activity) {
        if (isBuildVersionM()) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    public static boolean hasCameraPermission(@NonNull Context context) {
        if (isBuildVersionM()) {
            return ContextCompat
                    .checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestCameraPermission(@NonNull Activity activity) {
        if (isBuildVersionM()) {
            ActivityCompat.requestPermissions(activity,
                    new String[] { Manifest.permission.CAMERA },
                    PERMISSION_REQUEST_CAMERA);
        }
    }

    public static boolean hasRecordAudioPermission(@NonNull Context context) {
        if (isBuildVersionM()) {
            return ContextCompat
                    .checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestRecordAudioPermission(@NonNull Activity activity) {
        if (isBuildVersionM()) {
            ActivityCompat.requestPermissions(activity,
                    new String[] { Manifest.permission.RECORD_AUDIO },
                    PERMISSION_REQUEST_RECORD_AUDIO);
        }
    }

    public static boolean hasAccessFineLocationPermission(@NonNull Context context) {
        if (isBuildVersionM()) {
            return ContextCompat
                    .checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestAccessFineLocationPermission(@NonNull Activity activity) {
        if (isBuildVersionM()) {
            ActivityCompat.requestPermissions(activity,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
}
