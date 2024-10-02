package com.saitejajanjirala.newsapp.presentation

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController

@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: MainViewModel= hiltViewModel(),
){
    var webView: WebView? = null



    val url = viewModel.getUrl()
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl(url?:"www.expedia.com")
                webView = this // Reference WebView for back navigation
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    // Use BackHandler to manage WebView back navigation
    BackHandler(enabled = webView?.canGoBack() == true) {
        webView?.goBack() // Go back in WebView's history if possible
    }
    
}