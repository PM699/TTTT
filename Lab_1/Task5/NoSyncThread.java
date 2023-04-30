package Task5;

public class NoSyncThread extends Thread {
    char symbol;

    public NoSyncThread(char symbol) {
        this.symbol = symbol;
    }


    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                System.out.print(symbol);
            }
            System.out.println();
        }
    }
}
