package com.example.lawjoin.data.objects

import android.graphics.drawable.Drawable
import java.io.Serializable
import kotlinx.parcelize.Parcelize

data class LawyerObjects(

    val name: String,
    val summary: String,
    val review: String,
    val category: String,
    val imageResId: Int
    ) : Serializable