package com.app.filmfeed.presentation.screen.movie

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import com.app.filmfeed.R
import com.app.filmfeed.Route
import com.app.filmfeed.UserMovie
import com.app.filmfeed.presentation.ApiState
import com.app.filmfeed.presentation.MovieViewModel
import com.app.filmfeed.presentation.components.MovieMembers
import com.app.filmfeed.presentation.components.WebImage
import com.app.filmfeed.utils.formatWithSpaces
import com.google.protobuf.Timestamp
import kotlinx.coroutines.delay
import java.time.Instant
import kotlin.math.roundToInt

@Composable
fun AboutScreen(
    id: Long,
    navController: NavController,
    viewModel: MovieViewModel
){
    val movies by viewModel.movies.collectAsState()
    val movie = movies.find { it.id == id }!!
    val cnt = stringArrayResource(R.array.about_movie)
    val context = LocalContext.current

    val watchedMovies by viewModel.watchedMovies.collectAsState()
    val favoriteMovies by viewModel.favoriteMovies.collectAsState()
    val watchLater by viewModel.watchLater.collectAsState()
    val downloadedMovies by viewModel.downloadedMovies.collectAsState()
    val downloadState by viewModel.downloadState.collectAsState()
    val downloadStatus by viewModel.downloadStatus.collectAsState()
    val apiStates = stringArrayResource(R.array.api_states)
    val continueWatching by viewModel.continueWatching.collectAsState()

    LaunchedEffect(downloadState) {
        while(downloadState !is ApiState.Initial && downloadState !is ApiState.Success) {
            viewModel.getDownloadState(context)
            delay(100)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.currentMovieId = movie.id
    }

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
                    modifier = Modifier.fillMaxWidth(),
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
                        movie.genres.joinToString(" • "){it.name}
                    )
                }
                Spacer(Modifier.height(12.dp))
            }
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item{}
                    repeat(5){ index ->
                        val icon = when(index){
                            0 -> Icons.Rounded.Star
                            1 -> Icons.Rounded.Bookmark
                            2 -> when(downloadStatus) {
                                apiStates[1] -> Icons.Rounded.Error
                                apiStates[2] -> Icons.Rounded.CheckCircle
                                apiStates[3] -> Icons.Rounded.PauseCircle
                                else -> Icons.Rounded.Download
                            }
                            3 -> Icons.Rounded.Favorite
                            else -> Icons.Rounded.Visibility
                        }
                        val text = when(index){
                            0 -> cnt[9]
                            1 -> cnt[10]
                            2 -> if(downloadState is ApiState.Initial) cnt[11] else downloadStatus
                            3 -> cnt[12]
                            else -> cnt[13]
                        }
                        item {
                            val enabled =
                                when(index){
                                    0 -> id in watchedMovies && watchedMovies[id]?.rating != 0.0
                                    1 -> id in watchLater
                                    2 -> id in downloadedMovies
                                    3 -> id in favoriteMovies
                                    else -> id in watchedMovies && watchedMovies[id]?.isWatched == true
                                }
                            val callback = {
                                when (index) {
                                    0 -> if(enabled) viewModel.deleteGrade(id) else viewModel.evaluateMovie(id,9.4)
                                    1 -> if(enabled) viewModel.deleteWatchLater(id) else viewModel.addToWatchLater(id)
                                    2 -> if(enabled) viewModel.deleteDownloaded(context,id) else viewModel.downloadMovie(context,id)
                                    3 -> if(enabled) viewModel.deleteFavorite(id) else viewModel.addToFavorite(id)
                                    else -> if(enabled) viewModel.deleteWatched(id) else viewModel.addToWatched(id)
                                }
                            }
                            val colorAnim by animateColorAsState(
                                targetValue = if(enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(0.37f),
                                animationSpec = tween(300)
                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = if(index == 2 && downloadState !is ApiState.Initial) Modifier else Modifier.clickable { callback() }
                            ) {
                                if(index == 2 && downloadState is ApiState.Loading){
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onBackground.copy(0.35f),
                                        modifier = Modifier.size(36.dp)
                                    )
                                }else {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = text,
                                        tint = colorAnim,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                                Text(
                                    text = text,
                                    color = colorAnim
                                )
                            }
                        }
                    }
                    item{}
                }
                Spacer(Modifier.height(12.dp))
            }
            item { HorizontalDivider() }
            item {
                Text(
                    movie.description,
                    modifier = Modifier.padding(16.dp)
                )
            }
            item { HorizontalDivider() }
            item {
                MovieMembers(
                    members = movie.movieMembers,
                    isActor = true,
                    navController = navController,
                    movieId = id,
                    viewModel = viewModel
                )
            }
            item {
                MovieMembers(
                    members = movie.movieMembers,
                    isActor = false,
                    navController = navController,
                    movieId = id,
                    viewModel = viewModel
                )
            }
            item { HorizontalDivider() }
            item { MovieFinancialsCard(movie.budget,movie.boxOffice) }
            item { Spacer(Modifier.height(60.dp)) }
        }
        Row(
            modifier = Modifier.fillMaxHeight().padding(horizontal = 8.dp).offset(y = (-20).dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Button(
                onClick = {
                    viewModel.position = 0
                    viewModel.mediaItem = MediaItem.fromUri(movie.trailerURL)
                    navController.navigate(Route.Watch.createRoute(id))
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary.copy(0.8f)),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cnt[8],
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Button(
                onClick = {
                    if(continueWatching.containsKey(id)) {
                        val map = continueWatching.toMutableMap()
                        val now = Instant.now()
                        val userMovie = map[id]?.toBuilder()
                            ?.setLastWatchedData(
                                Timestamp.newBuilder()
                                    .setSeconds(now.epochSecond)
                                    .setNanos(now.nano)
                                    .build()
                            )
                            ?.build() ?: UserMovie.newBuilder().setLastWatchedData(
                            Timestamp.newBuilder()
                                .setSeconds(now.epochSecond)
                                .setNanos(now.nano)
                                .build()
                            ).build()
                        map.put(id,userMovie)
                        viewModel.updateContinueWatching(map.toMap())
                        viewModel.position = continueWatching[id]?.durationProgress ?: 0
                    }
                    viewModel.mediaItem = MediaItem.fromUri(movie.movieURL)
                    navController.navigate(Route.Watch.createRoute(id))
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary.copy(0.8f)),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cnt[0],
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun MovieFinancialsCard(
    budget: Long,
    boxOffice: Long,
    modifier: Modifier = Modifier
) {
    val cnt = stringArrayResource(R.array.about_movie)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Заголовок
            Text(
                text = cnt[6],
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(12.dp))

            // Две колонки: бюджет и сборы
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                FinancialItem(
                    label = cnt[4],
                    amount = budget,
                    icon = Icons.Default.AttachMoney,
                    modifier = Modifier.weight(1f) // пример иконки
                )
                FinancialItem(
                    label = cnt[5],
                    amount = boxOffice,
                    icon = Icons.Default.Paid,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Прогресс-бар, показывающий отношение сборов к бюджету
            val ratio = (boxOffice.toFloat() / maxOf(budget, 1L)).coerceAtMost(1f)
            LinearProgressIndicator(
            progress = { ratio },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = MaterialTheme.colorScheme.secondary,
//            trackColor = COMPILED_CODE,
//            strokeCap = COMPILED_CODE,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${(ratio * 100).roundToInt()}% ${cnt[7]}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun FinancialItem(
    label: String,
    amount: Long,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "$${formatWithSpaces(amount)}",
            style = MaterialTheme.typography.titleMedium
        )
    }
}