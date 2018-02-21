import javafx.util.Pair;
import utils.CredentialsManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;

public class TestLogin {
    public static void main(String[] args) throws IOException {

        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);

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

        URL url = new URL("https://poliformat.upv.es/portal/site/GRA_11565_2017"); // Example
        URLConnection connection = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
    }

    public static void printCokies(CookieManager manager) {
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
