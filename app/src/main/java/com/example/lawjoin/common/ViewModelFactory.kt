package com.example.lawjoin.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lawjoin.counselreservation.CounselReservationViewModel
import com.example.lawjoin.lawyerdetail.LawyerDetailViewModel

class ViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LawyerDetailViewModel::class.java)) {
            LawyerDetailViewModel() as T
        } else if (modelClass.isAssignableFrom(CounselReservationViewModel::class.java)) {
            CounselReservationViewModel() as T
        } else {
            throw IllegalArgumentException()
        }
    }
}