package com.example.lawjoin.account

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.MainActivity
import com.example.lawjoin.R
import com.example.lawjoin.common.AuthUtils
import com.example.lawjoin.counselreservation.CounselReservationActivity
import com.example.lawjoin.data.model.AuthUserDto
import com.example.lawjoin.data.objects.MenuObjects
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class AccountManagementActivity : AppCompatActivity() {
    private var context : Context = this
    private lateinit var database: DatabaseReference
    private lateinit var currentUser: AuthUserDto
    private lateinit var menuAdapter : MenuAdapter
    private lateinit var reservationDate : TextView
    private lateinit var reservationLawyer : TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_management)

        database = Firebase.database.reference
        reservationDate = findViewById(R.id.reservationDate)
        reservationLawyer = findViewById(R.id.reservationLawyer)
        val menuObjects = ArrayList<MenuObjects>()
        val reservationCancelBtn = findViewById<Button>(R.id.reservationCancelBtn)
        val reservationChangeBtn = findViewById<Button>(R.id.reservationChangeBtn)
        val mainBtn = findViewById<Button>(R.id.mainBtn)
        val rcMenuList = findViewById<RecyclerView> (R.id.menu_rcView)
        menuAdapter = MenuAdapter(menuObjects, this)

        menuObjects.add(MenuObjects("좋아요한 변호사", 1))
        menuObjects.add(MenuObjects("북마크한 법률 단어 보기", 2))
        menuObjects.add(MenuObjects("내가 쓴 글", 3))
        menuObjects.add(MenuObjects("내 정보", 4))
        menuObjects.add(MenuObjects("앱 설정", 5))
        menuObjects.add(MenuObjects("로그아웃", 6))

        val menuLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        rcMenuList.layoutManager = menuLayoutManager
        rcMenuList.adapter =  menuAdapter

        AuthUtils.getCurrentUser { authUserDto, _ ->
            currentUser = authUserDto!!
        }
        val userID = currentUser?.uid
        val userIDString: String = userID.toString()

        mainBtn.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        reservationCancelBtn.setOnClickListener(){
            showSettingPopup(userIDString)
        }
        reservationChangeBtn.setOnClickListener(){
            Toast.makeText(context, "변경할 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CounselReservationActivity::class.java)
            startActivity(intent)
        }

        displayReservationDetails(userIDString)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayReservationDetails(userId: String) {
        val reservationsRef = Firebase.database.getReference("reservations").child("reservation").child(userId)
        reservationsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val startTime = dataSnapshot.child("startTime").getValue(String::class.java)
                    val lawyerId = dataSnapshot.child("lawyerId").getValue(String::class.java)
                    val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
                    val dbDataTime = ZonedDateTime.parse(startTime.toString(), formatter)

                    if (startTime == null || lawyerId == null){
                        reservationDate.setText("예약 일정이 없습니다.")
                        reservationLawyer.setText("-")
                    }
                    else {
                        reservationDate.setText(dbDataTime.withZoneSameInstant
                            (TimeZone.getDefault().toZoneId()).toString())
                        reservationLawyer.setText("변호사 " + lawyerId)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showSettingPopup(userId: String) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_popup, null)
        val textView: TextView = view.findViewById((R.id.PopUpTv))
        textView.text = "예약을 취소하시겠습니까?"

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("예약 취소")
            .setPositiveButton("확인") { dialog, which ->
                reservationCancel(userId)
                Toast.makeText(applicationContext, "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show()
            }
            .setNeutralButton("취소", null)
            .create()
        alertDialog.setView(view)
        alertDialog.show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun reservationCancel(userId: String) {
        val reservationsRef = Firebase.database.getReference("reservations").child("reservation").child(userId)
        reservationsRef.child("startTime").setValue(null)
        reservationsRef.child("lawyerId").setValue(null)
        reservationsRef.child("summary").setValue(null)
        displayReservationDetails(userId)
    }
}