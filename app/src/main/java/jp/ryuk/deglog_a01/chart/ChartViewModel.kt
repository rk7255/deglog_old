package jp.ryuk.deglog_a01.chart

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import jp.ryuk.deglog_a01.database.Diary
import jp.ryuk.deglog_a01.database.DiaryDatabaseDao
import kotlinx.coroutines.*

class ChartViewModel(
    private val diaryDatabase: DiaryDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    var diaries = MediatorLiveData<List<Diary>>()

    private val filterNames = MutableLiveData<List<String?>>()

    init {
        initialize()
    }

    private fun initialize() {
        uiScope.launch {
            filterNames.value = getNames()
            diaries.value = getDiaries()

            val filtered = diaries.value!!.filter {
                it.name == "ナツ"
            }

            val data = mutableListOf<Int>()
            filtered.forEach { diary ->
                diary.weight?.let { data.add(it) }
            }



            Log.d("DEBUG_", "$filtered ,\n $data")

        }
    }

    /*
     * Database
     */
    private suspend fun  getDiaries(): List<Diary> {
        return withContext(Dispatchers.IO) {
            diaryDatabase.getDiaries()
        }
    }

    private suspend fun getNames(): List<String?> {
        return withContext(Dispatchers.IO) {
            val names = diaryDatabase.getNames()
            if (names.isNullOrEmpty()) {
                listOf("no entry")
            } else {
                listOf("all").plus(names)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}