package Task2;

import java.util.Random;

public class Producer implements Runnable {
    private int elementsAmout;
    private Drop drop;

    public Producer(Drop drop, int elementsAmount) {
        this.drop = drop;
        this.elementsAmout = elementsAmount;
    }

    public void run() {
        int[] elements = createRangeArray(elementsAmout);
        Random random = new Random();

        for (int element : elements) {
            drop.put(element);

            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException ignored) {}
        }

        drop.put(-1);
    }

    public static int[] createRangeArray(int size) {
        int[] range = new int[size];
        for (int i = 0; i < size; i++) {
            range[i] = i + 1;
        }

        return range;
    }
}
