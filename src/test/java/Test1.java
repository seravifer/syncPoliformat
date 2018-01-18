import model.Poliformat;
import model.User;

public class Test1 {
    public static void main(String[] args) throws Exception {
        User user = new User();
        user.login("44898207", "1805", false);
        Poliformat poliformat = new Poliformat();
        poliformat.syncSubjects();
    }
}
