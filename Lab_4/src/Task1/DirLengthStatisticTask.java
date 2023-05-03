package Task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirLengthStatisticTask extends RecursiveTask<HashMap<Integer, Integer>> {
    private final List<String> filePaths;
    public DirLengthStatisticTask(String dirPath) {
        try (Stream<Path> walk = Files.walk(Paths.get(dirPath))) {
            filePaths = walk.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Something went wrong");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected HashMap<Integer, Integer> compute() {
        List<FileLengthStatisticTask> tasks = new ArrayList<>();

        for(String filePath : filePaths) {
            FileLengthStatisticTask task = new FileLengthStatisticTask(filePath);
            task.fork();
            tasks.add(task);
        }

        HashMap<Integer, Integer> result = new HashMap<>();

        for(FileLengthStatisticTask task : tasks) {
            task.join().forEach((lengthKey, count) ->
                    result.merge(lengthKey, count, Integer::sum)
            );
        }

        return result;
    }
}
