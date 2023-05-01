package Task3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int weeksAmount = 5;

        Group[] groups = new Group[] {
            new Group("First group", 12, weeksAmount),
            new Group("Second group", 13, weeksAmount),
            new Group("Third group", 14, weeksAmount)
        };
        Journal journal = new Journal(groups);

        Thread[] checkers = new Thread[] {
            new Checker(Arrays.asList("First group", "Second group", "Third group"), weeksAmount, journal, true),
            new Checker(Arrays.asList("First group"), weeksAmount, journal, false),
            new Checker(Arrays.asList("Second group"), weeksAmount, journal, false),
            new Checker(Arrays.asList("Third group"), weeksAmount, journal, false)
        };

        for (var checker : checkers) checker.start();
        for (var checker : checkers) checker.join();

        journal.printMarks();
    }
}
