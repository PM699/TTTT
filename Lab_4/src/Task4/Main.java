package Task4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        String[] keyWords = new String[] { "IT", "AI", "automated"};
        ForkJoinPool pool = ForkJoinPool.commonPool();
        DirSearchByKeywordsTask task = new DirSearchByKeywordsTask("D:\\DDD\\IntelliJ IDEA\\TPO 4\\Lab_4\\src\\src\\longTexts", keyWords);

        ArrayList<String> filePaths = pool.invoke(task);
        pool.shutdown();

        System.out.println("Keywords " + Arrays.toString(keyWords) + " was found in the files: ");
        for (var file : filePaths) System.out.println("— " + file);
    }
}
