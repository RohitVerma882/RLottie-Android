package com.rohitop.rlottiesample;

import android.app.Application;

import cat.ereza.customactivityoncrash.config.CaocConfig;

import com.rohitop.rlottie.RLottie;

public class RLottieApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		CaocConfig.Builder.create()
			.backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM)
			.enabled(true)
			.showErrorDetails(true)
			.showRestartButton(true)
			.logErrorOnRestart(true)
			.trackActivities(true)
			.minTimeBetweenCrashesMs(2000)
			.apply();
			
		RLottie.init(this);
	}
}
