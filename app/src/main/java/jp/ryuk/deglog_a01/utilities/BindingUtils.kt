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
fun TextView.setDiaryName(item: Diary?) {
    item?.let {
        text = item.name
    }
}
// 日付
@BindingAdapter("diaryDateFormatted")
fun TextView.setDiaryDateFormatted(item: Diary?){
    item?.let {
        text = convertLongToDateStringInTime(item.date)
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