package com.example.lawjoin

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.lawjoin.data.objects.LawyerObjects
import android.widget.TextView
import com.bumptech.glide.Glide


class LawyerProfileDetailActivity : AppCompatActivity() {
    lateinit var datas : LawyerObjects
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lawyer_detail)

        var iv_lawyer_profile_image : ImageView = findViewById(R.id.iv_lawyer_profile_image)
        var tv_lawyer_detail_name : TextView = findViewById(R.id.tv_lawyer_detail_name)
        //var ly_lawyer_category : TextView = findViewById(R.id.ly_lawyer_category)
        //var tv3 : TextView = findViewById(R.id.tv3)
        //var tv4 : TextView = findViewById(R.id.tv4)


        datas = intent.getSerializableExtra("data") as LawyerObjects

        Glide.with(this).load(datas.imageResId).into(iv_lawyer_profile_image)
        tv_lawyer_detail_name.text = datas.name
        //ly_lawyer_category.text = datas.summary
        //tv3.text = datas.review
        //tv4.text = datas.category
    }
}