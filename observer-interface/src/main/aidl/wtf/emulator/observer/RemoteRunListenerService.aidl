package wtf.emulator.observer;

import wtf.emulator.observer.DescriptionWrapper;

interface RemoteRunListenerService {

    void testRunStarted(in DescriptionWrapper description);

    void testRunFinished(int runCount, long runTime, boolean wasSuccessful, int failureCount,
        int assumptionFailureCount, int ignoreCount, in List<DescriptionWrapper> failureDescriptions);

    void testSuiteStarted(in DescriptionWrapper description);

    void testSuiteFinished(in DescriptionWrapper description);

    void testStarted(in DescriptionWrapper description);

    void testFinished(in DescriptionWrapper description);

    void testFailure(in DescriptionWrapper description);

    void testAssumptionFailure(in DescriptionWrapper description);

    void testIgnored(in DescriptionWrapper description);

}
