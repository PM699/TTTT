package Task2;

import java.util.HashMap;
import java.util.stream.Collectors;

public class Journal {
    public HashMap<String, Group> groups;

    public Journal(Group[] groups) {
        this.groups = new HashMap<>();

        for(var group : groups) {
            this.groups.put(group.getGroupName(), group);
        }
    }

    public void printMarks() {
        var sortedGroups = groups.keySet().stream().sorted().collect(Collectors.toList());
        for (String groupName : sortedGroups) {
            System.out.printf("%6s\n", groupName);

            var sortedStudents = groups.get(groupName).marks.keySet().stream().sorted().collect(Collectors.toList());
            for (Integer student : sortedStudents ) {
                System.out.printf("Student №%3s", student);

                for (int mark : groups.get(groupName).marks.get(student)) {
                    System.out.printf("%10s", mark);
                }

                System.out.println();
            }

            System.out.println();
        }
    }
}
