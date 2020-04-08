package jp.ryuk.deglog_a01.addprofile

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import jp.ryuk.deglog_a01.database.Profile
import jp.ryuk.deglog_a01.database.ProfileDatabaseDao
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
    var editTextBirthday: Long = System.currentTimeMillis()


    /*
     * onClick
     */
    fun onClear() {
        uiScope.launch {
            clear()
        }
    }

    fun addNewProfile() {
        uiScope.launch {
            if (editTextName.isEmpty()) {
                Toast.makeText(context, "名前は入力必須項目です", Toast.LENGTH_LONG).show()
            } else {
                val newProfile = Profile()
                newProfile.name = editTextName
                newProfile.type = editTextType
                newProfile.sex = editTextSex
                newProfile.birthday = editTextBirthday
                insertOrUpdate(newProfile)
                _navigateToAddDiary.value = true
            }
        }
    }

    /*
     * DatePicker
     */
    private val calender = Calendar.getInstance()
    var year = calender.get(Calendar.YEAR)
    var month = calender.get(Calendar.MONTH)
    var day = calender.get(Calendar.DAY_OF_MONTH)

    private var _showDatePickerDialog = MutableLiveData<Boolean>()
    val showDatePickerDialog: LiveData<Boolean>
        get() = _showDatePickerDialog

    fun onShowDatePickerDialog() {
        _showDatePickerDialog.value = true
    }

    fun doneShowDatePickerDialog() {
        _showDatePickerDialog.value = false
    }

    /*
     * LiveData
     */
    private var _navigateToAddDiary = MutableLiveData<Boolean?>()
    val navigateToAddDiary: LiveData<Boolean?>
        get() = _navigateToAddDiary

    fun doneNavigate() {
        _navigateToAddDiary.value = false
    }

    /*
     * Database
     */
    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clearAll()
        }
    }

    private suspend fun insertOrUpdate(newProfile: Profile){
        withContext(Dispatchers.IO) {
            val profile = database.getProfile(newProfile.name)

            if (profile == null) {
                Log.d("DEBUG_DATABASE", "Profile Insert -> $newProfile")
                database.insert(newProfile)
            } else {
                Log.d("DEBUG_DATABASE", "Profile Update -> $newProfile")
                database.update(newProfile)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}