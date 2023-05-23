import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceChannel implements Runnable {
    private final ArrayBlockingQueue<Long> taskQueue;
    private final AtomicInteger processedCounter;

    public ServiceChannel(ArrayBlockingQueue<Long> taskQueue, AtomicInteger processedCounter) {
        this.taskQueue = taskQueue;
        this.processedCounter = processedCounter;
    }

    @Override
    public void run() {
        try {
            while (true) {
                var task = taskQueue.take();
                Thread.sleep(task);
                processedCounter.incrementAndGet();
            }
        } catch (InterruptedException ignored) {
        }
    }
}
