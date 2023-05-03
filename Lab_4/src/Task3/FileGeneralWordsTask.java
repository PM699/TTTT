package Task3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class FileGeneralWordsTask extends RecursiveTask<Set<String>> {
    private final ArrayList<String> filePaths;

    FileGeneralWordsTask(ArrayList<String> filePaths) {
        this.filePaths = filePaths;
    }

    @Override
    protected Set<String> compute() {
        ArrayList<Set<String>> setsToIntersect = new ArrayList<>();

        for (String filePath : filePaths) {
            setsToIntersect.add(getSetFromFile(filePath));
        }

        Set<String> intersectionOfSets = new HashSet<>(setsToIntersect.get(0));
        for (int i = 1; i < setsToIntersect.size(); i++) {
            intersectionOfSets.retainAll(setsToIntersect.get(i));
        }

        return intersectionOfSets;
    }

    private Set<String> getSetFromFile(String filePath) {
        try {
            return Files.lines(Paths.get(filePath))
                    .flatMap(line -> List.of(line.split(" ")).stream())
                    .map(word -> word.toLowerCase().replaceAll("[^\\p{L}]", ""))
                    .filter(word -> !word.isEmpty())
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
