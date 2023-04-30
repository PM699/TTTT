package Task5;

public class Lock {
    private int num;
    private int checkValue;
    private final int maximumSymbols;
    private boolean isToStop;

    public synchronized boolean isToStop() {
        return isToStop;
    }

    public synchronized int getCheckValue() {
        return checkValue;
    }

    public Lock(int maximumSymbols) {
        this.checkValue = 0;
        this.num = 0;
        this.isToStop = false;
        this.maximumSymbols = maximumSymbols;
    }

    public synchronized void print(char symbol, int controlValue) {
        while (getCheckValue() != controlValue) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
        System.out.print(symbol);
        num++;

        if (++checkValue == maximumSymbols) {
            checkValue = 0;
        }

        if (num % 100 == 0)
            System.out.println();
        if (num == 100 * 100 - (maximumSymbols - 1))
            isToStop = true;

        this.notifyAll();
    }
}
