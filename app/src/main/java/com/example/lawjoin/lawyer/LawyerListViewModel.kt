package com.example.lawjoin.lawyer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.data.repository.LawyerRepository

@RequiresApi(Build.VERSION_CODES.O)
class LawyerListViewModel() : ViewModel() {
    private val lawyerRepository = LawyerRepository.getInstance()
    private val _lawyers = MutableLiveData<List<Lawyer>>()
    val lawyers: LiveData<List<Lawyer>> = _lawyers

    fun getAllLawyers() {
        lawyerRepository.findAllLawyers { lawyers ->
            _lawyers.value = lawyers
        }
    }
}