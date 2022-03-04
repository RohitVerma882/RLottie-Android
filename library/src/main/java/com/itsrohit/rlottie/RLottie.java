package com.itsrohit.rlottie;

import android.content.Context;
import android.os.Handler;
import java.security.SecureRandom;
import java.io.File;
import java.io.FileInputStream;
import android.view.WindowManager;
import android.view.Display;

import androidx.annotation.NonNull;

public class RLottie {

	protected static final String TAG = RLottie.class.getSimpleName();

    private final static String LIB_NAME = "rlottie";
	private static boolean libLoaded = false;

	private static volatile Context context;
	private static volatile Handler handler;

	private static float density = 1;
    private static float screenRefreshRate = 60;
	
	private static SecureRandom random = new SecureRandom();
	private static volatile DispatchQueue queue = new DispatchQueue("rlottieQueue");
	
	static {
        try {
            random.setSeed(getSeedBuffer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RLottie() {
    }

	public static void init(@NonNull Context context) {
		RLottie.context = context.getApplicationContext();
		RLottie.handler = new Handler(context.getMainLooper());
		reloadConfiguration(context);

		synchronized (RLottie.class) {
			if (libLoaded) {
				return;
			}

			System.loadLibrary(LIB_NAME);
			libLoaded = true;
		}
	}

	private static void reloadConfiguration(@NonNull Context context) {
        density = context.getResources().getDisplayMetrics().density;

		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		if (manager != null) {
			Display display = manager.getDefaultDisplay();
			if (display != null) {
				screenRefreshRate = display.getRefreshRate();
			}
		}
    }

	protected static @NonNull Context getContext() {
		return context;
	}

	protected static @NonNull Handler getHandler() {
		return handler;
	}

	protected static DispatchQueue getQueue() {
		return queue;
	}

	protected static SecureRandom getRandom() {
		return random;
	}

	protected static float getDensity() {
        return density;
    }

	protected static float getScreenRefreshRate() {
        return screenRefreshRate;
    }

	protected static int toPx(float value) {
        if (value == 0) {
            return 0;
        }

        return (int) Math.ceil(density * value);
    }

	protected static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    protected static void runOnUIThread(Runnable runnable, long delay) {
        if (handler == null) {
            return;
        }

        if (delay == 0) {
            handler.post(runnable);
        } else {
            handler.postDelayed(runnable, delay);
        }
    }

	private static byte[] getSeedBuffer() throws Exception {
		File URANDOM_FILE = new File("/dev/urandom");
		FileInputStream sUrandomIn = new FileInputStream(URANDOM_FILE);
		byte[] buffer = new byte[1024];
		sUrandomIn.read(buffer);
		sUrandomIn.close();
		return buffer;
	}
}
