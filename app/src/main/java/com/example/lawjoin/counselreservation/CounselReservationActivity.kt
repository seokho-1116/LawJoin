package com.example.lawjoin.counselreservation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lawjoin.account.AccountManagementActivity
import com.example.lawjoin.common.AuthUtils
import com.example.lawjoin.data.model.CounselReservation
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.databinding.ActivityCounselReservationBinding
import com.example.lawjoin.common.ViewModelFactory
import com.example.lawjoin.data.model.AuthUserDto
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import com.wdullaer.materialdatetimepicker.time.Timepoint
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone

@RequiresApi(Build.VERSION_CODES.O)
class CounselReservationActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {
    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    private lateinit var binding : ActivityCounselReservationBinding
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var datePickerDialog: DatePickerDialog

    private lateinit var counselReservationViewModel: CounselReservationViewModel

    private lateinit var currentUser: AuthUserDto

    private val calendar = Calendar.getInstance(TimeZone.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCounselReservationBinding.inflate(layoutInflater)

        counselReservationViewModel = ViewModelProvider(this, ViewModelFactory())[CounselReservationViewModel::class.java]

        val intent = intent
        val lawyer: Lawyer = intent.serializable("lawyer")!!

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

        AuthUtils.getCurrentUser { authUserDto, _ ->
            currentUser = authUserDto!!
        }

        binding.btnConfirmReservation.setOnClickListener {
            counselReservationViewModel.existCounselReservation(currentUser.uid!!) {isDataExist ->
                if (isDataExist) {
                    Toast.makeText(this, "기존이 예약이 존재합니다. 기존 예약을 취소해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    counselReservationViewModel.updateUnavailableTimeOfLawyer(lawyer.uid, datePickerToZonedDateTime().toString())
                    val counselReservation = CounselReservation(
                        datePickerToZonedDateTime().toString(),
                        currentUser.uid!!,
                        lawyer.uid,
                        binding.edtReservationDetail.text.toString(),
                        currentUser.name!!,
                        lawyer.name
                    )
                    counselReservationViewModel.saveCounselReservation(counselReservation)
                    startActivity(Intent(this, AccountManagementActivity::class.java))
                    finish()
                }
            }
        }

        setContentView(binding.root)
    }

    private fun showTimePicker(lawyer: Lawyer) {
        timePickerDialog.setTimeInterval(1, 30)
        val unavailableTime = mutableListOf<Timepoint>()
        lawyer.unavailableTime.filter {
            val date = ZonedDateTime.parse(it, formatter)
                .withZoneSameInstant(TimeZone.getDefault().toZoneId())
            isSelectedDay(date)
        } .mapTo(unavailableTime) {
            val date = ZonedDateTime.parse(it, formatter)
                .withZoneSameInstant(TimeZone.getDefault().toZoneId())
            Timepoint(date.hour, date.minute, date.second)
        }
        timePickerDialog.setDisabledTimes(unavailableTime.toTypedArray())

        timePickerDialog.setMinTime(
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND) + 1
        )
        timePickerDialog.show(supportFragmentManager, "Datepickerdialog")
    }

    private fun isSelectedDay(zonedDateTime: ZonedDateTime): Boolean {
        if (datePickerDialog.selectedDay == null) {
            Toast.makeText(this, "날짜가 선택되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }

        val date = zonedDateTime.withZoneSameInstant(TimeZone.getDefault().toZoneId())

        return datePickerDialog.selectedDay.year == date.year
                && datePickerDialog.selectedDay.month + 1 == date.monthValue
                && datePickerDialog.selectedDay.day == date.dayOfMonth
    }

    private fun showDatePicker() {
        datePickerDialog.minDate = calendar
        datePickerDialog.show(supportFragmentManager, "DateTimePicker")
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        if (minute == 0) {
            binding.tvReservationTime.text = "$hourOfDay:00"
        } else {
            binding.tvReservationTime.text = "$hourOfDay:$minute"
        }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        binding.tvReservationDate.text = "$year.${monthOfYear+1}.$dayOfMonth"
    }

    private fun datePickerToZonedDateTime(): ZonedDateTime {
        val selectedDay = datePickerDialog.selectedDay
        val selectedTime = timePickerDialog.selectedTime
        return LocalDateTime.of(LocalDate.of(selectedDay.year, selectedDay.month.plus(1), selectedDay.day),
            LocalTime.of(selectedTime.hour, selectedTime.minute, selectedTime.second)).atZone(ZoneId.of("UTC"))
    }

    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }
}