package jp.ryuk.deglog_a01.diarydetail

import androidx.lifecycle.*
import jp.ryuk.deglog_a01.database.Animal
import jp.ryuk.deglog_a01.database.AnimalDatabaseDao
import jp.ryuk.deglog_a01.utilities.formatAnimals
import kotlinx.coroutines.Job

class DiaryDetailViewModel(
    private val animalKey: Long = 0L,
    dataSource: AnimalDatabaseDao
) : ViewModel() {

    val database = dataSource

    private val viewModelJob = Job()

    private val animal = MediatorLiveData<Animal>()

    fun getAnimal() = animal

    val animalString = Transformations.map(animal) {

        formatAnimals(animal.value)
    }

    init {
        animal.addSource(database.getAnimal(animalKey), animal::setValue)
    }

    private val _navigateToDiary = MutableLiveData<Boolean?>()

    val navigateToDiary: LiveData<Boolean?>
        get() = _navigateToDiary

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun doneNavigating() {
        _navigateToDiary.value = null
    }

    fun onClose() {
        _navigateToDiary.value = true
    }

}
