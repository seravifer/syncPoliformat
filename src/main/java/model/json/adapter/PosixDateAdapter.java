package model.json.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.Date;

public class PosixDateAdapter {

    public @FromJson @PosixDate Date fromJson(Long date) {
        return new Date(date);
    }

    public @ToJson Long toJson(@PosixDate Date date) {
        return date.getTime();
    }

}
