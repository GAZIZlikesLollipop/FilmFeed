package com.app.filmfeed.presentation.screen.member

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.filmfeed.R
import com.app.filmfeed.Route
import com.app.filmfeed.presentation.MovieViewModel
import com.app.filmfeed.presentation.components.MovieCard
import com.app.filmfeed.presentation.components.WebImage
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.Period

@Composable
fun MemberScreen(
    movieId: Long,
    memberId: Long,
    paddingValues: PaddingValues,
    viewModel: MovieViewModel,
    navController: NavController
){
    val cnt = stringArrayResource(R.array.member_cnt)
    val movies by viewModel.movies.collectAsState()
    val member = movies.find { it.id == movieId }!!.movieMembers.find { it.member.id == memberId }?.member

    if(member != null) {
        val memberAge =
            if (member.deathDate == null || member.deathDate == "") {
                Period.between(
                    OffsetDateTime.parse(member.birthDate).toLocalDate(),
                    LocalDate.now()
                ).years
            } else {
                Period.between(
                    OffsetDateTime.parse(member.birthDate).toLocalDate(),
                    OffsetDateTime.parse(member.deathDate).toLocalDate()
                ).years
            }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    WebImage(
                        url = member.photo,
                        text = member.name,
                        cntScale = ContentScale.Crop,
                        modifier = Modifier.width(130.dp)
                    )
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            member.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "${member.roles.joinToString(limit = 3).substringBeforeLast(",")}...",
                            color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            OffsetDateTime.parse(member.birthDate).toLocalDate().toString(),
                            color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (member.deathDate != null) {
                            Text(
                                OffsetDateTime.parse(member.deathDate).toLocalDate().toString(),
                                color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Text(
                            "$memberAge year",
                            color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            member.nationality,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            item { HorizontalDivider() }
            item {
                Text(
                    cnt[1],
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    member.biography,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            item { HorizontalDivider() }
            item {
                Text(
                    cnt[0],
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(Modifier.height(16.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(member.featuredFilms) {
                        MovieCard(it) { navController.navigate(Route.AboutMovie.createRoute(it.id)) }
                    }
                }
            }
        }
    }
}
