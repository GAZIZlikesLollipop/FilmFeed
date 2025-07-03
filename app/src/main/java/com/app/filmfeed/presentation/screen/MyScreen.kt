package com.app.filmfeed.presentation.screen

import android.os.Environment
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import com.app.filmfeed.R
import com.app.filmfeed.Route
import com.app.filmfeed.data.network.Movie
import com.app.filmfeed.presentation.MovieViewModel
import com.app.filmfeed.presentation.components.MovieCard
import com.app.filmfeed.presentation.components.TwoColumnTextRow
import java.io.File

@Composable
fun MyScreen(
    viewModel: MovieViewModel,
    navController: NavController,
    paddingValues: PaddingValues
) {
    val watchedMovies by viewModel.watchedMovies.collectAsState()
    val favoriteMovies by viewModel.favoriteMovies.collectAsState()
    val watchLater by viewModel.watchLater.collectAsState()
    val downloadedMovies by viewModel.downloadedMovies.collectAsState()
    val categories = stringArrayResource(R.array.my_categories)
    val movies by viewModel.movies.collectAsState()
    val context = LocalContext.current
    var watchedEnabled by rememberSaveable { mutableStateOf(false) }
    val error = stringArrayResource(R.array.error)
    val apiStates = stringArrayResource(R.array.api_states)
    val cnt = stringArrayResource(R.array.my_cnt)
    var textEnabled by rememberSaveable { mutableStateOf(false) }
    var isDelete by rememberSaveable { mutableStateOf(false) }
    val alert = stringArrayResource(R.array.alert)
    var deletedId by rememberSaveable { mutableLongStateOf(0) }

    if(watchLater.isNotEmpty() || watchedMovies.isNotEmpty() || favoriteMovies.isNotEmpty() || downloadedMovies.isNotEmpty()) {
        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                items(categories) { category ->
                    if (category == categories[0] && watchLater.isNotEmpty() && textEnabled) {
                        TwoColumnTextRow(
                            firstText = category,
                            secondText = stringArrayResource(R.array.main_cnt)[0],
                        ) {}
                    }

                    if (category == categories[1] && downloadedMovies.isNotEmpty()) {
                        TwoColumnTextRow(
                            firstText = category,
                            secondText = stringArrayResource(R.array.main_cnt)[0],
                        ) {}
                    }

                    if (category == categories[2] && favoriteMovies.isNotEmpty() && textEnabled) {
                        TwoColumnTextRow(
                            firstText = category,
                            secondText = stringArrayResource(R.array.main_cnt)[0],
                        ) {}
                    }

                    if (category == categories[3] && watchedEnabled && textEnabled) {
                        TwoColumnTextRow(
                            firstText = category,
                            secondText = stringArrayResource(R.array.main_cnt)[0],
                        ) {}
                    }

                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            when (category) {
                                categories[0] -> watchLater
                                categories[1] -> downloadedMovies.keys.toList()
                                categories[2] -> favoriteMovies
                                else -> watchedMovies.keys.toList()
                            }
                        ) { ind ->
                            when (category) {
                                categories[1] -> {
                                    val downloaded = downloadedMovies[ind]
                                    val movie = Movie(
                                        id = ind,
                                        name = downloaded?.name ?: "",
                                        posterURL = downloaded?.posterPath ?: "",
                                        movieURL = "",
                                        trailerURL = "",
                                        duration = downloaded?.duration ?: 0,
                                        age = 0,
                                        genres = emptyList(),
                                        rating = 0.0,
                                        reviews = 0,
                                        description = "",
                                        country = "",
                                        year = 0,
                                        budget = 0,
                                        boxOffice = 0,
                                        movieMembers = emptyList(),
                                    )
                                    Box(
                                        modifier = Modifier.height(250.dp).width(150.dp)
                                    ) {
                                        MovieCard(movie) {
                                            if (!viewModel.deleteMod) {
                                                if (downloadedMovies[ind]?.apiState == 2 || downloadedMovies[ind]?.apiState == 0) {
                                                    val dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
                                                    viewModel.position = downloaded?.durationProgress ?: 0
                                                    viewModel.mediaItem = MediaItem.fromUri(File(dir, "${downloaded?.name}.mp4".replace(" ", "").lowercase()).toUri())
                                                    navController.navigate(Route.Watch.createRoute(ind))
                                                }
                                            } else {
                                                deletedId = ind
                                                isDelete = true
                                            }
                                        }
                                        when(downloadedMovies[ind]?.apiState) {
                                            2 -> {
                                                if(viewModel.deleteMod) {
                                                    Box(
                                                        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.onBackground.copy(0.45f)),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Column(
                                                            horizontalAlignment = Alignment.CenterHorizontally,
                                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Rounded.Delete,
                                                                contentDescription = "",
                                                                tint = MaterialTheme.colorScheme.primary,
                                                                modifier = Modifier.size(50.dp)
                                                            )
                                                            Text(
                                                                text = cnt[1],
                                                                color = MaterialTheme.colorScheme.primary,
                                                                style = MaterialTheme.typography.headlineLarge
                                                            )
                                                        }
                                                    }
                                                } else {
                                                    Column(
                                                        modifier = Modifier.align(Alignment.BottomCenter),
                                                        horizontalAlignment = Alignment.End,
                                                        verticalArrangement = Arrangement.spacedBy(24.dp)
                                                    ) {
                                                        Text(
                                                            text = "${downloaded?.duration?.div(3600)} h ${(downloaded?.duration?.rem(3600))?.div(60)} min",
                                                            style = MaterialTheme.typography.titleLarge,
                                                            color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                                                            modifier = Modifier.offset(y = (-8).dp)
                                                        )
                                                        LinearProgressIndicator(
                                                            progress = {
                                                                downloaded?.durationProgress?.toFloat()?.div(downloaded.duration.toFloat()) ?: 0f
                                                            },
                                                            modifier = Modifier.fillMaxWidth(),
                                                        )
                                                    }
                                                }
                                            }

                                            3 -> {
                                                Surface(
                                                    color = MaterialTheme.colorScheme.onBackground.copy(0.45f),
                                                    modifier = Modifier.clickable {
                                                        viewModel.deleteDownloaded(context, ind)
                                                    },
                                                    shape = RoundedCornerShape(0.dp)
                                                ) {
                                                    Column(
                                                        modifier = Modifier.fillMaxSize(),
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.Center
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Rounded.ErrorOutline,
                                                            contentDescription = null,
                                                            modifier = Modifier.size(50.dp),
                                                            tint = MaterialTheme.colorScheme.primaryContainer
                                                        )
                                                        Spacer(Modifier.height(8.dp))
                                                        Text(
                                                            text = error[2],
                                                            style = MaterialTheme.typography.headlineLarge,
                                                            color = MaterialTheme.colorScheme.primaryContainer
                                                        )
                                                    }
                                                }
                                            }

                                            1 -> {
                                                Column(
                                                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.onBackground.copy(0.35f)),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    CircularProgressIndicator(
                                                        modifier = Modifier.size(50.dp),
                                                        color = MaterialTheme.colorScheme.onBackground,
                                                        strokeWidth = 6.dp
                                                    )
                                                    Text(
                                                        apiStates[0],
                                                        style = MaterialTheme.typography.headlineLarge,
                                                        color = MaterialTheme.colorScheme.onBackground
                                                    )
                                                }
                                            }

                                            else -> {}
                                        }
                                    }
                                }

                                categories[3] -> {
                                    val watched = watchedMovies[ind]
                                    if (watched?.isWatched == true || watched?.rating!! > 0.0) {
                                        watchedEnabled = true
                                        movies.find { it.id == ind }?.let {
                                            textEnabled = true
                                            MovieCard(it) {
                                                navController.navigate(
                                                    Route.AboutMovie.createRoute(
                                                        ind
                                                    )
                                                )
                                            }
                                        } ?: {
                                            textEnabled = false
                                        }
                                    } else {
                                        watchedEnabled = false
                                    }
                                }

                                else -> {
                                    movies.find { it.id == ind }?.let {
                                        textEnabled = true
                                        MovieCard(it) {
                                            navController.navigate(
                                                Route.AboutMovie.createRoute(
                                                    ind
                                                )
                                            )
                                        }
                                    } ?: {
                                        textEnabled = false
                                    }
                                }
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = isDelete
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.onBackground.copy(0.35f)),
                    contentAlignment = Alignment.Center
                ){
                    AlertDialog(
                        onDismissRequest = {isDelete = false},
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.deleteDownloaded(context,deletedId)
                                    isDelete = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red,
                                    contentColor = MaterialTheme.colorScheme.onBackground
                                )
                            ) {
                                Text(
                                    alert[0]
                                )
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    isDelete = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Green,
                                    contentColor = MaterialTheme.colorScheme.onBackground
                                )
                            ) {
                                Text(
                                    alert[1]
                                )
                            }
                        },
                        title = {
                            Text(
                                alert[2],
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    )
                }
            }
        }
    }else{
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = cnt[0],
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}