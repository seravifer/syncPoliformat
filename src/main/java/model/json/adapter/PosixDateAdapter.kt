package model.json.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

import java.util.Date

class PosixDateAdapter {

    @FromJson
    @PosixDate
    fun fromJson(date: Long): Date = Date(date)


    @ToJson
    fun toJson(@PosixDate date: Date): Long = date.time


}
