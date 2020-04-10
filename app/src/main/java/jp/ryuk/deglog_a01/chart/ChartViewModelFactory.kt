package jp.ryuk.deglog_a01.chart

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jp.ryuk.deglog_a01.database.DiaryDatabaseDao

class ChartViewModelFactory(
    private val diaryDatabase: DiaryDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChartViewModel::class.java)) {
            return ChartViewModel(diaryDatabase, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}