package jp.ryuk.deglog_a01.addanimal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jp.ryuk.deglog_a01.database.AnimalDatabaseDao
import jp.ryuk.deglog_a01.database.ProfileDatabaseDao

class AddAnimalViewModelFactory(
    private val dataSourceAnimal: AnimalDatabaseDao,
    private val dataSourceProfile: ProfileDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddAnimalViewModel::class.java)) {
            return AddAnimalViewModel(dataSourceAnimal, dataSourceProfile) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}