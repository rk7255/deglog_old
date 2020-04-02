package jp.ryuk.deglog_a01.utilities

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import jp.ryuk.deglog_a01.database.Animal
import jp.ryuk.deglog_a01.database.Profile
import java.text.SimpleDateFormat
import java.time.LocalDate

@SuppressLint("SimpleDateFormat")
fun getNowYear() : Int {
    return SimpleDateFormat("yyyy").format(System.currentTimeMillis()).toInt()
}

@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("yyyy/MM/dd EE HH:mm:ss")
        .format(systemTime).toString()
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