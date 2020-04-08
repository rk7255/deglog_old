package jp.ryuk.deglog_a01.adddiary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import jp.ryuk.deglog_a01.R
import jp.ryuk.deglog_a01.database.DiaryDatabase
import jp.ryuk.deglog_a01.database.ProfileDatabase
import jp.ryuk.deglog_a01.databinding.FragmentAddDiaryBinding
import jp.ryuk.deglog_a01.utilities.convertIntToString


class AddDiaryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAddDiaryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_diary, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSourceDiary = DiaryDatabase.getInstance(application).diaryDatabaseDao
        val dataSourceProfile = ProfileDatabase.getInstance(application).profileDatabaseDao
        val viewModelFactory = AddDiaryViewModelFactory(dataSourceDiary, dataSourceProfile)
        val addDiaryViewModel =
            ViewModelProvider(this, viewModelFactory).get(AddDiaryViewModel::class.java)

        binding.addDiaryViewModel = addDiaryViewModel
        binding.lifecycleOwner = this
        
        // navigation
        addDiaryViewModel.navigateToDiary.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AddDiaryFragmentDirections.actionAddDiaryFragmentToDiaryFragment()
                )
                addDiaryViewModel.doneNavigateToDiary()
            }
        })

        // 値を変更したらEditTextに反映する
        addDiaryViewModel.changeDataEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.editTextAddWeight.setText(addDiaryViewModel.weight)
                binding.editTextAddLength.setText(addDiaryViewModel.length)
                addDiaryViewModel.doneChangeDataEvent()
            }
        })


        addDiaryViewModel.navigateToAddProfile.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AddDiaryFragmentDirections.actionAddDiaryFragmentToAddProfileFragment()
                )
                addDiaryViewModel.doneNavigateToAddProfile()
            }
        })

        binding.spinnerAddName.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    addDiaryViewModel.selectedName = parent?.selectedItem.toString()
                    addDiaryViewModel.enable.value = addDiaryViewModel.selectedName != "no profile"
                    addDiaryViewModel.changeSelectedName()
                }
            }

        return binding.root
    }

}
