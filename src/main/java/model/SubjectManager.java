package model;

import javafx.util.Pair;
import model.json.ObjectParsers;
import utils.Settings;
import utils.Tree;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SubjectManager {

    private final SubjectInfo subjectInfo;
    private Tree<PoliformatFile> fileSystem;

    SubjectManager(SubjectInfo subjectInfo) {
        this.subjectInfo = subjectInfo;
    }

    public void updateLastUpdate(String lastUpdate) {
        subjectInfo.setLastUpdate(lastUpdate);
        Settings.updateSubject(subjectInfo);
    }

    /**
     * Método lanzadera que sincroniza los archivos de Poliformat.
     *
     */
    public void sync() throws IOException {
        String entityFilesJson = Utils.getJson("content/site/" + subjectInfo.getId() + ".json");
        PoliformatContentEntity entity = ObjectParsers.POLIFORMAT_ENTITY_FILES_ADAPTER.fromJson(entityFilesJson);
        fileSystem = entity.toFileTree();

        if (subjectInfo.getLastUpdate().isEmpty()) {
            downloadSubject();
        } else {
            syncSubject();
        }

        saveChanges();
    }

    /**
     * Descarga la asignatura por primera vez.
     *
     */
    private void downloadSubject() {
        System.out.printf("Download started: %s\n", subjectInfo.getName());
        downloadTree(fileSystem, Settings.poliformatDirectory());
        System.out.printf("Download finished: %s\n", subjectInfo.getName());
    }

    private void downloadTree(Tree<PoliformatFile> node, String parentPath) {
        PoliformatFile data = node.getData();
        Path path = Paths.get(parentPath, data.getTitle());

        if (data.isFolder()) {
            File directory = new File(path.toString());
            directory.mkdir();
            System.out.printf("Creating the folder: %s\n", path.toString());
        } else {
            try {
                Utils.downloadFile(data.getUrl(), path.toString());
                System.out.printf("Downloading the file: %s\n", path.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Tree<PoliformatFile> child : node.getChildren()) {
            downloadTree(child, path.toString());
        }
    }

    /**
     * Compara los archivos locales con el remoto y descarga la diferencia.
     *
     */
    private void syncSubject() throws IOException {
        PoliformatContentEntity response = ObjectParsers.POLIFORMAT_ENTITY_FILES_ADAPTER.fromJson(Settings.loadLocal(subjectInfo.getId()));
        Tree<PoliformatFile> localTree = response.toFileTree();
        List<Pair<PoliformatFile, String>> files = localTree.merge(fileSystem);
        downloadMergeFiles(files);
    }

    private void downloadMergeFiles(List<Pair<PoliformatFile, String>> pendingFiles) {
        String parentPath = Settings.poliformatDirectory();
        for (Pair<PoliformatFile, String> file : pendingFiles) {
            Path path = Paths.get(parentPath, file.getValue());
            if (file.getKey().isFolder()) {
                File directory = new File(path.toString());
                directory.mkdir();
                System.out.printf("Sync: Creating the folder: %s\n", path.toString());
            } else {
                try {
                    Utils.downloadFile(file.getKey().getUrl(), path.toString());
                    System.out.printf("Sync: Downloading the file: %s\n", path.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * Guarda en local el archivo 'json' que ya ha sido sincronizado.
     *
     */
    private void saveChanges() throws IOException {
        Settings.saveRemote(subjectInfo.getId());
    }
}
