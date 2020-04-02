package jp.ryuk.deglog_a01.addprofile

import android.app.Application
import android.app.DatePickerDialog
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import jp.ryuk.deglog_a01.database.Profile
import jp.ryuk.deglog_a01.database.ProfileDatabaseDao
import jp.ryuk.deglog_a01.utilities.formatProfiles
import jp.ryuk.deglog_a01.utilities.getNowYear
import kotlinx.coroutines.*
import java.lang.Exception
import java.text.SimpleDateFormat
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

//    private suspend fun insert(profile: Profile) {
//        withContext(Dispatchers.IO) {
//            database.insert(profile)
//        }
//    }
//
//    private suspend fun update(profile: Profile) {
//        withContext(Dispatchers.IO) {
//            database.update(profile)
//        }
//    }

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
            // TODO DatePickerDialog
//            newProfile.birthday = editTextBirthday.toLong()
            insertOrUpdate(newProfile)
            _navigateToDiary.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    // 仮の日付入力（ダミー）
    val yearBySpinner = arrayListOf<Int>()
    val monthBySpinner = arrayListOf<Int>()
    val dayBySpinner = arrayListOf<Int>()

    init {
        val y = getNowYear()
        for (i in 0..30) {
            yearBySpinner.add(y - i)
        }
        for (i in 1..12) {
            monthBySpinner.add(i)
        }
        for (i in 1..31) {
            dayBySpinner.add(i)
        }
    }
}