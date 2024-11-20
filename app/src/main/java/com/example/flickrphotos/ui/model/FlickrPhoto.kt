package com.example.flickrphotos.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FlickrPhoto(
    val title: String,
    val description: String,
    val author: String,
    val publishedDate: String,
    val image: String
) : Parcelable