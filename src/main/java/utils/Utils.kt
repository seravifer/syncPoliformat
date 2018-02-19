package utils

import javax.net.ssl.HttpsURLConnection
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.ThreadLocalRandom

object Utils {

    val curso: String
        get() {
            val time = Calendar.getInstance()

            val year = time.get(Calendar.YEAR)
            val month = time.get(Calendar.MONTH)

            return if (month < 9)
                Integer.toString(year - 1)
            else
                Integer.toString(year)
        }

    @Throws(IOException::class)
    fun getJson(url: String): String? {
        val link = URL("https://poliformat.upv.es/direct/" + url)
        val conn = link.openConnection() as HttpsURLConnection

        return inputStreamToString(conn.inputStream)
    }

    @Throws(IOException::class)
    fun readFile(file: File): String {
        val bytes = Files.readAllBytes(file.toPath())
        return String(bytes, charset("UTF-8"))
    }

    private fun inputStreamToString(inputStream: InputStream): String? {
        try {
            ByteArrayOutputStream().use { result ->
                val buffer = ByteArray(1024)
                var length = 0
                while (inputStream.read(buffer).also { length = it } != -1) {
                    result.write(buffer, 0, length)
                }

                return result.toString("UTF-8")
            }
        } catch (e: Exception) {
            return null
        }

    }

    fun now(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val date = Date()
        return dateFormat.format(date)
    }

    fun random(min: Int, max: Int): Int {
        return ThreadLocalRandom.current().nextInt(min, max + 1)
    }

    @Throws(IOException::class)
    fun downloadFile(url: URL, path: String) {
        var path = path
        val name = url.toString().substring(url.toString().lastIndexOf("/"))
        val pos = name.lastIndexOf(".")

        if (pos > 0) {
            val extension = name.substring(pos)
            if (!path.contains(extension)) path += extension
        }

        val to = Paths.get(path)
        url.openStream().use { from -> Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING) }
    }

}
