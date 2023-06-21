package com.example.lawjoin.account

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.lawjoin.MainActivity
import com.example.lawjoin.R
import com.example.lawjoin.common.AuthUtils
import com.example.lawjoin.counselreservation.CounselReservationActivity
import com.example.lawjoin.data.model.AuthUserDto
import com.example.lawjoin.databinding.ActivityAccountManagementBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AccountManagementActivity : AppCompatActivity() {
    private var context : Context = this
    private lateinit var database: DatabaseReference
    private lateinit var currentUser: AuthUserDto
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAccountManagementBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.database.reference
        binding = ActivityAccountManagementBinding.inflate(layoutInflater)

        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        AuthUtils.getCurrentUser { authUserDto, _ ->
            currentUser = authUserDto!!
        }

        val userID = currentUser?.uid
        val userIDString: String = userID.toString()

        binding.mainBtn.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.reservationCancelBtn.setOnClickListener(){
            showSettingPopup(userIDString)
        }

        binding.reservationChangeBtn.setOnClickListener(){
            Toast.makeText(context, "변경할 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CounselReservationActivity::class.java)
            startActivity(intent)
        }

        binding.btnBookmarkedPost.setOnClickListener {
            startActivity(Intent(this, BookmarkedPostActivity::class.java))
        }
        binding.btnMyPost.setOnClickListener {
            startActivity(Intent(this, MyPostActivity::class.java))
        }
        binding.btnLikeLawyer.setOnClickListener {
            startActivity(Intent(this, LikeLawyerActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            if (auth.currentUser != null) {
                Firebase.auth.signOut()
                GoogleSignIn.getClient(this, gso).signOut()
            } else {
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                    }
                    else {
                        Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                    }
                }
            }
        }


        displayReservationDetails(userIDString)

        setContentView(binding.root)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayReservationDetails(userId: String) {
        val reservationsRef = Firebase.database.getReference("reservations")
            .child("reservation")
            .child(userId)
        reservationsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val startTime = dataSnapshot.child("startTime").getValue(String::class.java)
                    val lawyerName = dataSnapshot.child("lawyerName").getValue(String::class.java)
                    val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
                    val dbDataTime = ZonedDateTime.parse(startTime.toString(), formatter).withZoneSameInstant(TimeZone.getDefault().toZoneId())
                    binding.reservationDate.text = dbDataTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd  E  HH:mm").withZone(TimeZone.getDefault().toZoneId()))
                    binding.reservationLawyer.text = "변호사 $lawyerName"
                } else {
                    binding.reservationDate.text = "예약 일정이 없습니다."
                    binding.reservationLawyer.text = "-"
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
        Firebase.database.getReference("reservations")
            .child("reservation")
            .child(userId)
            .removeValue()
        displayReservationDetails(userId)
    }
}