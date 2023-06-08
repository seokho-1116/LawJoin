package com.example.lawjoin.counselreservation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.lawjoin.data.model.CounselReservation
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.data.repository.CounselReservationRepository
import com.example.lawjoin.data.repository.LawyerRepository

@RequiresApi(Build.VERSION_CODES.O)
class CounselReservationViewModel : ViewModel() {
    private val counselReservationRepository = CounselReservationRepository.getInstance()
    private val lawyerRepository = LawyerRepository.getInstance()

    fun updateUnavailableTimeOfLawyer(id: String, time: String) {
        lawyerRepository.updateUnavailableTime(id, time)
    }

    fun saveCounselReservation(counselReservation: CounselReservation) {
        counselReservationRepository.saveCounselReservation(counselReservation)
    }
}