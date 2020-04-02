package jp.ryuk.deglog_a01.addanimal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import jp.ryuk.deglog_a01.R
import jp.ryuk.deglog_a01.database.AnimalDatabase
import jp.ryuk.deglog_a01.database.ProfileDatabase
import jp.ryuk.deglog_a01.databinding.FragmentAddAnimalBinding

class AddAnimalFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAddAnimalBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_animal, container, false)

        val application = requireNotNull(this.activity).application
        val dataSourceAnimal = AnimalDatabase.getInstance(application).animalDatabaseDao
        val dataSourceProfile = ProfileDatabase.getInstance(application).profileDatabaseDao
        val viewModelFactory = AddAnimalViewModelFactory(dataSourceAnimal, dataSourceProfile)
        val addAnimalViewModel = ViewModelProvider(this, viewModelFactory).get(AddAnimalViewModel::class.java)
        binding.addAnimalViewModel = addAnimalViewModel
        binding.lifecycleOwner = this

        addAnimalViewModel.navigateToDiary.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                 this.findNavController().navigate(
                     AddAnimalFragmentDirections
                         .actionAddAnimalFragmentToDiaryFragment())
                addAnimalViewModel.doneNavigate()
             }
        })

        return binding.root
    }

}
