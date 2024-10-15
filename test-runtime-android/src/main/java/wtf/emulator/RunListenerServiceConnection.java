package wtf.emulator;

import static wtf.emulator.LogUtil.logd;
import static wtf.emulator.LogUtil.loge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.runner.notification.RunListener;

import java.util.concurrent.CountDownLatch;

import wtf.emulator.observer.RemoteRunListenerService;

final class RunListenerServiceConnection extends RunListener implements ServiceConnection {
    private final static RemoteRunListenerService NO_OP_SERVICE = new RemoteRunListenerService.Default();
    private final Context context;

    private final CountDownLatch serviceConnectionLatch = new CountDownLatch(1);

    private RemoteRunListenerService runListenerService = NO_OP_SERVICE;

    public RunListenerServiceConnection(Context context) {
        this.context = context;
    }

    public void bindToService() {
        Bundle instrumentationArguments = InstrumentationRegistry.getArguments();

        // go to NO_OP if we're running for orchestrator test collection
        String listTestsForOrchestrator = instrumentationArguments.getString("listTestsForOrchestrator");
        if ("true".equals(listTestsForOrchestrator)) {
            logd("AJUR is collecting tests for orchestrator, skipping run listener");
            runListenerService = NO_OP_SERVICE;
            serviceConnectionLatch.countDown();
            return;
        }

        Intent intent = new Intent();
        intent.setComponent(new ComponentName("wtf.emulator.observer", "wtf.emulator.observer.RunListenerService"));

        boolean bindResult = context.bindService(intent, this, Context.BIND_AUTO_CREATE);
        if (!bindResult) {
            loge("Failed to bind to RunListenerService");
            runListenerService = NO_OP_SERVICE;
            serviceConnectionLatch.countDown();
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        logd("onServiceConnected");
        runListenerService = RemoteRunListenerService.Stub.asInterface(service);
        serviceConnectionLatch.countDown();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        logd("onServiceDisconnected. Waiting for a new connection");
        runListenerService = NO_OP_SERVICE;
        serviceConnectionLatch.countDown();
    }

    @Override
    public void onBindingDied(ComponentName name) {
        logd("Binding died. Trying to rebind");
        runListenerService = NO_OP_SERVICE;
        serviceConnectionLatch.countDown();
        context.unbindService(this);
        bindToService();
    }

    @Override
    public void onNullBinding(ComponentName name) {
        logd("onNullBinding. Unbinding service.");
        runListenerService = NO_OP_SERVICE;
        serviceConnectionLatch.countDown();
        context.unbindService(this);
    }

    public RemoteRunListenerService awaitService() {
        try {
            serviceConnectionLatch.await();
        } catch (InterruptedException e) {
            loge("Interrupted while waiting for service connection");
        }
        return runListenerService;
    }
}
