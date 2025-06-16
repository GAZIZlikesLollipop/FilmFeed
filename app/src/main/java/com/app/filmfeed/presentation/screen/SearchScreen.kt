package com.app.filmfeed.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.app.filmfeed.presentation.components.WebImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: MovieViewModel,
    paddingValues: PaddingValues,
    navController: NavController
){
    val movies by viewModel.movies.collectAsState()
    val filters = stringArrayResource(R.array.filters)
    val sorts = stringArrayResource(R.array.sorts)
    val cnt = stringArrayResource(R.array.search_cnt)

    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            items(movies) {
                if (it.name.replace(" ", "").lowercase()
                        .contains(viewModel.searchText.lowercase())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .clickable { navController.navigate(Route.AboutMovie.createRoute(it.id)) },
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WebImage(
                            url = it.posterURL,
                            text = it.name,
                            cntScale = ContentScale.FillBounds,
                            modifier = Modifier.width(100.dp).height(150.dp)
                        )
                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.height(150.dp)
                        ) {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "${it.year}, ${it.age}+",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
                            )
                            Text(
                                text = "${it.duration / 3600} h ${(it.duration % 3600) / 60} m",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
                            )
                        }
                        Text(
                            text = it.rating.toString(),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
        AnimatedVisibility(
            visible = viewModel.showFilterSheet,
            enter = slideInVertically(tween(400),{-it}),
            exit = slideOutVertically(tween(400),{-it})
        ) {
            ModalBottomSheet(
                onDismissRequest = {viewModel.showFilterSheet = false},
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)

                ){
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = cnt[1],
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(Modifier.weight(0.3f))
                            TextButton(
                                onClick = {},
                                enabled = true
                            ) {
                                Text(
                                    text = cnt[2],
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                    item {
                        Text(
                            text = filters[0],
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
                        )
                        Spacer(Modifier.height(8.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ){
                            items(sorts){
                                Button(
                                    onClick = {},
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                                ){
                                    Text(
                                        it,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                            }
                        }
                    }

                    items(filters) {
                        if(it != "Sort by"){
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ){
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                    Text(
                                        text = sorts[0],
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                                        modifier = Modifier.offset(x = (-4).dp)
                                    )
                                }
                                HorizontalDivider()
                            }
                        }else{
                            Text(
                                text = cnt[1],
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
                            )
                        }
                    }

                }
            }
        }
    }
}