package utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object Utils {

    // TODO implementar enum con los sistemas operativos
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

    fun now(): String = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())

    fun random(min: Int, max: Int): Int = ThreadLocalRandom.current().nextInt(min, max + 1)

}
