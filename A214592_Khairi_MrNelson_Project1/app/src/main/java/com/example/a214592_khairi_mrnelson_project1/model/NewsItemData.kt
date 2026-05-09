package com.example.a214592_khairi_mrnelson_project1.model

import androidx.compose.ui.graphics.Color

// ---> NEW FEATURE: Data class for community comments <---
data class CommentData(
    val id: Int,
    val username: String,
    val text: String,
    val likes: Int = 0,
    val isLikedByMe: Boolean = false // Tracks if the current user liked it
)

data class NewsItemData(
    val id: Int,  // unique id for each news
    val imageRes: Int, // reference for the image drawable
    val status: String, // teks sahih dan palsu
    val title: String, // headline
    val claim: String = "Dakwaan penyebaran berita di media sosial.",
    val description: String = "Sila klik pautan untuk membaca artikel penuh mengenai berita ini untuk memastikan kesahihannya.", // detail pasal berita
    // ---> NEW FEATURE: Added comments list to the main news data <---
    val comments: List<CommentData> = emptyList()
)

// Data class to hold user reports
data class ReportedNewsData(
    val id: Int,
    val title: String,
    val sourceUrl: String,
    val reason: String,
    val status: String = "Menunggu Semakan" // Pending Review
)