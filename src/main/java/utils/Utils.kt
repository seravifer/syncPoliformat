package utils

import javax.net.ssl.HttpsURLConnection
import java.io.IOException
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.ThreadLocalRandom

object Utils {

    val isWindowsHost by lazy { System.getProperty("os.name").contains("win", true) }

    val curso: String by lazy {
            val time = Calendar.getInstance()

            val year = time.get(Calendar.YEAR)
            val month = time.get(Calendar.MONTH)

            if (month < 9)
                Integer.toString(year - 1)
            else
                Integer.toString(year)
        }

    // TODO: Sustituir por Retrofit
    @Throws(IOException::class)
    fun getJson(url: String): String {
        val link = URL("https://poliformat.upv.es/direct/" + url)
        val conn = link.openConnection() as HttpsURLConnection

        return conn.inputStream.reader().readText()
    }

    fun now(): String = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())

    fun random(min: Int, max: Int): Int = ThreadLocalRandom.current().nextInt(min, max + 1)

    // TODO: Pasar funcionalidad a services
    @Throws(IOException::class)
    fun downloadFile(url: URL, path: Path) {
        val urlPath = Paths.get(url.path)
        val localPath = path.changeExtension(urlPath.toFile().extension)
        url.openStream().use { from ->
            from.copyTo(localPath.toFile().outputStream())
        }
    }

    private fun Path.changeExtension(ext: String): Path {
        return parent.resolve(toFile().nameWithoutExtension + "." + ext)
    }
}
