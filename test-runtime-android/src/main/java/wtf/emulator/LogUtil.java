package wtf.emulator;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

class LogUtil {
    private static final String TAG = "emulatorwtf";
    private static final boolean debugLogEnabled;

    private static final AtomicInteger stackLogCounter = new AtomicInteger(0);

    static {
        debugLogEnabled = Log.isLoggable(TAG, Log.DEBUG);
    }

    static void logd(String message) {
        if (debugLogEnabled) {
            Log.d(TAG, message);
        }
    }

    static void logd(String message, Throwable tr) {
        if (debugLogEnabled) {
            Log.d(TAG, message, tr);
        }
    }

    static void loge(String message) {
        Log.e(TAG, message);
    }

    static void loge(String message, @NonNull Throwable tr) {
        if (stackLogCounter.getAndIncrement() > 3) {
            Log.e(TAG, message);
            Log.e(TAG, "trace message: " + tr.getMessage());
            Log.e("TAG", "Stacktrace log count exceeded, not printing stacktrace itself");
            return;
        }
        Log.e(TAG, message, tr);
    }
}
