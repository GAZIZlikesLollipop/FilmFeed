package com.app.filmfeed.presentation.screen.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import com.app.filmfeed.R
import com.app.filmfeed.Route
import com.app.filmfeed.getMovs
import com.app.filmfeed.presentation.MovieViewModel
import com.app.filmfeed.presentation.components.MemberSurface
import com.app.filmfeed.presentation.components.WebImage

@Composable
fun AboutScreen(
    id: Long,
    navController: NavController,
    viewModel: MovieViewModel
){
    val movie = getMovs()[id.toInt()]
    val cnt = stringArrayResource(R.array.about_movie)

    LaunchedEffect(Unit) { viewModel.mediaItem = MediaItem.fromUri(movie.movieURL) }

    Box {
        LazyColumn(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                WebImage(
                    url = movie.posterURL,
                    text = movie.name,
                    cntScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
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
                Spacer(Modifier.height(12.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = "",
                            tint = Color.Yellow,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            "${movie.rating}/10 • ${movie.reviews/1000}K",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Text(
                        "${movie.duration / 3600} h ${(movie.duration % 3600) / 60} • ${movie.country}"
                    )
                    Text(
                        movie.categories.joinToString(" • ")
                    )
                }
                Spacer(Modifier.height(12.dp))
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
                HorizontalDivider()
            }
            item {
                MemberSurface(
                    members = movie.members,
                    isActor = true
                )
            }
            item {
                MemberSurface(
                    members = movie.members,
                    isActor = false
                )
            }
            item {
                Spacer(Modifier.height(50.dp))
            }
        }
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.BottomCenter
        ){
            Button(
                onClick = { navController.navigate(Route.WatchMovie.createRoute(id)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-20).dp)
            ) {
                Text(
                    cnt[0]
                )
            }
        }
    }
}
