package edu.iubat.vts.service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.iubat.vts.R;
import edu.iubat.vts.data.model.Bus;

public class TrackerService extends Service {
    private static final String LOG_TAG = TrackerService.class.getSimpleName();

    public static void startForegroundService(@NonNull Context context, @NonNull Bus bus) {
        trackingNowBus = bus;
        Intent intent = new Intent(context, TrackerService.class);
        Bundle bundle = new Bundle();
        bundle.putString(BUS_DOCUMENT_ID, bus.getDocumentId());
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    private static final String LOCATION_TRACKER_CHANNEL = "location_tracker_channel";
    private static final int LOCATION_TRACKER_NOTIFICATION = 0xb339;

    public static final String BUS_DOCUMENT_ID = "bus_document_id";
    public static final String ACTION_STOP_TRACKING = "action_stop_tracking";

    public static boolean isTracking = false;
    public static Bus trackingNowBus = null;
    public static TrackerCallback trackerCallback;

    public static void setTrackerCallback(@NonNull TrackerCallback callback) {
        trackerCallback = callback;
    }

    private String busDocumentId;
    private TrackerBroadcastReceiver trackerBroadcastReceiver;
    private NotificationManager notificationManager;
    private FusedLocationProviderClient locationProviderClient;
    private TrackerLocationCallback trackerLocationCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        this.notificationManager = (NotificationManager)
                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        trackerBroadcastReceiver = new TrackerBroadcastReceiver();
        registerReceiver(trackerBroadcastReceiver, new IntentFilter(ACTION_STOP_TRACKING));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() != null) {
            busDocumentId = intent.getExtras().getString(BUS_DOCUMENT_ID);
        }
        if (busDocumentId != null && !busDocumentId.isEmpty()) {
            buildNotification();
        } else {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isTracking = false;
        trackingNowBus = null;
        if (trackerCallback != null) {
            trackerCallback.onTrackerStatusUpdate(false);
        }
        unregisterReceiver(trackerBroadcastReceiver);
        if (locationProviderClient != null) {
            locationProviderClient.removeLocationUpdates(trackerLocationCallback);
        }
    }

    private void buildNotification() {
        if (shouldCreateNowPlayingChannel()) {
            createNowPlayingChannel();
        }
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(ACTION_STOP_TRACKING), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                LOCATION_TRACKER_CHANNEL)
                .setContentTitle(getString(R.string.text_tracking_now, trackingNowBus.getBusNumber()))
                .setContentText(getString(R.string.notification_content_text))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_directions_bus_black_24dp);
        startForeground(LOCATION_TRACKER_NOTIFICATION, builder.build());
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(5000);
        request.setFastestInterval(2000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationProviderClient = LocationServices
                .getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            trackerLocationCallback = new TrackerLocationCallback();
            locationProviderClient.requestLocationUpdates(request, trackerLocationCallback, null);
            isTracking = true;
        }
    }

    private void stopTracking() {
        isTracking = false;
        trackingNowBus = null;
        if (trackerCallback != null) {
            trackerCallback.onTrackerStatusUpdate(false);
        }
        stopForeground(true);
        stopSelf();
    }

    private boolean shouldCreateNowPlayingChannel() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !nowPlayingChannelExists();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private boolean nowPlayingChannelExists() {
        return notificationManager.getNotificationChannel(LOCATION_TRACKER_CHANNEL) != null;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNowPlayingChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(LOCATION_TRACKER_CHANNEL,
                getApplicationContext().getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW);
        notificationChannel.setDescription(getApplicationContext()
                .getString(R.string.notification_channel_description));
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private class TrackerLocationCallback extends LocationCallback {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(busDocumentId);
            Location location = locationResult.getLastLocation();
            if (location != null) {
                ref.setValue(location);
            }
        }
    }

    private class TrackerBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_STOP_TRACKING)) {
                stopTracking();
            }
        }
    }

    public interface TrackerCallback {
        void onTrackerStatusUpdate(boolean isActive);
    }
}
