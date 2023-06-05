package com.example.lawjoin.account

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import com.example.lawjoin.counselreservation.CounselReservationActivity
import com.example.lawjoin.data.model.CounselReservation
import com.example.lawjoin.data.objects.MenuObjects
import com.example.lawjoin.data.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountManagementActivity : AppCompatActivity() {
    private var context : Context = this
    private val auth = Firebase.auth
    lateinit private var menuAdapter : MenuAdapter
    lateinit private var reservationDate : TextView
    lateinit private var reservationLawyer : TextView
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_management)

        val userRepository = UserRepository.getInstance()
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

        var currentUser : FirebaseUser? = auth.currentUser
        val userID = currentUser?.uid
        val userIDString: String = userID.toString()
        // auth.currentUser 를 이용하여 userID에 현재 사용자 아이디값을 받고, String? -> String 변환

        mainBtn.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        reservationCancelBtn.setOnClickListener(){
            showSettingPopup(userIDString)
            reservationCancel(userIDString)
        }
        reservationChangeBtn.setOnClickListener(){
            Toast.makeText(context, "변경할 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, CounselReservationActivity::class.java)
            startActivity(intent)
        }

        displayReservationDetails(userIDString)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayReservationDetails(userId: String) {
        val userRepository = UserRepository.getInstance()

        userRepository.findUser(userId) { snapshot ->
            val user = snapshot.getValue(CounselReservation::class.java)

            // 사용자의 예약 정보를 가져와 UI에 표시
            user?.let {
                val reservationTime = it.startTime
                val lawyerId = it.lawyerId

                reservationDate.text = reservationTime
                reservationLawyer.text = lawyerId
            }
        }
    }
    private fun showSettingPopup(userId: String) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_popup, null)
        val textView: TextView = view.findViewById((R.id.PopUpTv))
        textView.text = "예약을 취소하시겠습니까?"

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("예약 취소")
            .setPositiveButton("확인") { dialog, which ->
                reservationCancel(userId)
                Toast.makeText(applicationContext, "예약이 취소되었습니다.", Toast.LENGTH_SHORT)
            }
            .setNeutralButton("취소", null)
            .create()
        alertDialog.setView(view)
        alertDialog.show()
    }
    private fun reservationCancel(userId: String) {
        val userRepository = UserRepository.getInstance()
        userRepository.saveUser(userId) { databaseReference ->
            databaseReference.child("reservation").removeValue()
        }
    }
}