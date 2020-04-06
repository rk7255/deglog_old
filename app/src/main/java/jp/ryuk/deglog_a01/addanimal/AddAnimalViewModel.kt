package jp.ryuk.deglog_a01.addanimal

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.ryuk.deglog_a01.database.Animal
import jp.ryuk.deglog_a01.database.AnimalDatabaseDao
import jp.ryuk.deglog_a01.database.ProfileDatabaseDao
import kotlinx.coroutines.*

class AddAnimalViewModel(
    val animalDatabase: AnimalDatabaseDao,
    val profileDatabase: ProfileDatabaseDao
) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var selectedName: String = ""
    var editTextWeight: String = ""
    var editTextLength: String = ""
    var editTextMemo: String = ""
    var enable = MutableLiveData(false)

    private var _navigateToDiary = MutableLiveData<Boolean?>()
    val navigateToDiary: LiveData<Boolean?>
        get() = _navigateToDiary

    fun doneNavigate() {
        _navigateToDiary.value = false
    }

    var names = MutableLiveData<List<String?>>()

    init {
        uiScope.launch {
            getNames()

            names.value = getNames()
            Log.d("TEST", "nameList: ${names.value}")
        }

    }

    private suspend fun getNames(): List<String?> {
        return withContext(Dispatchers.IO) {
            var names = profileDatabase.getNamesInProfile()
            if (names.isNullOrEmpty()) {
                names = listOf("no profile")
            }
            names
        }
    }

    private suspend fun insert(animal: Animal) {
        withContext(Dispatchers.IO) {
            animalDatabase.insert(animal)
        }
    }

    fun addNewAnimal() {
        uiScope.launch {
            if (editTextWeight == "") {
                editTextWeight = "-1"
            }
            if (editTextLength == "") {
                editTextLength = "-1"
            }

            val newAnimal = Animal()
            newAnimal.name = selectedName
            newAnimal.weight = editTextWeight.toInt()
            newAnimal.length = editTextLength.toInt()
            newAnimal.memo = editTextMemo
            insert(newAnimal)
            _navigateToDiary.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}