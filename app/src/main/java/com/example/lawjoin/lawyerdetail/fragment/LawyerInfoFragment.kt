package com.example.lawjoin.lawyerdetail.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.lawjoin.R
import com.example.lawjoin.data.model.Lawyer
import kotlin.text.StringBuilder

@RequiresApi(Build.VERSION_CODES.O)
class LawyerInfoFragment(val lawyer: Lawyer): Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_lawyer_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupLawyerInfo(view)
    }

    private fun setupLawyerInfo(view: View) {
        val tvCategory: TextView = view.findViewById(R.id.tv_categories)
        val tvCareer: TextView = view.findViewById(R.id.tv_career)
        val tvCertificate: TextView = view.findViewById(R.id.tv_certificates)
        val lyLawyerInfo: LinearLayout = view.findViewById(R.id.sv_ly_lawyer_info)
        val lyLawyerOfficeInfo: LinearLayout = view.findViewById(R.id.sv_ly_lawyer_office)
        val tvLawyerName = lyLawyerInfo.findViewById<TextView>(R.id.tv_lawyer_name)

        tvLawyerName.text = lawyer.name

        val categoryBuilder = StringBuilder().appendLine("전문 분야")
        for (category in lawyer.categories) {
            categoryBuilder.append("-")
            categoryBuilder.appendLine(category)
        }
        tvCategory.text = categoryBuilder.toString()

        val careerBuilder = StringBuilder().appendLine("경력")
        for (career in lawyer.career) {
            careerBuilder.append("-")
            categoryBuilder.appendLine(career)
        }
        tvCareer.text = careerBuilder.toString()

        val certificateBuilder = StringBuilder().appendLine("자격증")
        for (certificate in lawyer.certificate) {
            certificateBuilder.append("-")
            certificateBuilder.appendLine(certificate)
        }
        tvCertificate.text = certificateBuilder.toString()

        val introduce = TextView(context)
        introduce.text = lawyer.introduce
        lyLawyerInfo.addView(introduce)

        val tvLawyerOfficeName =
            lyLawyerOfficeInfo.findViewById<TextView>(R.id.tv_lawyer_office_name)
        tvLawyerOfficeName.text = lawyer.office.name
        val tvLawyerOfficeLocation =
            lyLawyerOfficeInfo.findViewById<TextView>(R.id.tv_lawyer_office_location)
        tvLawyerOfficeLocation.text = lawyer.office.location
        val tvLawyerOfficePhone =
            lyLawyerOfficeInfo.findViewById<TextView>(R.id.tv_lawyer_office_phone)
        tvLawyerOfficePhone.text = lawyer.office.phone
        val tvLawyerOfficeOpeningHours =
            lyLawyerOfficeInfo.findViewById<TextView>(R.id.tv_lawyer_office_opening_hours)
        tvLawyerOfficeOpeningHours.text = lawyer.office.openingTime.toString()
    }
}