package model;

import model.json.ObjectParsers;
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

    public SubjectManager(SubjectInfo subjectInfo) {
        this.subjectInfo = subjectInfo;
    }

    public void updateLastUpdate(String lastUpdate) {
        subjectInfo.setLastUpdate(lastUpdate);
        Utils.updateSubject(subjectInfo);
    }

    public Tree<PoliformatFile> getFilesystem() {
        return fileSystem;
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
        downloadTree(fileSystem, Utils.poliformatDirectory());
        System.out.printf("Download finished: %s\n", subjectInfo.getName());
    }

    private void downloadTree(Tree<PoliformatFile> node, String parentPath) {
        PoliformatFile data = node.getData();
        Path path = Paths.get(parentPath, data.getTitle());

        if (data.getType().equals("collection")) {
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
        PoliformatContentEntity response = ObjectParsers.POLIFORMAT_ENTITY_FILES_ADAPTER.fromJson(Utils.loadLocal(subjectInfo.getId()));
        Tree<PoliformatFile> localTree = response.toFileTree();
        List<PoliformatFile> files = fileSystem.merge(localTree);
        downloadMergeFiles(files);
    }

    private void downloadMergeFiles(List<PoliformatFile> files) {

    }

    /**
     * Guarda en local el archivo 'json' que ya ha sido sincronizado.
     *
     */
    public void saveChanges() throws IOException {
        Utils.saveRemote(subjectInfo.getId());
    }
}
