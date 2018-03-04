package utils

import javax.net.ssl.HttpsURLConnection
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.ThreadLocalRandom

object Utils {

    val isWindowsHost by lazy { System.getProperty("os.name").contains("win", true) }

    // Esto se usa al recoger los nombres reales de las asginaturas. NO BORRAR HASTA IMPLEMENTARLO.
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
}
