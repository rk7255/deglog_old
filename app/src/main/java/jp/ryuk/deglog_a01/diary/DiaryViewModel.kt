package jp.ryuk.deglog_a01.diary

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import jp.ryuk.deglog_a01.database.Diary
import jp.ryuk.deglog_a01.database.DiaryDatabaseDao
import kotlinx.coroutines.*

class DiaryViewModel(
    private val diaryDatabase: DiaryDatabaseDao,
    application: Application
    ) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    var diaries = MediatorLiveData<List<Diary>>()

    init {
        initialize()
    }

    private fun initialize() {
        uiScope.launch {
            diaries.value = getDiaries()
            Log.d("DEBUG_DATABASE", "Get Diaries -> find ${diaries.value?.size} Diaries")
        }
    }

    /*
     * onClick
     */
    fun onClear() {
        uiScope.launch {
            clear()
            diaries.value = getDiaries()
            Log.d("DEBUG_DATABASE", "Clear Diaries -> find ${diaries.value?.size} Diaries")
        }
    }


    /*
     *  LiveData
     */
    private var _navigateToAddDiary = MutableLiveData<Boolean>()
    val navigateToAddDiary: LiveData<Boolean>
        get() = _navigateToAddDiary
    fun onClickAddDiary() {
        _navigateToAddDiary.value = true
    }
    fun doneNavigateToAddDiary() {
        _navigateToAddDiary.value = false
    }

    private val _navigateToDiaryDetail = MutableLiveData<Long>()
    val navigateToDiaryDetail: LiveData<Long>
        get() = _navigateToDiaryDetail
    fun doneNavigateToDiaryDetail() {
        _navigateToDiaryDetail.value = null
    }
    fun onClickDiary(id: Long) {
        _navigateToDiaryDetail.value = id
    }

    /*
     * Database
     */
    private suspend fun  getDiaries(): List<Diary> {
        return withContext(Dispatchers.IO) {
            diaryDatabase.getDiaries()
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            diaryDatabase.clear()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}