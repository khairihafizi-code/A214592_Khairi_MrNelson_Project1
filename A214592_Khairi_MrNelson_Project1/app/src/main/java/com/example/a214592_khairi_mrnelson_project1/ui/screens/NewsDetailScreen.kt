package com.example.a214592_khairi_mrnelson_project1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.a214592_khairi_mrnelson_project1.ui.components.CommentCard
import com.example.a214592_khairi_mrnelson_project1.ui.components.SnopesClaimCard
import com.example.a214592_khairi_mrnelson_project1.ui.components.StatusBadge
import com.example.a214592_khairi_mrnelson_project1.viewmodel.VerifiNewsViewModel

@Composable
fun NewsDetailScreen(viewModel: VerifiNewsViewModel, navController: NavHostController) {
    val newsItem = viewModel.currentSelectedNews

    if (newsItem == null) {
        navController.navigateUp()
        return
    }

    // ---> NEW FEATURE: State for typing a new comment <---
    var newCommentText by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)) {
            Image(
                painter = painterResource(id = newsItem.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier
                    .padding(16.dp)
                    .statusBarsPadding()
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(50))
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = newsItem.title, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 30.sp)
            Spacer(modifier = Modifier.height(16.dp))

            SnopesClaimCard(claim = newsItem.claim)
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "KEPUTUSAN (VERDICT)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            StatusBadge(status = newsItem.status)

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "FAKTA SEBENAR", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = newsItem.description, fontSize = 16.sp, lineHeight = 24.sp)

            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(16.dp))

            // ---> NEW FEATURE: Community Section <---
            Text(text = "RUANGAN KOMUNITI (${newsItem.comments.size} Komen)", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))

            // Input Field
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newCommentText,
                    onValueChange = { newCommentText = it },
                    placeholder = { Text("Kongsi pandangan anda...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (newCommentText.isNotBlank()) {
                            viewModel.submitComment(newsItem.id, newCommentText)
                            newCommentText = "" // Clear field after submitting
                        }
                    },
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Hantar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Loop through and display comments using the Shared Component
            if (newsItem.comments.isEmpty()) {
                Text(text = "Belum ada komen. Jadilah yang pertama!", color = Color.Gray, fontSize = 14.sp)
            } else {
                newsItem.comments.forEach { comment ->
                    CommentCard(
                        comment = comment,
                        onLikeClick = { viewModel.toggleLikeComment(newsItem.id, comment.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp)) // Bottom padding
        }
    }
}