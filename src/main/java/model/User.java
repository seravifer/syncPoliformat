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

    private String nameUser;
    private String lastNameUser;
    private String mailUser;
    private Boolean isLogged = false;
    private CookieManager manager;

    public User() {
        manager = new CookieManager();
        CookieHandler.setDefault(manager);
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
            if (remember) saveCredentials();
        } else {
            isLogged = false;
        }
    }

    public void logout() {
        CookieHandler.setDefault(new CookieManager());
        CredentialsManager.deleteCredentials();
    }

    public boolean checkLogin() {
        return CredentialsManager.credentialsFile().exists();
    }

    public void silentLogin() throws IOException {
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

        //printCokies();

        isLogged = true;
        syncUserInfo();
    }

    private void saveCredentials() throws IOException {
        List<HttpCookie> cookies =  manager.getCookieStore().getCookies();
        HttpCookie token = cookies.get(0);
        HttpCookie dns = cookies.get(1);
        CredentialsManager.saveCredentials(token.getValue(), dns.getValue());
    }

    private void syncUserInfo() throws IOException {
        UserInfo info = ObjectParsers.USER_INFO_ADAPTER.fromJson(Utils.getJson("user/current.json"));
        nameUser = info.getFirstName();
        lastNameUser = info.getLastName();
        mailUser = info.getEmail();
    }

    private void printCokies() {
        CookieStore cookieJar =  manager.getCookieStore();
        List<HttpCookie> cookies = cookieJar.getCookies();
        for (HttpCookie cookie: cookies) {
            System.out.println(cookie.getValue() + " - " +
                    cookie.getDomain() + " - " +
                    cookie.getName() + " - " +
                    cookie.getPath() + " - " +
                    cookie.getSecure() + " - " +
                    cookie.getMaxAge() + " - " +
                    cookie.getVersion());
        }
    }

}
