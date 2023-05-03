package Task4;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Pattern;

public class FileSearchByKeywordsTask extends RecursiveTask<Boolean> {
    public final String filePath;
    private final List<String> wordsList;
    private final int start;
    private final int end;
    private final String[] keyWords;

    FileSearchByKeywordsTask(String filePath, String[] keyWords) {
        this.filePath = filePath;
        this.keyWords = keyWords;

        Scanner scanner = null;
        try {
            scanner = new Scanner(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Something went wrong");
            throw new RuntimeException(e);
        }

        String content = scanner.useDelimiter("\\A").next();
        scanner.close();

        wordsList = List.of(content.split("\\s+"));
        start = 0;
        end = wordsList.size();
    }

    FileSearchByKeywordsTask(String filePath, String[] keyWords, List<String> wordsList, int start, int end) {
        this.filePath = filePath;
        this.wordsList = wordsList;
        this.start = start;
        this.end = end;
        this.keyWords = keyWords;
    }

    @Override
    protected Boolean compute() {
        if(end - start < 500_000) {
            return wordsListContainsSearchWord();
        }

        int middleIndex = (end + start) / 2;

        FileSearchByKeywordsTask leftTask = new FileSearchByKeywordsTask(
            filePath, keyWords, wordsList, start, middleIndex);
        leftTask.fork();

        FileSearchByKeywordsTask rightTask = new FileSearchByKeywordsTask(
            filePath, keyWords, wordsList, middleIndex, end);

        return leftTask.join() || rightTask.compute();
    }

    private boolean wordsListContainsSearchWord() {
        Pattern pattern = Pattern.compile("\\p{Punct}");
        for (String str : wordsList) {
            String[] words = pattern.split(str.toLowerCase());
            for (String word : words) {
                for (var keyWord : keyWords){
                    if (word.equals(keyWord.toLowerCase())) {
                        return true;
                    }
                }

            }
        }

        return false;
    }
}

