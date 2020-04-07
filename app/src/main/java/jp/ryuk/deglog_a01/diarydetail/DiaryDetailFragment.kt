package jp.ryuk.deglog_a01.diarydetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import jp.ryuk.deglog_a01.R
import jp.ryuk.deglog_a01.database.DiaryDatabase
import jp.ryuk.deglog_a01.databinding.FragmentDiaryDetailBinding

/**
 * A simple [Fragment] subclass.
 */
class DiaryDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentDiaryDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_diary_detail, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = DiaryDetailFragmentArgs.fromBundle(arguments!!)

        val dataSource = DiaryDatabase.getInstance(application).diaryDatabaseDao
        val viewModelFactory = DiaryDetailViewModelFactory(arguments.animalKey, dataSource)
        val diaryDetailViewModel = ViewModelProvider(this, viewModelFactory).get(DiaryDetailViewModel::class.java)
        binding.diaryDetailViewModel= diaryDetailViewModel
        binding.lifecycleOwner = this

        diaryDetailViewModel.navigateToDiary.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    DiaryDetailFragmentDirections.actionDiaryDetailFragmentToDiaryFragment()
                )
                diaryDetailViewModel.doneNavigating()
            }
        })

        return binding.root
    }

}
