package jp.ryuk.deglog_a01.utilities

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.EditText
import androidx.core.text.HtmlCompat
import androidx.databinding.InverseMethod
import jp.ryuk.deglog_a01.database.Animal
import jp.ryuk.deglog_a01.database.Profile
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun convertYMDToLong(y: Int, m: Int, d: Int) : Long {
    val calendar = Calendar.getInstance();
    calendar.set(y, m, d, 0, 0, 0)
    return calendar.timeInMillis
//    return SimpleDateFormat("yyyy/MM/dd").format(calendar.time)
}

@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("yyyy/MM/dd EE HH:mm:ss")
        .format(systemTime).toString()
}

@SuppressLint("SimpleDateFormat")
fun convertLongToDateEditTextString(systemTime: Long): String {
    return SimpleDateFormat("yyyy/MM/dd")
        .format(systemTime).toString()
}

/*
 * https://riptutorial.com/ja/android/example/23921/%E6%97%A5%E4%BB%98%E5%BD%A2%E5%BC%8F%E3%82%92%E3%83%9F%E3%83%AA%E7%A7%92%E3%81%AB%E5%A4%89%E6%8F%9B
 */
@SuppressLint("SimpleDateFormat")
fun getMilliFromDate(dateFormat: String?): Long {
    var date = Date()
    val formatter = SimpleDateFormat("yyyy/MM/dd")
    try {
        date = formatter.parse(dateFormat)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    println("Today is $date")
    return date.getTime()
}

fun convertIntToString(num: Int?): String {
    return num?.toString() ?: ""
}

fun convertStringToInt(str: String?): Int? {
    return if (str.isNullOrEmpty()) null else str.toInt()
}

fun convertWeight(weight: Int): String? {
    return if (weight == -1) { null } else { "$weight g" }
}

fun convertLength(length: Int): String? {
    return if (length == -1) { null } else { "$length mm" }
}

fun convertMemo(memo: String): String? {
    return if (memo.isEmpty()) { null } else { memo }
}

fun formatAnimals(animal: Animal?): Spanned {
    val sb = StringBuilder()
    sb.apply {
        animal?.let {
            append("<br>")
            append("\t${convertLongToDateString(it.date)}<br>")
            append("\t${it?.id} - ${it.name}<br>")
            append("\tweight : ${it.weight}<br>")
            append("\tlength : ${it.length}<br>")
            append("\tmemo : ${it.memo}<br>")
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

fun formatProfiles(profiles: List<Profile>?): Spanned {
    val sb = StringBuilder()
    sb.apply {
        profiles?.forEach {
            append("<br>")
            append("\tname     : ${it.name}<br>")
            append("\ttype     : ${it.type}<br>")
            append("\tsex      : ${it.sex}<br>")
            append("\tbirthday : ${convertLongToDateString(it.birthday)}<br>")
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}