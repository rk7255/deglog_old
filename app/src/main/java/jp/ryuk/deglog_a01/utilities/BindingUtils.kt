package jp.ryuk.deglog_a01.utilities

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import jp.ryuk.deglog_a01.database.Diary

@BindingAdapter("isGone")
fun TextView.isGone(boolean: Boolean) {
    visibility = if (boolean) View.GONE else View.VISIBLE
}
// 名前
@BindingAdapter("diaryName")
fun TextView.setDiaryName(item: String?) {
    item?.let {
        text = item
    }
}
// 日付
@BindingAdapter("diaryDateFormatted")
fun TextView.setDiaryDateFormatted(item: Long?){
    item?.let {
        text = convertLongToDateStringInTime(item)
    }
}

// 体重
@BindingAdapter("diaryWeightFormatted")
fun TextView.setDiaryWeightFormatted(weight: Int?) {
    weight?.let {
        text = convertWeight(weight)
    }
}
// 体長
@BindingAdapter("diaryLengthFormatted")
fun TextView.setDiaryLengthFormatted(length: Int?) {
    length?.let {
        text = convertLength(length)
    }
}
// メモ
@BindingAdapter("diaryMemoFormatted")
fun TextView.setDiaryMemoFormatted(memo: String?) {
    memo?.let {
        text = memo
    }
}