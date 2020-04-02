package jp.ryuk.deglog_a01.addprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import jp.ryuk.deglog_a01.database.Profile
import jp.ryuk.deglog_a01.database.ProfileDatabaseDao
import jp.ryuk.deglog_a01.utilities.formatProfiles
import kotlinx.coroutines.*
import java.util.*

class AddProfileViewModel(
    val database: ProfileDatabaseDao,
    application: Application
): AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val context = getApplication<Application>().applicationContext

    var editTextName: String = ""
    var editTextType: String = ""
    var editTextSex: String = ""
    var editTextBirthday: Long = 0L

    private val profiles = database.getProfiles()

    val profileString = Transformations.map(profiles) {
        formatProfiles(it)
    }


    private var _navigateToDiary = MutableLiveData<Boolean?>()
    val navigateToDiary: LiveData<Boolean?>
        get() = _navigateToDiary

    fun doneNavigate() {
        _navigateToDiary.value = false
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clearAll()
        }
    }

    private suspend fun insertOrUpdate(newProfile: Profile){
        withContext(Dispatchers.IO) {
            val profile = database.getProfile(newProfile.name)

            if (profile == null) {
                database.insert(newProfile)
            } else {
                database.update(newProfile)
            }
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
        }
    }


    fun addNewProfile() {
        uiScope.launch {

            val newProfile = Profile()
            newProfile.name = editTextName
            newProfile.type = editTextType
            newProfile.sex = editTextSex
            newProfile.birthday = editTextBirthday
            insertOrUpdate(newProfile)
            _navigateToDiary.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    val calender = Calendar.getInstance()
    var year = calender.get(Calendar.YEAR)
    var month = calender.get(Calendar.MONTH)
    var day = calender.get(Calendar.DAY_OF_MONTH)

    fun onShowDatePickerDialog() {
        _showDatePickerDialog.value = true

    }

    private var _showDatePickerDialog = MutableLiveData<Boolean>()
    val showDatePickerDialog: LiveData<Boolean>
        get() = _showDatePickerDialog

    fun doneShowDatePickerDialog() {
        _showDatePickerDialog.value = false
    }


}