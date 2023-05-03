package Task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FileLengthStatisticTask extends RecursiveTask<HashMap<Integer, Integer>> {
    public final String filePath;
    private List<String> wordsList;
    private int start;
    private int end;
    private final boolean splitted;

    public FileLengthStatisticTask(String filePath) {
        this.filePath = filePath;
        splitted = false;
    }

    public FileLengthStatisticTask(String filePath, List<String> wordsList, int start, int end) {
        this.filePath = filePath;
        this.wordsList = wordsList;
        this.start = start;
        this.end = end;
        splitted = true;
    }

    @Override
    protected HashMap<Integer, Integer> compute() {
        if(!splitted) {
            initWordsList();
        }

        if(end - start < 500_000) {
            return getWordsData();
        }

        int middleIdx = (end + start) / 2;

        FileLengthStatisticTask leftTask = new FileLengthStatisticTask(filePath, wordsList, start, middleIdx);
        leftTask.fork();

        FileLengthStatisticTask rightTask = new FileLengthStatisticTask(filePath, wordsList, middleIdx, end);

        var result = rightTask.compute();
        leftTask.join().forEach((lengthKey, count) ->
                result.merge(lengthKey, count, Integer::sum)
        );

        return result;
    }

    private HashMap<Integer, Integer> getWordsData() {
        HashMap<Integer, Integer> lengthsMapper = new HashMap<>();

        wordsList.subList(start, end).forEach(word -> {
            int wordLength = word.length();

            if (lengthsMapper.containsKey(wordLength)) {
                lengthsMapper.put(wordLength, lengthsMapper.get(wordLength) + 1);
            } else {
                lengthsMapper.put(wordLength, 1);
            }
        });

        return lengthsMapper;
    }

    private void initWordsList() {
        try {
            String content = Files.readString(Paths.get(filePath));
            wordsList = List.of(content.split("\\s+"));
            start = 0;
            end = wordsList.size();
        } catch (IOException e) {
            System.out.println("Something went wrong");
            throw new RuntimeException(e);
        }
    }
}
