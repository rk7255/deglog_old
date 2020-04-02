package jp.ryuk.deglog_a01.diarydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jp.ryuk.deglog_a01.database.AnimalDatabaseDao

class DiaryDetailViewModelFactory(
    private val animalKey: Long,
    private val dataSource: AnimalDatabaseDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiaryDetailViewModel::class.java)) {
            return DiaryDetailViewModel(animalKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}