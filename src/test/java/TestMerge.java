import javafx.util.Pair;
import model.PoliformatContentEntity;
import model.PoliformatFile;
import model.json.ObjectParsers;
import utils.Tree;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class TestMerge {

    public static void main(String[] args) throws IOException {
        Tree<PoliformatFile> treeLocal = getTreeFromJson("local.json");
        Tree<PoliformatFile> treeRemote = getTreeFromJson("remote.json");

        List<Pair<PoliformatFile, String>> merge = treeLocal.merge(treeRemote);

        for (Pair<PoliformatFile, String> poliformatFile : merge) {
            System.out.println(poliformatFile.toString());
        }
    }

    private static Tree<PoliformatFile> getTreeFromJson(String id) throws IOException {
        File file = new File(TestMerge.class.getResource(id).getFile());
        FileInputStream fis = new FileInputStream(file);
        PoliformatContentEntity entity = ObjectParsers.POLIFORMAT_ENTITY_FILES_ADAPTER.fromJson("");
        //PoliformatContentEntity entity = ObjectParsers.POLIFORMAT_ENTITY_FILES_ADAPTER.fromJson(PolifromatApi.inputStreamToString(fis));
        return entity.toFileTree();
    }

}
