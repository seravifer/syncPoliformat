package model;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import utils.FileType;
import utils.Tree;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Subject {

    private String id;
    private String name;
    private String shortName;
    private String lastUpdate;
    private Tree<FileType> fileSystem;

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

    public Tree<FileType> getFilesystem() {
        return fileSystem;
    }

    /**
     * Método lanzadera que sincroniza los archivos de Poliformat.
     *
     * @throws IOException
     */
    public void sync() throws IOException {
        if (lastUpdate == null || lastUpdate.isEmpty()) {
            parser();
            downloadSubject();
        } else {
            syncSubject();
        }

        saveChanges();
    }

    /**
     * Convierte un archivo json en un arbol de archivos.
     *
     * @throws IOException
     */
    private void parser() throws IOException {
        JsonArray items = Json.parse(Utils.getJson("content/site/" + id + ".json"))
                .asObject().get("content_collection").asArray();

        HashMap<String, Tree<FileType>> aux = new HashMap<>();

        Pattern parentUrlPattern = Pattern.compile("/content.+(?=/.*/$)|/content.+/");
        Pattern urlPattern = Pattern.compile("/content.+");

        for (int i = 0; i < items.size(); i++) {
            JsonValue item = items.get(i);
            JsonObject object = item.asObject();

            String url = object.getString("url", null);
            String type = object.getString("type", null);
            String title = object.getString("title", null);

            Matcher matcherParent = parentUrlPattern.matcher(url);
            String match = null;
            String parentId = null;

            if (matcherParent.find()) {
                match = matcherParent.group();
                parentId = match;

                if (!match.endsWith("/")) {
                    parentId = String.format("%s/",match);
                }
            }

            Matcher matcherUrl = urlPattern.matcher(url);
            String route = null;

            if (matcherUrl.find()) {
                route = matcherUrl.group();
            }

            if (i == 0) {
                FileType root = new FileType(title, type, route, url);
                fileSystem = new Tree<>(root);
                aux.put(fileSystem.getData().getRoute(), fileSystem);
            } else {
                FileType file = new FileType(title, type, route, url);
                Tree<FileType> fileNode = new Tree<>(file);
                Tree<FileType> parentNode = aux.get(parentId);

                if (parentNode != null) {
                    parentNode.addChild(fileNode);
                    aux.put(fileNode.getData().getRoute(), fileNode);
                }
            }
        }
    }

    /**
     * Descarga la asignatura por primera vez.
     */
    private void downloadSubject() {
        System.out.printf("Download started: %s\n", name);
        downloadTree(fileSystem, Utils.poliformatDirectory());
        System.out.printf("Download finished: %s\n", name);
    }

    private void downloadTree(Tree<FileType> node, String parentPath) {
        FileType data = node.getData();
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

        for (Tree<FileType> child : node.getChildren()) {
            downloadTree(child, path.toString());
        }
    }

    /**
     * Compara los archivos locales con el remoto y descarga la diferencia.
     */
    private void syncSubject() throws IOException {
        parser();
        downloadSubject();
    }

    /**
     * Guarda en local el archivo 'json' que ya ha sido sincronizado.
     */
    public void saveChanges() throws IOException {
        Utils.saveRemote(id);
    }
}
