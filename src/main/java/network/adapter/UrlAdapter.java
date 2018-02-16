package network.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlAdapter {
    @ToJson String toJson(URL url) {
        return url.toString();
    }

    @FromJson URL fromJson(String json) {
        try {
            return new URL(json);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
