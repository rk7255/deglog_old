package jp.ryuk.deglog_a01.diary

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jp.ryuk.deglog_a01.database.DiaryDatabaseDao

class DiaryViewModelFactory(
    private val dataSource: DiaryDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
            return DiaryViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}