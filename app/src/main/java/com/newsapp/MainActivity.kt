package com.newsapp

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.ui.dashboard.DashboardScreen
import com.google.firebase.analytics.FirebaseAnalytics
import com.newsapp.ui.article_details.ArticleDetailScreen
import com.newsapp.ui.theme.NewsAppTheme
import com.newsapp.util.CustomAnalytics
import com.newsapp.util.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("TAG", "Application Started")
        NetworkUtils.init(applicationContext)
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CustomAnalytics.init(this)
        setContent {
            NewsAppTheme(
                darkTheme = false
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NewsApp()
                }
            }
        }
    }
}

@Composable
fun NewsApp() {
    val navController = rememberNavController()
    NavGraph(navController = navController)
}


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            DashboardScreen(navController)
        }
        composable("articleDetails/{title}/{source}/{publishedAt}/{description}/{articleImageUrl}/{articleUrl}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: "No Title"
            val publishedAt = backStackEntry.arguments?.getString("publishedAt") ?: "No Date"
            val source = backStackEntry.arguments?.getString("source") ?: "No Source"
            val description = backStackEntry.arguments?.getString("description") ?: "No Description"
            val articleImageUrl = backStackEntry.arguments?.getString("articleImageUrl") ?: ""
            val articleUrl = backStackEntry.arguments?.getString("articleUrl") ?: ""

            ArticleDetailScreen(
                navController = navController,
                title = title,
                publishedAt = publishedAt,
                source = source,
                description = description,
                articleImageUrl = articleImageUrl,
                articleUrl = articleUrl
            )
        }
    }
}

