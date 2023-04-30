package Task5;

public class SyncThread extends Thread {
    private final Lock lock;

    private final int controlValue;

    private final char symbol;

    public SyncThread(Lock lock, char symbol, int controlValue) {
        this.lock = lock;
        this.symbol = symbol;
        this.controlValue = controlValue;
    }

    @Override
    public void run() {
        while (true) {
            lock.print(symbol, controlValue);
            if (lock.isToStop())
                return;
        }
    }
}
