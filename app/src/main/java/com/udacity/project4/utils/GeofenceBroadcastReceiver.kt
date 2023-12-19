package com.udacity.project4.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.udacity.project4.locationreminders.geofence.GeofenceTransitionsJobIntentService
import com.udacity.project4.locationreminders.savereminder.SaveReminderFragment.Companion.ACTION_GEOFENCE_EVENT

/*
 * Triggered by the Geofence.  Since we only have one active Geofence at once, we pull the request
 * ID from the first Geofence, and locate it within the registered landmark data in our
 * GeofencingConstants within GeofenceUtils, which is a linear string search. If we had  very large
 * numbers of Geofence possibilities, it might make sense to use a different data structure.  We
 * then pass the Geofence index into the notification, which allows us to have a custom "found"
 * message associated with each Geofence.
 */
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == ACTION_GEOFENCE_EVENT) {
            Log.i("GeofenceReceiver", "Geofence event received")
            GeofenceTransitionsJobIntentService.enqueueWork(context, intent)
        }

    }
}