package model;

import javafx.util.Pair;
import model.json.ObjectParsers;
import utils.CredentialsManager;
import utils.Utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.List;

public class User {

    private String displayName;
    private String email;
    private Boolean isLogged = false;

    private CookieManager manager;

    public User() {
        manager = new CookieManager();
        CookieHandler.setDefault(manager);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public Boolean isLogged() {
        return isLogged;
    }

    public void login(String username, String password, Boolean remember) throws IOException {
        String param = "&id=c&estilo=500&vista=MSE&cua=sakai&dni=" + username + "&clau=" + password + "&=Entrar";

        URL url = new URL("https://www.upv.es/exp/aute_intranet");

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);

        DataOutputStream post = new DataOutputStream(conn.getOutputStream());
        post.writeBytes(param);
        post.flush();
        post.close();

        if (conn.getHeaderField("X-Sakai-Session") != null) {
            isLogged = true;
            syncUserInfo();
            if (remember) rememberCredentials();
        } else {
            isLogged = false;
        }
    }

    public void logout() {
        manager = new CookieManager();
        CookieHandler.setDefault(manager);
        CredentialsManager.deleteCredentials();
    }

    public boolean checkRememberLogin() {
        return CredentialsManager.credentialsFile().exists();
    }

    public void silenceLogin() throws IOException {
        Pair<String, String> credentials = CredentialsManager.getCredentials();

        HttpCookie cookieToken = new HttpCookie("TDp", credentials.getKey());
        cookieToken.setPath("/");
        cookieToken.setVersion(0);
        cookieToken.setDomain("upv.es");

        HttpCookie cookieDns = new HttpCookie("JSESSIONID", credentials.getValue());
        cookieDns.setPath("/");
        cookieDns.setVersion(0);
        cookieDns.setDomain("poliformat.upv.es");
        cookieDns.setSecure(true);

        CookieStore cookieJar =  manager.getCookieStore();
        cookieJar.add(null, cookieToken);
        cookieJar.add(null, cookieDns);

        new URL("https://poliformat.upv.es/portal/login").openConnection().getInputStream();

        isLogged = true;
        syncUserInfo();
    }

    private void rememberCredentials() {
        List<HttpCookie> cookies =  manager.getCookieStore().getCookies();
        HttpCookie token = cookies.get(0);
        HttpCookie dns = cookies.get(1);
        CredentialsManager.saveCredentials(token.getValue(), dns.getValue());
    }

    private void syncUserInfo() throws IOException {
        UserInfo info = ObjectParsers.USER_INFO_ADAPTER.fromJson(Utils.getJson("user/current.json"));
        displayName = info.getDisplayName();
        email = info.getEmail();
    }

}
