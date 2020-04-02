package jp.ryuk.deglog_a01.diary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import jp.ryuk.deglog_a01.database.Animal
import jp.ryuk.deglog_a01.database.AnimalDatabaseDao
import kotlinx.coroutines.*

class DiaryViewModel(
    val database: AnimalDatabaseDao,
    application: Application
    ) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var latestAnimal = MutableLiveData<Animal?>()
    var animals = database.getAnimals()

    private var _navigateToAddAnimal = MutableLiveData<Boolean?>()
    val navigateToAddAnimal: LiveData<Boolean?>
        get() = _navigateToAddAnimal

    fun doneNavigation() {
        _navigateToAddAnimal.value = false
    }


    init {
        initializeAnimal()
    }

    private fun initializeAnimal() {
        uiScope.launch {
            latestAnimal.value = getLatestFromDatabase()
        }
    }

    private suspend fun getLatestFromDatabase(): Animal? {
        return withContext(Dispatchers.IO) {
            var animal = database.getLatest()
            if (animal?.name == "") {
                animal = null
            }
            animal
        }
    }

    fun addAnimal() {
        uiScope.launch {
            _navigateToAddAnimal.value = true
        }
    }

    fun debugAddAnimal() {
        uiScope.launch {

                val newAnimal = Animal()
                newAnimal.name = "test"
                newAnimal.weight = 300
                newAnimal.length = 100
                newAnimal.memo = "data for testing"
                insert(newAnimal)
            }
    }

    fun clearAnimals() {
        uiScope.launch {
            clearAll()
        }
    }

    private suspend fun insert(animal: Animal) {
        withContext(Dispatchers.IO) {
            database.insert(animal)
        }
    }
    private suspend fun update(animal: Animal) {
        withContext(Dispatchers.IO) {
            database.update(animal)
        }
    }
    private suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            database.clearAll()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val _navigateToDiaryDetail = MutableLiveData<Long>()
    val navigateToDiaryDetail: LiveData<Long>
        get() = _navigateToDiaryDetail

    fun onDiaryClicked(id: Long) {
        _navigateToDiaryDetail.value = id
    }

    fun onDiaryDetailNavigated() {
        _navigateToDiaryDetail.value = null
    }

    private val _navigateToAddProfile = MutableLiveData<Boolean>()
    val navigateToAddProfile: LiveData<Boolean>
        get() = _navigateToAddProfile

    fun onAddProfileClicked() {
        _navigateToAddProfile.value = true
    }

    fun onAddProfileNavigated() {
        _navigateToAddProfile.value = false
    }

}