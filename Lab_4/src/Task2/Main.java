package Task2;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args)  {
        var oldTime = measureTime(() -> oldImplementation());
        var newTime = measureTime(() -> newImplementation());

        System.out.println("Execution time of the old implementation: " + oldTime + " Ms");
        System.out.println("Execution time of the new implementation: " + newTime + " Ms");
    }

    public static void oldImplementation(){
        int weeksAmount = 5;

        Group[] groups = new Group[] {
                new Group("First group", 12, weeksAmount),
                new Group("Second group", 13, weeksAmount),
                new Group("Third group", 14, weeksAmount)
        };
        Journal journal = new Journal(groups);

        Thread[] checkers = new Thread[] {
                new CheckerThread(Arrays.asList("First group", "Second group", "Third group"), weeksAmount, journal, true),
                new CheckerThread(Arrays.asList("First group"), weeksAmount, journal, false),
                new CheckerThread(Arrays.asList("Second group"), weeksAmount, journal, false),
                new CheckerThread(Arrays.asList("Third group"), weeksAmount, journal, false)
        };

        for (var checker : checkers) checker.start();
        for (var checker : checkers) {
            try {
                checker.join();
            } catch (InterruptedException e) {}
        }

        journal.printMarks();
    }

    public static void newImplementation()  {
        int weeksAmount = 5;

        Group[] groups = new Group[] {
                new Group("First group", 12, weeksAmount),
                new Group("Second group", 13, weeksAmount),
                new Group("Third group", 14, weeksAmount)
        };
        Journal journal = new Journal(groups);

        List<String> allGroupNames = Arrays.asList("First group", "Second group", "Third group");

        ForkJoinPool pool = new ForkJoinPool();

        CheckerTask mainTask = new CheckerTask(allGroupNames, weeksAmount, journal, true);

        pool.invoke(mainTask);

        journal.printMarks();
    }

    private static long measureTime(Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();

        return endTime - startTime;
    }
}
