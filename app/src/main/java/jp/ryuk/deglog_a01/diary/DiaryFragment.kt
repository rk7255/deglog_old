package jp.ryuk.deglog_a01.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import jp.ryuk.deglog_a01.R
import jp.ryuk.deglog_a01.adapters.DiaryAdapter
import jp.ryuk.deglog_a01.adapters.DiaryListener
import jp.ryuk.deglog_a01.database.DiaryDatabase
import jp.ryuk.deglog_a01.databinding.FragmentDiaryBinding

class DiaryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDiaryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_diary, container, false)

        val application = requireNotNull(this.activity).application
        val diaryDatabase = DiaryDatabase.getInstance(application).diaryDatabaseDao
        val viewModelFactory = DiaryViewModelFactory(diaryDatabase, application)
        val diaryViewModel = ViewModelProvider(this, viewModelFactory).get(DiaryViewModel::class.java)
        binding.diaryViewModel= diaryViewModel
        binding.lifecycleOwner = this

        /*
         * RecyclerView
         */
        val adapter = DiaryAdapter(DiaryListener { id ->
            diaryViewModel.onClickDiary(id)
        })
        binding.recyclerviewDairy.adapter = adapter

        diaryViewModel.diaries.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        /*
         *  Navigation
         */
        diaryViewModel.navigateToAddDiary.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    DiaryFragmentDirections.actionDiaryFragmentToAddDiaryFragment()
                )
                diaryViewModel.doneNavigateToAddDiary()
            }
        })

        diaryViewModel.navigateToDiaryDetail.observe(viewLifecycleOwner, Observer { diaryKey ->
            diaryKey?.let {
                this.findNavController().navigate(
                    DiaryFragmentDirections.actionDiaryFragmentToDiaryDetailFragment(it)
                )
                diaryViewModel.doneNavigateToDiaryDetail()
            }
         })

        binding.spinnerDiaryFilter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    diaryViewModel.showDiaries(parent?.selectedItem.toString())
                }
            }

        return binding.root
    }

}
