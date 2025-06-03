package com.app.filmfeed.presentation.screen.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.app.filmfeed.getMovs

@Composable
fun AboutScreen(
    id: Long,
    paddingValues: PaddingValues
){
    val movie = getMovs()[id.toInt()]
    val context = LocalContext.current
    val request = ImageRequest.Builder(context)
        .data(movie.poster)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
    LazyColumn(
        Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.Start
    ) {
        item {
            SubcomposeAsyncImage(
                model = request,
                contentDescription = movie.name,
                modifier = Modifier.height(250.dp).fillMaxWidth(),
                contentScale = ContentScale.Crop,
                success = { SubcomposeAsyncImageContent() },
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = movie.name.filter { it.isLetter() && it.isUpperCase() },
                            color = MaterialTheme.colorScheme.onBackground.copy(0.40f),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            )
        }
    }
}
