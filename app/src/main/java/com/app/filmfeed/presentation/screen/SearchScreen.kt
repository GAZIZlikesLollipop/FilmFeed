package com.app.filmfeed.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Article
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.rounded.Animation
import androidx.compose.material.icons.rounded.Castle
import androidx.compose.material.icons.rounded.ContentCut
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.FamilyRestroom
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Gavel
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Landscape
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.MilitaryTech
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material.icons.rounded.SportsSoccer
import androidx.compose.material.icons.rounded.TheaterComedy
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.filmfeed.R
import com.app.filmfeed.Route
import com.app.filmfeed.data.network.Genre
import com.app.filmfeed.presentation.MovieViewModel
import com.app.filmfeed.presentation.components.IntTextField
import com.app.filmfeed.presentation.components.WebImage
import com.app.filmfeed.utils.Filters
import java.time.OffsetDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: MovieViewModel,
    paddingValues: PaddingValues,
    navController: NavController
){
    val rawMovies by viewModel.movies.collectAsState()
    val filtersCnt = stringArrayResource(R.array.filters)
    val sorts = stringArrayResource(R.array.sorts)
    val cnt = stringArrayResource(R.array.search_cnt)
    val countries = stringArrayResource(R.array.countries)
    val ages = stringArrayResource(R.array.ages)
    val genres = stringArrayResource(R.array.genres)
    val gnrs = remember { mutableStateListOf(*viewModel.filters.genres.toTypedArray()) }
    val movies = when {
        viewModel.filters.byPopularity -> rawMovies.sortedByDescending { it.reviews }
        viewModel.filters.byNewest -> rawMovies.sortedByDescending { OffsetDateTime.parse(it.createdAt).toLocalTime() }
        viewModel.filters.byHighRating -> rawMovies.sortedBy { it.rating }
        viewModel.filters.byAlphabetical -> rawMovies.sortedBy { it.name.lowercase() }

        viewModel.filters.country != null -> rawMovies.filter { it.country == viewModel.filters.country }
        viewModel.filters.age != null -> rawMovies.filter { it.age == viewModel.filters.age }
        viewModel.filters.fromYear != null && viewModel.filters.toYear != null -> rawMovies.filter { it.year >= viewModel.filters.fromYear!! && it.year <= viewModel.filters.toYear!! }
        viewModel.filters.minDuration != null && viewModel.filters.maxDuration != null -> rawMovies.filter { it.duration >= viewModel.filters.minDuration!! && it.duration <= viewModel.filters.maxDuration!! }
        viewModel.filters.genres.isNotEmpty() -> rawMovies.filter { it -> viewModel.filters.genres.all { genre -> it.genres.any { genre.name == it.name } } }

        else -> rawMovies
    }

    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            items(movies) {
                if (it.name.replace(" ", "").lowercase().contains(viewModel.searchText.lowercase())) {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { navController.navigate(Route.AboutMovie.createRoute(it.id)) },
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
            enter = slideInVertically(tween(400)) { -it },
            exit = slideOutVertically(tween(400)) { -it }
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
                                onClick = { viewModel.filters = Filters() },
                                enabled = viewModel.filters != Filters()
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
                            text = filtersCnt[0],
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
                        )
                        Spacer(Modifier.height(8.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ){
                            itemsIndexed(sorts){ ind, str ->
                                Button(
                                    onClick = {
                                        when (ind) {
                                            0 -> viewModel.filters = viewModel.filters.copy(
                                                byPopularity = false,
                                                byHighRating = false,
                                                byNewest = false,
                                                byAlphabetical = false
                                            )
                                            1 -> viewModel.filters = viewModel.filters.copy(byPopularity = !viewModel.filters.byPopularity)
                                            2 -> viewModel.filters = viewModel.filters.copy(byNewest = !viewModel.filters.byNewest)
                                            3 -> viewModel.filters = viewModel.filters.copy(byHighRating = !viewModel.filters.byHighRating)
                                            else -> viewModel.filters = viewModel.filters.copy(byAlphabetical = !viewModel.filters.byAlphabetical)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor =
                                        when {
                                            ind == 0 &&
                                            viewModel.filters.byPopularity == false &&
                                            viewModel.filters.byNewest == false &&
                                            viewModel.filters.byHighRating == false &&
                                            viewModel.filters.byAlphabetical == false -> MaterialTheme.colorScheme.secondary
                                            ind == 1 && viewModel.filters.byPopularity != false -> MaterialTheme.colorScheme.secondary
                                            ind == 2 && viewModel.filters.byNewest != false -> MaterialTheme.colorScheme.secondary
                                            ind == 3 && viewModel.filters.byHighRating != false -> MaterialTheme.colorScheme.secondary
                                            ind == 4 && viewModel.filters.byAlphabetical != false -> MaterialTheme.colorScheme.secondary
                                            else -> MaterialTheme.colorScheme.surfaceContainerHighest
                                        },
                                        contentColor = when {
                                            ind == 0 &&
                                            viewModel.filters.byPopularity == false &&
                                            viewModel.filters.byNewest == false &&
                                            viewModel.filters.byHighRating == false &&
                                            viewModel.filters.byAlphabetical == false -> MaterialTheme.colorScheme.onBackground
                                            ind == 1 && viewModel.filters.byPopularity != false -> MaterialTheme.colorScheme.onBackground
                                            ind == 2 && viewModel.filters.byNewest != false -> MaterialTheme.colorScheme.onBackground
                                            ind == 3 && viewModel.filters.byHighRating != false -> MaterialTheme.colorScheme.onBackground
                                            ind == 4 && viewModel.filters.byAlphabetical != false -> MaterialTheme.colorScheme.onBackground
                                            else -> MaterialTheme.colorScheme.onBackground.copy(0.5f)
                                        }
                                    ),
                                    enabled = true
                                ){
                                    Text(
                                        text = str,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                            }
                        }
                    }
                    itemsIndexed(filtersCnt) { ind, str ->
                        var isOpen by rememberSaveable { mutableStateOf(false) }
                        val rotateAnim by animateFloatAsState(
                            targetValue = if(isOpen) 180f else 0f,
                            animationSpec = tween(300)
                        )
                        if(str != "Sort by"){
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ){
                                Row(
                                    modifier = Modifier.fillMaxWidth().clickable{isOpen = !isOpen},
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Text(
                                        text = str,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Icon(
                                        imageVector = Icons.Rounded.KeyboardArrowDown,
                                        contentDescription = null,
                                        modifier = Modifier.rotate(rotateAnim).size(32.dp)
                                    )
                                }
                                AnimatedVisibility(
                                    visible = isOpen,
                                    enter = expandVertically(tween(300), initialHeight = {-it}),
                                    exit = shrinkVertically(tween(300), targetHeight = {-it})
                                ) {
                                    when(ind){
                                        1 -> {
                                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                                                itemsIndexed(genres){ ind, str ->
                                                    val genre = Genre(ind.toLong(),str)
                                                    val icon = when(ind){
                                                        0 -> Icons.Rounded.LocalFireDepartment
                                                        1 -> Icons.Rounded.Explore
                                                        2 -> ImageVector.vectorResource(R.drawable.comedy_mask)
                                                        3 -> Icons.Rounded.TheaterComedy
                                                        4 -> ImageVector.vectorResource(R.drawable.skull)
                                                        5 -> Icons.Rounded.Science
                                                        6 -> Icons.Rounded.Castle
                                                        7 -> Icons.Rounded.VisibilityOff
                                                        8 -> Icons.Rounded.Favorite
                                                        9 -> Icons.Rounded.Gavel
                                                        10 -> Icons.Rounded.Animation
                                                        11 -> Icons.AutoMirrored.Rounded.Article
                                                        12 -> Icons.Rounded.FamilyRestroom
                                                        13 -> Icons.AutoMirrored.Rounded.Help
                                                        14 -> Icons.Rounded.History
                                                        15 -> Icons.Rounded.MilitaryTech
                                                        16 -> Icons.Rounded.Landscape
                                                        17 -> Icons.Rounded.SportsSoccer
                                                        18 -> Icons.Rounded.Person
                                                        19 -> Icons.Rounded.ContentCut
                                                        else -> Icons.Rounded.MusicNote
                                                    }
                                                    Button(
                                                        onClick = {
                                                            if(viewModel.filters.genres.contains(genre)) gnrs.remove(genre) else gnrs.add(genre)
                                                            viewModel.filters = viewModel.filters.copy(genres = gnrs.toList())
                                                        },
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = if(viewModel.filters.genres.contains(genre)) MaterialTheme.colorScheme.primaryContainer else  MaterialTheme.colorScheme.surfaceContainerHighest,
                                                            contentColor = if(viewModel.filters.genres.contains(genre)) MaterialTheme.colorScheme.onBackground else  MaterialTheme.colorScheme.onBackground.copy(0.5f)
                                                        ),
                                                        shape = RoundedCornerShape(24.dp)
                                                    ) {
                                                        Column(
                                                            horizontalAlignment = Alignment.CenterHorizontally,
                                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                                        ){
                                                            Icon(
                                                                imageVector = icon,
                                                                contentDescription = str,
                                                                modifier = Modifier.size(32.dp)
                                                            )
                                                            Text(
                                                                str,
                                                                style = MaterialTheme.typography.titleLarge
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        2 -> {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ){
                                                repeat(2) { ind ->
                                                    IntTextField(
                                                        headText = if(ind == 0) cnt[3] else cnt[4],
                                                        onValueChange = {
                                                            if(ind == 0) {
                                                                viewModel.filters =
                                                                    viewModel.filters.copy(fromYear = it.toInt())
                                                            }else{
                                                                viewModel.filters =
                                                                    viewModel.filters.copy(toYear = it.toInt())
                                                            }
                                                        },
                                                        modifier = Modifier.weight(0.5f)
                                                    )
                                                    if(ind == 0){
                                                        Spacer(Modifier.weight(0.1f))
                                                    }
                                                }
                                            }
                                        }
                                        3 -> {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ){
                                                repeat(2) { ind ->
                                                    IntTextField(
                                                        headText = if(ind == 0) cnt[5] else cnt[6],
                                                        onValueChange = {
                                                            if(ind == 0) {
                                                                viewModel.filters = viewModel.filters.copy(minDuration = it.toInt())
                                                            }else{
                                                                viewModel.filters = viewModel.filters.copy(maxDuration = it.toInt())
                                                            }
                                                        },
                                                        modifier = Modifier.weight(0.5f)
                                                    )
                                                    if(ind == 0){
                                                        Spacer(Modifier.weight(0.1f))
                                                    }
                                                }
                                            }
                                        }
                                        else -> {
                                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                items(if(ind == 4) countries else ages) {
                                                    Button(
                                                        onClick = {
                                                            if(ind == 4) {
                                                                viewModel.filters = viewModel.filters.copy(country = it)
                                                            }else{
                                                                viewModel.filters = viewModel.filters.copy(age = it.toInt())
                                                            }
                                                        },
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = if(if(ind == 4)viewModel.filters.country == it else viewModel.filters.age == it.toInt()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHighest,
                                                            contentColor = if(if(ind == 4)viewModel.filters.country == it else viewModel.filters.age == it.toInt()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(0.5f)
                                                        )
                                                    ) {
                                                        Text(
                                                            if(ind == 4) it else "$it+",
                                                            style = MaterialTheme.typography.titleLarge
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
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