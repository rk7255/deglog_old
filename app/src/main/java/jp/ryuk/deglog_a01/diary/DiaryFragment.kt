package jp.ryuk.deglog_a01.diary

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import jp.ryuk.deglog_a01.R
import jp.ryuk.deglog_a01.adapters.AnimalListener
import jp.ryuk.deglog_a01.adapters.DiaryAdapter
import jp.ryuk.deglog_a01.database.AnimalDatabase
import jp.ryuk.deglog_a01.databinding.FragmentDiaryBinding

class DiaryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDiaryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_diary, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = AnimalDatabase.getInstance(application).animalDatabaseDao
        val viewModelFactory = DiaryViewModelFactory(dataSource, application)
        val diaryViewModel = ViewModelProvider(this, viewModelFactory).get(DiaryViewModel::class.java)
        binding.diaryViewModel= diaryViewModel
        binding.lifecycleOwner = this

        val adapter = DiaryAdapter(AnimalListener {
            id -> diaryViewModel.onDiaryClicked(id)
        })

        binding.recyclerviewDairy.adapter = adapter

        diaryViewModel.animals.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        diaryViewModel.navigateToAddAnimal.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    DiaryFragmentDirections
                        .actionDiaryFragmentToAddAnimalFragment())
                diaryViewModel.doneNavigation()
            }

        })

        diaryViewModel.navigateToDiaryDetail.observe(viewLifecycleOwner, Observer { animal ->
            animal?.let {
                this.findNavController().navigate(
                    DiaryFragmentDirections.actionDiaryFragmentToDiaryDetailFragment(animal)
                )
                diaryViewModel.onDiaryDetailNavigated()
            }
        })

        diaryViewModel.navigateToAddProfile.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    DiaryFragmentDirections.actionDiaryFragmentToAddProfileFragment()
                )
                diaryViewModel.onAddProfileNavigated()
            }
        })

        return binding.root
    }

}
