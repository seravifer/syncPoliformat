package utils;

import javafx.util.Pair;

import java.io.*;

public final class CredentialsManager {

    // TODO: Encriptar las credenciales
    private CredentialsManager() {}

    public static File credentialsFile() {
        return new File(Settings.appDirectory(), "credentials");
    }

    public static Pair<String, String> getCredentials() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(credentialsFile()));

        Object[] credentials = br.lines().toArray();
        String token = credentials[0].toString();
        String dns = credentials[1].toString();

        return new Pair<>(token, dns);
    }

    public static void saveCredentials(String token, String dns) {
        try {
            credentialsFile().createNewFile();

            PrintWriter printer = new PrintWriter(credentialsFile(), "UTF-8");
            printer.println(token);
            printer.println(dns);
            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCredentials() {
        credentialsFile().delete();
    }

}
