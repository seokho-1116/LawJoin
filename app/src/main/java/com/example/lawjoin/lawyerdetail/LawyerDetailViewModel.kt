package com.example.lawjoin.lawyerdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.data.repository.LawyerRepository

class LawyerDetailViewModel() : ViewModel() {
    private val lawyerRepository = LawyerRepository()
    private val _lawyer = MutableLiveData<Lawyer>()
    val lawyer: LiveData<Lawyer> = _lawyer

    fun getLawyer(lawyerId: Int) {
        _lawyer.value = lawyerRepository.findLawyerById(lawyerId)
    }
}