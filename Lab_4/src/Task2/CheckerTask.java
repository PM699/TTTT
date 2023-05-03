package Task2;

import java.util.List;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

public class CheckerTask extends RecursiveAction {
    private final List<String> groupNames;
    private final Journal journal;
    private final int nWeeks;
    private boolean isLecturer;

    public CheckerTask(List<String> groupNames, int nWeeks, Journal journal, boolean isLecturer) {
        this.groupNames = groupNames;
        this.journal = journal;
        this.nWeeks = nWeeks;
        this.isLecturer = isLecturer;
    }

    @Override
    protected void compute() {
        if (groupNames.size() == 1) {
            String groupName = groupNames.get(0);
            Random random = new Random();

            for (int i = 0; i < nWeeks; i++) {
                for (Integer student : journal.groups.get(groupName).marks.keySet()) {
                    int mark = random.nextInt(1, 100);
                    journal.groups.get(groupName).addMark(student, mark, isLecturer, i);
                }
            }
        } else {
            List<String> leftGroupNames = groupNames.subList(0, groupNames.size() / 2);
            List<String> rightGroupNames = groupNames.subList(groupNames.size() / 2, groupNames.size());

            CheckerTask leftTask = new CheckerTask(leftGroupNames, nWeeks, journal, isLecturer);
            CheckerTask rightTask = new CheckerTask(rightGroupNames, nWeeks, journal, isLecturer);

            invokeAll(leftTask, rightTask);
        }
    }
}
