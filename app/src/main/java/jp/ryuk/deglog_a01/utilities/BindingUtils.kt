package jp.ryuk.deglog_a01.utilities

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import jp.ryuk.deglog_a01.database.Animal
import org.w3c.dom.Text

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
    item?.let {
        visibility = if (item == -1) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}


@BindingAdapter("diaryName")
fun TextView.setDiaryName(item: Animal?) {
    item?.let {
        text = item.name
        visibility = View.VISIBLE
    }
}

@BindingAdapter("diaryDateFormatted")
fun TextView.setDiaryDateFormatted(item: Animal?){
    item?.let {
        text = convertLongToDateString(item.date)
        visibility = View.VISIBLE

    }
}

@BindingAdapter("diaryWeightFormatted")
fun TextView.setDiaryWeightFormatted(item: Animal?) {
    item?.let {
        if (item.weight == -1) {
            visibility = View.GONE
        } else {
            text = convertWeight(item.weight)
            visibility = View.VISIBLE

        }
    }
}

@BindingAdapter("diaryLengthFormatted")
fun TextView.setDiaryLengthFormatted(item: Animal?) {
    item?.let {
        if (item.length == -1) {
            visibility = View.GONE
        } else {
            text = convertLength(item.length)
            visibility = View.VISIBLE

        }
    }
}

@BindingAdapter("diaryMemoFormatted")
fun TextView.setDiaryMemoFormatted(item: Animal?) {
    item?.let {
        if (item.memo.isEmpty()) {
            visibility = View.GONE
        } else {
            text = convertMemo(item.memo)
            visibility = View.VISIBLE

        }
    }
}