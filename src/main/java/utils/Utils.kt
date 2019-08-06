package utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object Utils {
    val curso: String by lazy {
            val time = Calendar.getInstance()

            val year = time.get(Calendar.YEAR)
            val month = time.get(Calendar.MONTH)

            if (month < 9) (year - 1).toString()
            else year.toString()
        }

    fun now(): String = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())
}
