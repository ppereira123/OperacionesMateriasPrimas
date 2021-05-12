package com.example.operacionesmteriasprimas;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

public class HolcimQuarry extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
