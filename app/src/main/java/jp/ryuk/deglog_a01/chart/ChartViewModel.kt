package jp.ryuk.deglog_a01.chart

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.MPPointF
import jp.ryuk.deglog_a01.database.Diary
import jp.ryuk.deglog_a01.database.DiaryDatabaseDao
import kotlinx.coroutines.*

class ChartViewModel(
    private val diaryDatabase: DiaryDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var diaries = MediatorLiveData<List<Diary>>()
    private var dataIsEmpty = true

    lateinit var chart: LineChart

    val filterNames = MutableLiveData<List<String?>>()
    var selectedPosition = 0
    var selectedName = ""

    init {
        initialize()
    }

    private fun initialize() {
        uiScope.launch {
            filterNames.value = getNames()
        }
    }

    /**
     * Spinner Event
     */
    fun changeItemSelected(position: Int) {
        uiScope.launch {
            selectedPosition = position
            selectedName = filterNames.value!![selectedPosition].toString()
            diaries.value = getDiary(selectedName)

            createChart()
        }
    }

    /**
     * chart
     */
    private fun createChart() {
        val dataFiltered = diaries.value!!.filter { it.name == selectedName }.filter { it.weight != null }
        val listOfDate = dataFiltered.mapNotNull(Diary::date)
        val listOfWeight = dataFiltered.mapNotNull(Diary::weight)

        dataIsEmpty = dataFiltered.isEmpty()

        createLineChart(chart, listOfDate.reversed(), listOfWeight.reversed())
    }

    private fun createLineChart(chart: LineChart, date: List<Long>, data: List<Int>) {
        chart.setNoDataText("データがありません")
        chart.description.text = if (dataIsEmpty) "DATA IS EMPTY" else ""
        chart.description.textSize = if (dataIsEmpty) 32f else 16f

        val legend = chart.legend
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

        val values = arrayListOf<Entry>()
        date.forEachIndexed { index, value ->
            val entry = Entry(value.toFloat(), data[index].toFloat())
            values.add(entry)
        }

        val valuesSet = LineDataSet(values, selectedName)
//        複数表示するとき使う
//        val valuesSets = arrayListOf<ILineDataSet>()
//        valuesSets.add(valuesSet)

        // 描画
        chart.data = LineData(valuesSet)
        chart.invalidate()


        // チャートの設定
        valuesSet.apply {
            lineWidth = 4f
            setDrawValues(false)
            circleRadius = 2f
        }

        chart.apply {
            isEnabled = true
            isDoubleTapToZoomEnabled = true
            setTouchEnabled(true)
            setDrawGridBackground(true)
            setDrawBorders(false)
            animateX(200)

            // 縦軸
            xAxis.apply {
                setDrawLabels(false)
                setDrawGridLines(false)
            }
            // 横軸左
            axisLeft.apply {
                enableGridDashedLine(20f, 10f, 0f)
                textSize = 16f
            }
            // 横軸右
            axisRight. isEnabled = false
        }

    }

    /**
     * Database
     */
    private suspend fun getDiary(name: String): List<Diary> {
        return withContext(Dispatchers.IO) {
            diaryDatabase.getDiaryAtName(name)
        }
    }

    private suspend fun getDiaries(): List<Diary> {
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
                names
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}