package com.example.a214592_khairi_mrnelson_project1.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.a214592_khairi_mrnelson_project1.R
import com.example.a214592_khairi_mrnelson_project1.model.NewsItemData
import com.example.a214592_khairi_mrnelson_project1.ui.components.StatusBadge
import com.example.a214592_khairi_mrnelson_project1.viewmodel.VerifiNewsViewModel

@Composable
fun VerifiNewsMainScreen(viewModel: VerifiNewsViewModel, navController: NavHostController) {
    val filteredNews = viewModel.allNews.filter {
        it.title.contains(viewModel.searchQuery, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier.fillMaxSize().systemBarsPadding().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeaderSection(
                loggedInUser = viewModel.loggedInUser,
                navController = navController
            )
            SearchBarSection(searchQuery = viewModel.searchQuery, onSearchQueryChange = { viewModel.updateSearchQuery(it) })
            if (viewModel.searchQuery.isEmpty()) { FeaturedNewsSection() }
            NewsListSection(newsItems = filteredNews, onNewsClick = { selectedItem ->
                viewModel.selectNewsItem(selectedItem)
                navController.navigate("Details")
            })
        }
    }
}

@Composable
fun HeaderSection(loggedInUser: String, navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.size(28.dp))
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text("Profil") }, onClick = { expanded = false; navController.navigate("Profile") })
                DropdownMenuItem(text = { Text("Lapor Berita Palsu") }, onClick = { expanded = false; navController.navigate("Report") })
                DropdownMenuItem(text = { Text("Senarai Laporan Saya") }, onClick = { expanded = false; navController.navigate("ReportList") })
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.verifinews_logo), contentDescription = "Logo", modifier = Modifier.size(28.dp))
                Text(text = "erifiNews", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            }
            Text(text = "A214592", fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }

        if (loggedInUser.isNotEmpty()) {
            Text(text = "Hi, $loggedInUser!", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        } else {
            IconButton(onClick = { navController.navigate("Profile") }) {
                Icon(Icons.Default.Person, contentDescription = "Login", modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun SearchBarSection(searchQuery: String, onSearchQueryChange: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary).height(72.dp),
        contentAlignment = Alignment.Center
    ) {
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text(text = stringResource(R.string.search_cari_berita), color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).clip(RoundedCornerShape(30.dp)),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@Composable
fun FeaturedNewsSection() {
    Card(
        modifier = Modifier.fillMaxWidth().height(240.dp).padding(horizontal = 20.dp, vertical = 16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.berita_ribut_petir),
                contentDescription = "Featured News",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f)), startY = 150f)
                )
            )
            Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp).fillMaxWidth()) {
                StatusBadge(status = "PALSU (FAKE)")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tular mesej amaran ribut petir di WhatsApp: Ini fakta sebenar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    lineHeight = 24.sp
                )
            }
        }
    }
}

@Composable
fun NewsListSection(newsItems: List<NewsItemData>, onNewsClick: (NewsItemData) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = "Semakan Fakta Terkini",
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        newsItems.forEach { item ->
            NewsListItem(
                item = item,
                onReadMoreClick = { onNewsClick(item) }
            )
        }
    }
}

@Composable
fun NewsListItem(item: NewsItemData, onReadMoreClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { expanded = !expanded }
            .animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = "Thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(85.dp).clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    StatusBadge(status = item.status)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.title,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = if (expanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = item.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onReadMoreClick,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Baca Lanjut", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}