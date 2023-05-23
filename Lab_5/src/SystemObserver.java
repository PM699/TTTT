public class SystemObserver extends Thread {
    private final static int CHECKS_PER_OUTPUT = 3;

    private final long checkInterval;
    private ServiceSystem system;

    private int totalChecks = 0;
    private double queueSizesSum = 0;

    public SystemObserver(long checkInterval) {
        this.checkInterval = checkInterval;
    }

    @Override
    public void run() {
        try {
            while (system.getIsRunning().get()) {
                queueSizesSum += system.getQueueSize();
                totalChecks++;
                if (totalChecks % CHECKS_PER_OUTPUT == 0) {
                    this.printSystemParameters();
                }
                Thread.sleep(checkInterval);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSystem(ServiceSystem system) {
        this.system = system;
    }

    public double getAverageQueueSize() {
        return queueSizesSum / totalChecks;
    }

    public double getFailureProbability() {
        double processed = system.getProcessedCounter().get();
        double failed = system.getFailureCounter().get();
        return failed / (processed + failed);
    }

    public void printSystemParameters() {
        var averageQueueSize = this.getAverageQueueSize();
        var failureProbability = this.getFailureProbability();
        System.out.println("System #" + system.getId() +
                " Average queue size: " + averageQueueSize +
                " | Failure probability: " + failureProbability
        );
    }
}
