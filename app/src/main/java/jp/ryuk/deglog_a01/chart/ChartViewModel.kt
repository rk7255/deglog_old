package jp.ryuk.deglog_a01.chart

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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
    private var valuesForChart = listOf<Int>()
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

    fun changeItemSelected(position: Int) {
        uiScope.launch {
            selectedPosition = position
            selectedName = filterNames.value!![selectedPosition].toString()
            diaries.value = getDiary(selectedName)

            createChart()
        }
    }

    private fun createChart() {
        val dataFilter = diaries.value!!.filter {
            it.name == selectedName
        }

        val dataList = mutableListOf<Int>()
            dataFilter.forEach {  diary ->
                diary.weight?.let { dataList.add(it) }
        }

        valuesForChart = dataList
        dataIsEmpty = valuesForChart.isEmpty()

        createLineChart(chart, valuesForChart.reversed())
    }

    /**
     * chart
     */
    private fun createLineChart(chart: LineChart, data: List<Int>) {
        chart.description = (
                if (dataIsEmpty)  {
                    "DATA IS EMPTY"
                } else  {
                    "DATA DESCRIPTION"
                }
                ).toDescription()

        chart.description.textSize = if (dataIsEmpty) 32f else 16f

        val legend = chart.legend
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

        val values = arrayListOf<Entry>()
        data.forEachIndexed { index, value ->
            val entry = Entry(index.toFloat(), value.toFloat())
            values.add(entry)
        }

        val valuesSet = LineDataSet(values, "values 1st")
        valuesSet.axisDependency = YAxis.AxisDependency.LEFT

        val valuesSets = arrayListOf<ILineDataSet>()
        valuesSets.add(valuesSet)

        chart.data = LineData(valuesSets)
        chart.invalidate()
    }

    private fun String.toDescription(): Description {
        val description = Description()
        description.text = this
        return description
    }

    /**
     * Database
     */
    private suspend fun getDiary(name: String): List<Diary> {
        return withContext(Dispatchers.IO) {
            diaryDatabase.getDiaryAtName(name)
        }
    }

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
                names
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}