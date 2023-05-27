package com.example.lawjoin.counselreservation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lawjoin.data.model.CounselReservation
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.databinding.ActivityCounselReservationBinding
import com.example.lawjoin.common.ViewModelFactory
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import com.wdullaer.materialdatetimepicker.time.Timepoint
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.TimeZone


inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}

@RequiresApi(Build.VERSION_CODES.O)
class CounselReservationActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {
    private lateinit var binding : ActivityCounselReservationBinding
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var counselReservationViewModel: CounselReservationViewModel
    private val calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("UTC")))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCounselReservationBinding.inflate(layoutInflater)

        counselReservationViewModel = ViewModelProvider(this, ViewModelFactory())[CounselReservationViewModel::class.java]

        val intent = intent
        val lawyer: Lawyer = intent.serializable("lawyer") ?: Lawyer()

        binding.tvReservationLawyerName.text = lawyer.name
        binding.tvReservationLawyerCategory.text = lawyer.categories.joinToString(",")
        binding.tvReservationLawyerOffice.text = lawyer.office.location

        binding.tvReservationDate.text =
            "${calendar.get(Calendar.YEAR)}.${calendar.get(Calendar.MONTH) + 1}.${calendar.get(Calendar.DAY_OF_MONTH)}"

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog.newInstance(
            this@CounselReservationActivity,
            year,
            month,
            day
        )

        timePickerDialog = TimePickerDialog.newInstance(
            this@CounselReservationActivity, 10, 30, false

        )

        binding.btnDateMore.setOnClickListener {
            showDatePicker()
        }

        binding.btnStartTimeMore.setOnClickListener {
            showTimePicker(lawyer)
        }

        /**
         * TODO: 현재 사용자 정보 가져오기, 예약 끝나고 내 정보로 이동해주기
         */
        binding.btnConfirmReservation.setOnClickListener {
            counselReservationViewModel.updateUnavailableTimeOfLawyer(lawyer)
            val counselReservation = CounselReservation(
                datePickerToZonedDateTime(),
                1,
                lawyer.id,
                binding.edtReservationDetail.text.toString()
            )
            counselReservationViewModel.save(counselReservation)
        }

        setContentView(binding.root)
    }

    private fun showTimePicker(lawyer: Lawyer) {
        timePickerDialog.setTimeInterval(1, 30)
        val unavailableTime = mutableListOf<Timepoint>()
        lawyer.unavailableTime.filter {
            isSelectedDay(it)
        } .mapTo(unavailableTime) {
            Timepoint(it.hour, it.minute, it.second)
        }

        timePickerDialog.setDisabledTimes(unavailableTime.toTypedArray())

        timePickerDialog.show(supportFragmentManager, "Datepickerdialog")
    }

    private fun isSelectedDay(zonedDateTime: ZonedDateTime): Boolean {
        if (datePickerDialog.selectedDay == null) {
            Toast.makeText(this, "날짜가 선택되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }

        return datePickerDialog.selectedDay.year == zonedDateTime.year
                && datePickerDialog.selectedDay.month + 1 == zonedDateTime.monthValue
                && datePickerDialog.selectedDay.day == zonedDateTime.dayOfMonth
    }

    private fun showDatePicker() {
        datePickerDialog.minDate = calendar
        datePickerDialog.show(supportFragmentManager, "DateTimePicker")
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        binding.tvReservationTime.text = "$hourOfDay:$minute"
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        binding.tvReservationDate.text = "$year.${monthOfYear+1}.$dayOfMonth"
    }

    private fun datePickerToZonedDateTime(): ZonedDateTime {
        val selectedDay = datePickerDialog.selectedDay
        val selectedTime = timePickerDialog.selectedTime
        return ZonedDateTime.of(LocalDateTime.of(LocalDate.of(selectedDay.year, selectedDay.month, selectedDay.day),
            LocalTime.of(selectedTime.hour, selectedTime.minute)), ZoneId.of("Asia/Seoul"))
            .withZoneSameInstant(ZoneId.of("UTC"))
    }
}