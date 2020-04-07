package jp.ryuk.deglog_a01.diarydetail

import androidx.lifecycle.*
import jp.ryuk.deglog_a01.database.Diary
import jp.ryuk.deglog_a01.database.DiaryDatabaseDao
import jp.ryuk.deglog_a01.utilities.formatAnimals
import kotlinx.coroutines.Job

class DiaryDetailViewModel(
    private val animalKey: Long = 0L,
    val database: DiaryDatabaseDao
) : ViewModel() {

    private val viewModelJob = Job()

    private val animal = MediatorLiveData<Diary>()

    fun getAnimal() = animal

    val animalString = Transformations.map(animal) {
        formatAnimals(animal.value)
    }

    init {
        // TODO AnimalDatabase削除により動作せず 直す
//        animal.addSource(database.getDiary(animalKey), animal::setValue)
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
