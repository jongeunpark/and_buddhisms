package com.jp.buddhisms;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class MYJPAPP extends Application {

	private static final String PROPERTY_ID = "UA-52978084-4";

	public static int GENERAL_TRACKER = 0;

	public enum TrackerName {
		APP_TRACKER, GLOBAL_TRACKER, ECOMMERCE_TRACKER,
	}

	public HashMap mTrackers = new HashMap();

	public MYJPAPP() {
		super();
	}

	public synchronized Tracker getTracker(TrackerName appTracker) {
		if (!mTrackers.containsKey(appTracker)) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			Tracker t = (appTracker == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID) : (appTracker == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker) : analytics.newTracker(R.xml.ecommerce_tracker);
			t.enableAdvertisingIdCollection(true);
			mTrackers.put(appTracker, t);
		}
		
		return (Tracker) mTrackers.get(appTracker);
	}
}