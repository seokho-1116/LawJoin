package com.example.lawjoin

import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.adapter.CategoryAdapter
import com.example.lawjoin.adapter.LawyerListAdapter
import com.example.lawjoin.data.objects.CategoryObjects
import com.example.lawjoin.data.objects.LawyerObjects

class LawyerListActivity : AppCompatActivity() {
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var lawyerListAdapter : LawyerListAdapter
    lateinit var edt_lawyer_search : SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lawyer_list)

        val lawyerObjects = ArrayList<LawyerObjects>()
        val categoryObjects = ArrayList<CategoryObjects>()

        val rvCategoryList = findViewById<RecyclerView> (R.id.rv_category_list)
        val rvLawyerList = findViewById<RecyclerView>(R.id.rv_lawyer_list)
        edt_lawyer_search = findViewById<SearchView>(R.id.edt_lawyer_search)

        edt_lawyer_search.setOnQueryTextListener(searchViewTextListener)

        categoryAdapter = CategoryAdapter(categoryObjects, this, lawyerObjects)
        lawyerListAdapter = LawyerListAdapter(lawyerObjects, this)

        categoryObjects.add(CategoryObjects("카테고리1"))
        categoryObjects.add(CategoryObjects("카테고리2"))
        categoryObjects.add(CategoryObjects("카테고리3"))
        categoryObjects.add(CategoryObjects("카테고리4"))
        categoryObjects.add(CategoryObjects("모욕죄"))
        categoryObjects.add(CategoryObjects("따이병후악"))

        lawyerObjects.add(LawyerObjects("HanMunChul", "서울", "유명해요", "모든분야", R.drawable.ic_rectangle_background))
        lawyerObjects.add(LawyerObjects("UmJunPyo", "수원", "좋아요", "모욕죄", R.drawable.ic_rectangle_background))
        lawyerObjects.add(LawyerObjects("DaiByunHuak", "으잉", "멋있어요", "따이병후악", R.drawable.ic_rectangle_background))
        lawyerObjects.add(LawyerObjects("한문철", "출신4", "리뷰", "카테고리", R.drawable.ic_rectangle_background))
        lawyerObjects.add(LawyerObjects("엄준표", "출신5", "리뷰", "카테고리", R.drawable.ic_rectangle_background))
        lawyerObjects.add(LawyerObjects("대병학", "출신6", "리뷰", "카테고리", R.drawable.ic_rectangle_background))
        lawyerObjects.add(LawyerObjects("이름하나", "출신7", "리뷰", "카테고리", R.drawable.ic_rectangle_background))
        lawyerObjects.add(LawyerObjects("이름둘", "출신8", "리뷰", "카테고리",R.drawable.ic_rectangle_background))
        lawyerObjects.add(LawyerObjects("이름셋", "출신9", "리뷰", "카테고리", R.drawable.ic_rectangle_background))
        lawyerObjects.add(LawyerObjects("이름넷", "출신10", "리뷰", "카테고리", R.drawable.ic_rectangle_background))

        val lawyerLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        val categoryLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)

        rvCategoryList.adapter =  categoryAdapter
        rvLawyerList.adapter = lawyerListAdapter
        rvCategoryList.layoutManager = categoryLayoutManager
        rvLawyerList.layoutManager = lawyerLayoutManager
        rvLawyerList.itemAnimator = DefaultItemAnimator()

    }
    var searchViewTextListener : SearchView.OnQueryTextListener =
        object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                lawyerListAdapter.filter.filter(s)
                return false
            }
        }
}