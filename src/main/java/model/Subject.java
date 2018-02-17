package model;

import model.json.ObjectParsers;
import utils.Tree;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Subject {

    private String id;
    private String name;
    private String shortName;
    private String lastUpdate;
    private Tree<PoliformatFile> fileSystem;

    public Subject(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLastUpdate() {
        if (lastUpdate == null || lastUpdate.isEmpty())
            return "No ha sido sincronizada todavía.";
        else return lastUpdate;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName.replaceAll("[^a-zA-Z]", "").substring(0, 3).toUpperCase();
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void updateLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
        Utils.updateSubject(this);
    }

    public Tree<PoliformatFile> getFilesystem() {
        return fileSystem;
    }

    /**
     * Método lanzadera que sincroniza los archivos de Poliformat.
     *
     * @throws IOException
     */
    public void sync() throws IOException {
        if (lastUpdate == null || lastUpdate.isEmpty()) {
            PoliformatEntity response =  ObjectParsers.ENTITY_PARSER.fromJson(Utils.getJson("content/site/" + id + ".json"));
            fileSystem = response.toFileTree();
            downloadSubject();
        } else {
            syncSubject();
        }

        saveChanges();
    }

    /**
     * Descarga la asignatura por primera vez.
     */
    private void downloadSubject() {
        System.out.printf("Download started: %s\n", name);
        downloadTree(fileSystem, Utils.poliformatDirectory());
        System.out.printf("Download finished: %s\n", name);
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
     */
    private void syncSubject() throws IOException {
        PoliformatEntity response =  ObjectParsers.ENTITY_PARSER.fromJson(Utils.getJson("content/site/" + id + ".json"));
        fileSystem = response.toFileTree();
        downloadSubject();
    }

    /**
     * Guarda en local el archivo 'json' que ya ha sido sincronizado.
     */
    public void saveChanges() throws IOException {
        Utils.saveRemote(id);
    }
}
