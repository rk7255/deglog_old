package jp.ryuk.deglog_a01.utilities

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import jp.ryuk.deglog_a01.database.Diary

@BindingAdapter("isGone")
fun TextView.isGone(boolean: Boolean) {
    visibility = if (boolean) View.GONE else View.VISIBLE
}

@BindingAdapter("diaryImageGone")
fun ImageView.setDiaryImageGoneString(item: String?) {
    item?.let {
        visibility = if (item.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}

@BindingAdapter("diaryImageGone")
fun ImageView.setDiaryImageGoneInt(item: Int?) {
    visibility = if (item == null) {
        View.GONE
    } else {
        View.VISIBLE
    }

}

@BindingAdapter("diaryName")
fun TextView.setDiaryName(item: Diary?) {
    item?.let {
        text = item.name
        visibility = View.VISIBLE
    }
}

@BindingAdapter("diaryDateFormatted")
fun TextView.setDiaryDateFormatted(item: Diary?){
    item?.let {
        text = convertLongToDateStringInTime(item.date)
        visibility = View.VISIBLE

    }
}

@BindingAdapter("diaryWeightFormatted")
fun TextView.setDiaryWeightFormatted(item: Int?) {
    if (item == null) {
        visibility = View.GONE
    } else {
        text = convertWeight(item)
        visibility = View.VISIBLE

    }
}

@BindingAdapter("diaryLengthFormatted")
fun TextView.setDiaryLengthFormatted(item: Int?) {
    if (item == null) {
        visibility = View.GONE
    } else {
        text = convertLength(item)
        visibility = View.VISIBLE

    }
}

@BindingAdapter("diaryMemoFormatted")
fun TextView.setDiaryMemoFormatted(item: Diary?) {
    item?.let {
        if (item.memo.isNullOrEmpty()) {
            visibility = View.GONE
        } else {
            text = item.memo
            visibility = View.VISIBLE

        }
    }
}