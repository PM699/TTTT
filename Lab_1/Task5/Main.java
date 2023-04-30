package Task5;

public class Main {
    public static void main(String[] args) {
//        noSyncSymbol();
        syncSymbol();
    }

    public static void noSyncSymbol() {
        NoSyncThread noSyncThread = new NoSyncThread('-');
        NoSyncThread noSyncThread1 = new NoSyncThread('|');
        noSyncThread.start();
        noSyncThread1.start();
    }

    public static void syncSymbol() {
        Lock lock = new Lock(2);
        SyncThread SyncThread = new SyncThread(lock, '-', 0);
        SyncThread syncThread1 = new SyncThread(lock, '|', 1);
        SyncThread.start();
        syncThread1.start();
    }
}
