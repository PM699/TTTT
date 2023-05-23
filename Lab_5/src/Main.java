import java.util.ArrayList;
import java.util.concurrent.Executors;

public class Main {
    // ServiceSystem parameters
    public final static int QUEUE_SIZE = 10;
    public final static int CHANNELS_NUMBER = 4;
    public final static int TASK_EXECUTION_TIME = 50;
    public final static int AVERAGE_TIME_BETWEEN_TASKS = 10;
    public final static int TIME_STANDARD_DEVIATION = 5;
    public final static int MINIMUM_PROCESSED_NUMBER = 1000;

    // Observer parameters
    public final static int CHECK_INTERVAL = 500;

    // Testing parameters
    public final static int PARALLEL_SIMULATIONS_NUMBER = 4;

    public static void main(String[] args) {
//        serialSimulationSingle();
        parallelSimulationMultiple();
    }

    public static void serialSimulationSingle() {
        SystemObserver systemObserver = new SystemObserver(CHECK_INTERVAL);
        var serviceSystem = new ServiceSystem(
                QUEUE_SIZE,
                CHANNELS_NUMBER,
                TASK_EXECUTION_TIME,
                AVERAGE_TIME_BETWEEN_TASKS,
                TIME_STANDARD_DEVIATION,
                MINIMUM_PROCESSED_NUMBER,
                systemObserver
        );
        serviceSystem.simulate();
        System.out.println("SIMULATION RESULTS");
        systemObserver.printSystemParameters();
    }

    public static void parallelSimulationMultiple() {
        ArrayList<SystemSimulationTask> simulationTasks = new ArrayList<>();
        for (int i = 0; i < PARALLEL_SIMULATIONS_NUMBER; i++) {
            var systemObserver = new SystemObserver(CHECK_INTERVAL);
            var serviceSystem = new ServiceSystem(
                    QUEUE_SIZE,
                    CHANNELS_NUMBER,
                    TASK_EXECUTION_TIME,
                    AVERAGE_TIME_BETWEEN_TASKS,
                    TIME_STANDARD_DEVIATION,
                    MINIMUM_PROCESSED_NUMBER,
                    systemObserver
            );
            simulationTasks.add(new SystemSimulationTask(serviceSystem, systemObserver));
        }

        try {
            var threadPool = Executors.newFixedThreadPool(PARALLEL_SIMULATIONS_NUMBER);
            var systemObservers = threadPool.invokeAll(simulationTasks);
            threadPool.shutdown();
            System.out.println("SIMULATIONS RESULTS");
            for (var observer : systemObservers) {
                observer.get().printSystemParameters();
            }
            System.out.println("GLOBAL RESULTS");
            double queueSizesSum = 0;
            double failureProbabilitiesSum = 0;
            for (var observer : systemObservers) {
                queueSizesSum += observer.get().getAverageQueueSize();
                failureProbabilitiesSum += observer.get().getFailureProbability();
            }
            System.out.println("Global Average queue size: " + queueSizesSum / PARALLEL_SIMULATIONS_NUMBER +
                    " | Failure probability: " + failureProbabilitiesSum / PARALLEL_SIMULATIONS_NUMBER);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}