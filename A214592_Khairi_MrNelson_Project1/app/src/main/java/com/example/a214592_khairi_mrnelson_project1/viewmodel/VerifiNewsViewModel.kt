package com.example.a214592_khairi_mrnelson_project1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.a214592_khairi_mrnelson_project1.R
import com.example.a214592_khairi_mrnelson_project1.model.CommentData
import com.example.a214592_khairi_mrnelson_project1.model.NewsItemData
import com.example.a214592_khairi_mrnelson_project1.model.ReportedNewsData

class VerifiNewsViewModel : ViewModel() {
    var loggedInUser by mutableStateOf("")
        private set

    var searchQuery by mutableStateOf("")
        private set

    var currentSelectedNews by mutableStateOf<NewsItemData?>(null)
        private set

    private val _reportedNewsList = mutableStateListOf<ReportedNewsData>()
    val reportedNewsList: List<ReportedNewsData> get() = _reportedNewsList

    // NEW FEATURE: Converted static list to mutableStateListOf so UI updates instantly when comments/likes change <---
    private val _allNews = mutableStateListOf(
        NewsItemData(1, R.drawable.bantuan_tunai, "SAHIH (TRUE)", "Bantuan tunai e-dompet fasa 2 mula disalurkan", "Kerajaan telah mengesahkan bahawa fasa ke-2 akan mula dikreditkan pada hujung bulan ini.", comments = listOf(CommentData(101, "Ahmad", "Nasib baik sahih, baru nak isi form!", 5, false))),
        NewsItemData(2, R.drawable.berita_tol_percuma, "PALSU (FAKE)", "Dakwaan tol percuma sempena cuti sekolah", "Kementerian Kerja Raya menafikan mesej tular mengenai pemberian tol percuma pada minggu ini."),
        NewsItemData(3, R.drawable.lalu_selat_hormuz, "SAHIH (TRUE)", "Kapal milik Malaysia dibenarkan melalui Selat Hormuz", "Kementerian Luar Negeri mengesahkan laluan kapal dagang Malaysia adalah selamat."),
        NewsItemData(4, R.drawable.harga_petrol, "PALSU (FAKE)", "Maklumat harga minyak yang menggunakan logo TV3", "Pihak TV3 menafikan grafik harga minyak tersebut dikeluarkan oleh pihak mereka.", comments = listOf(CommentData(102, "Siti99", "Berita palsu ni bahaya betul.", 12, true))),
        NewsItemData(5, R.drawable.berita_ribut_petir, "PALSU (FAKE)", "Tular mesej amaran ribut petir di WhatsApp: Ini fakta sebenar", "MetMalaysia menyatakan mesej amaran yang tular itu adalah palsu dan disebarkan oleh pihak tidak bertanggungjawab.")
    )
    val allNews: List<NewsItemData> get() = _allNews

    fun login(username: String) {
        loggedInUser = username
    }

    fun logout() {
        loggedInUser = ""
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun selectNewsItem(item: NewsItemData) {
        currentSelectedNews = item
    }

    fun submitReport(title: String, sourceUrl: String, reason: String) {
        val newReport = ReportedNewsData(
            id = (1000..9999).random(),
            title = title,
            sourceUrl = sourceUrl,
            reason = reason
        )
        _reportedNewsList.add(newReport)
    }

    // NEW FEATURE: Submit a comment to a specific news item <---
    fun submitComment(newsId: Int, text: String) {
        val username = if (loggedInUser.isNotEmpty()) loggedInUser else "Tetamu"
        val newComment = CommentData(
            id = (10000..99999).random(),
            username = username,
            text = text
        )

        val index = _allNews.indexOfFirst { it.id == newsId }
        if (index != -1) {
            val newsItem = _allNews[index]
            val updatedNews = newsItem.copy(comments = newsItem.comments + newComment)
            _allNews[index] = updatedNews

            // Sync with current viewing screen
            if (currentSelectedNews?.id == newsId) {
                currentSelectedNews = updatedNews
            }
        }
    }

    // NEW FEATURE: Toggle the 'Like' status on a comment <---
    fun toggleLikeComment(newsId: Int, commentId: Int) {
        val newsIndex = _allNews.indexOfFirst { it.id == newsId }
        if (newsIndex != -1) {
            val newsItem = _allNews[newsIndex]

            val updatedComments = newsItem.comments.map { comment ->
                if (comment.id == commentId) {
                    if (comment.isLikedByMe) {
                        comment.copy(likes = comment.likes - 1, isLikedByMe = false)
                    } else {
                        comment.copy(likes = comment.likes + 1, isLikedByMe = true)
                    }
                } else {
                    comment
                }
            }

            val updatedNews = newsItem.copy(comments = updatedComments)
            _allNews[newsIndex] = updatedNews

            if (currentSelectedNews?.id == newsId) {
                currentSelectedNews = updatedNews
            }
        }
    }
}