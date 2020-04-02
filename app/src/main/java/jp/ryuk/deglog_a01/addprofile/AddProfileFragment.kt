package jp.ryuk.deglog_a01.addprofile

import android.app.DatePickerDialog
import android.os.Build
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
import com.google.android.material.snackbar.Snackbar

import jp.ryuk.deglog_a01.R
import jp.ryuk.deglog_a01.database.ProfileDatabase
import jp.ryuk.deglog_a01.databinding.FragmentAddProfileBinding
import jp.ryuk.deglog_a01.utilities.convertLongToDateEditTextString
import jp.ryuk.deglog_a01.utilities.convertLongToDateString
import jp.ryuk.deglog_a01.utilities.convertYMDToLong
import java.lang.Exception
import java.util.*

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
        binding.viewModel = addProfileViewModel
        binding.lifecycleOwner = this

        addProfileViewModel.navigateToDiary.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AddProfileFragmentDirections.actionAddProfileFragmentToDiaryFragment()
                )
                addProfileViewModel.doneNavigate()
            }
        })

        addProfileViewModel.showDatePickerDialog.observe(viewLifecycleOwner, Observer {
            if (it == true){

                try {
                    val dpb = DatePickerDialog(this.requireContext(), DatePickerDialog
                        .OnDateSetListener{ view, y, m, d ->
                            addProfileViewModel.editTextBirthday = convertYMDToLong(y, m, d)
                            binding.editTextBirthday.setText(
                                convertLongToDateEditTextString(addProfileViewModel.editTextBirthday))
                        },
                        addProfileViewModel.year,
                        addProfileViewModel.month + 1,
                        addProfileViewModel.day)
                    dpb.show()
                } catch (e: Exception) {
                    Log.d("TEST", "DatePickerDialog: $e")
                }

                addProfileViewModel.doneShowDatePickerDialog()
            }
        })

        return binding.root
    }

}
