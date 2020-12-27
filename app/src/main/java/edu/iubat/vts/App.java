package edu.iubat.vts;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseAnalytics.getInstance(this);
    }
}
