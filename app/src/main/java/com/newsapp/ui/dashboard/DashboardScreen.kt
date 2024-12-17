package com.example.newsapp.ui.dashboard

import DateUtils.formatDateISO
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.newsapp.data.model.NewsArticle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavController
import dateOutputFormat
import java.net.URLEncoder

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {

    val newsState = viewModel.newsState.value
    val isRefreshing = (newsState is DashboardViewState.Loading) && newsState.isRefresh
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { viewModel.refreshNews() }
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            viewModel.onSearchSubmit(searchQuery)
                            focusManager.clearFocus()
                        }
                    )

                )

                when (newsState) {
                    is DashboardViewState.Loading -> {
                        if (newsState.loading) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    is DashboardViewState.Success -> {
                        NewsArticleList(newsState.data, navController)
                    }

                    is DashboardViewState.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                Icon(
                                    imageVector = Icons.Filled.Warning,
                                    contentDescription = "Error Icon",
                                    modifier = Modifier.size(80.dp)
                                )
                                Text(
                                    text = newsState.errorMessage,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun NewsArticleList(articles: List<NewsArticle>, navController: NavController) {

    LazyColumn {
        items(articles) { article ->
            NewsArticleItem(article, navController)
        }
    }
}

@Composable
fun NewsArticleItem(article: NewsArticle, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable {
                val encodedImageUrl = if (article.urlToImage != null) {
                    URLEncoder.encode(article.urlToImage, "UTF-8")
                } else {
                    "--"
                }
                val encodedArticleUrl = URLEncoder.encode(article.url, "UTF-8")
                navController.navigate(
                    "articleDetails/${article.title}/${article.source.name}/${article.publishedAt}/${article.description}/${encodedImageUrl}/${encodedArticleUrl}"
                )
            }
    ) {
        article.urlToImage?.let {
            AsyncImage(
                model = it,
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Text(
            text = article.title,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        article.description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Text(
            text = "Published: ${formatDateISO(article.publishedAt, dateOutputFormat)}",
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}