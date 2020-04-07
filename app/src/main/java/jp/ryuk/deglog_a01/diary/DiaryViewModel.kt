package jp.ryuk.deglog_a01.diary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import jp.ryuk.deglog_a01.database.DiaryDatabaseDao
import kotlinx.coroutines.*

class DiaryViewModel(
    private val diaryDatabase: DiaryDatabaseDao,
    application: Application
    ) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    var diaries = diaryDatabase.getDiaries()

    init {
        diaries = diaryDatabase.getDiaries()
    }

    /*
     *  LiveData関連
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

    private val _navigateToAddProfile = MutableLiveData<Boolean>()
    val navigateToAddProfile: LiveData<Boolean>
        get() = _navigateToAddProfile
    fun doneNavigateToAddProfile() {
        _navigateToAddProfile.value = false
    }
    fun onClickAddProfile() {
        _navigateToAddProfile.value = true
    }


    /*
     * データベース操作
     */
//    private suspend fun insert(diary: Diary) {
//        withContext(Dispatchers.IO) {
//            diaryDatabase.insert(diary)
//        }
//    }
//    private suspend fun update(diary: Diary) {
//        withContext(Dispatchers.IO) {
//            diaryDatabase.update(diary)
//        }
//    }
    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            diaryDatabase.clear()
        }
    }
    fun onClear() {
        uiScope.launch {
            clear()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}