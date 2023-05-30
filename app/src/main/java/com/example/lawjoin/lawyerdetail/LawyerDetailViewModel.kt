package com.example.lawjoin.lawyerdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.data.repository.LawyerRepository

@RequiresApi(Build.VERSION_CODES.O)
class LawyerDetailViewModel() : ViewModel() {
    private val lawyerRepository = LawyerRepository()
    private val _lawyer = MutableLiveData<Lawyer>()
    val lawyer: LiveData<Lawyer> = _lawyer

    fun getLawyer(lawyerId: String) {
        lawyerRepository.findLawyerById(lawyerId) {
            _lawyer.value = it
        }
    }
}