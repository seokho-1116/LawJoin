package com.example.lawjoin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.adapter.CategoryAdapter
import com.example.lawjoin.adapter.LawyerListAdapter
import com.example.lawjoin.adapter.MenuAdapter
import com.example.lawjoin.data.objects.CategoryObjects
import com.example.lawjoin.data.objects.LawyerObjects
import com.example.lawjoin.data.objects.MenuObjects

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)



        button1.setOnClickListener {
            val intent = Intent(this, AccountManagementActivity::class.java)
            startActivity(intent)
        }
        button2.setOnClickListener{
            val intent = Intent (this, LawyerListActivity::class.java)
            startActivity(intent)
        }
    }
}









// 추가해야하는 것들이 데이터베이스에서 정보 가져와서 내 정보에 예약 정보 띄우고
// 사용자 id 값은 미리 데베에서 불러와서 계속 인텐트로 넘기면서 갖고 있어야 하는가?
// 예약 변경 버튼은 데이터베이스에서 예약 정보를 받아와서 인텐트로 예약 정보를 넘기면서 예약 정보에 맞게 화면 넘기기 하면 될듯

// 객체를 하나하나 나열해서 변호사 리스트에 넣는것

// 모든 화면에서 사용자 고유 id값을 가지고 다녀야 하는걸 고려해서 만들것
// 화면 클릭시 인텐트로 변호사 정보 값들 다 넘겨주는거
