import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceSystem {
    private final String id = UUID.randomUUID().toString();
    private final int channelsNumber;
    private final long taskExecutionTime;
    private final int averageTimeBetweenTasks;
    private final int timeStandardDeviation;
    private final int minimumProcessedNumber;
    private final SystemObserver systemObserver;

    private final ArrayBlockingQueue<Long> tasksQueue;
    private final AtomicInteger processedCounter = new AtomicInteger(0);
    private final AtomicInteger failureCounter = new AtomicInteger(0);
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    public ServiceSystem(
            int queueSize,
            int channelsNumber,
            int taskExecutionTime,
            int averageTimeBetweenTasks,
            int timeStandardDeviation,
            int minimumProcessedNumber,
            SystemObserver systemObserver
    ) {
        this.channelsNumber = channelsNumber;
        this.taskExecutionTime = taskExecutionTime;
        this.averageTimeBetweenTasks = averageTimeBetweenTasks;
        this.timeStandardDeviation = timeStandardDeviation;
        this.minimumProcessedNumber = minimumProcessedNumber;
        tasksQueue = new ArrayBlockingQueue<>(queueSize);
        this.systemObserver = systemObserver;
        systemObserver.setSystem(this);
    }

    public void simulate() {
        var channels = new ArrayList<ServiceChannel>();
        var channelsPool = Executors.newFixedThreadPool(channelsNumber);

        for (int i = 0; i < channelsNumber; i++) {
            var channel = new ServiceChannel(tasksQueue, processedCounter);
            channels.add(channel);
            channelsPool.execute(channel);
        }
        systemObserver.start();

        var random = new Random();
        while (processedCounter.get() < minimumProcessedNumber) {
            try {
                if (!tasksQueue.offer(taskExecutionTime)) {
                    failureCounter.incrementAndGet();
                }
                var waitingTime = (long) (averageTimeBetweenTasks + timeStandardDeviation * random.nextGaussian());
                Thread.sleep(waitingTime > 0 ? waitingTime : 1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        channelsPool.shutdownNow();
        isRunning.set(false);
        try {
            systemObserver.join();
            if (channelsPool.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("System #" + id + " is stopped successfully");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getId() {
        return this.id;
    }

    public AtomicInteger getProcessedCounter() {
        return processedCounter;
    }

    public AtomicInteger getFailureCounter() {
        return failureCounter;
    }

    public AtomicBoolean getIsRunning() {
        return isRunning;
    }

    public int getQueueSize() {
        return tasksQueue.size();
    }
}
