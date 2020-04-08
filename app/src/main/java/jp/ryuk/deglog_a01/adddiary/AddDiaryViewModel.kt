package jp.ryuk.deglog_a01.adddiary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.ryuk.deglog_a01.database.*
import jp.ryuk.deglog_a01.utilities.convertIntToString
import jp.ryuk.deglog_a01.utilities.convertStringToInt
import kotlinx.coroutines.*

class AddDiaryViewModel (
    private val diaryDatabase: DiaryDatabaseDao,
    private val profileDatabase: ProfileDatabaseDao
) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var enable = MutableLiveData(false)

    // 名前リストと選択した名前
    val names = MutableLiveData<List<String?>>()
    var selectedName: String = ""

    var weight: String? = null
    var length: String? = null
    var memo: String = ""

    /*
     * 初期化
     */
    init {
        initialize()
    }

    private fun initialize() {
        uiScope.launch {
            getNames()
            names.value = getNames()
            selectedName = names.value!![0].toString()
        }
    }

    private suspend fun getNames(): List<String> {
        // 登録済みの名前のリストを取得する
        return withContext(Dispatchers.IO) {
            var names = profileDatabase.getNamesInProfile()
            if (names.isNullOrEmpty()) {
                names = listOf("no profile")
            }
            names
        }
    }

    /*
     * 名前の選択を変更したらEditTextにデータを入力
     */
    fun changeSelectedName() {
        uiScope.launch {
            val diary = getDiaryLatestAtName(selectedName)
            weight = convertIntToString(diary?.weight)
            length = convertIntToString(diary?.length)

            _changeDataEvent.value = true
        }
    }
    private suspend fun getDiaryLatestAtName(name: String): Diary? {
        // 名前ごとに最新のデータを取得
        return withContext(Dispatchers.IO) {
            val diary = diaryDatabase.getLatestDiaryAtName(name)
            diary
        }
    }

    /*
     * ボタンのクリックイベント
     */
    fun addNewDiary() {
        uiScope.launch {
            val newDiary = Diary()
            newDiary.name = selectedName
            newDiary.weight = convertStringToInt(weight)
            newDiary.length = convertStringToInt(length)
            newDiary.memo = memo
            Log.d("TEST", "insert: $newDiary")
            insert(newDiary)
            _navigateToDiary.value = true
        }
    }
    // TODO すっきりさせたい
    fun plusWeight() {
        weight = plus(weight)
        _changeDataEvent.value = true
    }
    fun minusWeight() {
        weight = minus(weight)
        _changeDataEvent.value = true
    }
    fun plusLength() {
        length = plus(length)
        _changeDataEvent.value = true
    }
    fun minusLength() {
        length = minus(length)
        _changeDataEvent.value = true
    }

    fun plus(num: String?): String {
        return if (num.isNullOrEmpty()) "0" else (num.toInt() + 1).toString()
    }
    fun minus(num: String?): String {
        return if (num.isNullOrEmpty()) "0" else (num.toInt() - 1).toString()
    }

    fun clearWeight() {
        weight = null
        _changeDataEvent.value = true
    }
    fun clearLength() {
        length = null
        _changeDataEvent.value = true
    }

    /*
     *  LiveData関連
     */
    private var _navigateToDiary = MutableLiveData<Boolean>()
    val navigateToDiary: LiveData<Boolean>
        get() = _navigateToDiary
    fun doneNavigateToDiary() {
        _navigateToDiary.value = false
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

    // TODO 変更をEditTextに反映される用。無くす方法考える
    private var _changeDataEvent = MutableLiveData<Boolean>()
    val changeDataEvent: LiveData<Boolean>
        get() = _changeDataEvent
    fun doneChangeDataEvent() {
        _changeDataEvent.value = false
    }

    /*
     * データベース操作
     */
    private suspend fun insert(diary: Diary) {
        withContext(Dispatchers.IO) {
            diaryDatabase.insert(diary)
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
