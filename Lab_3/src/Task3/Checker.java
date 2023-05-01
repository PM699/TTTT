package Task3;

import java.util.List;
import java.util.Random;

public class Checker extends Thread {
    private final List<String> groupNames;
    private final Journal journal;
    private final int nWeeks;
    private boolean isLecturer;

    public Checker(List<String> groupNames, int nWeeks, Journal journal, boolean isLecturer) {
        this.groupNames = groupNames;
        this.journal = journal;
        this.nWeeks = nWeeks;
        this.isLecturer = isLecturer;
    }

    @Override
    public void run(){
        Random random = new Random();

        for (int i = 0; i < nWeeks; i++) {
            for (String groupName : groupNames) {
                for (Integer student : journal.groups.get(groupName).marks.keySet()) {
                    int mark = random.nextInt(1, 100);
                    journal.groups.get(groupName).addMark(groupName, student, mark, isLecturer, i);
                }
            }
        }
    }
}
