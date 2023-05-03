package Task3;

import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private static final String DIRECTORY_PATH = "D:\\DDD\\IntelliJ IDEA\\TPO 4\\Lab_4\\src\\src\\longTexts";

    public static void main(String[] args) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        DirGeneralWordsTask task = new DirGeneralWordsTask(DIRECTORY_PATH);

        Set<String> words = pool.invoke(task);
        pool.shutdown();

        System.out.println("Common words for all these files are: " + words.toString());
    }
}
