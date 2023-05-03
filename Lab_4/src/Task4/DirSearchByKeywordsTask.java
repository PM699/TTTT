package Task4;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;


    public class DirSearchByKeywordsTask extends RecursiveTask<ArrayList<String>> {
    private final String dirPath;
    private List<String> filePaths;
    private List<String> subDirs;
    private final String[] keyWords;

    public DirSearchByKeywordsTask(String dirPath, String[] keyWords) {
        this.dirPath = dirPath;
        this.keyWords = keyWords;

        File directory = new File(dirPath);
        File[] filesAndDirs = directory.listFiles();

        filePaths = new ArrayList<>();
        subDirs = new ArrayList<>();

        if (filesAndDirs != null) {
            for (File file : filesAndDirs) {
                if (file.isDirectory()) {
                    subDirs.add(file.getAbsolutePath());
                } else {
                    filePaths.add(file.getAbsolutePath());
                }
            }
        }
    }

    @Override
    protected ArrayList<String> compute() {
        List<DirSearchByKeywordsTask> subDirsTasks = new ArrayList<>();
        List<FileSearchByKeywordsTask> filesTasks = new ArrayList<>();

        for(String dirPath : subDirs) {
            DirSearchByKeywordsTask task = new DirSearchByKeywordsTask(dirPath, keyWords);
            subDirsTasks.add(task);

            task.fork();
        }

        for(String filePath : filePaths) {
            FileSearchByKeywordsTask task = new FileSearchByKeywordsTask(filePath, keyWords);
            filesTasks.add(task);

            task.fork();
        }

        ArrayList<String> results = new ArrayList<>();

        for (FileSearchByKeywordsTask task : filesTasks) {
            if(task.join()) {
                results.add(task.filePath);
            }
        }

        for(DirSearchByKeywordsTask task : subDirsTasks)
            results.addAll(task.join());

        return results;
    }
}
