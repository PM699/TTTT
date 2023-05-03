package Task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private static final String DIRECTORY_PATH = "D:\\DDD\\IntelliJ IDEA\\TPO 4\\Lab_4\\src\\src\\longTexts";

    public static void main(String[] args) {
        measureTime(() ->  syncAnalyseDirectory(DIRECTORY_PATH),"Sync");
        measureTime(() ->  parallelAnalyseDirectory(DIRECTORY_PATH),"Parallel");
    }

    public static void syncAnalyseDirectory(String dirPath) {
        ArrayList<Path> filePaths = new ArrayList<>();
        try {
            Files.walk(Paths.get(dirPath))
                    .filter(Files::isRegularFile)
                    .forEach(filePaths::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<Integer, Integer> result = new HashMap<>();

        for (Path filePath : filePaths) {
            try {
                String content = Files.readString(filePath);
                String[] words = content.split("\\s+");

                for (String word : words) {
                    int length = word.length();
                    result.put(length, result.getOrDefault(length, 0) + 1);
                }
            } catch (IOException e) {
                System.out.println("Something went wrong");
                throw new RuntimeException(e);
            }
        }

        printStatistics(result);
    }
    public static void parallelAnalyseDirectory(String dirPath) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        DirLengthStatisticTask task = new DirLengthStatisticTask(dirPath);

        var result = pool.invoke(task);

        pool.shutdown();
        printStatistics(result);
    }

    private static void measureTime(Runnable runnable, String type) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();
        System.out.println("Execution time" + type + " analysis " +  ": " + (endTime - startTime) + " Ms\n");
    }
    public static void printStatistics(HashMap<Integer, Integer> map) {
        int wordsAmount = 0;
        double charsAmount = 0;

        for(int lengthKey : map.keySet()) {
            wordsAmount += map.get(lengthKey);
            charsAmount += map.get(lengthKey) * lengthKey;
        }

        double meadWordsLength = charsAmount / wordsAmount;
        double dispersion = 0;

        for(int lengthKey : map.keySet()) {
            for (int i = 0; i < map.get(lengthKey); i++) {
                dispersion += Math.pow(lengthKey - meadWordsLength, 2);
            }
        }

        dispersion /= wordsAmount;

        System.out.println("Total word count: " + wordsAmount);
        System.out.println("Average word length: " + Math.round(meadWordsLength * 100.0) / 100.0);
        System.out.println("Variance of word length: " + Math.round(dispersion * 100.0) / 100.0);
        System.out.println("Root mean square deviation: " + Math.round(Math.sqrt(dispersion) * 100.0) / 100.0);
    }
}
