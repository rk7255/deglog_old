package jp.ryuk.deglog_a01.diarydetail

import android.util.Log
import androidx.lifecycle.*
import jp.ryuk.deglog_a01.database.Diary
import jp.ryuk.deglog_a01.database.DiaryDatabaseDao
import kotlinx.coroutines.*

class DiaryDetailViewModel(
    private val diaryKey: Long = 0L,
    private val diaryDatabase: DiaryDatabaseDao
) : ViewModel() {

    private val viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val diary = MediatorLiveData<Diary>()

    init {
        initialize()
    }

    private fun initialize() {
        uiScope.launch {
            diary.value = getDiary(diaryKey)
            Log.d("DEBUG_DATABASE", "Get DiaryID:$diaryKey -> ${diary.value}")
        }
    }

    /*
     * LiveData
     */
    private val _navigateToDiary = MutableLiveData<Boolean>()
    val navigateToDiary: LiveData<Boolean>
        get() = _navigateToDiary

    fun doneNavigateToDiary() {
        _navigateToDiary.value = false
    }

    /*
     * Database
     */
    private suspend fun getDiary(key: Long): Diary {
        return withContext(Dispatchers.IO) {
            diaryDatabase.getDiary(key)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}
