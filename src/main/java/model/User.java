package model;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import utils.Utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;

public class User {

    private String nameUser;
    private String lastNameUser;
    private String mailUser;
    private Boolean isLogged = false;

    public User() {
        CookieHandler.setDefault(new CookieManager());
    }

    public String getNameUser() {
        return nameUser;
    }

    public String getLastNameUser() {
        return lastNameUser;
    }

    public String getMailUser() {
        return mailUser;
    }

    public Boolean isLogged() {
        return isLogged;
    }

    public void login(String username, String password, Boolean remember) throws IOException {
        String param = "&id=c&estilo=500&vista=MSE&cua=sakai&dni=" + username + "&clau=" + password + "&=Entrar";

        URL link = new URL("https://www.upv.es/exp/aute_intranet");

        HttpsURLConnection conn = (HttpsURLConnection) link.openConnection();
        conn.setDoOutput(true);

        DataOutputStream post = new DataOutputStream(conn.getOutputStream());
        post.writeBytes(param);
        post.flush();
        post.close();

        if (conn.getHeaderField("X-Sakai-Session") != null) {
            isLogged = true;
            syncUserInfo();
            if (remember) saveCredentials(username, password);
        } else {
            isLogged = false;
        }
    }

    public void logout() {
        CookieHandler.setDefault(new CookieManager());
        // Or https://intranet.upv.es/bin2/intranet/expira_intranet/alumno?c
    }

    // TODO recordar las credenciales
    private void saveCredentials(String username, String password) {}

    private void syncUserInfo() throws IOException {
        JsonObject user = Json.parse(Utils.getJson("user/current.json")).asObject();
        nameUser = user.get("firstName").asString();
        lastNameUser = user.get("lastName").asString();
        mailUser = user.get("email").asString();
    }
}
