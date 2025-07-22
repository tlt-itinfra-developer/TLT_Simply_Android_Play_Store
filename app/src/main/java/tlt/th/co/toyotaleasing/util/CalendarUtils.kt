package tlt.th.co.toyotaleasing.util

import android.app.DatePickerDialog
import android.content.Context
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import java.text.SimpleDateFormat
import java.util.*

object CalendarUtils {

    private fun createListener(listener: Listener): DatePickerDialog.OnDateSetListener {
        return DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val myFormat = "dd/MM/yyyy"
            val calendar = Calendar.getInstance()
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)

            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            listener.onResult(simpleDateFormat.format(calendar.time))
        }
    }

    fun create(context: Context, listener: Listener): DatePickerDialog {
        val calendar = Calendar.getInstance()

        return DatePickerDialog(context, createListener(listener),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
    }

    fun getCurrentYear(): String {
        val pattern = "yyyy"
        return getCurrentDateByPattern(pattern)
    }

    fun getCurrentDate(): String {
        val pattern = "dd/MM/yyyy"
        return getCurrentDateByPattern(pattern)
    }

    fun getCurrentDateByPattern(pattern: String): String {
        val localTimeInBangkok = LocalDateTime.now(ZoneId.of("Asia/Bangkok"))
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return localTimeInBangkok.format(formatter)
    }

    interface Listener {
        fun onResult(result: String)
    }
}