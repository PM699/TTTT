package Task2;

public class Drop {
    private final int[] buffer;
    private int currentPutIndex = 0;
    private int currentTakeIndex = 0;
    private int bufferCount = 0;

    public  Drop(int sizeOfBuffer) {
        buffer = new int[sizeOfBuffer];
    }

    public synchronized int take() {
        while (bufferCount == 0) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        int element = buffer[currentTakeIndex];
        currentTakeIndex += 1;

        if(currentTakeIndex == buffer.length) {
            currentTakeIndex = 0;
        }

        bufferCount -= 1;

        notifyAll();

        return element;
    }

    public synchronized void put(int element) {
        while (bufferCount == buffer.length) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        buffer[currentPutIndex] = element;

        currentPutIndex += 1;

        if(currentPutIndex == buffer.length) {
            currentPutIndex = 0;
        }

        bufferCount += 1;

        notifyAll();
    }
}