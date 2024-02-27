package wtf.emulator;

import static wtf.emulator.LogUtil.logd;
import static wtf.emulator.LogUtil.loge;

import android.os.RemoteException;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.ArrayList;
import java.util.List;

import wtf.emulator.observer.DescriptionWrapper;

/**
 * A JUnit {@link org.junit.runner.notification.RunListener} that forwards test run events to a
 * service running on the device.
 */
@SuppressWarnings("unused")
public final class EWRunListener extends RunListener {

    private final RunListenerServiceConnection serviceConnection =
            new RunListenerServiceConnection(InstrumentationRegistry.getInstrumentation().getContext());

    public EWRunListener() {
        try {
            serviceConnection.bindToService();
        } catch (Exception e) {
            loge("Failed to bind to service", e);
        }
    }

    @Override
    public void testRunStarted(Description description) {
        try {
            serviceConnection.awaitService().testRunStarted(toDescriptionWrapper(description));
        } catch (Exception e) {
            loge("Failed to start test run", e);
        }
    }

    @Override
    public void testRunFinished(Result result) {
        try {
            List<DescriptionWrapper> failures = new ArrayList<>(result.getFailures().size());
            for (Failure failure : result.getFailures()) {
                failures.add(toDescriptionWrapper(failure.getDescription()));
            }

            serviceConnection.awaitService().testRunFinished(
                    result.getRunCount(),
                    result.getRunTime(),
                    result.wasSuccessful(),
                    result.getFailureCount(),
                    result.getAssumptionFailureCount(),
                    result.getIgnoreCount(),
                    failures
            );
        } catch (Exception e) {
            loge("Failed to finish test run", e);
        }
    }

    @Override
    public void testSuiteStarted(Description description) {
        try {
            serviceConnection.awaitService().testSuiteStarted(toDescriptionWrapper(description));
        } catch (Exception e) {
            loge("Failed to start test suite", e);
        }
    }

    @Override
    public void testSuiteFinished(Description description) {
        try {
            serviceConnection.awaitService().testSuiteFinished(toDescriptionWrapper(description));
        } catch (Exception e) {
            loge("Failed to finish test suite", e);
        }
    }

    @Override
    public void testStarted(Description description) {
        try {
            long start = System.nanoTime();
            serviceConnection.awaitService().testStarted(toDescriptionWrapper(description));
            long end = System.nanoTime();
            long ms = (end - start) / 1_000_000;
            logd("testStarted took " + ms + "ms");
        } catch (Exception e) {
            loge("Failed to start test", e);
        }
    }

    @Override
    public void testFinished(Description description) {
        try {
            serviceConnection.awaitService().testFinished(toDescriptionWrapper(description));
        } catch (Exception e) {
            loge("Failed to finish test", e);
        }
    }

    @Override
    public void testFailure(Failure failure) {
        try {
            serviceConnection.awaitService().testFailure(toDescriptionWrapper(failure.getDescription()));
        } catch (Exception e) {
            loge("Failed to report test failure", e);
        }
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        try {
            serviceConnection.awaitService().testAssumptionFailure(toDescriptionWrapper(failure.getDescription()));
        } catch (RemoteException e) {
            loge("Failed to notify about assumption failure", e);
        }
    }

    @Override
    public void testIgnored(Description description) {
        try {
            serviceConnection.awaitService().testIgnored(toDescriptionWrapper(description));
        } catch (Exception e) {
            loge("Failed to ignore test", e);
        }
    }

    private DescriptionWrapper toDescriptionWrapper(Description description) {
        return DescriptionWrapper.create(description.getClassName(), description.getMethodName(), description.getDisplayName());
    }
}
