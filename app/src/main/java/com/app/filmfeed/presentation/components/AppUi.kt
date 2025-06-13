package com.app.filmfeed.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.app.filmfeed.R
import com.app.filmfeed.Route
import com.app.filmfeed.data.Movie
import com.app.filmfeed.data.MovieMember
import com.app.filmfeed.presentation.MovieViewModel
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.Period

@Composable
fun MovieCard(
    movie: Movie,
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
fun MemberCard(
    members: List<MovieMember>,
    isActor: Boolean,
    navController: NavController,
    movieId: Long,
    viewModel: MovieViewModel
){
    val cnt = stringArrayResource(R.array.about_movie)
    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    viewModel.isActor = isActor
                    navController.navigate(Route.Members.createRoute(movieId))
                },
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
                if (if (isActor) it.roles[0].contains("Actor") else !it.roles[0].contains("Actor")) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clickable { navController.navigate(Route.Member.createRoute(movieId,it.member.id)) }
                            .width(125.dp)
                    ) {
                        WebImage(
                            url = it.member.photo,
                            text = it.member.name,
                            cntScale = ContentScale.FillBounds,
                            modifier = Modifier.height(175.dp).width(125.dp)
                        )
                        Text(
                            it.member.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Text(
                            it.character ?: it.roles[0],
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailMemberCard(
    member: MovieMember,
    navController: NavController,
    movieId: Long,
){
    val memberAge = Period.between(OffsetDateTime.parse(member.member.birthDate).toLocalDate(),LocalDate.now()).years
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.clickable { navController.navigate(Route.Member.createRoute(movieId,member.member.id)) }
    ){
        Row(
            modifier = Modifier.fillMaxSize().height(135.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            WebImage(
                url = member.member.photo,
                text = member.member.name,
                cntScale = ContentScale.FillBounds,
                modifier = Modifier.width(100.dp)
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = member.member.name,
                    fontWeight = FontWeight.W400,
                    fontSize = 24.sp
                )
                Text(
                    text = "${OffsetDateTime.parse(member.member.birthDate).toLocalDate()} • $memberAge y",
                    color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                    fontSize = 20.sp
                )
                Text(
                    text = member.character ?: member.roles.joinToString(),
                    color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }
        }
        HorizontalDivider(thickness = 2.dp)
    }
}