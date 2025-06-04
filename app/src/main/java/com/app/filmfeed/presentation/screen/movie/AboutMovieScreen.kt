package com.app.filmfeed.presentation.screen.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.app.filmfeed.getMovs

@Composable
fun AboutScreen(
    id: Long
){
    val movie = getMovs()[id.toInt()]
    val context = LocalContext.current
    val request = ImageRequest.Builder(context)
        .data(movie.poster)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
    Box(){
        LazyColumn(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                SubcomposeAsyncImage(
                    model = request,
                    contentDescription = movie.name,
                    modifier = Modifier.fillMaxWidth().offset(y = (-28).dp),
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
            item {
                Text(
                    movie.name,
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            item {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = "",
                            tint = Color.Yellow,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            "${movie.rating}/10 • ",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Text(
                        "${movie.duration / 3600} h ${(movie.duration % 3600) / 60} • "
                    )
                    Text(
                        "${movie.categories.joinToString()} • "
                    )
                    Text(
                        "${movie.country} • ${movie.reviews}"
                    )
                }
            }
            item {
                HorizontalDivider()
            }
            item {
                Text(
                    movie.description,
                    modifier = Modifier.padding(16.dp)
                )
            }
            item{
                Spacer(Modifier.height(50.dp))
            }
        }
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.BottomCenter
        ){
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp).offset(y = (-16).dp)
            ) {
                Text(
                    "Watch"
                )
            }
        }
    }
}
