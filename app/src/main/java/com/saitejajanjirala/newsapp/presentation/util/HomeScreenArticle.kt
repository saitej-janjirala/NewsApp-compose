package com.saitejajanjirala.newsapp.presentation.util

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.saitejajanjirala.newsapp.data.models.Article

@Composable
fun HomeScreenArticle(article: Article,onArticleClicked : ()->Unit){
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp)
        ,
    ) {
        ElevatedCard(
            onClick = {
                onArticleClicked.invoke()
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                AsyncImage(
                    model = article.urlToImage,
                    "newsimage",
                    alignment = AbsoluteAlignment.CenterLeft,
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = article.title.toString(), modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.titleMedium, maxLines = 3,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = article.description.toString(), modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.bodyMedium, maxLines = 3
                )
            }
        }
    }
}