package model;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import utils.File;
import utils.Tree;
import utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Subject {

    private String id;
    private String name;
    private String shortName;
    private Tree<File> fileSystem;

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

    public void setShortName(String shortName) {
        this.shortName = shortName.replaceAll("[^a-zA-Z]", "").substring(0, 3).toUpperCase();
    }

    public Tree<File> getFilesystem() {
        return fileSystem;
    }

    public void parseSubject() throws IOException {
        JsonArray items = Json.parse(Utils.getJson("content/site/" + id + ".json"))
                .asObject().get("content_collection").asArray();

        HashMap<String, Tree<File>> aux = new HashMap<>();

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
                File root = new File(title, type, route, url);
                fileSystem = new Tree<>(root);
                aux.put(fileSystem.getData().getRoute(), fileSystem);
            } else {
                File file = new File(title, type, route, url);
                Tree<File> fileNode = new Tree<>(file);
                Tree<File> parentNode = aux.get(parentId);

                if (parentNode != null) {
                    parentNode.addChild(fileNode);
                    aux.put(fileNode.getData().getRoute(), fileNode);
                }
            }
        }

    }

    /**
     * Usar el metodo Utils.downloadFile();
     */
    public void downloadSubject() {
        fileSystem.print();
    }
}
