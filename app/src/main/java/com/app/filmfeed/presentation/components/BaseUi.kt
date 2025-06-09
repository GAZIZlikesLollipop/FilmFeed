@file:OptIn(ExperimentalMaterial3Api::class)

package com.app.filmfeed.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.filmfeed.Navigation
import com.app.filmfeed.R
import com.app.filmfeed.Route
import com.app.filmfeed.presentation.MovieViewModel

@Composable
fun BaseScreen(movieViewModel: MovieViewModel){
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(
        topBar = {
            if(currentRoute != Route.WatchMovie.route) BaseTopBar(navController,movieViewModel)
        },
        bottomBar = {
            if(
                currentRoute == Route.Main.route ||
                currentRoute == Route.Catalog.route ||
                currentRoute == Route.My.route ||
                currentRoute == Route.Search.route
            )
            BaseBottomBar(navController)
        },
        modifier = Modifier.fillMaxSize()
    ){
        Navigation(
            movieViewModel,
            it,
            navController
        )
    }
}

@Composable
fun BaseTopBar(
    navController: NavController,
    viewModel: MovieViewModel
){
    val ams = stringArrayResource(R.array.base_routes)
    val membs = stringArrayResource(R.array.about_movie)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val name = when(currentRoute){
        Route.Main.route -> ams[0]
        Route.Catalog.route -> ams[1]
        Route.My.route -> ams[2]
        Route.Search.route -> ams[3]
        Route.Members.route -> if(viewModel.isActor) membs[1] else membs[2]
        else -> ""
    }
    val barColor by animateColorAsState(
        targetValue =
            if(
                currentRoute != Route.Main.route &&
                currentRoute != Route.Catalog.route &&
                currentRoute != Route.My.route &&
                currentRoute != Route.Search.route
            ) Color.Transparent else MaterialTheme.colorScheme.surfaceContainer,
    )
    TopAppBar(
        title = {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            if(
                currentRoute != Route.Main.route &&
                currentRoute != Route.Catalog.route &&
                currentRoute != Route.My.route &&
                currentRoute != Route.Search.route
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(barColor)
    )
}

data class BarItem(
    val name: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BaseBottomBar(
    navController: NavController
){
    val names = stringArrayResource(R.array.base_routes)
    val items = listOf(
        BarItem(
            name = names[0],
            icon = Icons.Rounded.Home,
            route = Route.Main.route
        ),
        BarItem(
            name = names[1],
            icon = Icons.AutoMirrored.Rounded.ViewList,
            route = Route.Catalog.route
        ),
        BarItem(
            name = names[2],
            icon = Icons.Rounded.Bookmark,
            route = Route.My.route
        ),
        BarItem(
            name = names[3],
            icon = Icons.Rounded.Search,
            route = Route.Search.route
        )
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer){
        items.forEach {
            NavigationBarItem(
                selected = currentRoute == it.route,
                onClick = { if(currentRoute != it.route)navController.navigate(it.route) },
                icon = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(3.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Icon(
                            imageVector = it.icon,
                            contentDescription = it.name,
                            modifier = Modifier.size(26.dp)
                        )
                        Text(
                            it.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                    indicatorColor = Color.Transparent,
                    disabledIconColor = Color.Transparent
                )
            )
        }
    }
}