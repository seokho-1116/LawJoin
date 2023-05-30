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
import androidx.lifecycle.ViewModelProvider
import com.example.lawjoin.R
import com.example.lawjoin.common.ViewModelFactory
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.lawyerdetail.LawyerDetailViewModel

@RequiresApi(Build.VERSION_CODES.O)
class LawyerInfoFragment(): Fragment() {
    private lateinit var lawyerDetailViewModel : LawyerDetailViewModel
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
        lawyerDetailViewModel = ViewModelProvider(requireActivity(), ViewModelFactory())[LawyerDetailViewModel::class.java]
        lawyerDetailViewModel.lawyer.observe(viewLifecycleOwner) {lawyer ->
            val lyLawyerInfo: LinearLayout = view.findViewById(R.id.sv_ly_lawyer_info)
            val lyLawyerOfficeInfo: LinearLayout = view.findViewById(R.id.sv_ly_lawyer_office)
            val tvLawyerName = lyLawyerInfo.findViewById<TextView>(R.id.tv_lawyer_name)

            tvLawyerName.text = lawyer.name

            for (category in lawyer.categories) {
                val tv = TextView(context)
                tv.text = category
                lyLawyerInfo.addView(tv)
            }

            for (career in lawyer.career) {
                val tv = TextView(context)
                tv.text = career
                lyLawyerInfo.addView(tv)
            }

            for (certificate in lawyer.certificate) {
                val tv = TextView(context)
                tv.text = certificate
                lyLawyerInfo.addView(tv)
            }

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
}