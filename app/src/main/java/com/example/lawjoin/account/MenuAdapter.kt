package com.example.lawjoin.account

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.lawyer.LawyerProfileDetailActivity
import com.example.lawjoin.R
import com.example.lawjoin.data.objects.MenuObjects

class MenuAdapter(private val menuSet: ArrayList<MenuObjects>, var context:Context)
    : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    var userId : Int = 0
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val managementItem1: TextView
        init {
            managementItem1 = view.findViewById(R.id.managementItem1)
        }

        fun bind(item : MenuObjects){
            managementItem1.text = item.menu

            itemView.setOnClickListener {
                Toast.makeText(context, "시발", Toast.LENGTH_SHORT).show()
                when(managementItem1.text){
                    "좋아요한 변호사" -> Intent(context, LawyerProfileDetailActivity::class.java).apply{
                        putExtra("data", userId)
                    }.run{context.startActivity(this)}

                    "북마크한 법률 단어 보기" -> Intent(context, LawyerProfileDetailActivity::class.java).apply{
                        putExtra("data", userId)
                    }.run{context.startActivity(this)}

                    "내가 쓴 글" -> Intent(context, LawyerProfileDetailActivity::class.java).apply{
                        putExtra("data", userId)
                    }.run{context.startActivity(this)}

                    "내 정보" -> Intent(context, LawyerProfileDetailActivity::class.java).apply{
                        putExtra("data", userId)
                    }.run{context.startActivity(this)}

                    "앱 설정" -> Intent(context, LawyerProfileDetailActivity::class.java).apply{
                        putExtra("data", userId)
                    }.run{context.startActivity(this)}

                    //"로그아웃" -> 로그아웃 하는 코드로 팝업창? 띄우고 로그아웃 하는 코드 작성

                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.account_management_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.managementItem1.text = menuSet!![position].menu
        viewHolder.bind(menuSet[position])
    }
    override fun getItemCount() = menuSet.size
}