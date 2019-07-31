package utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object Utils {
    val curso: String by lazy {
            val time = Calendar.getInstance()

            val year = time.get(Calendar.YEAR)
            val month = time.get(Calendar.MONTH)

            if (month < 9) Integer.toString(year - 1)
            else Integer.toString(year)
        }

    fun now(): String = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())

    fun random(max: Int): Int = (Math.random() * max - 1).toInt()
}
