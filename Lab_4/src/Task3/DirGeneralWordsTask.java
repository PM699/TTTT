package Task3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class DirGeneralWordsTask extends RecursiveTask<Set<String>> {
    private final String dirPath;

    private List<String> filePaths;
    private List<String> subDirsPaths;

    public DirGeneralWordsTask(String dirPath) {
        this.dirPath = dirPath;

        File directory = new File(dirPath);
        File[] filesAndFolders = directory.listFiles();

        filePaths = new ArrayList<>();
        subDirsPaths = new ArrayList<>();

        if (filesAndFolders != null) {
            for (File file : filesAndFolders) {
                if (file.isDirectory()) {
                    subDirsPaths.add(file.getAbsolutePath());
                } else {
                    filePaths.add(file.getAbsolutePath());
                }
            }
        }
    }

    @Override
    protected Set<String> compute() {
        List<RecursiveTask<Set<String>>> tasks = new ArrayList<>();

        for(String dirPath : subDirsPaths) {
            DirGeneralWordsTask task = new DirGeneralWordsTask(dirPath);
            tasks.add(task);
            task.fork();
        }

        ArrayList<String> filesToResolve = new ArrayList<>();
        int c = 0;
        for (var filePath : filePaths) {
            filesToResolve.add(filePath);
            c++;

            if(c >= 5) {
                FileGeneralWordsTask task = new FileGeneralWordsTask(new ArrayList<>(filesToResolve));
                tasks.add(task);
                task.fork();

                c = 0;
                filesToResolve.clear();
            }
        }
        if (!filesToResolve.isEmpty()) {
            FileGeneralWordsTask task = new FileGeneralWordsTask(new ArrayList<>(filesToResolve));
            tasks.add(task);
            task.fork();
        }

        ArrayList<Set<String>> setsToIntersect = new ArrayList<>();
        for (RecursiveTask<Set<String>> task : tasks) {
            setsToIntersect.add(task.join());
        }

        Set<String> intersectionOfSets = new HashSet<>(setsToIntersect.get(0));
        for (int i = 1; i < setsToIntersect.size(); i++) {
            intersectionOfSets.retainAll(setsToIntersect.get(i));
        }

        return intersectionOfSets;
    }
}
