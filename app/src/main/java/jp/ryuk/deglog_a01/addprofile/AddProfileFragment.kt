package jp.ryuk.deglog_a01.addprofile

import android.app.DatePickerDialog
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
import jp.ryuk.deglog_a01.database.ProfileDatabase
import jp.ryuk.deglog_a01.databinding.FragmentAddProfileBinding
import jp.ryuk.deglog_a01.utilities.convertLongToDateString
import jp.ryuk.deglog_a01.utilities.convertYMDToLong
import java.lang.Exception

class AddProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAddProfileBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_profile, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = ProfileDatabase.getInstance(application).profileDatabaseDao
        val viewModelFactory = AddProfileViewModelFactory(dataSource, application)
        val addProfileViewModel = ViewModelProvider(this, viewModelFactory).get(AddProfileViewModel::class.java)
        binding.addProfileViewModel = addProfileViewModel
        binding.lifecycleOwner = this

        addProfileViewModel.navigateToAddDiary.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AddProfileFragmentDirections.actionAddProfileFragmentToAddDiaryFragment()
                )
                addProfileViewModel.doneNavigate()
            }
        })

        addProfileViewModel.showDatePickerDialog.observe(viewLifecycleOwner, Observer {
            if (it == true){
                showDatePicker(addProfileViewModel, binding)
            }
        })

        return binding.root
    }

    private fun showDatePicker(
        addProfileViewModel: AddProfileViewModel,
        binding: FragmentAddProfileBinding
    ) {
        val dpb = DatePickerDialog(
            this.requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    addProfileViewModel.editTextBirthday = convertYMDToLong(year, month, dayOfMonth)
                    binding.editProfBirthday.setText(
                        convertLongToDateString(addProfileViewModel.editTextBirthday)
                    )
            },
            addProfileViewModel.year,
            addProfileViewModel.month,
            addProfileViewModel.day
        )
        dpb.show()

        addProfileViewModel.doneShowDatePickerDialog()
    }

}
