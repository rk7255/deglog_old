package jp.ryuk.deglog_a01.chart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import jp.ryuk.deglog_a01.R
import jp.ryuk.deglog_a01.database.DiaryDatabase
import jp.ryuk.deglog_a01.databinding.FragmentChartBinding
import jp.ryuk.deglog_a01.diary.DiaryViewModel
import jp.ryuk.deglog_a01.diary.DiaryViewModelFactory
import kotlinx.android.synthetic.main.fragment_chart.*

/**
 * A simple [Fragment] subclass.
 */
class ChartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentChartBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_chart, container, false
        )

        val application = requireNotNull(this.activity).application
        val diaryDatabase = DiaryDatabase.getInstance(application).diaryDatabaseDao
        val viewModelFactory = ChartViewModelFactory(diaryDatabase, application)
        val chartViewModel =
            ViewModelProvider(this, viewModelFactory).get(ChartViewModel::class.java)
        binding.chartViewModel = chartViewModel
        binding.lifecycleOwner = this

        chartViewModel.chart = binding.chart

        /**
         * Spinner
         */
        binding.spinnerChartFilter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    chartViewModel.changeItemSelected(position)
                }

            }

        return binding.root
    }

}
