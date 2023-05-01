package Task2;

import java.util.Random;

public class Consumer implements Runnable {
    private Drop drop;

    public Consumer(Drop drop) {
        this.drop = drop;
    }

    public void run() {
        Random random = new Random();
        int message = drop.take();

        while(message != -1) {
            try {
                System.out.println("MESSAGE RECEIVED: " + message);
                message = drop.take();

                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException ignored) {}
        }
    }
}
