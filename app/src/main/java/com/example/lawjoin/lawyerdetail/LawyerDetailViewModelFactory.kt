package com.example.lawjoin.lawyerdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LawyerDetailViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LawyerDetailViewModel::class.java)) {
            LawyerDetailViewModel() as T
        } else {
            throw IllegalArgumentException()
        }
    }
}