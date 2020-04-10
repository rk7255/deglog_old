package jp.ryuk.deglog_a01.chart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import jp.ryuk.deglog_a01.R
import jp.ryuk.deglog_a01.database.DiaryDatabase
import jp.ryuk.deglog_a01.diary.DiaryViewModel
import jp.ryuk.deglog_a01.diary.DiaryViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class ChartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        val diaryDatabase = DiaryDatabase.getInstance(application).diaryDatabaseDao
        val viewModelFactory = ChartViewModelFactory(diaryDatabase, application)
        val chartViewModel =
            ViewModelProvider(this, viewModelFactory).get(ChartViewModel::class.java)


        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

}
