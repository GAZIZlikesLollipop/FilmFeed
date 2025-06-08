package com.app.filmfeed.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.app.filmfeed.R
import com.app.filmfeed.data.MovieItem
import com.app.filmfeed.data.MovieMember

@Composable
fun MovieCard(
    movie: MovieItem,
    onClick: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.clickable{onClick()}
    ){
        WebImage(
            url = movie.posterURL,
            text = movie.name,
            cntScale = ContentScale.Crop,
            modifier = Modifier.height(250.dp).width(175.dp)
        )
        Text(
            text = movie.name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun WebImage(
    url: String,
    text: String,
    cntScale: ContentScale,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val request = ImageRequest.Builder(context)
        .data(url)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
    SubcomposeAsyncImage(
        model = request,
        contentDescription = text,
        modifier = modifier,
        contentScale = cntScale,
        success = {SubcomposeAsyncImageContent()},
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
                    text = text.filter { it.isLetter() && it.isUpperCase() },
                    color = MaterialTheme.colorScheme.onBackground.copy(0.40f),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    )
}

@Composable
fun MemberSurface(
    members: List<MovieMember>,
    isActor: Boolean
){
    val cnt = stringArrayResource(R.array.about_movie)
    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth().clickable{},
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                if(isActor) cnt[1] else cnt[2],
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                cnt[3],
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(8.dp)
            )
        }
        LazyRow {
            items(members) {
                if (if (isActor) !it.role.contains("Director") else it.role.contains("Director")) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.clickable{}
                    ) {
                        WebImage(
                            url = it.photo,
                            text = it.name,
                            cntScale = ContentScale.FillBounds,
                            modifier = Modifier.height(150.dp).width(100.dp)
                        )
                        Text(
                            it.name,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            it.role,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
                        )
                    }
                }
            }
        }
    }
}