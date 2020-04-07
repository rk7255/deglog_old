package jp.ryuk.deglog_a01.adddiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jp.ryuk.deglog_a01.database.DiaryDatabaseDao
import jp.ryuk.deglog_a01.database.ProfileDatabaseDao

class AddDiaryViewModelFactory (
    private val dataSourceDiary: DiaryDatabaseDao,
    private val dataSourceProfile: ProfileDatabaseDao
    ) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddDiaryViewModel::class.java)) {
            return AddDiaryViewModel(dataSourceDiary, dataSourceProfile) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}