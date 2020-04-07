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
import kotlin.math.E

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

    private var _initialized = MutableLiveData<Boolean>()
    val initialized: LiveData<Boolean>
        get() = _initialized

    fun doneInitialized() {
        _initialized.value = false
    }

    var names = MutableLiveData<List<String?>>()

    init {
        initialize()
        doneInitialized()
    }


    private fun initialize() {
        uiScope.launch {
            getNames()
            names.value = getNames()
            selectedName = names.value!![0].toString()
            Log.d("TEST", "nameList: ${names.value}, selectedName: $selectedName")

            val animal = getAnimalLatest(selectedName)
            Log.d("TEST", "animal: $animal")

            editTextWeight = animal?.weight.toString()
            editTextLength = animal?.length.toString()

            _initialized.value = true

        }
    }

    fun weightPlus() {
        editTextWeight = (editTextWeight.toInt() + 1).toString()
        _initialized.value = true

    }

    fun weightMinus() {
        editTextWeight = (editTextWeight.toInt() - 1).toString()
        _initialized.value = true

    }

    fun lengthPlus() {
        editTextLength = (editTextLength.toInt() + 1).toString()
        _initialized.value = true

    }

    fun lengthMinus() {
        editTextLength = (editTextLength.toInt() - 1).toString()
        _initialized.value = true

    }



    fun changeData() {
        uiScope.launch {
            val animal = getAnimalLatest(selectedName)
            editTextWeight = animal?.weight.toString()
            editTextLength = animal?.length.toString()

            _initialized.value = true
        }
    }

    private suspend fun getAnimalLatest(name: String): Animal? {
        return withContext(Dispatchers.IO) {
            val animal = animalDatabase.getAnimalLatest(name)
            animal
        }
    }

    private suspend fun getLatest(): Animal? {
        return withContext(Dispatchers.IO) {
            val latest = animalDatabase.getLatest()
            latest
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