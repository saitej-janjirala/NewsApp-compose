package com.saitejajanjirala.newsapp.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.saitejajanjirala.newsapp.domain.models.Result
import com.saitejajanjirala.newsapp.presentation.util.HomeScreenArticle
import com.saitejajanjirala.newsapp.presentation.util.Screen
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val news = viewModel.items.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember{
        SnackbarHostState()
    }
    val snackBarState = remember {
        mutableStateOf<String>("")
    }
    LaunchedEffect(snackBarState) {
        coroutineScope.launch {
            if(snackBarState.value.isNotEmpty()) {
                snackBarHostState.showSnackbar(snackBarState.value, null)
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = "Compose News")
                },

            )
        }
    ){paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)){
            when (news.value) {
                is Result.Error -> {
                    snackBarState.value = news.value.message.toString()
                }

                is Result.Loading -> {
                    LoadingView()
                }

                is Result.Success -> {
                    LazyColumn (modifier = Modifier.fillMaxSize()){
                        news.value.data?.let {
                            items(it){article->
                                HomeScreenArticle(article = article){
                                    navController.navigate(Screen.DetailSreen.route + "?url=${article.url}")
                                }
                            }
                        }
                    }
                }

            }
        }


    }



}


@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}