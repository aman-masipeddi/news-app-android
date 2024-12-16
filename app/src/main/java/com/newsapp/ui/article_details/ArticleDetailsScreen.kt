package com.newsapp.ui.article_details

import DateUtils.formatDateISO
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.analytics.FirebaseAnalytics
import com.newsapp.util.CustomAnalytics
import dateOutputFormat

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    navController: NavController,
    title: String,
    publishedAt: String,
    source: String,
    description: String,
    articleImageUrl: String,
    articleUrl: String,
) {

    Log.d("ArticleDetailScreen", title)
    Log.d("ArticleDetailScreen", publishedAt)
    Log.d("ArticleDetailScreen", source)
    Log.d("ArticleDetailScreen", description)

    val context = LocalContext.current

    // Logging analytics event
    CustomAnalytics.logEvent("headline_opened",  Bundle().apply {
        putString(FirebaseAnalytics.Param.ITEM_NAME, title)
    })

    Scaffold(
        topBar = {
            // Top App Bar with Back button
            TopAppBar(
                title = { Text("Article Details", style = MaterialTheme.typography.h6,) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                )
            )
        },
        content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Title
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Article Image
                    AsyncImage(
                        model = articleImageUrl,
                        contentDescription = title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    // Publication Date
                    Text(
                        text = "Published on: ${formatDateISO(publishedAt, dateOutputFormat)}",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Source
                    Text(
                        text = "Source: $source",
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Description
                    Text(
                        text = description,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // "More" Button to open article in browser
                    TextButton(
                        onClick = {
                            CustomAnalytics.logEvent("article_opened",  Bundle().apply {
                                putString("article_url", articleUrl)
                            })

                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("More...",style = MaterialTheme.typography.h6, color = Color.Blue)
                    }
                }
        })
}
