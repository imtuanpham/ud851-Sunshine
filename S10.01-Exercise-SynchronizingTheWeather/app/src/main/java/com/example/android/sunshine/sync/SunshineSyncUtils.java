package com.example.android.sunshine.sync;
// COMPLETED (9) Create a class called SunshineSyncUtils
    //  COMPLETED (10) Create a public static void method called startImmediateSync
    //  COMPLETED (11) Within that method, start the SunshineSyncIntentService

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class SunshineSyncUtils {
    public static void startImmediateSync(@NonNull final Context context) {
        Intent syncIntent = new Intent(context, SunshineSyncIntentService.class);
        context.startService(syncIntent);
    }
}