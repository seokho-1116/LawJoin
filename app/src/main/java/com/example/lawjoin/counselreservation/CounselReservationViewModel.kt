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
    private val counselReservationRepository = CounselReservationRepository()
    private val lawyerRepository = LawyerRepository()

    fun updateUnavailableTimeOfLawyer(lawyer: Lawyer) {
        lawyerRepository.updateUnavailableTime(lawyer)
    }

    fun save(counselReservation: CounselReservation) {
        counselReservationRepository.saveCounselReservation(counselReservation)
    }
}